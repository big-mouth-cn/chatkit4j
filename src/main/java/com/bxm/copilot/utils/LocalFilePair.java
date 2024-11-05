package com.bxm.copilot.utils;


import com.bxm.copilot.utils.pair.DefaultValue;
import com.bxm.copilot.utils.pair.MutablePair;
import com.bxm.copilot.utils.pair.Value;

import java.io.File;
import java.util.Collection;

/**
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
public class LocalFilePair implements MutablePair {

    @Override
    public Value get(String key) {
        String s = PromptUtils.readFullFromUserDir(path(key));
        return new DefaultValue(s);
    }

    @Override
    public Collection<String> keys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(String key, String value) {
        PromptUtils.writeFullFromUserDir(path(key), value);
    }
    @Override
    public void delete(String key) {
        PromptUtils.deleteFromUserDir(path(key));
    }

    public static String path(String key) {
        if (!isValidString(key)) {
            throw new IllegalArgumentException("Invalid key: " + key);
        }
        return ".copilot" + File.separator + "pair" + File.separator + key;
    }

    public static boolean isValidString(String str) {
        return str.matches("[0-9a-zA-Z-_]+");
    }
}
