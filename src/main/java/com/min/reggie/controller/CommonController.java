package com.min.reggie.controller;


import com.min.reggie.common.Rest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggieProject.path}")  // 根据配置文件制动注入
    private String basePath;

    /**
     * 文件上传
     *
     * @return 包装的文件名
     */
    @PostMapping("/upload")
    public Rest<String> update(MultipartFile file) {  // file必须保持和前端一直
        // 获取原始的文件名
        String originalFilename = file.getOriginalFilename(); // 文件原始文件名
        assert originalFilename != null;
        int beginIndex = originalFilename.lastIndexOf(".");  // 获取点所在的索引
        String substring = originalFilename.substring(beginIndex);  // 从 . 所在的索引开始截取字符串，得到文件的后缀名

        String uuid = UUID.randomUUID().toString(); // 生成uuid来做新的文件名称，防止重复
        String newFileName = uuid + substring; // 将文件名和后缀名拼接在一起
        File fd = new File(basePath);
        // 判断文件夹是否存在
        if (!fd.exists()) {
            // 不存在创建
            fd.mkdirs();
        }
        File dest = new File(basePath + newFileName);
        log.info("文件路径 ：{}", dest);
        try {

            file.transferTo(dest);  // 转存文件
        } catch (IOException e) {
            e.printStackTrace();
        }


        return Rest.success(newFileName);  // 将文件名返回给前端

    }

    /**
     * 下载图片
     *
     * @param name     要下载的文件名
     * @param response 通过响应将图片文件返回
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        // 自动关闭流
        try (
                // 读取文件存放在文件输入流中
                FileInputStream fis = new FileInputStream(new File(basePath + name));
                // 通过response输出流将图片输出    创建一根空水管
                ServletOutputStream outputStream = response.getOutputStream();
        ) {
            response.setContentType("image/jepg");  // 设置内容类型
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
