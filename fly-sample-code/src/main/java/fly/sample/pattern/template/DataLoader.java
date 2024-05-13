package fly.sample.pattern.template;

/**
 * Created by qryc on 2021/11/1
 */
public abstract class DataLoader<T> {
    public abstract T getObjectFromCache(Long id);

    public abstract T getObjectFromDb(Long id);

    public abstract void setObjectToCache(Long id, T Object);

    public T getObject(Long id) {
        //先从cache中取
        T object = this.getObjectFromCache(id);
        if (null != object) {
            //缓存中渠道直接返回
            return object;
        }
        //缓存中取不到，再从db取
        object = this.getObjectFromDb(id);
        //把DB取到的结果放进缓存，此处只是示例，真实线上需要对null进行占位，不然会不存在数据一直击穿到数据库
        if (null != object) {
            this.setObjectToCache(id, object);
        }
        return object;
    }
}
