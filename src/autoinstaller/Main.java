package autoinstaller;
import java.util.Scanner;

import autoinstaller.Accounts.LoginManager;
import autoinstaller.Accounts.Admin.Admin;
import autoinstaller.Utils.Utils;
import autoinstaller.installer.Installer;

public class Main {
    public static void main(String[] args) {
        Utils.clearConsole();
        Scanner sc = new Scanner(System.in);
        // 로그인 유형 선택
        System.out.println("===== 로그인 유형 선택 =====");
        System.out.println("1. 일반 사용자 로그인");
        System.out.println("2. 관리자 로그인");
        System.out.print("선택 >> ");
        String loginType = sc.nextLine().trim();

        boolean isAdmin = false;
        switch (loginType) {
            case "1":
                isAdmin = false;
                break;
            case "2":
                isAdmin = true;
                break;
            default:
                System.out.println("[오류] 잘못된 입력입니다.");
                Utils.pause();
                Utils.clearConsole();
                main(args);
                return;
        }

        if (!LoginManager.authenticate(sc, isAdmin)) return;

        if (isAdmin) {
            Admin.run(sc);
        } else {
            Installer.run(sc);
        }
    }
}