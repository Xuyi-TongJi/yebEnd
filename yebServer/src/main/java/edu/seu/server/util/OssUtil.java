package edu.seu.server.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.OSSObject;
import edu.seu.server.common.excpetion.CustomRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * 将文件上传至oss的核心工具类
 * @author xuyitjuseu
 */
@Slf4j
public class OssUtil {

    /**
     * 地域节点
     */
    private static final String ENDPOINT_URL = "oss-cn-shanghai.aliyuncs.com";
    /**
     * ACCESS信息，不可开源到github
     */
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";
    /**
     * 桶名称
     */
    private static final String BUCKET_NAME = "xuyidebucket";
    /**
     * bucket域名
     */
    private static final String SUFFIX_URL = "http://xuyidebucket.oss-cn-shanghai.aliyuncs.com/";

    private static final String[] SUPPORTED_SUFFIX = new String[]{ "bmp", "jpg", "jpeg", "png" };

    /**
     * 获取Oss连接
     */
    private static OSSClient getOssClient() {
        OSSClient ossClient = new OSSClient(ENDPOINT_URL, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        // 判断仓库是否存在
        if (!ossClient.doesBucketExist(BUCKET_NAME)) {
            // 通过api创建bucket
            CreateBucketRequest bucketRequest = new CreateBucketRequest(BUCKET_NAME);
            bucketRequest.setCannedACL(CannedAccessControlList.Private);
            ossClient.createBucket(bucketRequest);
        }
        return ossClient;
    }

    /**
     * 实现图片文件上传
     * @param file 文件
     * @param businessType 业务类型
     * @return 文件的访问路径
     */
    public static String uploadImages(MultipartFile file, String businessType) {
        // 获取文件后缀名
        String[] strings = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String suffix = strings[strings.length - 1];
        boolean isSupported = false;
        for (String imageSuffix: SUPPORTED_SUFFIX) {
            if (suffix.equals(imageSuffix)) {
                isSupported = true;
                break;
            }
        }
        if (!isSupported) {
            throw new CustomRuntimeException("不支持的文件类型！");
        }
        // 获取Oss连接
        OSSClient ossClient = getOssClient();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = "yebOss" + "/" + businessType + "/" + uuid + "." +suffix;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(file.getBytes());
            // 通过ossClient获取文件上传成功后返回的url
            ossClient.putObject(BUCKET_NAME, fileName, bis);
            return fileName;
        } catch (IOException e) {
            log.error("文件上传失败，异常信息为{}", e.getMessage());
            return null;
        } finally {
            ossClient.shutdown();
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件
     * @param filename 文件名
     * @return 输入流
     */
    public static InputStream downloadImages(String filename) {
        OSSClient ossClient = getOssClient();
        OSSObject object = ossClient.getObject(BUCKET_NAME, filename);
        return object.getObjectContent();
    }
}
