import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils {
    static final String INSTALL_LIST = "data/install_list.json";
    static final String LOG_FILE = "data/install_log.txt";
    static final String INSTALLER_DIR = "installer/";

    private static Scanner scanner = new Scanner(System.in);

    public static boolean checkAdminPrivileges() {
        return System.getProperty("user.name").equals("Administrator");
    }

    public static boolean checkEnvironment() {
        return new File("C:/").getFreeSpace() > 5L * 1024 * 1024 * 1024;
    }

    public static List<Program> loadPrograms() {
        try {
            String json = Files.readString(Paths.get(INSTALL_LIST));
            String[] items = json.replace("[", "").replace("]", "").split("},");
            List<Program> list = new ArrayList<>();
            for (String item : items) {
                if (!item.endsWith("}")) item += "}";
                String name = getValue(item, "name");
                String url = getValue(item, "url");
                String saveAs = getValue(item, "saveAs");
                String checkPath = getValue(item, "checkPath");
                String command = getValue(item, "installCommand");
                list.add(new Program(name, url, saveAs, checkPath, command));
            }
            return list;
        } catch (Exception e) {
            System.err.println("[오류] install_list.json 로드 실패: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static Set<Integer> parseSelection(String input, int max) {
        Set<Integer> set = new HashSet<>();
        if (input.equalsIgnoreCase("all")) {
            for (int i = 0; i < max; i++) set.add(i);
        } else {
            for (String s : input.split(",")) {
                try {
                    int i = Integer.parseInt(s.trim()) - 1;
                    if (i >= 0 && i < max) set.add(i);
                } catch (Exception ignored) {}
            }
        }
        return set;
    }

    public static boolean downloadInstaller(Program p) {
        try {
            Path dir = Paths.get(INSTALLER_DIR);
            if (!Files.exists(dir)) Files.createDirectory(dir);
            Path dest = dir.resolve(p.saveAs);
            if (Files.exists(dest)) return true;

            try (InputStream in = URI.create(p.url).toURL().openStream()) {
                Files.copy(in, dest);
                return true;
            }
        } catch (Exception e) {
            System.err.println("[오류] 다운로드 실패: " + p.name);
            return false;
        }
    }

    public static boolean executeCommand(String command) {
        try {
            return new ProcessBuilder("cmd", "/c", command).inheritIO().start().waitFor() == 0;
        } catch (Exception e) {
            System.err.println("[오류] 설치 실패: " + e.getMessage());
            return false;
        }
    }

    public static void promptRestart(Scanner sc) {
        System.out.print("시스템을 다시 시작하시겠습니까? (Y/N) >> ");
        String input = sc.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            try {
                new ProcessBuilder("shutdown", "/r", "/t", "0").start();
            } catch (IOException e) {
                System.err.println("[오류] 시스템 재시작 실패");
            }
        }
    }

    public static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write(message + " - " + timestamp + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[오류] 로그 기록 실패: " + e.getMessage());
        }
    }

    public static String getValue(String json, String key) {
        String[] split = json.split("\"" + key + "\"\\s*:\\s*\"");
        if (split.length < 2) return "";
        return split[1].split("\"")[0];
    }

    public static void printPrograms() {
        System.out.println("===사용 가능한 프로그램===");
        loadPrograms().forEach(p -> System.out.println("- " + p.name));
        pause();
    }

    public static void addProgram(Scanner sc) {
        System.out.println("===프로그램 추가===");
        try {
            System.out.print("이름: ");
            String name = sc.nextLine().trim();
            System.out.print("URL: ");
            String url = sc.nextLine().trim();
            System.out.print("저장 이름: ");
            String saveAs = sc.nextLine().trim();
            System.out.print("체크 경로: ");
            String checkPath = sc.nextLine().trim();
            System.out.print("설치 명령어: ");
            String command = sc.nextLine().trim();
    
            // 하나라도 비어 있을 경우 저장하지 않음
            if (name.isEmpty() || url.isEmpty() || saveAs.isEmpty() || checkPath.isEmpty() || command.isEmpty()) {
                System.out.println("[경고] 하나 이상의 항목이 비어 있어 추가를 취소합니다.");
                pause();
                return;
            }
    
            String json = String.format("{\"name\":\"%s\",\"url\":\"%s\",\"saveAs\":\"%s\",\"checkPath\":\"%s\",\"installCommand\":\"%s\"}",
                    name, url, saveAs, checkPath, command);
    
            Files.writeString(Paths.get(Utils.INSTALL_LIST), json + ",", StandardOpenOption.APPEND);
            System.out.println("[성공] 프로그램이 추가되었습니다.");
        } catch (IOException e) {
            System.err.println("[오류] 프로그램 추가 실패: " + e.getMessage());
        }
    }
    
    

    public static void showLogs() {
        try {
            System.out.println("\n[설치 로그]");
            Files.lines(Paths.get(LOG_FILE)).forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("[오류] 로그 파일 열기 실패");
        }
        pause();
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("[경고] 콘솔 화면 지우기 실패: " + e.getMessage());
        }
    }
    
    private static void pause() {
        System.out.println("\n[정보] 엔터를 누르면 관리자 메뉴로 돌아갑니다...");
        scanner.nextLine();
    }

    public static String readPassword(String prompt, Scanner sc) {
        System.out.print(prompt);
        return sc.nextLine(); // IDE 환경에서도 오류 없이 동작
    } 
}
