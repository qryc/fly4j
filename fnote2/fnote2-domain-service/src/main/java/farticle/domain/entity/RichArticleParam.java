package farticle.domain.entity;

public class RichArticleParam {
    private String noteFileStr;
    private String pin;
    private String title;
    private String content;
    private String workspace;

    public String getTitle() {
        return title;
    }

    public RichArticleParam setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public RichArticleParam setContent(String content) {
        this.content = content;
        return this;
    }


    public RichArticleParam setNoteFileStr(String noteFileStr) {
        this.noteFileStr = noteFileStr;
        return this;
    }

    public String getNoteFileStr() {
        return noteFileStr;
    }

    public RichArticleParam setPin(String pin) {
        this.pin = pin;
        return this;
    }

    public String getPin() {
        return pin;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }
}
