package fnote.filebrowser.core;

/**
 * Created by qryc on 2017-12-22.
 */

import java.io.File;
import java.util.Comparator;

/**
 * This class is a comparator to sortModes the filenames and dirs
 */
public class FileComp implements Comparator<File> {

    private final int mode; //排序模式
    private final int sign;//正序或倒序

    public FileComp() {
        this.mode = 1;
        this.sign = 1;
    }

    /**
     * @param mode sortModes by 1=Filename, 2=Size, 3=Date, 4=Type
     *             The default sorting method is by Name
     *             Negative mode means descending sortModes
     */
    public FileComp(int mode) {
        if (mode < 0) {
            this.mode = -mode;
            sign = -1;
        } else {
            this.mode = mode;
            this.sign = 1;
        }
    }

    public int compare(File f1, File f2) {
        if (f1.isDirectory()) {
            if (f2.isDirectory()) {
                return switch (mode) {
                    //Filename or Type
                    case 1, 4 -> sortByName(f1, f2);
                    //Filesize
                    case 2 -> sortBySize(f1, f2);
                    //Date
                    case 3 -> sortByModified(f1, f2);
                    default -> 1;
                };
            } else {
                return -1;
            }
        } else {
            if (f2.isDirectory()) {
                return 1;
            } else {
                return switch (mode) {
                    case 1 -> sortByName(f1, f2);
                    //Filesize
                    case 2 -> sortBySize(f1, f2);
                    //Date
                    case 3 -> sortByModified(f1, f2);
                    case 4 -> sortByExtension(f1, f2);// Sort by extension
                    default -> 1;
                };
            }
        }
    }

    private int sortByModified(File f1, File f2) {
        return sign * Long.valueOf(f1.lastModified()).compareTo(Long.valueOf(f2.lastModified()));
    }

    private int sortBySize(File f1, File f2) {
        return sign * (Long.valueOf(f1.length()).compareTo(Long.valueOf(f2.length())));
    }

    private int sortByName(File f1, File f2) {
        return sign * f1.getAbsolutePath().toUpperCase().compareTo(
                f2.getAbsolutePath().toUpperCase());
    }

    private int sortByExtension(File f1, File f2) {
        int tempIndexf1 = f1.getAbsolutePath().lastIndexOf('.');
        int tempIndexf2 = f2.getAbsolutePath().lastIndexOf('.');
        if ((tempIndexf1 == -1) && (tempIndexf2 == -1)) { // Neither have an extension
            return sign
                    * f1.getAbsolutePath().toUpperCase().compareTo(
                    f2.getAbsolutePath().toUpperCase());
        }
        // f1 has no extension
        else if (tempIndexf1 == -1) return -sign;
            // f2 has no extension
        else if (tempIndexf2 == -1) return sign;
            // Both have an extension
        else {
            String tempEndf1 = f1.getAbsolutePath().toUpperCase()
                    .substring(tempIndexf1);
            String tempEndf2 = f2.getAbsolutePath().toUpperCase()
                    .substring(tempIndexf2);
            return sign * tempEndf1.compareTo(tempEndf2);
        }
    }
}
