package com.tanguri.shopping.controller;

import com.tanguri.shopping.AuthenticationHelper;
import com.tanguri.shopping.domain.dto.ResponseDto;
import com.tanguri.shopping.domain.dto.oauth2.CustomOAuth2User;
import com.tanguri.shopping.domain.dto.product.PagingProductDto;
import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.dto.user.UserLoginDto;
import com.tanguri.shopping.domain.dto.user.UserModifyDto;
import com.tanguri.shopping.domain.dto.user.UserSignUpDto;
import com.tanguri.shopping.domain.entity.Cart;
import com.tanguri.shopping.domain.entity.CartItem;
import com.tanguri.shopping.domain.entity.Order;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.domain.enums.Status;
import com.tanguri.shopping.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/")
    public String mainPage(@PageableDefault(page = 1) Pageable pageable,
                           Model model) {
        authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
        Page<PagingProductDto> allProducts = productService.getAllProducts(pageable);

        int blockLimit = 5;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), allProducts.getTotalPages());
        model.addAttribute("productDtos", allProducts);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "home/home";
    }

    @GetMapping("/user/signup")
    public String SignUpPage(@ModelAttribute("user") UserSignUpDto userSignUpDto) {
        return "user/usersignup";
    }

    @PostMapping("/user/signup")
    public String PostSignUp(@Validated @ModelAttribute("user") UserSignUpDto userSignUpDto, BindingResult bindingResult) {
        if (!userSignUpDto.getPassword().equals(userSignUpDto.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "passwordCheckValid", "비밀번호가 일치하지 않습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "user/usersignup";
        }
        userService.saveUser(userSignUpDto);
        return "redirect:/";
    }

    @GetMapping("/user/login")
    public String LoginPage(@ModelAttribute("user") UserLoginDto userLoginDto, Model model, HttpServletRequest request,
                            @RequestParam(value = "error",required = false)String error,
                            @RequestParam(value = "exception",required = false)String exception) {
        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);
        return "user/userlogin";
    }

    @PostMapping("/user/login")
    public String PostLogin(@Validated @ModelAttribute("user") UserLoginDto userLoginDto, BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "user/userlogin";
        }
        User user = loginService.login(userLoginDto.getUsername(), userLoginDto.getPassword());
        if (user == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/userlogin";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/user/signup/loginIdCheck")
    public @ResponseBody ResponseDto<?> check(@RequestBody(required = false) String loginId) {
        if (loginId == null || loginId.isEmpty()) {
            return new ResponseDto<>(-1, "아이디를 입력해주세요.", null);
        }
        if (!loginId.matches("^[a-z0-9]+$") || loginId.length() < 4 || loginId.length() > 10) {
            return new ResponseDto<>(-1, "아이디는 알파벳 소문자와 숫자만 포함할 수 있습니다. 4글자 이상 10글자 이하로 입력해주세요.", null);
        }
        User user = userService.getUserByLoginId(loginId);
        if (user == null) {
            return new ResponseDto<>(1, "사용 가능한 ID입니다.", true);
        } else {
            return new ResponseDto<>(1, "중복된 아이디입니다.", false);
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("user/cart/{id}")
    public String cartPage(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
        Cart cart = userService.getCartByLoginId(id);
        List<CartItem> cartItems = cart.getCartItems();
        int totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getCount() * cartItem.getProduct().getPrice();
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalProductCount", cartItems.size());
        model.addAttribute("cartItems", cartItems);
        return "user/buyer/userCart";
    }

    @GetMapping("user/money/{id}")
    public String moneyPage(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                            Model model) {
        User user = authenticationHelper.getAuthenticatedUser().orElse(null);
        List<CartItem> cartItems = user.getCart().getCartItems();
        model.addAttribute("user", user);
        model.addAttribute("totalProductCount", cartItems.size());
        return "user/money";
    }

    // 잔액충전 처리
    @PostMapping("/user/charge/pro")
    public String chargePro(@RequestParam("amount") int amount) {
        User user = authenticationHelper.getAuthenticatedUser().orElse(null);
        System.out.println("user.getMoney() = " + user.getMoney());
        userService.chargeMoney(user.getId(), amount);
        return "redirect:/";
    }



    @GetMapping("/seller/sellHist/{id}")
    public String sellHistory(@PathVariable("id") Long id, Model model) {
        User user = authenticationHelper.getAuthenticatedUser().orElse(null);
        List<Order> orders = orderService.getSellerItems(id);
        Integer totalSellCount = 0;
        Integer totalSellEarning = 0;
        for (Order order : orders) {
            if(order.getStatus().equals(Status.배송준비))
            {
                totalSellCount+=order.getProductCount();
                totalSellEarning+=order.getTotalPrice();
            }
            else if(order.getStatus().equals(Status.배송중)){
                totalSellCount+=order.getProductCount();
                totalSellEarning+=order.getTotalPrice();
            }
            System.out.println("order.getStatus() = " + order.getStatus());
        }
        model.addAttribute("user",user);
        model.addAttribute("orders",orders);
        model.addAttribute("totalSellCount",totalSellCount);
        model.addAttribute("totalSellEarning",totalSellEarning);

        return "user/seller/sellHist";
    }
    @GetMapping("/user/{id}")
    public String userInfo(@PathVariable("id")Long id, Model model){
        User user = authenticationHelper.getAuthenticatedUser().orElse(null);
        List<CartItem> cartItems = user.getCart().getCartItems();
        model.addAttribute("totalProductCount",cartItems.size());
        model.addAttribute("user",user);
        return "user/userPage";
    }

    @GetMapping("/user/modify/{id}")
    public String modifyUserInfoForm(@PathVariable("id") Long id, Model model) {
        authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
        return "user/userModify";
    }
    @PostMapping("/user/modify/{id}")
    public String modifyUserInfo(@PathVariable("id")Long id, @ModelAttribute UserModifyDto userModifyDto){
        User user = authenticationHelper.getAuthenticatedUser().orElse(null);
        userService.modifyUserInfo(id,userModifyDto);
        return "redirect:/user/"+id;
    }
}
