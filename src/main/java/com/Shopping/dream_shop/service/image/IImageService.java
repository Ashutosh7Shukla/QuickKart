package com.Shopping.dream_shop.service.image;

import com.Shopping.dream_shop.dto.ImageDto;
import com.Shopping.dream_shop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long imageId);

    void deleteImageById(Long imageId);

    List<ImageDto> saveImages(List<MultipartFile>  files , Long productId);

    void updateImage(MultipartFile file , Long imageId);
}
