package com.feecn.org.builder;

import com.feecn.org.core.ConstantParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Properties;

/**
 *
 * @author pczhangyu
 * @date 2018/10/16
 */
public class JedisBuilder{

    private final static Logger logger = LoggerFactory.getLogger(JedisBuilder.class);

    private static Jedis jedis;

    static final String REDIS_CONFIG = "application.properties";

    static {
        Properties constant = ConstantParam.loadProp(REDIS_CONFIG);
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(Integer.valueOf(constant.getProperty("redis.maxIdle", "100")));
        poolConfig.setMaxTotal(Integer.valueOf(constant.getProperty("redis.maxTotal", "100")));
        poolConfig.setTimeBetweenEvictionRunsMillis(-1);
        poolConfig.setTestOnBorrow(true);
        String host = constant.getProperty("redis.host");
        int port = Integer.valueOf(constant.getProperty("redis.port"));
        int timeout = Integer.valueOf(constant.getProperty("redis.timeout"));
        String pwd = constant.getProperty("redis.pass");
        int db = Integer.valueOf(constant.getProperty("redis.db", "0"));
        JedisPool jedisPool;
        if (pwd == null || pwd.isEmpty()) {
            jedisPool = new JedisPool(poolConfig, host, port, timeout);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port, timeout, pwd, db);
        }
        if (jedis==null){
            jedis = jedisPool.getResource();
        }
    }

    /**
      LPUSH key value [value ...]

     将一个或多个值 value 插入到列表 key 的表头

     如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。

     如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。

     当 key 存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values
     * @return
     */
    public static Long lpush (String key ,String... values){
        return jedis.lpush(key,values);
    }

    /**
     * BRPOP key [key ...] timeout

     BRPOP 是列表的阻塞式(blocking)弹出原语。

     它是 RPOP 命令的阻塞版本，当给定列表内没有任何元素可供弹出的时候，连接将被 BRPOP 命令阻塞，直到等待超时或发现可弹出元素为止。

     当给定多个 key 参数时，按参数 key 的先后顺序依次检查各个列表，弹出第一个非空列表的尾部元素。

     关于阻塞操作的更多信息，请查看 BLPOP 命令， BRPOP 除了弹出元素的位置和 BLPOP 不同之外，其他表现一致。
     * @param timeOut
     * @param keys
     * @return
     */
    public static List<String> brpop(int timeOut , String... keys){
        List<String> brpop = jedis.brpop(timeOut, keys);
        if (brpop!=null){
            return brpop;
        }
        return null;
    }

    public static void main(String[] args) {

        class PushKey implements Runnable{
            @Override
            public void run() {
                while (true){
                    JedisBuilder.lpush("test_key",String.valueOf(System.currentTimeMillis()));
                }
            }
        }

//        class Rpop implements Runnable{
//            @Override
//            public void run() {
//                while (true){
//                    List<String> test_key = JedisBuilder.brpop(0, "test_key");
//                    logger.info(test_key.toString());
//                }
//
//            }
//        }

        Thread thread1 = new Thread(new PushKey());
        thread1.setDaemon(true);
        thread1.run();
//
//        Thread thread2 = new Thread(new Rpop());
//        thread2.setDaemon(true);
//        thread2.run();

    }

}
