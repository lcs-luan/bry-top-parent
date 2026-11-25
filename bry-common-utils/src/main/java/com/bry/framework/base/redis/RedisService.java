package com.bry.framework.base.redis;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * redisTemplate封装
 */
@Component
@Slf4j
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<Object, Object> redisTemplateTwo;

    private static final String SET_EXPIRE_SCRIPT =
            "redis.call('SET', KEYS[1], ARGV[1])\n" +
                    "redis.call('EXPIREAT',  KEYS[1], ARGV[2])\n" +
                    "return true";

    private static final String GET_AND_DECR = "if tonumber(redis.call('get',KEYS[1]))>0 then redis.call('decr',KEYS[1]) return true else return false end";


    /**
     * 查询, 如果不存在则创建
     * 设置默认值
     *
     * @param key        key
     * @param supplier   supplier
     * @param expireTime 过期时间
     * @param <T>        T
     * @return 查询到的值或者表达式中的值
     */
    public <T extends Object> T getOrCreate(String key, Supplier<T> supplier, int expireTime) {
        T cached = get(key);
        if (cached != null) {
            if (cached instanceof RedisNullValue) {
                return null;
            }
            return cached;
        }
        if (supplier == null) {
            return null;
        }
        T supplierData = supplier.get();
        if (supplierData == null) {
            putNullValue(key, expireTime);
            return null;
        }
        set(key, supplierData, expireTime);
        return supplierData;
    }

    /**
     * 查询, 如果不存在则创建
     *
     * @param key        key
     * @param supplier   supplier
     * @param expireTime 过期时间
     * @param <T>        T
     * @return 查询到的值或者表达式中的值
     */
    public <T extends Object> T getOrCreateNoDefault(String key, Supplier<T> supplier, int expireTime) {
        T cached = get(key);
        if (cached != null) {
            return cached;
        }
        if (supplier == null) {
            return null;
        }
        T supplierData = supplier.get();
        if (supplierData == null) {
            return null;
        }
        set(key, supplierData, expireTime);
        return supplierData;
    }

    public void putNullValue(String key, int seconds) {
        redisTemplate.opsForValue().set(key, new RedisNullValue(), seconds, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("expire fail, key={}, time={}", key, time, e);
            return false;
        }
    }

    /**
     * 缓存指定时间失效
     *
     * @param key  键
     * @param date 时间
     * @return
     */
    public Boolean expireAt(String key, Date date) {
        try {
            return redisTemplate.expireAt(key, date);
        } catch (Exception e) {
            log.error("expireAt fail, key={}, date={}", key, date, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("hasKey fail, key={}", key, e);
            return false;
        }
    }

    /**
     * 判断key是否存在[安全方式]
     *
     * @param key 键
     * @return true 存在 false不存在 null异常
     */
    public Boolean hasKeySafe(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("hasKeySafe fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T extends Serializable> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("set fail, key={}, value={}", key, value, e);
            return false;
        }
    }

    /**
     * 设置key，校验是否存在
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean setNx(String key, Object value, Integer time) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("setNx fail, key={}, value={}, time={}", key, value, time, e);
            return false;
        }
    }

    /**
     * 设置key，校验是否存在，并设置过期时间（原子操作）
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean setAndExpireAt(String key, Object value, Integer time) {
        try {
            // 创建一个json的序列化方式
            RedisScript<Boolean> script = RedisScript.of(SET_EXPIRE_SCRIPT, Boolean.class);
            return redisTemplateTwo.execute(script, Collections.singletonList(key), value, time);
        } catch (Exception e) {
            log.error("setNx fail, key={}, value={}, time={}", key, value, time, e);
            return false;
        }
    }

    /**
     * 设置key，校验是否存在，并设置过期时间（原子操作）
     *
     * @param key
     * @return
     */
    public boolean getAndDecr(String key) {
        try {
            // 创建一个json的序列化方式
            RedisScript<Boolean> script = RedisScript.of(GET_AND_DECR, Boolean.class);
            return redisTemplateTwo.execute(script, Collections.singletonList(key));
        } catch (Exception e) {
            log.error("getAndDecr fail, key={}, value={}, time={}", key, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("set fail, key={}, value={}, time={}", key, value, time, e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================ hash =================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("hmset fail, key={}, map={}", key, map, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("hmset fail, key={}, map={},time={}", key, map, time, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("hset fail, key={}, item={}, value={}", key, item, value, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("hset fail, key={}, item={}, value={}, time={}", key, item, value, time, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("sGet fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("sHasKey fail, key={}, value={}", key, value, e);
            return false;
        }
    }

    /**
     * 根据key和value，从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKeyValue(String key, Object value) {
        try {
            Boolean member = redisTemplate.opsForSet().isMember(key, value);
            return Boolean.TRUE.equals(member);
        } catch (Exception e) {
            log.error("sHasKey fail, key={}, value={}", key, value, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("sSet fail, key={}, values={}", key, values, e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("sSetAndTime fail, key={}, time={}, values={}", key, time, values, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("sGetSetSize fail, key={}", key, e);
            return 0;
        }
    }

    /**
     * 判断是否包含  sismember
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Boolean contains(String key, Object... values) {
        try {
            Map<Object, Boolean> member = redisTemplate.opsForSet().isMember(key, values);
            return member != null && !member.isEmpty();
        } catch (Exception e) {
            log.error("contains fail, key={},values={}", key, values, e);
            return false;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("setRemove fail, key={}, values={}", key, values, e);
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("lGet fail, key={}, start={}, end={}", key, start, end, e);
            return null;
        }
    }

    public Object lLeftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("lLeftPop fail, key={}", key, e);
            return null;
        }
    }

    public Object lRightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("lRightPop fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("lGetListSize fail, key={}", key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("lGetIndex fail, key={}, index={}", key, index, e);
            return null;
        }
    }

    /**
     * 将对象插入list的尾部(rightPush)
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("list rightPush fail, key={}, value={}", key, value, e);
            return false;
        }
    }

    /**
     * 将对象插入list的头部(leftPush)
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean leftPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("list leftPush fail, key={}, value={}", key, value, e);
            return false;
        }
    }

    /**
     * 将一组对象插入list的头部(leftPush)
     *
     * @param key    键
     * @param values 值
     * @return
     */
    public boolean leftPushAll(String key, List<Object> values) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            return true;
        } catch (Exception e) {
            log.error("list leftPushAll fail, key={}, value={}", key, values, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object... value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("list rightPush fail, key={}", key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("rightPush fail, key={}, value={}, time={}", key, value, time, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("rightPushAll fail, key={}, value={}", key, value, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("list rightPushAll with time fail, key={}", key, e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("lUpdateIndex fail, key={}", key, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("lRemove fail, key={}", key, e);
            return 0;
        }
    }

    /**
     * 右边移除并向左边增加
     *
     * @param key   键
     * @param value 值
     */
    public Boolean rightPopAndLeftPush(String key, String value) {
        try {
            redisTemplate.opsForList().rightPopAndLeftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("rightPopAndLeftPush fail, key={}", key, e);
            return false;
        }
    }

    /**
     * 对一个列表进行修剪
     *
     * @param key   键
     * @param start 值
     * @param end   值
     */
    public void lTrim(String key, long start, long end) {
        try {
            this.redisTemplate.opsForList().trim(key, start, end);
        } catch (Exception var7) {
            log.error("lTrim fail, key={}, start={}, end={}", key, start, end, var7);
        }
    }

    //=============================== bitmap ===================================

    /**
     * 设置bit值
     *
     * @param key
     * @param offset
     * @return boolean
     */
    public Boolean setBit(String key, int offset) {
        try {
            return redisTemplate.opsForValue().setBit(key, offset, true);
        } catch (Exception e) {
            log.error("setBit fail, key={}, offset={}", key, offset, e);
            return null;
        }
    }

    /**
     * 清空bit值
     *
     * @param key
     * @param offset
     * @return boolean
     */
    public Boolean emptyBit(String key, int offset) {
        try {
            return redisTemplate.opsForValue().setBit(key, offset, false);
        } catch (Exception e) {
            log.error("emptyBit fail,key={}, offset={}", key, offset, e);
            return null;
        }
    }

    /**
     * 获取bit值
     *
     * @param key
     * @param offset
     * @return boolean
     */
    public Boolean getBit(String key, int offset) {
        try {
            return redisTemplate.opsForValue().getBit(key, offset);
        } catch (Exception e) {
            log.error("getBit fail, key={}", key, e);
            return null;
        }

    }

    /**
     * 统计bit值为true的数量
     *
     * @param key
     * @return java.lang.Long
     */
    public Long countBit(String key) {
        try {
            return redisTemplate.execute((RedisCallback<Long>) conn -> conn.bitCount(key.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("countBit fail, key={}", key, e);
            return null;
        }
    }


    //============================zset=============================
    //redis 正是通过分数来为集合中的成员进行从小到大的排序。

    /**
     * 返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
     *
     * @param key 键
     * @return
     */
    public Double zScore(String key, Object member) {
        try {
            return redisTemplate.opsForZSet().score(key, member);
        } catch (Exception e) {
            log.error("zScore fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 移除有序集中的一个或多个成员，不存在的成员将被忽略。
     *
     * @param key     键
     * @param objects 值
     * @return true 存在 false不存在
     */
    public Long zRemove(String key, Object... objects) {
        try {
            return redisTemplate.opsForZSet().remove(key, objects);
        } catch (Exception e) {
            log.error("zScore fail, key={}", key, e);
            return 0L;
        }
    }

    /**
     * 将数据放入ZSet缓存
     *
     * @param key   键
     * @param value 值
     * @return 成功个数
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error("zAdd fail,key={},value={},score={}", key, value, score, e);
            return false;
        }
    }

    /**
     * 批量将数据放入ZSet缓存
     *
     * @param key      键
     * @param valueSet 值集合
     * @return 成功个数
     */
    public Long zAddBatch(String key, Set<ZSetOperations.TypedTuple<Object>> valueSet) {
        try {
            return redisTemplate.opsForZSet().add(key, valueSet);
        } catch (Exception e) {
            log.error("zAddBatch fail, key={}", key, e);
            return 0L;
        }
    }

    /**
     * 返回指定区间内的成员。其中成员的位置按分数值递增(从小到大)来排序。<br>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key 键
     * @return
     */
    public Set<Object> zRange(String key, long minIndex, long maxIndex) {
        try {
            return redisTemplate.opsForZSet().range(key, minIndex, maxIndex);
        } catch (Exception e) {
            log.error("zRange fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 返回指定区间内的成员。其中成员的位置按分数值递减(从大到小)来排列。<br>
     * 除了成员按分数值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
     *
     * @param key 键
     * @return
     */
    public Set<Object> zReverseRange(String key, long minIndex, long maxIndex) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, minIndex, maxIndex);
        } catch (Exception e) {
            log.error("zRange fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 获取分数值最小的成员(zset为空则返回null)
     *
     * @param key
     * @return java.lang.Object OR null
     */
    public Object zGetSmallestScoreMember(String key) {
        try {
            Set<Object> set = redisTemplate.opsForZSet().range(key, 0, 0);
            if (CollectionUtils.isEmpty(set)) {
                return null;
            }
            return set.stream().findFirst().get();
        } catch (Exception e) {
            log.error("zGetSmallestScoreMember fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 获取分数值最大的成员(zset为空则返回null)
     *
     * @param key
     * @return java.lang.Object OR null
     */
    public Object zGetBiggestScoreMember(String key) {
        try {
            Set<Object> set = redisTemplate.opsForZSet().reverseRange(key, 0, 0);
            if (CollectionUtils.isEmpty(set)) {
                return null;
            }
            return set.stream().findFirst().get();
        } catch (Exception e) {
            log.error("zGetBiggestScoreMember fail, key={}", key, e);
            return null;
        }
    }

    /**
     * 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。
     *
     * @param key 键
     * @return
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("zReverseRangeByScore fail,key={},minScore={},maxScore={}", key, min, max, e);
            return null;
        }
    }

    /**
     * 获取指定分数范围内，分数值最大的对象
     *
     * @param key
     * @param maxScore 分数值，大于等于1
     * @return java.lang.Object
     */
    public Object zReverseRangeByScoreGetMax(String key, double minScore, double maxScore) {
        try {
            if (maxScore < 1) {
                return null;
            }
            Set<Object> set = redisTemplate.opsForZSet().reverseRangeByScore(key, minScore, maxScore, 0, 1);
            if (CollectionUtils.isEmpty(set)) {
                return null;
            }
            return set.stream().findFirst().get();
        } catch (Exception e) {
            log.error("zReverseRangeByScore fail, key={},maxScore={}", key, maxScore, e);
            return null;
        }
    }

    /**
     * 获取指定分数范围内，分数值最小的对象
     *
     * @param key
     * @param maxScore 分数值，大于等于1
     * @return java.lang.Object
     */
    public Object zRangeByScoreGetMin(String key, double minScore, double maxScore) {
        try {
            if (maxScore < 1) {
                return null;
            }
            Set<Object> set = redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore, 0, 1);
            if (CollectionUtils.isEmpty(set)) {
                return null;
            }
            return set.stream().findFirst().get();
        } catch (Exception e) {
            log.error("zReverseRangeByScore fail, key={},maxScore={}", key, maxScore, e);
            return null;
        }
    }

    /**
     * 移除有序集中，指定分数（score）区间内的所有成员。
     *
     * @param key 键
     * @param min 分数起始值
     * @param max 分数终止值
     * @return 成功个数
     */
    public Long zRemRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("zRemRangeByScore fail, key={}", key, e);
            return 0L;
        }
    }

    /**
     * 查询zset长度
     *
     * @param key
     * @return java.lang.Object
     */
    public Long zCard(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            log.error("zCard fail, key={}", key, e);
            return 0L;
        }
    }

    /**
     * 计算指定分数范围内的元素数量<br>
     *
     * @param key      键
     * @param minScore
     * @param maxScore
     * @return
     */
    public Long zCount(String key, double minScore, double maxScore) {
        try {
            return redisTemplate.opsForZSet().count(key, minScore, maxScore);
        } catch (Exception e) {
            log.error("zRange fail, key={}", key, e);
            return null;
        }
    }

    /**
     * score的增加or减少
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Double incrScore(String key, String value, double score) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, score);
        } catch (Exception e) {
            log.error("zRange fail, key={}", key, e);
            return null;
        }
    }

    public Set<Object> zRangeByScore(String key, long minIndex, long maxIndex, long offset, long count) {
        try {
            return this.redisTemplate.opsForZSet().rangeByScore(key, minIndex, maxIndex, offset, count);
        } catch (Exception var7) {
            log.error("zRange fail, key={}", key, var7);
            return null;
        }
    }

    public Set<Object> zReverseRangeByScore(String key, long minIndex, long maxIndex, long offset, long count) {
        try {
            return this.redisTemplate.opsForZSet().reverseRangeByScore(key, minIndex, maxIndex, offset, count);
        } catch (Exception var7) {
            log.error("zRange fail, key={}", key, var7);
            return null;
        }
    }

    public void delByPrex(String prex) {
        Set<String> keys = redisTemplate.keys(prex);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, long minIndex, long maxIndex) {
        try {
            return this.redisTemplate.opsForZSet().rangeWithScores(key, minIndex, maxIndex);
        } catch (Exception var7) {
            log.error("zRangeWithScores fail, key={}", key, var7);
            return null;
        }
    }

}