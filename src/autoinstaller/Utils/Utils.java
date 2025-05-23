package autoinstaller.Utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import autoinstaller.Program.Program;
import autoinstaller.Program.ProgramInstallResult;

public class Utils {
    static final String INSTALL_LIST = "resources/JSON/install_list.json";
    static final String LOG_FILE = "logs/logs.txt";
    static final String INSTALLER_DIR = "resources/installer/";
    private static final Scanner scanner = new Scanner(System.in);

    /*EnvironMent */
    
    public static boolean checkEnvironment() {
        long freeSpace = new File("C:/").getFreeSpace();
        boolean isSufficient = freeSpace > 5L * 1024 * 1024 * 1024;
        if (!isSufficient) {
            System.out.println("[경고] 디스크 공간이 5GB보다 적습니다.");
        }
        return isSufficient;
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
            if (Files.exists(dest)) {
                System.out.println("[정보] 설치 파일이 이미 존재합니다: " + p.saveAs);
                return true;
            }
            try (InputStream in = URI.create(p.url).toURL().openStream()) {
                Files.copy(in, dest);
                System.out.println("[성공] 다운로드 완료: " + p.name);
                return true;
            }
        } catch (Exception e) {
            System.err.println("[오류] 다운로드 실패: " + p.name + " - " + e.getMessage());
            return false;
        }
    }

    public static boolean executeCommand(String command) {
        try {
            return new ProcessBuilder("cmd", "/c", command).inheritIO().start().waitFor() == 0;
        } catch (Exception e) {
            System.err.println("[오류] 명령어 실행 실패: " + e.getMessage());
            return false;
        }
    }

    public static void promptRestart(Scanner sc) {
        System.out.print("시스템을 다시 시작하시겠습니까? (Y/N) >> ");
        String input = sc.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            try {
                new ProcessBuilder("shutdown", "/r", "/t", "0").start();
                System.out.println("[정보] 시스템 재시작을 수행합니다.");
            } catch (IOException e) {
                System.err.println("[오류] 시스템 재시작 실패: " + e.getMessage());
            }
        } else {
            System.out.println("[정보] 시스템 재시작이 취소되었습니다.");
        }
    }

    public static String getValue(String json, String key) {
        String[] split = json.split("\"" + key + "\"\\s*:\\s*\"");
        if (split.length < 2) return "";
        return split[1].split("\"")[0];
    }

    public static void printPrograms() {
        List<Program> list = loadPrograms();
        System.out.println("=== 사용 가능한 프로그램 목록 ===");
        list.forEach(p -> System.out.println("- " + p.name));
        if (list.isEmpty()) {
            System.out.println("[오류] 설치 목록이 비어 있습니다.");
            return;
        }
    }

    public static void addProgram(Scanner sc) {
        System.out.println("=== 프로그램 추가 ===");
        try {
            List<Program> list = loadPrograms();
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

            if (name.isEmpty() || url.isEmpty() || saveAs.isEmpty() || checkPath.isEmpty() || command.isEmpty()) {
                System.out.println("[경고] 하나 이상의 항목이 비어 있어 추가를 취소합니다.");
                pause();
                return;
            }

            for (Program p : list) {
                if (p.name.equalsIgnoreCase(name)) {
                    System.out.println("[경고] 동일한 이름의 프로그램이 이미 존재합니다.");
                    pause();
                    return;
                }
            }

            list.add(new Program(name, url, saveAs, checkPath, command));

            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < list.size(); i++) {
                Program p = list.get(i);
                sb.append(String.format("  {\"name\":\"%s\",\"url\":\"%s\",\"saveAs\":\"%s\",\"checkPath\":\"%s\",\"installCommand\":\"%s\"}",
                        p.name, p.url, p.saveAs, p.checkPath, p.installCommand));
                if (i < list.size() - 1) sb.append(",\n");
            }
            sb.append("\n]");
            Files.writeString(Paths.get(INSTALL_LIST), sb.toString(), StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("[성공] 프로그램이 추가되었습니다.");
        } catch (IOException e) {
            System.err.println("[오류] 프로그램 추가 실패: " + e.getMessage());
        }
        pause();
    }

    public static void deleteProgram(Scanner sc) {
        List<Program> list = loadPrograms();
        if (list.isEmpty()) {
            System.out.println("[정보] 등록된 프로그램이 없습니다.");
            pause();
            return;
        }

        System.out.println("=== 프로그램 목록 ===");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, list.get(i).name);
        }

        System.out.print("삭제할 프로그램 번호를 입력하세요 (취소: 0): ");
        String input = sc.nextLine().trim();
        if (input.equals("0")) {
            System.out.println("[정보] 삭제를 취소하였습니다.");
            pause();
            return;
        }

        try {
            int index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= list.size()) {
                System.out.println("[경고] 잘못된 번호입니다.");
                pause();
                return;
            }

            Program removed = list.remove(index);
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < list.size(); i++) {
                Program p = list.get(i);
                sb.append(String.format("  {\"name\":\"%s\",\"url\":\"%s\",\"saveAs\":\"%s\",\"checkPath\":\"%s\",\"installCommand\":\"%s\"}",
                        p.name, p.url, p.saveAs, p.checkPath, p.installCommand));
                if (i < list.size() - 1) sb.append(",\n");
            }
            sb.append("\n]");
            Files.writeString(Paths.get(INSTALL_LIST), sb.toString(), StandardOpenOption.TRUNCATE_EXISTING);

            System.out.printf("[성공] '%s' 프로그램이 삭제되었습니다.\n", removed.name);
        } catch (NumberFormatException e) {
            System.out.println("[오류] 숫자를 입력해주세요.");
        } catch (IOException e) {
            System.out.println("[오류] 파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        pause();
    }
    
    /*LOG */
    public static void log(String message) {
        try {
            Files.createDirectories(Paths.get("logs"));
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream("logs/logs.txt", true),
                    StandardCharsets.UTF_8
                )
            );
            writer.write("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + message);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("[오류] 로그 파일 저장 실패: " + e.getMessage());
        }
    }


    public static void printAllLogs(String unused) {
        clearConsole();
        try {
            List<String> logs = Files.readAllLines(Paths.get(LOG_FILE));
            if (logs.isEmpty()) {
                System.out.println("[정보] 로그 파일이 비어 있습니다.");
            } else {
                logs.forEach(System.out::println);
            }
        } catch (IOException e) {
            System.out.println("[오류] 로그 파일을 읽는 중 문제가 발생했습니다: " + e.getMessage());
        }
        pause();
    }

    public static void printLogsByType(String unused, Scanner sc) {
    System.out.print("필터링할 로그 유형을 입력하세요 ([성공], [실패], [건너뜀], [경고], [오류]) >> ");
    String ftype = sc.nextLine().trim();
    String type = "[" + ftype + "]";

    if (!type.matches("\\[[가-힣]+]")) {
        System.out.println("[경고] 올바른 로그 유형 형식이 아닙니다.");
        pause();
        return;
    }

    try {
        System.out.println("'"+type+"'"+" 에 대한 검색 결과");
        List<String> logs = Files.readAllLines(Paths.get(LOG_FILE));
        List<String> filtered = logs.stream()
            .filter(line -> line.contains(type))
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("[정보] 해당 유형의 로그가 없습니다.");
        } else {
            filtered.forEach(System.out::println);
        }
    } catch (IOException e) {
        System.out.println("[오류] 로그 파일을 읽는 중 문제가 발생했습니다: " + e.getMessage());
    }
    pause();
    }
        
    public static void printLogsByDate(String unused, Scanner sc) {
    System.out.print("필터링할 날짜를 입력하세요 (예: 2025-04-07) >> ");
    String date = sc.nextLine().trim();

    try {
        System.out.println(date+"에 대한 검색 결과");
        LocalDate.parse(date); // 유효한 날짜인지 확인
        List<String> logs = Files.readAllLines(Paths.get(LOG_FILE));
        List<String> filtered = logs.stream()
            .filter(line -> line.startsWith(date))
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("[정보] 해당 날짜의 로그가 없습니다.");
        } else {
            filtered.forEach(System.out::println);
        }
    } catch (Exception e) {
        System.out.println("[경고] 잘못된 날짜 형식입니다.");
    }
    pause();
    }
        
    public static void printLogsByKeyword(String unused, Scanner sc) {
        System.out.print("검색할 키워드를 입력하세요 >> ");
        String keyword = sc.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("[경고] 키워드는 비워둘 수 없습니다.");
            pause();
            return;
        }
        else{
            try {
                System.out.println("'"+keyword+"'"+" 에 대한 검색 결과");
                List<String> logs = Files.readAllLines(Paths.get(LOG_FILE));
                List<String> filtered = logs.stream()
                    .filter(line -> line.contains(keyword))
                    .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    System.out.println("[정보] 해당 키워드가 포함된 로그가 없습니다.");
                } else {
                    filtered.forEach(System.out::println);
                }
            } catch (IOException e) {
                System.out.println("[오류] 로그 파일을 읽는 중 문제가 발생했습니다: " + e.getMessage());
            }
            pause();
        }
    }
        
    /*Console */

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

    public static void pause() {
        System.out.println("\n[정보] 엔터를 눌러 계속하십시오...");
        scanner.nextLine();
    }

    public static String readPassword(String prompt, Scanner sc) {
        System.out.print(prompt);
        return sc.nextLine(); // IDE 환경에서도 호환성 있게 처리
    }

    //설치 결과 저장용 리스트
    private static List<ProgramInstallResult> resultList = new ArrayList<>();

    public static List<ProgramInstallResult> getResultList() {
        return resultList;
    }
    public static void printDetailedSummary() {
        if (resultList.isEmpty()) {
            System.out.println("[정보] 설치된 프로그램이 없습니다.");
            return;
        }
    
        System.out.println("\n[설치 요약 리포트]");
        System.out.println("────────────────────────────────────────────");
        System.out.printf("%-25s %-8s %-10s %-30s%n", "프로그램", "상태", "경과(초)", "비고");
        System.out.println("────────────────────────────────────────────");
    
        int successCount = 0, failCount = 0, skipCount = 0;
    
        for (ProgramInstallResult result : resultList) {
            String name = result.getName();
            String status = result.getStatus();
            long time = result.getElapsedSeconds();
            String reason = result.getReason();
    
            switch (status) {
                case "성공" -> successCount++;
                case "실패" -> failCount++;
                case "건너뜀" -> skipCount++;
            }
    
            System.out.printf("%-25s %-8s %-10d %-30s%n", name, status, time, reason);
        }
    
        System.out.println("────────────────────────────────────────────");
        System.out.printf("총 %d개 중 %d개 성공, %d개 실패, %d개 건너뜀%n",
            resultList.size(), successCount, failCount, skipCount);
        System.out.println("────────────────────────────────────────────");
    }
    public static void writeSummaryToLog() {
        if (resultList.isEmpty()) {
            return;
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("\n[설치 요약 리포트]").append(System.lineSeparator());
        sb.append("────────────────────────────────────────────").append(System.lineSeparator());
        sb.append(String.format("%-25s %-8s %-10s %-30s%n", "프로그램", "상태", "경과(초)", "비고"));
        sb.append("────────────────────────────────────────────").append(System.lineSeparator());
    
        int successCount = 0, failCount = 0, skipCount = 0;
    
        for (ProgramInstallResult result : resultList) {
            String name = result.getName();
            String status = result.getStatus();
            long time = result.getElapsedSeconds();
            String reason = result.getReason();
    
            switch (status) {
                case "성공" -> successCount++;
                case "실패" -> failCount++;
                case "건너뜀" -> skipCount++;
            }
    
            sb.append(String.format("%-25s %-8s %-10d %-30s%n", name, status, time, reason));
        }
    
        sb.append("────────────────────────────────────────────").append(System.lineSeparator());
        sb.append(String.format("총 %d개 중 %d개 성공, %d개 실패, %d개 건너뜀%n",
            resultList.size(), successCount, failCount, skipCount));
        sb.append("────────────────────────────────────────────").append(System.lineSeparator());
    
        log(sb.toString());
    }
    public static void writeSummaryToFile() {
        if (resultList.isEmpty()) return;
    
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File reportFile = new File("logs/report_" + timestamp + ".txt");
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            writer.println("[설치 요약 리포트]");
            writer.println("────────────────────────────────────────────");
            writer.printf("%-25s %-8s %-10s %-30s%n", "프로그램", "상태", "경과(초)", "비고");
            writer.println("────────────────────────────────────────────");
    
            int successCount = 0, failCount = 0, skipCount = 0;
    
            for (ProgramInstallResult result : resultList) {
                String name = result.getName();
                String status = result.getStatus();
                long time = result.getElapsedSeconds();
                String reason = result.getReason();
    
                switch (status) {
                    case "성공" -> successCount++;
                    case "실패" -> failCount++;
                    case "건너뜀" -> skipCount++;
                }
    
                writer.printf("%-25s %-8s %-10d %-30s%n", name, status, time, reason);

            }
    
            writer.println("────────────────────────────────────────────");
            writer.printf("총 %d개 중 %d개 성공, %d개 실패, %d개 건너뜀%n",
                    resultList.size(), successCount, failCount, skipCount);
            writer.println("────────────────────────────────────────────");
    
            System.out.println("[정보] 설치 요약 리포트가 logs 폴더에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("[오류] 설치 리포트 파일 저장 실패: " + e.getMessage());
        }
    }   
}