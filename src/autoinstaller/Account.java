package autoinstaller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Account {
    private String username;
    private String password;
    private String role;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    private static final String ACCOUNT_FILE = "JSON/accounts.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    /* Account Management */
    public static void AccountManagementMenu(Scanner sc) {
        Utils.clearConsole();
        while (true) {
            System.out.println("=== 계정 관리 ===");
            System.out.println("1. 계정 조회");
            System.out.println("2. 계정 생성");
            System.out.println("3. 계정 삭제");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 >> ");
            String input = sc.nextLine().trim();

            switch (input) {
                case "1":
                    Utils.clearConsole();
                    ViewAccounts(sc);
                    break;
                case "2":
                    Utils.clearConsole();
                    CreateAccount(sc);
                    break;
                case "3":
                    Utils.clearConsole();
                    DeleteAccount(sc);
                    break;
                case "0":
                    Utils.clearConsole();
                    return;
                default:
                    System.out.println("[경고] 잘못된 입력입니다.");
            }
            Utils.pause();
            Utils.clearConsole();
        }
    }

    /* View Accounts */
    public static void ViewAccounts(Scanner sc) {
        try {
            List<Account> accountList = mapper.readValue(new File(ACCOUNT_FILE), new TypeReference<List<Account>>() {});
            System.out.println("총 " + accountList.size() + "개의 계정이 있습니다.");
            System.out.println("----------------------------");
            for (Account acc : accountList) {
                System.out.println("ID: " + acc.getUsername() + " | 권한: " + acc.getRole());
            }
            System.out.println("----------------------------");
        } catch (Exception e) {
            System.out.println("[오류] 계정 조회 중 오류 발생: " + e.getMessage());
        }
    }

    /* Create Account */
    public static void CreateAccount(Scanner sc) {
        try {
            List<Account> accountList = mapper.readValue(new File(ACCOUNT_FILE), new TypeReference<List<Account>>() {});

            System.out.print("새 ID 입력: ");
            String newId = sc.nextLine().trim();
            System.out.print("비밀번호 입력: ");
            String newPw = sc.nextLine().trim();
            System.out.print("권한 입력 (admin/user): ");
            String newRole = sc.nextLine().trim().toLowerCase();

            if (!newRole.equals("admin") && !newRole.equals("user")) {
                System.out.println("[경고] 권한은 admin 또는 user만 입력 가능합니다.");
                return;
            }

            for (Account acc : accountList) {
                if (acc.getUsername().equalsIgnoreCase(newId)) {
                    System.out.println("[경고] 이미 존재하는 ID입니다.");
                    return;
                }
            }

            Account newAcc = new Account();
            newAcc.username = newId;
            newAcc.password = newPw;
            newAcc.role = newRole;

            accountList.add(newAcc);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(ACCOUNT_FILE), accountList);

            System.out.println("[성공] 계정이 추가되었습니다.");
            Utils.log("[정보] 계정 생성됨 - ID: " + newId + ", 권한: " + newRole);
        } catch (Exception e) {
            System.out.println("[오류] 계정 생성 중 오류 발생: " + e.getMessage());
        }
    }

    /* Delete Account */
    public static void DeleteAccount(Scanner sc) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Account> accountList = mapper.readValue(new File("JSON/accounts.json"), new TypeReference<List<Account>>() {});

            System.out.print("삭제할 ID 입력: ");
            String delId = sc.nextLine().trim();

            Iterator<Account> iterator = accountList.iterator();
            boolean found = false;

            while (iterator.hasNext()) {
                Account acc = iterator.next();
                if (acc.getUsername().equalsIgnoreCase(delId)) {
                    if ("admin".equalsIgnoreCase(acc.getRole())) {
                        System.out.println("[경고] 관리자 계정은 삭제할 수 없습니다.");
                        Utils.log("[경고] 관리자 계정 삭제 시도 차단 - ID: " + delId);
                        return;
                    }

                    iterator.remove();
                    found = true;
                    break;
                }
            }

            if (found) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File("JSON/accounts.json"), accountList);
                System.out.println("[성공] 계정이 삭제되었습니다.");
                Utils.log("[정보] 계정 삭제됨 - ID: " + delId);
            } else {
                System.out.println("[경고] 해당 ID를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            System.out.println("[오류] 계정 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
