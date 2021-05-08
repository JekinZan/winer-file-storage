package com.winer.winerfilestorage.base;


import java.io.InputStream;

/**
 * @program: winer-file-storage-dev
 * @description: 文件存储对象
 * @Author Jekin
 * @Date 2021/4/26
 */
public class FileStorageObject extends FileObject {
    /**
     *
     */
    private static final long serialVersionUID = 2107478581741859300L;

    private InputStream inputStream;

    /**
     * 获取流
     *
     * @return
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * 设置流
     *
     * @param inputStream
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
