package io.github.bigmouthcn.chatkit4j.utils.pair;

import java.util.Collection;

/**
 * 键值
 *
 * @author allen
 * @since 1.0.0
 */
public interface Pair {

    /**
     * 获取一个值
     * @param key 键
     * @return 返回这个值
     */
    Value get(String key);

    /**
     * 获取所有的键
     * @return 键列表
     */
    Collection<String> keys();
}
