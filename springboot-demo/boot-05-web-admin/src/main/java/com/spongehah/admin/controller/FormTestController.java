package com.spongehah.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@Slf4j
public class FormTestController {

    @GetMapping("/form_layouts")
    public String form_layouts(){
        return "form/form_layouts";
    }
    
    @PostMapping("/upload")
    public String upload(@RequestParam("email") String email,
                         @RequestParam("username") String username,
                         @RequestPart("headerImg")MultipartFile headerImg,
                         @RequestPart("photos") MultipartFile[] photos/*,
                         HttpSession session*/) throws IOException {
        
        //放到项目运行的web服务器(如tomcat)的真实运行路径
        /*ServletContext servletContext = session.getServletContext();
        String photoPath = servletContext.getRealPath("photo");*/
        
        //放到项目resources目录下的photo文件夹
        File file = new File("src/main/resources/static/photo");
        if(!file.exists()){
            file.mkdirs();
        }

        if (!headerImg.isEmpty()) {
            String originalFilename = headerImg.getOriginalFilename();
            String hzName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = originalFilename.substring(0,originalFilename.lastIndexOf(".")) + UUID.randomUUID().toString() + hzName;
            headerImg.transferTo(new File(file.getAbsoluteFile() + File.separator + filename));
        }
        if(photos.length > 0){
            for (MultipartFile photo : photos) {
                if(!photo.isEmpty()){
                    String originalFilename = photo.getOriginalFilename();
                    String hzName = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String filename = originalFilename.substring(0,originalFilename.lastIndexOf(".")) + UUID.randomUUID().toString() + hzName;
                    photo.transferTo(new File(file.getAbsoluteFile() + File.separator + filename));
                }
            }
        }
        return "main";
    }
}
