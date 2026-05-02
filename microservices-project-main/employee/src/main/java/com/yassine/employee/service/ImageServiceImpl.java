package com.yassine.employee.service;

import com.yassine.employee.Repository.EmployeeRepository;
import com.yassine.employee.Repository.ImageRepository;
import com.yassine.employee.entity.Employee;
import com.yassine.employee.entity.Image;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        return imageRepository.save(
                Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .image(file.getBytes())
                        .build()
        );
    }

    @Override
    public Image uploadImageEmp(MultipartFile file, int idEmp) throws IOException {
        Employee employee = employeeRepository.findById(idEmp)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec id: " + idEmp));

        return imageRepository.save(
                Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .image(file.getBytes())
                        .employee(employee)
                        .build()
        );
    }

    @Override
    public Image getImageDetails(Long id) throws IOException {
        Image dbImage = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image non trouvée"));
        return Image.builder()
                .idImage(dbImage.getIdImage())
                .name(dbImage.getName())
                .type(dbImage.getType())
                .image(dbImage.getImage())
                .build();
    }

    @Override
    public List<Image> getImagesParEmp(int idEmp) {
        if (!employeeRepository.existsById(idEmp)) {
            throw new RuntimeException("Employe introuvable avec id: " + idEmp);
        }

        return imageRepository.findByEmployeeIdEmploye(idEmp);
    }

    @Override
    public ResponseEntity<byte[]> getImage(Long id) throws IOException {
        Image dbImage = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image non trouvée"));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(dbImage.getType()))
                .body(dbImage.getImage());
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
