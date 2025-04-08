package autoinstaller;
import java.util.Scanner;

public class Admin {
    public static void run(Scanner sc) {
        while (true) {
            Utils.clearConsole();
            Utils.log("[정보] 관리자 로그인");
            System.out.println("\n===== 관리자 메뉴 =====");
            System.out.println("1. 프로그램 목록 보기");
            System.out.println("2. 프로그램 추가");
            System.out.println("3. 프로그램 제거");
            System.out.println("4. 로그 보기");
            System.out.println("0. 종료");
            System.out.print("선택 >> ");

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    Utils.clearConsole();
                    Utils.printPrograms();
                    break;
                case "2":
                    Utils.clearConsole();
                    Utils.addProgram(sc);
                    break;
                case "3":
                    Utils.clearConsole();
                    Utils.deleteProgram(sc);
                    break;    
                case "4":
                    Utils.clearConsole();
                    Utils.showLogs(sc);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("[경고] 올바르지 않은 선택입니다.");
            }
        }
    }
}
