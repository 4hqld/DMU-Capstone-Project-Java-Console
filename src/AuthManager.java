import java.util.Scanner;

public class AuthManager {
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PW = "admin123";
    private static final String USER_ID = "user";
    private static final String USER_PW = "user123";

    public static boolean authenticate(Scanner sc, boolean isAdmin) {
        int attempts = 0;

        while (attempts < 5) {
            System.out.print(isAdmin ? "관리자 ID: " : "ID: ");
            String id = sc.nextLine().trim();

            String pw = Utils.readPassword(isAdmin ? "관리자 PW: " : "PW: ", sc);

            if (isAdmin) {
                if (id.equals(ADMIN_ID) && pw.equals(ADMIN_PW)) {
                    System.out.println("[성공] 관리자 로그인 완료");
                    return true;
                } else {
                    System.out.println("[오류] 관리자 로그인 실패 (" + (4 - attempts) + "회 남음)");
                }
            } else {
                if (id.equals(USER_ID) && pw.equals(USER_PW)) {
                    System.out.println("[성공] 일반 사용자 로그인 완료");
                    return true;
                } else {
                    System.out.println("[오류] 일반 사용자 로그인 실패");
                }
            }

            attempts++;
        }

        System.out.println("[오류] 로그인 시도 5회 초과. 프로그램을 종료합니다.");
        Utils.log("[경고] 로그인 5회 실패. 프로그램 종료됨");
        return false;
    }
}
