package com.winer.winerfilestorage.annotation;

import com.winer.winerfilestorage.configure.WinerStorageAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @program: winer-file-storage
 * @description: 启用存储
 * @Author Jekin
 * @Date 2021/4/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ WinerStorageAutoConfiguration.class })
public @interface EnableWinerStorage {
}
