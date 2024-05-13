package international;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * UTC(世界标准时间)
 * 协调世界时，又称世界标准时间或世界协调时间，简称UTC（从英文“Coordinated Universal Time”／法文“Temps
 * Universel Coordonné”而来），是最主要的世界时间标准，其以原子时秒长为基础，在时刻上尽量接近于格林尼治标准时间。
 * GMT(格林尼治平时)
 * 是指位于英国伦敦郊区的皇家格林尼治天文台的标准时间，因为本初子午线被定义在通过那里的经线。
 * 在它的椭圆轨道里的运动速度不均匀，这个时刻可能与实际的太阳时有误差，最大误差达16分钟。
 * 因此格林尼治时间已经不再被作为标准时间使用。
 * 现在的标准时间，是由原子钟报时的协调世界时（UTC）。
 *
 * Java Date使用UTC时间，如Tue Nov 11 16:59:08 CST 2014，CST表示China Standard Time UT+8:00
 *
 * getTime（）该时刻距离1970年1月1日0点经过了1503542885955毫秒。
 */
public class InternationalTimeInJava {
    public static void main(String[] args) throws Exception {
        testTimeZone1();
//        testTimeZone2();
//        printAll();


    }

    private static void testTimeZone1() {
        //设置时区
        Date now=new Date();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat utcSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区

        SimpleDateFormat tokyoSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 东京
        tokyoSdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));  // 设置东京时区

        SimpleDateFormat londonSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 伦敦
        londonSdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));  // 设置伦敦时区

        SimpleDateFormat t1Sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 泰国
        t1Sdf.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));  //

        SimpleDateFormat t2Sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 泰国
        t2Sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));  //


        System.out.println("毫秒数:" + now.getTime() + ", UTC时间:" + utcSdf.format(now));
        System.out.println("毫秒数:" + now.getTime() + ", 北京时间:" + bjSdf.format(now));
        System.out.println("毫秒数:" + now.getTime() + ", 东京时间:" + tokyoSdf.format(now));
        System.out.println("毫秒数:" + now.getTime() + ", 伦敦时间:" + londonSdf.format(now));

        System.out.println("毫秒数:" + now.getTime() + ", 泰国时间:" + t1Sdf.format(now));
        System.out.println("毫秒数:" + now.getTime() + ", 泰国时间:" + t2Sdf.format(now));



    }

    private static void printAll() {
        String[] ids = TimeZone.getAvailableIDs();
        for (String id:ids){
            System.out.println(id+", ");
        }
    }

    private static  void testTimeZone2(){
        TimeZone timeZone1 = TimeZone.getDefault();//获取当前服务器时区
        TimeZone timeZone2 = TimeZone.getTimeZone("Asia/Shanghai");//获取上海时区
        TimeZone timeZone3 = TimeZone.getTimeZone("GMT");//获取格林威治标准时区
        TimeZone timeZone4 = TimeZone.getTimeZone("GMT+8");//获取东八区时区
        TimeZone timeZone5 = TimeZone.getTimeZone("UTC");//获取UTC标准时间
        TimeZone timeZone6 = TimeZone.getTimeZone("CST");//获取CST时区

        System.out.println(timeZone1.getRawOffset());
        System.out.println(timeZone2.getRawOffset());
        System.out.println(timeZone3.getRawOffset());
        System.out.println(timeZone4.getRawOffset());
        System.out.println(timeZone5.getRawOffset());
        System.out.println(timeZone6.getRawOffset());
    }
}
