package com.bxm.copilot.utils.pair;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Set;

/**
 * @author allen
 * @since 1.0.0
 */
public class DefaultValue implements Value {

    private static final char DEFAULT_SPLIT_CHAR = ',';
    private final String value;

    public DefaultValue(String value) {
        this.value = value;
    }

    @Override
    public String of() {
        return value;
    }

    @Override
    public List<String> ofArrayList() {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Lists.newArrayList(simpleSplitToArray());
    }

    @Override
    public Integer ofInt() {
        return TypeUtils.castToInt(value);
    }

    @Override
    public int ofInt(int defaultValue) {
        return NumberUtils.toInt(value, defaultValue);
    }

    @Override
    public Boolean ofBoolean() {
        return TypeUtils.castToBoolean(value);
    }

    @Override
    public boolean ofBoolean(boolean defaultValue) {
        Boolean b = ofBoolean();
        return null == b ? defaultValue : b;
    }

    @Override
    public Set<String> ofHashSet() {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Sets.newHashSet(simpleSplitToArray());
    }

    @Override
    public <T> T toObject(Class<T> cls) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return JSONObject.parseObject(value, cls);
    }

    private String[] simpleSplitToArray() {
        return StringUtils.split(value, DEFAULT_SPLIT_CHAR);
    }
}
