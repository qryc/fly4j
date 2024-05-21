package farticle.domain.entity;

public class DtreeObj {
    private int id;
    private int pid;
    private String name;
    private String url;
    private String target = "";
    private String icon = "";
    private String iconOpen = "";
    private boolean open = false;

    public DtreeObj(int id, int pid, String name, String url, String target, String icon, String iconOpen, boolean open) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.url = url;
        this.target = target;
        this.icon = icon;
        this.iconOpen = iconOpen;
        this.open = open;
    }

    public DtreeObj(int id, int pid, String name, String url, String target) {
        this.id = id;
        this.pid = pid;
        this.name = name.replaceAll("\\.flyNote", "");
        this.url = url;
        this.target = target;
    }

    public DtreeObj(int id, int pid, String name, String url) {
        if (name.length() > 20) {
            name = name.substring(0, 20);
        }
        this.id = id;
        this.pid = pid;
        this.name = name.replaceAll(".flyNote", "");
        this.url = url;
    }

    public DtreeObj(int id, int pid, String name, String url, boolean trim) {
        if (trim) {
            if (name.length() > 20) {
                name = name.substring(0, 20);
            }
        }
        this.id = id;
        this.pid = pid;
        this.name = name.replaceAll(".flyNote", "");
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIconOpen(String iconOpen) {
        this.iconOpen = iconOpen;
    }

    public String getIconOpen() {
        return iconOpen;
    }

    public DtreeObj setOpen(boolean open) {
        this.open = open;
        return this;
    }

    public boolean isOpen() {
        return open;
    }
}
