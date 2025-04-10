package autoinstaller;
import java.util.Scanner;

public class Admin {
    public static void run(Scanner sc) {
        Utils.clearConsole();
        Utils.log("[정보] 관리자 로그인");
        while (true) {
            System.out.println("\n=== 관리자 메뉴 ===");
            System.out.println("1. 프로그램 관리");
            System.out.println("2. 로그 보기");
            System.out.println("3. 로그아웃");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    Utils.clearConsole();
                    programManagementMenu(sc);
                    break;
                case "2":
                    Utils.clearConsole();
                    Utils.viewLogs(sc);
                    break;
                case "3":
                    Utils.clearConsole();
                    System.out.println("[정보] 로그아웃합니다.");
                    return;
                default:
                    System.out.println("[경고] 올바른 번호를 입력해주세요.");
            }
        }
    }
    private static void programManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== 프로그램 관리 ===");
            System.out.println("1. 프로그램 목록 보기");
            System.out.println("2. 프로그램 추가");
            System.out.println("3. 프로그램 제거");
            System.out.println("0. 뒤로가기");
            System.out.print("선택: ");
            String subChoice = scanner.nextLine();

            switch (subChoice) {
                case "1":
                    Utils.clearConsole();
                    Utils.printPrograms();
                    Utils.pause();
                    break;
                case "2":
                    Utils.clearConsole();
                    Utils.addProgram(scanner);
                    break;
                case "3":
                    Utils.clearConsole();
                    Utils.deleteProgram(scanner);
                    break;
                case "0":
                    Utils.clearConsole();
                    return;
                default:
                    Utils.clearConsole();
                    System.out.println("[경고] 올바른 번호를 입력해주세요.");
            }
        }
    }
}

