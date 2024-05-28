package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.entity.Image;
import com.tanguri.shopping.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public void deleteImage(Long id){
        Image image = imageRepository.findByProductId(id);
        imageRepository.delete(image);
    }
}
