package com.winer.winerfilestorage.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;


/**
 * 资源帮助
 *
 * @author 老码农 2019-04-21 17:45:22
 */
public class ResourceUtils {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    /**
     * 获取资源的根路径
     *
     * @return
     */
    public static String getResourceRootPath() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources(ResourceLoader.CLASSPATH_URL_PREFIX);
            for (Resource resource : resources) {
                if (resource.exists()) {
                    String rootPath = resource.getFile().getAbsolutePath().replace("\\", "/");
                    File dir = new File(rootPath);
                    if (!dir.exists() || !dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    return rootPath;
                }
            }
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 将类名称转换为瓷源路径
     *
     * @param className
     * @return
     */
    public static String convertClassNameToResourcePath(String className) {
        return ClassUtils.convertClassNameToResourcePath(className);
    }


}
