# 1.How To Install 

1. Use  maven install fly4j-common, fly4j-common is the common jar.
2. Use maven install fnote2, it can generate the war.
3. install apache tomcat server.
4. deploy the war on the tomcat server.
5. Run tomcat server,it's done.
6. type 127.0.0.1:port on you browser,and use the article manager system. 

## 2.project goals

Make a knowledge manager system ，it can help you improve yourself。

Simultaneously precipitate and accumulate basic technological frameworks，like fly4j-common,can surport other projects.

## 3.features

- article add ,edit,delete，search。
-  supprot markdown file,html file
- support upload pictures ,files.

## 4.modules-(fly4j-common)'s use guidelines

it contains base code:cache,file store,file back ....this features can support other projects

**cache**

```
//1000 is the maxSize
FlyCache flyCache = new FlyCacheJVM(1000);
//2 is the cache life
flyCache.put("a", "123", 2);
// it will be return 123
flyCache.get("akey");
// it will be return left life
flyCache.ttl("akey");
```

**LimitRate**

```
FlyCache flyCache = new FlyCacheJVM(1000);
// 20 is the between time ,2 is the limitNum
LimitRate limitRate = new LimitRateImpl(flyCache, 20, 2);
// return is limit
boolean isHot = limitRate.isHotLimit("127.0.0.1");
```
