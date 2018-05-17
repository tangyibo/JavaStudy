
package cn.com.ruijie.test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import redis.clients.jedis.*;

public class TestRedis {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("172.16.56.119",6379);
        System.out.println("连接成功");

        //////set,get
        jedis.set("runoobkey", "www.runoob.com");
        System.out.println("redis 存储的字符串为: " + jedis.get("runoobkey"));

        //////lpush
        //存储数据到列表中
        jedis.lpush("site-list", "Runoob");
        jedis.lpush("site-list", "Google");
        jedis.lpush("site-list", "Taobao");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0, 2);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("列表项为: " + list.get(i));
        }
        
        ////////keys *
        // 获取数据并输出
        Set<String> keys = jedis.keys("*"); 
        Iterator<String> it=keys.iterator() ;   
        while(it.hasNext()){   
            String key = it.next();   
            System.out.println(key);   
        }
    }
}
