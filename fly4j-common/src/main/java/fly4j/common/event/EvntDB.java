package fly4j.common.event;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by qryc on 2020/2/8.
 */
public class EvntDB {
    //简单使用静态变量存储所有消息事件
    private static Queue<AlterEvent> events = new LinkedList<>();
    //插入消息
    public static void offer(AlterEvent alterEvent) {
        events.offer(alterEvent);
    }
    //取出消息
    public static AlterEvent poll() {
        return events.poll();
    }

}
