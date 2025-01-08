package com.movie.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class FileUtil {

    private final String posterUploadDir;
    private final String eventUploadDir;

    @Autowired
    public FileUtil(
            @Value("${file.upload.poster-dir}") String posterUploadDir,
            @Value("${file.upload.event-dir}") String eventUploadDir) {
        this.posterUploadDir = posterUploadDir;
        this.eventUploadDir = eventUploadDir;
    }

    private String getUploadDir() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", File.separator);
    }

    // 파일 저장
    public String saveFile(MultipartFile file, boolean isPoster) throws IOException {
        String baseDir = isPoster ? posterUploadDir : eventUploadDir;
        String uploadFolderPath = getUploadDir();

        File uploadPath = new File(baseDir, uploadFolderPath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("Invalid file name: " + originalFilename);
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + fileExtension;

        File saveFile = new File(uploadPath, uniqueFilename);
        file.transferTo(saveFile);

        // 클라이언트에서 접근 가능한 경로 반환 (예: "2024/04/27/movie_20240427123000.jpg")
        return Paths.get(uploadFolderPath, uniqueFilename).toString().replace("\\", "/");
    }
}
