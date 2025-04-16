package autoinstaller.Utils;

public class ProgramInstallResult {
    private String name;
    private String status;  // "성공", "실패", "건너뜀"
    private long elapsedSeconds;
    private String reason;  // 실패 또는 건너뜀 사유

    public ProgramInstallResult(String name, String status, long elapsedSeconds, String reason) {
        this.name = name;
        this.status = status;
        this.elapsedSeconds = elapsedSeconds;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public String getReason() {
        return reason;
    }
}
