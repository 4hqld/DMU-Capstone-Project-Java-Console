package autoinstaller;

import java.io.File;
import java.util.*;

public class Installer {
    public static void run(Scanner sc) {
        Utils.clearConsole();
        if (!Utils.checkEnvironment()) {
            System.out.println("[오류] 환경 요구사항을 충족하지 못했습니다.");
            return;
        }

        List<Program> list = Utils.loadPrograms();
        if (list.isEmpty()) {
            System.out.println("[오류] 설치 목록이 비어 있습니다.");
            return;
        }

        System.out.println("설치할 프로그램을 선택하세요:");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, list.get(i).name);
        }

        System.out.print("예: 1,3 또는 all >> ");
        String selectionInput = sc.nextLine();
        Set<Integer> selections = Utils.parseSelection(selectionInput, list.size());
        if (selections.isEmpty()) {
            System.out.println("[경고] 선택이 올바르지 않습니다.");
            Utils.pause();
            Utils.clearConsole();
            run(sc);
            return;
        }

        int total = selections.size();
        int count = 0;

        for (int i : selections) {
            Program p = list.get(i);
            count++;
            System.out.printf("[%d/%d] %s 설치 진행 중...\n", count, total, p.name);

            long installStart = System.currentTimeMillis();

            if (new File(p.checkPath).exists()) {
                System.out.println("[정보] 이미 설치됨: " + p.name);
                Utils.log("[건너뜀] " + p.name + " (이미 설치됨)");
                Utils.getResultList().add(new ProgramInstallResult(p.name, "건너뜀", 0, "이미 설치됨"));
                continue;
            }

            if (!Utils.downloadInstaller(p)) {
                long elapsed = (System.currentTimeMillis() - installStart) / 1000;
                Utils.log("[실패] " + p.name + " 다운로드 실패");
                Utils.getResultList().add(new ProgramInstallResult(p.name, "실패", elapsed, "다운로드 실패"));
                continue;
            }

            boolean success = Utils.executeCommand(p.installCommand);
            long elapsed = (System.currentTimeMillis() - installStart) / 1000;

            Utils.log("[" + (success ? "성공" : "실패") + "] " + p.name + " 설치");
            System.out.println("[정보] 설치 " + (success ? "완료" : "실패") + ": " + p.name);

            Utils.getResultList().add(new ProgramInstallResult(
                p.name,
                success ? "성공" : "실패",
                elapsed,
                success ? "" : "설치 명령 실행 실패"
            ));
        }

        Utils.printDetailedSummary();  // 요약 리포트 출력
        Utils.writeSummaryToLog();      // 로그 파일 저장
        Utils.writeSummaryToFile();     // 별도 리포트 파일 저장
        Utils.promptRestart(sc);       // 재시작 여부 확인
    }
}
