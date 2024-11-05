package com.bxm.copilot.utils.pair;

import java.util.List;
import java.util.Set;

/**
 * 值
 * @author allen
 * @since 1.0.0
 */
public interface Value {

    /**
     * 返回原始值
     * 
     * @return 返回一个字符串
     */
    String of();

    /**
     * 对原始值转换成一个Integer
     * 
     * @return 返回一个整型对象，如果原始值是<code>null</code>，那么返回<code>null</code>
     */
    Integer ofInt();

    /**
     * 对原始值转换成一个Integer，如果原始值是<code>null</code>，那么会返回传入的默认值
     * 
     * @param defaultValue 如果为<code>null</code>，返回这个值
     * @return 返回一个整型值
     */
    int ofInt(int defaultValue);

    /**
     * 对原始值转换成一个Boolean值。
     * 
     * <pre>
     *     "true" / "TRUE" / "1" / "y" / "Y" / "T" 都会返回 true
     *     "false" / "FALSE" / "0" / "f" / "F" / "N" 都会返回 false
     * </pre>
     * 
     * @return 返回一个布尔型对象，如果原始值是<code>null</code>，那么返回<code>null</code>
     */
    Boolean ofBoolean();

    /**
     * 对原始值转换成一个Boolean值，如果原始值是<code>null</code>，那么会返回传入的默认值
     *
     * @param defaultValue 如果为<code>null</code>，返回这个值
     * @return 返回一个布尔型值
     */
    boolean ofBoolean(boolean defaultValue);

    /**
     * 对原始值截取，并封装成一个ArrayList集合
     * @return 返回一个ArrayList集合
     */
    List<String> ofArrayList();

    /**
     * 对原始值截取，并封装成一个HashSet集合
     * @return 返回一个HashSet集合
     */
    Set<String> ofHashSet();

    /**
     * 对原始值 JSON 转换成一个对象。
     *
     * @param cls 对象类
     * @param <T> 泛型
     * @return 返回一个对象
     */
    <T> T toObject(Class<T> cls);
}
