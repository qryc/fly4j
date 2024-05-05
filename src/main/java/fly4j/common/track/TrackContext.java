package fly4j.common.track;

/**
 * 保存程序上下文信息
 * Created by qryc on 2018-01-11.
 */
public class TrackContext {
    public static String SPLIT = "----";
    private static ThreadLocal<TrackContext> threadLocal = new ThreadLocal<TrackContext>();


    public static void reset() {
        threadLocal.set(new TrackContext());
    }

    /**
     * 业务跟踪
     */
    private StringBuilder trackInfoStrBuilder = new StringBuilder();

    public static String getTrackInfo() {
        if(threadLocal.get()==null)
            return "";
        return threadLocal.get().trackInfoStrBuilder.toString();
    }

    public static void appendTrackInfo(String traceStr) {
        if (null == threadLocal.get())
            reset();
        threadLocal.get().trackInfoStrBuilder.append(traceStr).append(SPLIT);
        System.out.println(traceStr);
    }
}
