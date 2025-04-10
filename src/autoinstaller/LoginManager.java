package autoinstaller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Console;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class LoginManager {

    private static final String ACCOUNT_FILE = "JSON/accounts.json";
    private static List<Account> accountList;

    static {
        loadAccounts();
    }

    private static void loadAccounts() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            accountList = mapper.readValue(new File(ACCOUNT_FILE), new TypeReference<List<Account>>() {});
        } catch (Exception e) {
            System.out.println("[오류] 계정 로드 실패: " + e.getMessage());
            System.exit(1);
        }
    }

    public static boolean authenticate(Scanner sc, boolean isAdmin) {
        return authenticateAndGetAccount(sc, isAdmin) != null;
    }

    public static Account authenticateAndGetAccount(Scanner sc, boolean isAdmin) {
        int attempts = 0;
        Console console = System.console();

        while (attempts < 5) {
            System.out.print(isAdmin ? "관리자 ID: " : "ID: ");
            String id = sc.nextLine().trim();

            String pw;

            if (console != null) {
                char[] pwChars = console.readPassword(isAdmin ? "관리자 PW: " : "PW: ");
                pw = new String(pwChars);
            } else {
                System.out.print(isAdmin ? "관리자 PW (콘솔 없음): " : "PW (콘솔 없음): ");
                pw = sc.nextLine();
            }

            if (id.isEmpty() || pw.isEmpty()) {
                System.out.println("[경고] ID 또는 PW가 비어 있습니다.");
                Utils.pause();
                Utils.clearConsole(); 
                continue;
            }

            for (Account acc : accountList) {
                if (acc.getUsername().equals(id) && acc.getPassword().equals(pw)) {
                    // 권한 확인
                    if (isAdmin && !"admin".equalsIgnoreCase(acc.getRole())) {
                        System.out.println("[오류] 해당 계정은 관리자 권한이 없습니다.");
                        Utils.log("[경고] 관리자 권한 없는 계정 로그인 시도: " + id);
                        return null;
                    }
                    if (!isAdmin && !"user".equalsIgnoreCase(acc.getRole())) {
                        System.out.println("[오류] 해당 계정은 접근 권한이 없습니다.");
                        Utils.log("[경고] 사용자 권한 없는 계정 로그인 시도: " + id);
                        return null;
                    }

                    System.out.println("[성공] " + (isAdmin ? "관리자" : "일반 사용자") + " 로그인 완료");
                    Utils.log("[성공] 로그인 성공 (" + acc.getRole() + "): " + id);
                    return acc;
                }
            }

            System.out.println("[오류] 로그인 실패 (" + (4 - attempts) + "회 남음)");
            attempts++;
        }

        System.out.println("[오류] 로그인 시도 5회 초과. 프로그램을 종료합니다.");
        Utils.log("[경고] 로그인 5회 실패. 프로그램 종료됨");
        return null;
    }
}
