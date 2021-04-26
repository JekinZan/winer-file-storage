package com.winer.winerfilestorage.base;
import com.winer.winerfilestorage.base.FileInfo;
import com.winer.winerfilestorage.utils.StringUtils;
import lombok.Getter;

import java.io.InputStream;

/**
 * @program: winer-file-storage-dev
 * @description: 文件存储请求
 * @Author Jekin
 * @Date 2021/4/26
 */
@Getter
public class FileStorageRequest {
    private final String bucketName;
    private final InputStream inputStream;
    private final FileInfo fileInfo;

    /**
     *
     * @param bucketName
     *            分区名称
     * @param fullPath
     *            完整路径，包括文件名称
     * @param inputStream
     *            输入流
     */
    public FileStorageRequest(String bucketName, String fullPath, InputStream inputStream) {
        this(bucketName, new FileInfo(fullPath, true, 0L), inputStream);
    }


    /**
     *
     * @param bucketName
     *            分区名称
     * @param fileInfo
     *            文件信息
     * @param inputStream
     *            输入流
     */
    public FileStorageRequest(String bucketName, FileInfo fileInfo, InputStream inputStream) {
        if (StringUtils.isNullOrBlank(bucketName)) {
            throw new RuntimeException("分区名不能为空");
        }
        if (fileInfo == null) {
            throw new RuntimeException("文件信息fileInfo不能为null。");
        }
        this.fileInfo = fileInfo;
        if (inputStream == null) {
            throw new RuntimeException("文件流不能为空。");
        }
        this.bucketName = bucketName;
        this.inputStream = inputStream;
    }
}
