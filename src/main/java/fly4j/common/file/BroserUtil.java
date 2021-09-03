package fly4j.common.file;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by qryc on 2017-12-22.
 */
public class BroserUtil {





    /**
     * Returns the Mime Type of the file, depending on the extension of the filename
     */
    public static String getMimeType(String fName) {
        fName = fName.toLowerCase();
        if (fName.endsWith(".jpg") || fName.endsWith(".jpeg") || fName.endsWith(".jpe")) return "image/jpeg";
        else if (fName.endsWith(".gif")) return "image/gif";
        else if (fName.endsWith(".pdf")) return "application/pdf";
        else if (fName.endsWith(".htm") || fName.endsWith(".html") || fName.endsWith(".shtml")) return "text/html";
        else if (fName.endsWith(".avi")) return "video/x-msvideo";
        else if (fName.endsWith(".mov") || fName.endsWith(".qt")) return "video/quicktime";
        else if (fName.endsWith(".mpg") || fName.endsWith(".mpeg") || fName.endsWith(".mpe")) return "video/mpeg";
        else if (fName.endsWith(".zip")) return "application/zip";
        else if (fName.endsWith(".tiff") || fName.endsWith(".tif")) return "image/tiff";
        else if (fName.endsWith(".rtf")) return "application/rtf";
        else if (fName.endsWith(".mid") || fName.endsWith(".midi")) return "audio/x-midi";
        else if (fName.endsWith(".xl") || fName.endsWith(".xls") || fName.endsWith(".xlv")
                || fName.endsWith(".xla") || fName.endsWith(".xlb") || fName.endsWith(".xlt")
                || fName.endsWith(".xlm") || fName.endsWith(".xlk")) return "application/excel";
        else if (fName.endsWith(".doc") || fName.endsWith(".dot")) return "application/msword";
        else if (fName.endsWith(".png")) return "image/png";
        else if (fName.endsWith(".xml")) return "text/xml";
        else if (fName.endsWith(".svg")) return "image/svg+xml";
        else if (fName.endsWith(".mp3")) return "audio/mp3";
        else if (fName.endsWith(".ogg")) return "audio/ogg";
        else return "text/plain";
    }

    /**
     * Converts some important chars (int) to the corresponding html string
     */
    static String conv2Html(int i) {
        if (i == '&') return "&amp;";
        else if (i == '<') return "&lt;";
        else if (i == '>') return "&gt;";
        else if (i == '"') return "&quot;";
        else return "" + (char) i;
    }

    /**
     * Converts a normal string to a html conform string
     */
    public static String conv2Html(String st) {
        if(StringUtils.isEmpty(st)){
            return st;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < st.length(); i++) {
            buf.append(conv2Html(st.charAt(i)));
        }
        return buf.toString();
    }


//    /**
//     * Converts a dir string to a linked dir string
//     *
//     * @param dir         the directory string (e.g. /usr/local/httpd)
//     * @param browserLink web-pathStr to Browser.jsp
//     */
//    public static String dir2linkdir(String dir, String browserLink, int sortMode) {
//        File f = new File(dir);
//        StringBuffer buf = new StringBuffer();
//        while (f.getParentFile() != null) {
//            if (f.canRead()) {
//                String encPath = URLEncoder.encode(f.getAbsolutePath());
//                buf.insert(0, "<a href=\"" + browserLink + "?sort=" + sortMode + "&amp;dir="
//                        + encPath + "\">" + conv2Html(f.getName()) + File.separator + "</a>");
//            } else buf.insert(0, conv2Html(f.getName()) + File.separator);
//            f = f.getParentFile();
//        }
//        if (f.canRead()) {
//            String encPath = URLEncoder.encode(f.getAbsolutePath());
//            buf.insert(0, "<a href=\"" + browserLink + "?sort=" + sortMode + "&amp;dir=" + encPath
//                    + "\">" + conv2Html(f.getAbsolutePath()) + "</a>");
//        } else buf.insert(0, f.getAbsolutePath());
//        return buf.toString();
//    }

    /**
     * Returns true if the given filename tends towards a packed file
     */
    public static boolean isPacked(String name, boolean gz) {
        return (name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".jar")
                || (gz && name.toLowerCase().endsWith(".gz")) || name.toLowerCase()
                .endsWith(".war"));
    }


}
