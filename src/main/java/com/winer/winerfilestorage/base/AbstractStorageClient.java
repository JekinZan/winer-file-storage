package com.winer.winerfilestorage.base;

import cn.hutool.core.util.StrUtil;
import com.winer.winerfilestorage.properties.AbstractStorageClientProperties;
import com.winer.winerfilestorage.utils.tuple.TupleTwo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: winer-file-storage-dev
 * @description: 存储客户端抽象
 * @Author Jekin
 * @Date 2021/4/26
 */
@Slf4j
public abstract class AbstractStorageClient<TBucket extends AbstractBucket> implements StorageClient{

    private static final Map<String, TupleTwo<String, Integer>> EXTENSION_CONTENT_TYPE_MAP = new HashMap<>(16);
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    static {
        EXTENSION_CONTENT_TYPE_MAP.put("jpg", new TupleTwo<>("image/jpg", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("png", new TupleTwo<>("image/png", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("bmp", new TupleTwo<>("image/bmp", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("tif", new TupleTwo<>("image/tif", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("gif", new TupleTwo<>("image/gif", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("jpeg", new TupleTwo<>("image/jpeg", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("tiff", new TupleTwo<>("image/tiff", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("dib", new TupleTwo<>("image/dib", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("html", new TupleTwo<>("text/html", 2));
        EXTENSION_CONTENT_TYPE_MAP.put("xml", new TupleTwo<>("text/xml", 2));
        EXTENSION_CONTENT_TYPE_MAP.put("plain", new TupleTwo<>("text/plain", 2));
        EXTENSION_CONTENT_TYPE_MAP.put("json", new TupleTwo<>("application/json", 3));
        EXTENSION_CONTENT_TYPE_MAP.put("pdf", new TupleTwo<>("application/pdf", 4));
    }
    /**
     * URL 分隔符
     */
    public static final char URL_SEPARATOR = '/';
    private final String endpoint;
    private final String defaultBucketName;
    private int writeBlockSize = 2048;
    private int readBlockSize = 4096;

    @Override
    public String getEndpoint() {
        return this.endpoint;
    }

    @Override
    public String getDefaultBucketName() {
        return this.defaultBucketName;
    }

    @Override
    public abstract TBucket createBucket(String bucketName);

    @Override
    public abstract TBucket getBucket(String bucketName);

    /**
     * 获取读取块大小
     *
     * @return
     */
    @Override
    public int getReadBlockSize() {
        return this.readBlockSize;
    }

    /**
     * 设置读块大小
     *
     * @param readBlockSize 读块大小
     */
    @Override
    public void setReadBlockSize(int readBlockSize) {
        this.readBlockSize = readBlockSize;
    }

    /**
     * 获取写块大小
     *
     * @return
     */
    @Override
    public int getWriteBlockSize() {
        return this.writeBlockSize;
    }

    /**
     * 设置写块大小
     *
     * @param writeBlockSize 写块大小
     */
    @Override
    public void setWriteBlockSize(int writeBlockSize) {
        this.writeBlockSize = writeBlockSize;
    }

    @Override
    public final List<FileObject> listFileObjects(String bucketName) {
        return this.listFileObjects(bucketName, null);
    }

    @Override
    public String toString() {
        return "启用 FileStorage " + this.getChannelName() + " 默认分区:" + this.getDefaultBucketName() + " 访问根路径:"
                + this.getEndpoint();
    }
    /**
     * 实例化
     *
     * @param properties 属性
     */
    public AbstractStorageClient(AbstractStorageClientProperties properties) {
        this(properties.getEndpoint(), properties.getDefaultBucketName());
    }
    /**
     * AbstractStorageClient
     *
     * @param endpoint          终节点
     * @param defaultBucketName 默认分区
     */
    public AbstractStorageClient(String endpoint, String defaultBucketName) {
        if (StrUtil.isBlank(endpoint)) {
            throw new RuntimeException("终节点(url根路径)不能为空。");
        }

        this.endpoint =  StrUtil.removePrefix(StrUtil.removeSuffix(endpoint,String.valueOf(URL_SEPARATOR)),String.valueOf(URL_SEPARATOR)).toLowerCase();
        if (defaultBucketName == null) {
            defaultBucketName = "";
        } else {
            defaultBucketName = this.checkBucketName(defaultBucketName);
        }
        this.defaultBucketName = defaultBucketName.trim().toLowerCase();
    }

    @Override
    public final FileObject saveFile(String bucketName, String fullPath, InputStream input) throws Exception {
        FileStorageRequest request = new FileStorageRequest(bucketName, fullPath, input);
        return this.saveFile(request);
    }

    /**
     * 设置客户端属性
     *
     * @param properties 属性
     */
    protected void setStorageClientProperties(AbstractStorageClientProperties properties) {
        this.setReadBlockSize(properties.getReadBlockSize());
        this.setWriteBlockSize(properties.getWriteBlockSize());
    }
    /**
     * 检查分区名称
     *
     * @param bucketName 分区名称
     * @return
     */
    protected String checkBucketName(String bucketName) {
        if (StrUtil.isNotBlank(bucketName)) {
            return bucketName;
        }
        if (StrUtil.isBlank(this.getDefaultBucketName())) {
            throw new RuntimeException("默认分区名为空.");
        }
        return this.getDefaultBucketName();
    }

    /**
     * 获取文件
     *
     * @param ext 扩展名
     * @return
     */
    protected String getFileContentType(String ext) {
        if (ext == null) {
            return "application/octet-stream";
        }
        TupleTwo<String, Integer> two = EXTENSION_CONTENT_TYPE_MAP.get(ext.toLowerCase().trim());
        if (two != null) {
            return two.getItem1();
        }
        return "application/octet-stream";
    }
    /**
     * 获取内容 Disposition
     *
     * @param request
     * @return
     */
    protected String getContentDisposition(FileStorageRequest request) {
        if (StrUtil.isNotBlank(request.getFileInfo().getFriendlyName())) {
            try {
                String fileName = request.getFileInfo().getFriendlyName() + "." + request.getFileInfo().getExtensionName();
                return "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
        return "";
    }

    /**
     * 写入到输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @return
     * @throws IOException
     */
    protected final long writeOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        long size = 0;
        int byteCount;
        byte[] bytes = new byte[this.getWriteBlockSize()];
        while ((byteCount = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, byteCount);
            size += byteCount;
        }
        return size;
    }
    /**
     * 获取路径地址
     *
     * @param args 参数集合
     * @return
     */
    protected final String getPathAddress(String... args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i > 0) {
                builder.append(URL_SEPARATOR);
            }
            builder.append(arg);
        }
        return builder.toString();
    }

    /**
     * 读取请求流
     *
     * @param request 请求
     * @return
     * @throws IOException
     */
    protected TupleTwo<Long, InputStream> readRequestStream(FileStorageRequest request) throws IOException {
        InputStream inputStream;
        long size;
        if (request.getFileInfo().getLength() > 0L) {
            size = request.getFileInfo().getLength();
            inputStream = request.getInputStream();
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                size = this.writeOutputStream(request.getInputStream(), outputStream);
                if (request.getInputStream() instanceof ByteArrayInputStream) {
                    ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) request.getInputStream();
                    byteArrayInputStream.reset();
                    inputStream = byteArrayInputStream;
                } else {
                    inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                }
            } finally {
                IOUtils.closeQuietly(outputStream);
            }
        }
        return new TupleTwo<>(size, inputStream);
    }
}
