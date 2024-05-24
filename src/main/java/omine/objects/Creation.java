package omine.objects;

import java.util.List;

public class Creation {

    private List<RequiredItem> item;
    private String permission;
    private List<String> commands;

    public Creation(List<RequiredItem> item, String permission, List<String> commands) {
        this.item = item;
        this.permission = permission;
        this.commands = commands;
    }

    public List<RequiredItem> getItem() {
        return item;
    }

    public void setItem(List<RequiredItem> item) {
        this.item = item;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
