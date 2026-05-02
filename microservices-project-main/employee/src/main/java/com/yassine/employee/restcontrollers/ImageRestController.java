package com.yassine.employee.restcontrollers;

import com.yassine.employee.dto.EmployeeDTO;
import com.yassine.employee.entity.Employee;
import com.yassine.employee.entity.Image;
import com.yassine.employee.service.EmployeeService;
import com.yassine.employee.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.nio.file.Path;


@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class ImageRestController {

    private final ImageService imageService;

    @Autowired
    private EmployeeService employeeService;
    // Upload une image
    @PostMapping("/upload")
    public Image uploadImage(@RequestParam("image") MultipartFile file)
            throws IOException {
        return imageService.uploadImage(file);
    }

    // Upload une image et l'associer à un employé
    @PostMapping({
            "/uploadImageEmp/{idEmp}",
            "/uplaodImageEmp/{idEmp}"
    })
    public Image uploadMultiImages(@RequestParam("image") MultipartFile file,
                                   @PathVariable("idEmp") int idEmp) throws IOException {
        return imageService.uploadImageEmp(file, idEmp);
    }

    // Obtenir les détails (avec données base64)
    @GetMapping("/get/info/{id}")
    public Image getImageInfo(@PathVariable Long id) throws IOException {
        return imageService.getImageDetails(id);
    }

    // Obtenir toutes les images d'un employé
    @GetMapping({"/getImagesEmp/{idEmp}", "/getImagesByEmp/{idEmp}", "/getImagesProd/{idEmp}"})
    public List<Image> getImagesEmp(@PathVariable("idEmp") int idEmp) {
        return imageService.getImagesParEmp(idEmp);
    }

    // Obtenir l'image binaire
    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id)
            throws IOException {
        return imageService.getImage(id);
    }

    // Supprimer une image
    @DeleteMapping("/delete/{id}")
    public void deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
    }

//    @RequestMapping(value = "/uploadFS/{id}", method = RequestMethod.POST)
//    public void uploadImageFS(@RequestParam("image") MultipartFile file,
//                              @PathVariable("id") int id) throws IOException {
//        Employee emp = employeeService.getEmploye(id);
//        emp.setImagePath(id + ".jpg");
//        Files.createDirectories(Paths.get(System.getProperty("user.home") + "/images"));
//        Files.write(Paths.get(System.getProperty("user.home") + "/images/" + emp.getImagePath()), file.getBytes());
//        employeeService.saveEmploye(emp);
//    }
@RequestMapping(value = "/uploadFS/{id}", method = RequestMethod.POST)
public ResponseEntity<String> uploadImageFS(@RequestParam("image") MultipartFile file,
                                            @PathVariable("id") int id) throws IOException {

    System.out.println("=== DEBUT uploadImageFS pour ID: " + id + " ===");

    Employee emp = employeeService.getEmploye(id);
    if (emp == null) {
        System.out.println("ERREUR: Employé non trouvé pour ID: " + id);
        return ResponseEntity.notFound().build();
    }

    String imagePath = id + ".jpg";
    emp.setImagePath(imagePath);
    System.out.println("ImagePath défini: " + imagePath);

    // Créer le dossier
    Path dirPath = Paths.get(System.getProperty("user.home") + "/images");
    Files.createDirectories(dirPath);
    System.out.println("Dossier créé/vérifié: " + dirPath);

    // Écrire le fichier
    Path filePath = Paths.get(System.getProperty("user.home") + "/images/" + imagePath);
    Files.write(filePath, file.getBytes());
    System.out.println("Fichier écrit: " + filePath);
    System.out.println("Taille du fichier: " + file.getBytes().length + " bytes");

    // Sauvegarder l'employé
    employeeService.saveEmploye(emp);
    System.out.println("Employé sauvegardé avec imagePath: " + emp.getImagePath());

    System.out.println("=== FIN uploadImageFS ===");

    return ResponseEntity.ok("Image uploaded: " + imagePath);
}

//    @RequestMapping(value = "/loadfromFS/{id}",
//            method = RequestMethod.GET,
//            produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] getImageFS(@PathVariable("id") int id) throws IOException {
//        Employee emp = employeeService.getEmploye(id);
//        return Files.readAllBytes(
//                Paths.get(System.getProperty("user.home") + "/images/" + emp.getImagePath())
//        );
//    }
@RequestMapping(value = "/loadfromFS/{id}",
        method = RequestMethod.GET,
        produces = MediaType.IMAGE_JPEG_VALUE)
public ResponseEntity<byte[]> getImageFS(@PathVariable("id") int id) throws IOException {

    System.out.println("=== DEBUT loadfromFS pour ID: " + id + " ===");

    Employee emp = employeeService.getEmploye(id);

    if (emp == null) {
        System.out.println("ERREUR: Employé non trouvé pour ID: " + id);
        return ResponseEntity.notFound().build();
    }

    System.out.println("Employé trouvé: " + emp.getNomEmploye());
    System.out.println("ImagePath de l'employé: " + emp.getImagePath());

    if (emp.getImagePath() == null || emp.getImagePath().isEmpty()) {
        System.out.println("ERREUR: imagePath est null ou vide");
        return ResponseEntity.notFound().build();
    }

    Path imagePath = Paths.get(System.getProperty("user.home") + "/images/" + emp.getImagePath());
    System.out.println("Chemin complet du fichier: " + imagePath.toString());
    System.out.println("Le fichier existe? " + Files.exists(imagePath));

    if (!Files.exists(imagePath)) {
        System.out.println("ERREUR: Le fichier n'existe pas: " + imagePath);
        return ResponseEntity.notFound().build();
    }

    byte[] imageBytes = Files.readAllBytes(imagePath);
    System.out.println("Fichier lu avec succès, taille: " + imageBytes.length + " bytes");
    System.out.println("=== FIN loadfromFS ===");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    headers.setCacheControl("no-cache, no-store, must-revalidate");

    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
}
}
