package fnote.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    @Override
    public String toString() {
        return "Menu{" +
                "showDownMenu=" + showDownMenu +
                ", menuItems=" + menuItems +
                ", downMenuItems=" + downMenuItems +
                ", sitemapItems=" + sitemapItems +
                '}';
    }

    //是否显示下拉菜单
    protected boolean showDownMenu = false;
    //菜单列表
    protected List<MenuItemVO> menuItems = new ArrayList<>();
    //下拉菜单列表
    protected List<MenuItemVO> downMenuItems = new ArrayList<>();
    //网站地图
    protected List<MenuItemVO> sitemapItems = new ArrayList<>();

    protected void addMenuItem(MenuItemVO menuItem) {
        menuItems.add(menuItem);
    }

    public List<MenuItemVO> getMenuItems() {
        return menuItems;
    }

    protected void addDownMenuItem(MenuItemVO menuItem) {
        downMenuItems.add(menuItem);
    }

    public List<MenuItemVO> getDownMenuItems() {
        return downMenuItems;
    }

    protected void addSitemapItem(MenuItemVO menuItem) {
        sitemapItems.add(menuItem);
    }

    public List<MenuItemVO> getSitemapItems() {
        return sitemapItems;
    }

    public boolean isShowDownMenu() {
        return showDownMenu;
    }
}
