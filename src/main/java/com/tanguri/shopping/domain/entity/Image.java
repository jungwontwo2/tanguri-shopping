package com.tanguri.shopping.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String uuid_image_name;
    private String image_url;
    private String original_image_name;
    public void UpdateImage(Image image){
        this.uuid_image_name=image.getUuid_image_name();
        this.image_url=image.getImage_url();
        this.original_image_name=image.getOriginal_image_name();
    }
}
