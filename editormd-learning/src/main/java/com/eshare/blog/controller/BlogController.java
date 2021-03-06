package com.eshare.blog.controller;

import com.eshare.blog.entity.Blog;
import com.eshare.blog.service.BlogService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * 编辑器控制器
 * Created by liangyh on 2018/7/8.
 * Email:10856214@163.com
 */
@Controller
public class BlogController {

    @Resource
    private BlogService blogService;

    @RequestMapping("/edit")
    public String edit() {
        return "edit";
    }


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/save")
    @ResponseBody
    public void submit(Blog blog) {
        System.out.println(blog.getContent());
        System.out.println(blog.getHtmlContent());
        blogService.save(blog);
    }

    @RequestMapping("/list")
    public String list(Model model) {
        List<Blog> blogs = blogService.findAll();
        //暂时展示一条
        model.addAttribute("blog", blogs.get(0));
        return "view";
    }


    @RequestMapping("/view")
    public String view(Model model, @RequestParam(value = "title", required = false) String title) {
        List<Blog> blogs = blogService.findByBlogTitle(title);
        model.addAttribute("blog", blogs.get(0));
        return "view";
    }

    @PostMapping("/uploadfile")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "editormd-image-file", required = false) MultipartFile attach) {
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            String rootPath = request.getSession().getServletContext().getRealPath("/resources/upload/");

            /**
             * 文件路径不存在则需要创建文件路径
             */
            File filePath = new File(rootPath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }

            //最终文件名
            File realFile = new File(rootPath + File.separator + attach.getOriginalFilename());
            FileUtils.copyInputStreamToFile(attach.getInputStream(), realFile);

            //下面response返回的json格式是editor.md所限制的，规范输出就OK
            response.getWriter().write("{\"success\": 1, \"message\":\"上传成功\",\"url\":\"/resources/upload/" + attach.getOriginalFilename() + "\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
