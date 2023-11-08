package com.look4.demo.controllers;

import com.look4.demo.dto.ImageDTO;
import com.look4.demo.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    ImageService imageService;

    @GetMapping("/all")
    public List<ImageDTO> getAllImages(){
        return imageService.getAllImages();
    }
    @PostMapping("")
    public ImageDTO addImage(@RequestBody ImageDTO newImage){
        return imageService.addImage(newImage);
    }
    @PatchMapping("")
    public ImageDTO updateImage(@RequestBody ImageDTO imageToBeUpdated){
        return imageService.updateImage(imageToBeUpdated);
    }
    @DeleteMapping("")
    public void deleteImage(@RequestParam Long imageId){
        imageService.deleteImage(imageId);
    }
    @PostMapping("/upload/{socialPostId}")
    public ImageDTO uploadFile(@RequestBody MultipartFile file, @PathVariable Long socialPostId) throws IOException {
       return imageService.uploadPhoto(file,socialPostId);
    }
    @PostMapping("/uploadprofile/{userId}")
    public ImageDTO uploadProfilePhoto(@RequestBody MultipartFile file, @PathVariable Long userId) throws IOException {
        return imageService.uploadProfilePhoto(file,userId);
    }
}
