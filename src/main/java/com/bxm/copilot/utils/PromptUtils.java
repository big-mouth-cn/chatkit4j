package com.bxm.copilot.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
@Slf4j
public final class PromptUtils {

    public static String fetch(String fileName) {
        return fetch(fileName, null);
    }

    public static String fetch(String fileName, Map<String, String> replaced) {
        try {
            String md = readFullFromUserDir(fileName);
            if (StringUtils.isBlank(md)) {
                md = readFull(fileName);
            }
            if (MapUtils.isNotEmpty(replaced)) {
                for (Map.Entry<String, String> entry : replaced.entrySet()) {
                    md = md.replaceAll(entry.getKey(), entry.getValue());
                }
            }
            return md;
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return null;
        }
    }

    public static String readFullFromUserDir(String fileName) {
        try {
            String userDir = System.getProperty("user.home");
            File file = new File(userDir + File.separator + fileName);
            return FileUtil.readUtf8String(file);
        } catch (IORuntimeException e) {
            return null;
        }
    }

    public static void writeFullFromUserDir(String fileName, String content) {
        String userDir = System.getProperty("user.home");
        File file = new File(userDir + File.separator + fileName);
        FileUtil.writeUtf8String(content, file);
    }

    public static void deleteFromUserDir(String fileName) {
        String userDir = System.getProperty("user.home");
        File file = new File(userDir + File.separator + fileName);
        FileUtil.del(file);
    }

    /**
     * 读取文件内容
     * @param fileName 文件名，填写 classpath:// 下的文件名即可。比如：application.yml
     * @return 文件内容
     * @throws IOException
     */
    public static String readFull(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource((fileName));
        return IoUtil.read(classPathResource.getInputStream(), StandardCharsets.UTF_8);
    }
}
