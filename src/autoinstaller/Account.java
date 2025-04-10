package autoinstaller;

import java.util.Scanner;

public class Account {
    private String username;
    private String password;
    private String role;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    /*Account Management */
    public static void AccountManagementMenu(Scanner sc) {
        Utils.clearConsole();
        while (true) {
            System.out.println("=== 계정 관리 ===");
            System.out.println("1. 계정 생성");
            System.out.println("2. 계정 삭제 (추후 구현)");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 >> ");
            String input = sc.nextLine().trim();
    
            switch (input) {
                case "1":
                    Utils.clearConsole();
                    CreateAccount(sc);
                    break;
                case "2":
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

    /*Create Account */
    public static void CreateAccount(Scanner sc){

    }

    /*Delete Account */
    public static void DeleteAccount(Scanner sc){

    }
}
