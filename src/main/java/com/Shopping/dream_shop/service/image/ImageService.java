package com.Shopping.dream_shop.service.image;

import com.Shopping.dream_shop.dto.ImageDto;
import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Image;
import com.Shopping.dream_shop.model.Product;
import com.Shopping.dream_shop.repository.ImageRepository;
import com.Shopping.dream_shop.service.product.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;

    public ImageService(ImageRepository imageRepository, IProductService productService) {
        this.imageRepository = imageRepository;
        this.productService = productService;
    }

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("no image found" + imageId));
    }

    @Override
    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("no image found" + imageId);
        });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto= new ArrayList<>();
        for(MultipartFile file:files){
            try{
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFiletype(file.getContentType());
                image.setImage(file.getBytes());
                image.setProduct(product);

                imageRepository.save(image);
                String downloadUrl = "/api/v1/images/image/download/" + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage= imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFilename());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);

        try{
            image.setFilename(file.getOriginalFilename());
            image.setFiletype(file.getContentType());
            image.setImage(file.getBytes());
            imageRepository.save(image);
        } catch (IOException  e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public IProductService getProductService() {
        return productService;
    }
}
