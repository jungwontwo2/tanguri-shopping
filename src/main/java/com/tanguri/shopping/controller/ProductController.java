package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.product.AddProductDto;
import com.tanguri.shopping.domain.dto.product.BuyOrCartProductDto;
import com.tanguri.shopping.domain.dto.product.ViewProductDto;
import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @GetMapping("product/upload")
    public String productUploadForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    Model model, @ModelAttribute("product")AddProductDto addProductDto){
        User user = customUserDetails.getUserEntity();
        model.addAttribute("user",user);
        return "user/seller/addProduct";
    }
    @PostMapping("product/upload")
    public String productUpload(@Validated @ModelAttribute("product") AddProductDto addProductDto, BindingResult bindingResult,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,Model model) throws IOException {
        if(bindingResult.hasErrors()){
            model.addAttribute("user",customUserDetails.getUserEntity());
            return "user/seller/addProduct";
        }
        productService.uploadProduct(addProductDto,customUserDetails.getId());
        return "redirect:/";
    }

    @GetMapping("product/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @ModelAttribute("buyOrCartProductDto")BuyOrCartProductDto buyOrCartProductDto){
        if(customUserDetails!=null){
            model.addAttribute("user", customUserDetails.getUserEntity());
        }
        ViewProductDto product = productService.getProduct(id);
        model.addAttribute("product",product);
        return "product/ProductView";
    }
    @PostMapping("product/order/{productId}")
    public String buyProduct(@PathVariable("productId")Long productId,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @ModelAttribute("product") BuyOrCartProductDto buyOrCartProductDto){
        productService.orderProduct(productId,customUserDetails.getId(),buyOrCartProductDto);
        System.out.println("buyOrCartProductDto = " + buyOrCartProductDto.getCount());
        return "redirect:/product/"+productId;
    }

}
