package farticle.domain.view;


import farticle.domain.consts.FlyConst;
import farticle.domain.entity.ArticleOrganize;
import fnote.domain.config.FlyContext;

import java.io.File;

class TreeFilePredicate implements FileFilter {
    FlyContext flyContext;


    @Override
    public boolean test(File cfile,FlyContext flyContext) {
        if (cfile.isDirectory()) {
            //公司空间不展示
            if (ArticleOrganize.WORKSPACE_COMPANY.equals(flyContext.clientConfig().getWorkspace())) {
                if (FlyConst.workNotDisplayFileNames.contains(cfile.getName())) {
                    return false;
                }
                if (cfile.getName().contains("9999")) {
                    return false;
                }
            }
            if (cfile.getName().equals("pic") || cfile.getName().equals("picture")) {
                return false;
            } else {
                return true;
            }
        } else {
            if (cfile.getName().endsWith(".md") || cfile.getName().endsWith("flyNote") || cfile.getName().endsWith("txt")
                    || cfile.getName().endsWith("java")|| cfile.getName().endsWith("xml")|| cfile.getName().endsWith("properties")) {
                return true;
            } else {
                return false;
            }
        }
    }
}
