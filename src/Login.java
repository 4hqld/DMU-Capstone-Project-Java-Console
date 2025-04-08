import java.util.Scanner;

public class Login {
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PW = "admin123";
    private static final String USER_ID = "user";
    private static final String USER_PW = "user123";

    public static boolean loginUser(Scanner sc) {
        System.out.print("ID: ");
        String id = sc.nextLine();
        String pw = Utils.readPassword("PW: ", sc);

        if (id.equals(USER_ID) && pw.equals(USER_PW)) {
            System.out.println("[성공] 일반 사용자 로그인 완료");
            return true;
        } else {
            System.out.println("[오류] 일반 사용자 로그인 실패");
            return false;
        }
    }

    public static boolean loginAdmin(Scanner sc) {
        int attempts = 0;
        while (attempts < 5) {
            System.out.print("관리자 ID: ");
            String id = sc.nextLine();
            String pw = Utils.readPassword("관리자 PW: ", sc);

            if (id.equals(ADMIN_ID) && pw.equals(ADMIN_PW)) {
                System.out.println("[성공] 관리자 로그인 완료");
                return true;
            } else {
                System.out.println("[오류] 관리자 로그인 실패 (" + (4 - attempts) + "회 남음)");
            }
            attempts++;
        }
        System.out.println("[오류] 로그인 시도 5회 초과. 프로그램을 종료합니다.");
        return false;
    }
}
