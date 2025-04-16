package autoinstaller.Utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Program {
    @JsonProperty("name")
    public String name;
    @JsonProperty("url")
    public String url;
    @JsonProperty("saveAs")
    public String saveAs;
    @JsonProperty("checkPath")
    public String checkPath;
    @JsonProperty("installCommand")
    public String installCommand;

    @JsonCreator
    public Program(
        @JsonProperty("name") String name,
        @JsonProperty("url") String url,
        @JsonProperty("saveAs") String saveAs,
        @JsonProperty("checkPath") String checkPath,
        @JsonProperty("installCommand") String installCommand) {
        this.name = name;
        this.url = url;
        this.saveAs = saveAs;
        this.checkPath = checkPath;
        this.installCommand = installCommand;
    }
}
