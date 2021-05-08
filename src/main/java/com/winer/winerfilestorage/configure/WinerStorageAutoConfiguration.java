package com.winer.winerfilestorage.configure;
import com.winer.winerfilestorage.base.StorageClient;
import com.winer.winerfilestorage.base.StorageClientContext;
import com.winer.winerfilestorage.base.impl.StorageClientContextImpl;
import com.winer.winerfilestorage.clients.aliyun.AliyunStorageClient;
import com.winer.winerfilestorage.clients.aliyun.AliyunStorageClientProperties;
import com.winer.winerfilestorage.clients.fastdfs.FastDFSStorageClient;
import com.winer.winerfilestorage.clients.fastdfs.FastDFSStorageClientProperties;
import com.winer.winerfilestorage.clients.local.LocalStorageClient;
import com.winer.winerfilestorage.clients.local.LocalStorageClientProperties;
import com.winer.winerfilestorage.clients.minio.MinioStorageClient;
import com.winer.winerfilestorage.clients.minio.MinioStorageClientProperties;
import com.winer.winerfilestorage.properties.WinerStorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: winer-file-storage
 * @description: 存储配置类
 * @Author Jekin
 * @Date 2021/4/25
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({WinerStorageProperties.class})
public class WinerStorageAutoConfiguration {


    /**
     * 存储客户端上下文
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(StorageClientContext.class)
    public StorageClientContext storageClientContext() {
        return new StorageClientContextImpl();
    }

    /**
     * 阿里云 oos
     *
     * @param properties 属性
     * @return
     */
    @Bean(AliyunStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = AliyunStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(AliyunStorageClient.class)
    public StorageClient aliyunStorageClient(WinerStorageProperties properties) {
        return new AliyunStorageClient(properties.getAliyun());
    }


    /**
     * FastDFS 客户端
     *
     * @param properties 属性
     * @return
     */
    @Bean(FastDFSStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = FastDFSStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(FastDFSStorageClient.class)
    public StorageClient fastDFSStorageClient(WinerStorageProperties properties) {
        FastDFSStorageClientProperties clientProperties = properties.getFastDFS();
        clientProperties.initByProperties();
        return new FastDFSStorageClient(clientProperties);
    }

    /**
     * Minio 客户端
     *
     * @param properties 属性
     * @return
     */
    @Bean(MinioStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = MinioStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(MinioStorageClient.class)
    public StorageClient minioStorageClient(WinerStorageProperties properties) {
        return new MinioStorageClient(properties.getMinio());
    }


    /**
     * 本地文件存储
     *
     * @param properties 属性
     * @return
     */
    @Bean(LocalStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = LocalStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(LocalStorageClient.class)
    public StorageClient localStorageClient(WinerStorageProperties properties) {
        LocalStorageClientProperties clientProperties = properties.getLocal();
        clientProperties.initByProperties();
        return new LocalStorageClient(clientProperties);
    }

    /**
     * 注册所有的客户端
     *
     * @param clientContext 客户端上下文
     * @return
     */
    @Bean
    public BeanPostProcessor autumnStorageChannelBeanPostProcessor(StorageClientContext clientContext) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof StorageClient) {
                    StorageClient client = (StorageClient) bean;
                    log.info(client.toString());
                    clientContext.register(client);
                }
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        };
    }

}
