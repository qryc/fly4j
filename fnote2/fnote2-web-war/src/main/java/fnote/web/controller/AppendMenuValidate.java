package fnote.web.controller;

import fnote.menu.MenuService;

public class AppendMenuValidate implements MenuService.ELValidate {
    @Override
    public boolean validate(MenuService.MenuContext menuContext) {
        //只读工作空间不可编辑
        return null != menuContext.getExtStringValue("noteFileStr");
    }

}
