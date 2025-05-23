package autoinstaller.Accounts.Admin;
import java.util.Scanner;

import autoinstaller.Accounts.AccountsManager;
import autoinstaller.Utils.Utils;

public class Admin {
    public static void run(Scanner sc) {
        Utils.log("[정보] 관리자 로그인");
        while (true) {
            Utils.clearConsole();
            System.out.println("\n=== 관리자 메뉴 ===");
            System.out.println("1. 프로그램 관리");
            System.out.println("2. 계정 관리");
            System.out.println("3. 로그 보기");
            System.out.println("4. 로그아웃");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    Utils.clearConsole();
                    programManagementMenu(sc);
                    break;
                case "2":
                    AccountsManager.AccountManagementMenu(sc);
                    break;
                case "3":
                    Utils.clearConsole();
                    LogMenu(sc);
                    break;
                case "4":
                    Utils.clearConsole();
                    System.out.println("[정보] 로그아웃합니다.");
                    return;
                default:
                    System.out.println("[경고] 올바른 번호를 입력해주세요.");
            }
        }
    }

    /*Program Management Menu */
    private static void programManagementMenu(Scanner sc) {
        while (true) {
            Utils.clearConsole();
            System.out.println("\n=== 프로그램 관리 ===");
            System.out.println("1. 프로그램 목록 보기");
            System.out.println("2. 프로그램 추가");
            System.out.println("3. 프로그램 제거");
            System.out.println("0. 뒤로가기");
            System.out.print("선택: ");
            String subChoice = sc.nextLine();

            switch (subChoice) {
                case "1":
                    Utils.clearConsole();
                    Utils.printPrograms();
                    Utils.pause();
                    break;
                case "2":
                    Utils.clearConsole();
                    Utils.addProgram(sc);
                    break;
                case "3":
                    Utils.clearConsole();
                    Utils.deleteProgram(sc);
                    break;
                case "0":
                    Utils.clearConsole();
                    return;
                default:
                    System.out.println("[경고] 올바른 번호를 입력해주세요.");
            }
        }
    }

    /*Log Menu */
    private static void LogMenu(Scanner sc){
        while(true){
            Utils.clearConsole();
            System.out.println("=== 로그 보기 ===");
            System.out.println("1. 전체 로그 보기");
            System.out.println("2. 로그 유형 필터 ([성공], [실패], [건너뜀], [경고], [오류])");
            System.out.println("3. 날짜 필터 (예: 2025-04-07)");
            System.out.println("4. 키워드 검색");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 >> ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": //print All Log
                    Utils.clearConsole();
                    Utils.printAllLogs(null);
                    break;
                case "2": //print filtered with String Log
                    Utils.clearConsole();
                    Utils.printLogsByType(null, sc);
                    break;
                case "3": //print timefiltered Log
                    Utils.clearConsole();
                    Utils.printLogsByDate(null, sc);
                    break;
                case "4": //print filter Keyword
                    Utils.clearConsole();
                    Utils.printLogsByKeyword(null, sc);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("[경고] 잘못된 입력입니다.");
            }
        }
    }    
}