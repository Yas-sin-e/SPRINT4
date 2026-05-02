package com.yassine.employee.service;

import com.yassine.employee.entity.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ImageService {
    Image uploadImage(MultipartFile file) throws IOException;
    Image uploadImageEmp(MultipartFile file, int idEmp) throws IOException;
    Image getImageDetails(Long id) throws IOException;
    List<Image> getImagesParEmp(int idEmp);
    ResponseEntity<byte[]> getImage(Long id) throws IOException;
    void deleteImage(Long id);
}
