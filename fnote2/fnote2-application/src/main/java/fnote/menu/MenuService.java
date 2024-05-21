package fnote.menu;

import fly4j.common.domain.IExtMap;

import java.util.*;

/**
 * Created by qryc on 2021/11/8
 */
public class MenuService {
    private List<MenuItem> menuItems;
    private List<String> contextVariables;


    public interface ELValidate {
        boolean validate(MenuContext menuContext);

    }

    /**
     * 渲染菜单需要的上下文信息
     */
    public static class MenuContext implements IExtMap<MenuContext> {
        Map<String, String> contentMap = new HashMap<>();


        @Override
        public Map<String, String> getExtMap() {
            return contentMap;
        }

        public MenuContext setExtMap(Map<String, String> extMap) {
            this.contentMap = extMap;
            return this;
        }
    }

    boolean validateRole(MenuItem item, MenuContext menuContext) {
        for (String role : item.roles) {
            if (role.equals(menuContext.getExtStringValue("role"))) {
                return true;
            }
        }
        return false;
    }

    public Menu getMenu(MenuContext menuContext) {
        Menu menu = new Menu();
        if (!menuContext.getExtBooleanValue("isLogin")) {
            return menu;
        }
        for (MenuItem menuItem : this.getMenuItems()) {
            if (!validateRole(menuItem, menuContext)) {
                continue;
            }
            if (menuItem.elValidate != null && !menuItem.elValidate.validate(menuContext)) {
                continue;
            }
            var href = menuItem.href;
            if (null != contextVariables) {
                for (var contextVariable : contextVariables) {
                    if (href.contains("$" + contextVariable)) {
                        href = href.replaceAll("\\$" + contextVariable, "" + menuContext.getExtStringValue(contextVariable));
                    }
                }
            }

            if (menuItem.position.contains("menu")) {
                menu.addMenuItem(new MenuItemVO(menuItem.title, href));
            }
            if (menuItem.position.contains("down")) {
                menu.addDownMenuItem(new MenuItemVO(menuItem.title, href));
            }
            if (menuItem.position.contains("siteMap")) {
                menu.addSitemapItem(new MenuItemVO(menuItem.title, href));
            }


        }
        menu.showDownMenu = true;
        return menu;
    }


    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setContextVariables(List<String> contextVariables) {
        this.contextVariables = contextVariables;
    }

    public List<String> getContextVariables() {
        return contextVariables;
    }
}
