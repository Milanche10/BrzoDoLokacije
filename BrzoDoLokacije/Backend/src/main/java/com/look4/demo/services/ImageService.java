package com.look4.demo.services;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.ImageDTO;
import com.look4.demo.dto.ImageSlimDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.dto.mappers.AppUserMapper;
import com.look4.demo.dto.mappers.ImageMapper;
import com.look4.demo.entities.Image;
import com.look4.demo.repositories.AppUserRepository;
import com.look4.demo.repositories.ImageRepository;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.*;
import liquibase.pro.packaged.ab;
import org.apache.commons.compress.utils.FileNameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ImageMapper imageMapper;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserMapper appUserMapper;
    @Lazy
    @Autowired
    AppUserService appUserService;

    List<String> imageExt = Arrays.asList("jpg", "tif","png","bmp","raw");
    List<String> videoExt = Arrays.asList("mp4", "mov","wmv","avi","mkv","flv","f4v","webm");
    public ImageDTO addImage(ImageDTO imageDTO){
        Image i = imageMapper.toEntity(imageDTO);
        i = imageRepository.save(i);
        return imageMapper.toDto(i);
    }
    public List<ImageDTO> getAllImages(){
        return imageRepository.findAll().stream().map(imageMapper::toDto).collect(Collectors.toList());
    }

    public ImageDTO updateImage(ImageDTO imageToBeUpdated){
        return imageMapper.toDto(imageRepository.save(imageMapper.toEntity(imageToBeUpdated)));
    }
    public void deleteImage(Long imageId){
        boolean exists = imageRepository.existsById(imageId);
        if(!exists){
            throw new IllegalStateException("Image with id "+imageId+" does not exist");
        }

        ImageDTO imageDTO = new ImageDTO();
        imageDTO = imageMapper.toDto(imageRepository.findById(imageId).get());
        //System.out.println(imageDTO.getName());
        //System.out.println("Nesto");
        try {
            Map result = Singleton.getCloudinary().uploader().destroy(imageDTO.getName().toString(),ObjectUtils.asMap("resource_type","image"));
            imageRepository.deleteById(imageId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    public ImageDTO uploadPhoto(MultipartFile photoUpload, Long socialPostId) throws IOException {
        Map uploadResult = null;
        String imageName = null;
        String imageUrl = null;

        if (photoUpload != null && !photoUpload.isEmpty()) {
            uploadResult = Singleton.getCloudinary().uploader().upload(photoUpload.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
        }
        for (Object pair : uploadResult.entrySet()) {
            String temp = pair.toString().split("=")[0];
            //System.out.println(temp);
            if(temp.equals("public_id")){
                //System.out.println("Nasao sam public id");
                imageName = pair.toString().split("=")[1];
            }
            if(temp.equals("url")){
                //System.out.println("Nasao sam url");
                imageUrl= pair.toString().split("=")[1];
            }
        }

        System.out.println("--------------------------------------------------------");
        System.out.println(uploadResult);
        System.out.println("--------------------------------------------------------");
        System.out.println(imageName);
        System.out.println(imageUrl);
        SocialPostDTO socialPostDTO = new SocialPostDTO();
        socialPostDTO.setId(socialPostId);
        ImageDTO newImage = new ImageDTO();
        newImage.setSocialPost(socialPostDTO);
        newImage.setName(imageName);
        newImage.setUrl(imageUrl);
        addImage(newImage);

        return null;
    }*/
    public ImageDTO uploadProfilePhoto(MultipartFile photoUpload, Long userId) throws IOException {
        Map uploadResult = null;
        String imageName = null;
        String imageUrl = null;

        try {
            BufferedImage imBuff = ImageIO.read(photoUpload.getInputStream());
            String ext = FileNameUtils.getExtension(photoUpload.getOriginalFilename());


            Dimension bound = new Dimension(1024, 1024);
            Dimension imageSize = new Dimension(imBuff.getWidth(), imBuff.getHeight());
            Dimension scale = getScaledDimension(imageSize, bound);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(imgscalrResizeImage(imBuff, scale.width, scale.height), ext, os);
            //InputStream is = new ByteArrayInputStream(os.toByteArray());

            byte[] ImageOutput = os.toByteArray();
            //byte[] ImageOutput=compress.resizeImage(file.getBytes(), ImageFormats.JPEG, ResizeResolution.R480P);
            uploadResult = Singleton.getCloudinary().uploader().upload(ImageOutput,
                    ObjectUtils.asMap("resource_type", "auto"));

            for (Object pair : uploadResult.entrySet()) {
                String temp = pair.toString().split("=")[0];
                //System.out.println(temp);
                if (temp.equals("public_id")) {
                    //System.out.println("Nasao sam public id");
                    imageName = pair.toString().split("=")[1];
                }
                if (temp.equals("url")) {
                    //System.out.println("Nasao sam url");
                    imageUrl = pair.toString().split("=")[1];
                }
            }

            /*
            System.out.println("--------------------------------------------------------");
            System.out.println(uploadResult);
            System.out.println("--------------------------------------------------------");
            System.out.println(imageName);
            System.out.println(imageUrl);*/
            AppUserDTO appUserDTO = appUserMapper.toDto(appUserRepository.findById(userId).get());


            ImageDTO newImage = new ImageDTO();
            newImage.setName(imageName);
            newImage.setUrl(imageUrl);
            newImage = addImage(newImage);
            appUserDTO.setImage(newImage);
            appUserRepository.save(appUserMapper.toEntity(appUserDTO));

            return newImage;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to resize the file");
        }
    }

    BufferedImage imgscalrResizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        //return Scalr.resize(originalImage, targetWidth);
        return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }
    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    //
    public ImageDTO uploadPhoto(MultipartFile file,Long socialPostId){
        if (file == null || file.isEmpty()){
            throw new IllegalStateException("File is empty or null");
        }
        Map uploadResult = null;
        String imageName = null;
        String imageUrl = null;
        IVCompressor compress=new IVCompressor();

        try {
            BufferedImage imBuff = ImageIO.read(file.getInputStream());
            String ext = FileNameUtils.getExtension(file.getOriginalFilename());


            if(imageExt.contains(ext)){
                Dimension bound = new Dimension(1024, 1024);
                Dimension imageSize = new Dimension(imBuff.getWidth(), imBuff.getHeight());
                Dimension scale = getScaledDimension(imageSize, bound);

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(imgscalrResizeImage(imBuff, scale.width, scale.height), ext, os);
                //InputStream is = new ByteArrayInputStream(os.toByteArray());

                byte[] ImageOutput = os.toByteArray();
                //byte[] ImageOutput=compress.resizeImage(file.getBytes(), ImageFormats.JPEG, ResizeResolution.R480P);
                uploadResult = Singleton.getCloudinary().uploader().upload(ImageOutput,
                        ObjectUtils.asMap("resource_type", "auto"));
            } else if (videoExt.contains(ext)) {
                IVSize customRes = new IVSize();
                customRes.setWidth(1280);
                customRes.setHeight(720);
                IVAudioAttributes audioAttribute = new IVAudioAttributes();
                // here 64kbit/s is 64000
                audioAttribute.setBitRate(64000);
                audioAttribute.setChannels(2);
                audioAttribute.setSamplingRate(44100);
                IVVideoAttributes videoAttribute = new IVVideoAttributes();
                // Here 160 kbps video is 160000
                videoAttribute.setBitRate(160000);
                // More the frames more quality and size, but keep it low based on //devices like mobile
                videoAttribute.setFrameRate(30);
                videoAttribute.setSize(customRes);
                byte[] VideoOutput = compress.encodeVideoWithAttributes(file.getBytes(), VideoFormats.MP4,audioAttribute, videoAttribute);
                //byte[] VideoOutput=compress.reduceVideoSize(file.getBytes(), VideoFormats.MP4, ResizeResolution.R720P);//for video compression
                uploadResult = Singleton.getCloudinary().uploader().upload(VideoOutput,
                        ObjectUtils.asMap("resource_type", "auto"));
            }
            else {
                throw new IllegalStateException("Failed to find ext");
            }
            for (Object pair : uploadResult.entrySet()) {
                String temp = pair.toString().split("=")[0];
                //System.out.println(temp);
                if(temp.equals("public_id")){
                    //System.out.println("Nasao sam public id");
                    imageName = pair.toString().split("=")[1];
                }
                if(temp.equals("url")){
                    //System.out.println("Nasao sam url");
                    imageUrl= pair.toString().split("=")[1];
                }
            }

            SocialPostDTO socialPostDTO = new SocialPostDTO();
            socialPostDTO.setId(socialPostId);
            ImageDTO newImage = new ImageDTO();
            newImage.setSocialPost(socialPostDTO);
            newImage.setName(imageName);
            newImage.setUrl(imageUrl);
            addImage(newImage);

            return newImage;

        }catch (Exception e){
            throw new IllegalStateException("Failed to resize the file", e);
        }
    }

}
