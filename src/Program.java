public class Program {
    public String name;
    public String url;
    public String saveAs;
    public String checkPath;
    public String installCommand;

    public Program(String name, String url, String saveAs, String checkPath, String installCommand) {
        this.name = name;
        this.url = url;
        this.saveAs = saveAs;
        this.checkPath = checkPath;
        this.installCommand = installCommand;
    }
}
