package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * @Description 文件上传
 * @Author apdoer
 * @Date 2019/3/19 21:32
 * @Version 1.0
 */
@Service
@Slf4j//日志
@EnableConfigurationProperties(UploadProperties.class)//读取properties配置文件
public class UploadService {
    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties prop;//配置文件,包含了路径部分参数,和支持的文件类型

    //支持的文件类型
    //private static final List<String> ALLOW_FILE_TYPES=Arrays.asList("image/png","image/jpeg","image/gif","image/ipg");

    /**
     * 文件上传
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        try {
            //校验文件类型
            String contentType = file.getContentType();//文件类型
            if (!prop.getAllowTypes().contains(contentType)){
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage==null){
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //上传到fdfs
            //这个方法比平常的截取字符串要高效和优雅不少
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //返回路径,图片回显
            return prop.getBaseUrl()+storePath.getFullPath();
        }catch (Exception e){
            //上传失败
            log.error("上传失败",e);
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }
}
