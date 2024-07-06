package fnote.web.controller;

import fnote.menu.MenuService;

public class AddArticleMenuValidate implements MenuService.ELValidate {

    @Override
    public boolean validate(MenuService.MenuContext menuContext) {
        if (menuContext.getExtBooleanValue("isPC")) {
            //PC电脑，展示添加按钮
            return true;
        }
        //只有详情页展示添加按钮
        return null != menuContext.getExtStringValue("noteFileStr");
    }

}
