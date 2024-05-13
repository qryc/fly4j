load永远都是同步的，不管你是否使用异步包装：

https://blog.csdn.net/xxcupid/article/details/92834521
本文基于的guava版本是19.0

使用guava cache的时候，在cache中没有值或者值需要更新的时候，都需要去load，而这个load往往对应从数据库或者远程接口拿数据并缓存下来的操作。在高qps场景的服务中，这个load可能会导致调用链的阻塞，如果阻塞时间长，可能会影响服务，甚至可能拖垮服务，所以要了解哪些地方会阻塞，有没有什么方法能够尽量少的去阻塞。
一.同步load
1.load
load是第一次加载，加载之前cache中没有值。load永远都是同步的，不管是否使用异步进行包装，具体见下面第三部分。
对于同一个key，多次请求只会触发一次加载。比如，线程1访问key1，发现cache中不存在key1，然后触发key1的load。与此同时，在load加载完成之前，线程2，线程3...线程N都访问key1，这些访问不会再触发key1的加载，但是在key1的load加载完成之前，这些请求都会被hang在那里等待。调用链如下：

get:4952, LocalCache$LocalLoadingCache

getOrLoad:3967, LocalCache

get:3963, LocalCache

get:2046, LocalCache$Segment

lockedGetOrLoad:2140, LocalCache$Segment

waitForLoadingValue:2153, LocalCache$Segment

waitForValue:3571, LocalCache$LoadingValueReference
getUninterruptibly:168, Uninterruptibles->这里用future的get方法阻塞在这里，直到第一个触发load操作的线程完成load操作并将结果设置到该future中，这个get方法就会解除阻塞并获取到load到的值。
2.reload
reload是之前cache中有值，需要刷新该值，比如设置了过期时间后，到了缓存过期需要更新的时间，会触发scheduleRefresh去做刷新。手动调用refresh方法的时候也会触发reload。

3.refresh
手动调用了refresh，会导致loadingcache的重新load操作。调用的是Segment中的refresh方法，里面有loadAsync方法，LoadingValueReference的loadFuture中会根据之前cache中是否有值来决定load或者reload。

load永远都是同步的，不管你是否使用异步包装：
4.refreshAfterWrite
guava cache的所有更新操作都是依靠读写方法触发的，因为其内部没有时钟或者定时任务。比如上一次写之后超过了refresh设置的更新时间，但是之后没有cache的访问了，那么下次get的时候才会触发refresh，这次触发会导致get的阻塞。
这里会有检测，如果当前key已经在loading状态，那么refresh直接返回null，不会阻塞。

而loading又需要时间，所以在loading完毕之前，其他get方法拿到的都是旧值。

二.异步load
CacheLoader.asyncReloading
这里的reload方法被包装成了一个future，内部用任务将reload操作包装为异步操作，所以在reload的时候会调用被封装为异步的方法：
这里调用不会被阻塞，所以即使是触发get方法，因为是封装为异步任务，所以也不会被阻塞。

但是load方法不同于reload，它还是阻塞的。

所以这里的异步优化，相对于上面的同步reload，只减少了“一个”线程的阻塞。