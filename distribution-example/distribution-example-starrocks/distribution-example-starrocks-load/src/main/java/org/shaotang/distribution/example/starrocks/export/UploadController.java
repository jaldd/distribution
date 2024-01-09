package org.shaotang.distribution.example.starrocks.export;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@RestController
public class UploadController {


    @RequestMapping("/upload")
    public String upload(@RequestParam(value = "files", required = false) MultipartFile[] files,
                         HttpServletRequest request) throws ServletException, IOException {


        for (MultipartFile file : files) {
            System.out.println(file.getOriginalFilename());
        }
        Collection<Part> parts = request.getParts();
        String data = request.getParameter("data");
        return null;
    }


}
