package autoinstaller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class AuthManager {

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
        int attempts = 0;

        while (attempts < 5) {
            System.out.print(isAdmin ? "관리자 ID: " : "ID: ");
            String id = sc.nextLine().trim();
            String pw = Utils.readPassword(isAdmin ? "관리자 PW: " : "PW: ", sc);

            for (Account acc : accountList) {
                if (acc.getUsername().equals(id) && acc.getPassword().equals(pw)) {
                    if (isAdmin && !"admin".equalsIgnoreCase(acc.getRole())) {
                        System.out.println("[오류] 해당 계정은 관리자 권한이 없습니다.");
                        return false;
                    }
                    if (!isAdmin && !"user".equalsIgnoreCase(acc.getRole())) {
                        System.out.println("[오류] 일반 사용자 로그인 시 관리자 계정을 사용할 수 없습니다.");
                        return false;
                    }

                    System.out.println("[성공] " + (isAdmin ? "관리자" : "일반 사용자") + " 로그인 완료");
                    return true;
                }
            }

            System.out.println("[오류] 로그인 실패 (" + (4 - attempts) + "회 남음)");
            attempts++;
        }

        System.out.println("[오류] 로그인 시도 5회 초과. 프로그램을 종료합니다.");
        Utils.log("[경고] 로그인 5회 실패. 프로그램 종료됨");
        return false;
    }
}
