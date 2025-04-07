import java.util.Scanner;

public class AuthManager {
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PW = "1234";

    public static boolean authenticate(Scanner sc, boolean isAdmin) {
        int attempts = 0;

        while (attempts < 5) {
            System.out.print("아이디: ");
            String id = sc.nextLine().trim();

            System.out.print("비밀번호: ");
            String pw = sc.nextLine().trim();

            if (isAdmin && id.equals(ADMIN_ID) && pw.equals(ADMIN_PW)) {
                return true;
            } else if (!isAdmin && !id.isBlank()) {
                return true;
            }

            attempts++;
            System.out.println("[오류] 로그인 실패 (" + attempts + "/5)");
        }

        Utils.log("[경고] 로그인 5회 실패. 프로그램을 종료합니다.");
        return false;
    }
}
