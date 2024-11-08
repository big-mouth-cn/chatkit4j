package io.github.bigmouthcn.chatkit4j.utils.pair;

/**
 * @author allen
 * @since 1.0.0
 */
public interface MutablePair extends Pair {

    void set(String key, String value);

    void delete(String key);
}
