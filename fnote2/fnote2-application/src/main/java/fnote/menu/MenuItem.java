package fnote.menu;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MenuItem {
    String title;
    String href;
    String target;
    Collection<String> roles;
    String position;
    MenuService.ELValidate elValidate;

    public MenuItem() {
    }

    public MenuItem(String title, String href, String target, Collection<String> roles) {
        this.title = title;
        this.href = href;
        this.target = target;
        this.roles = roles;
    }

    public void setElValidate(MenuService.ELValidate elValidate) {
        this.elValidate = elValidate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setRoles(String rolesStr) {
        Set<String> roles = new HashSet<>();
        if (StringUtils.isNotBlank(rolesStr)) {
            Collections.addAll(roles, rolesStr.split(","));
        }
        this.roles = roles;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
