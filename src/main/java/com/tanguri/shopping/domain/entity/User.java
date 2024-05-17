package com.tanguri.shopping.domain.entity;

import com.tanguri.shopping.domain.dto.user.UserModifyDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
    private Cart cart;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
    private String username;
    private String password;
    private String email;
    private String name;
    private String addressNumber;
    private String address;
    private String detailAddress;
    private String phone;
    private String role;
    private Integer money;
    private Integer earning;
    private LocalDateTime createDate;

    public User(Cart cart,String username,String password,String email,String name,
                String addressNumber,String address,String detailAddress,boolean isSeller,String phone){
        this.username=username;
        this.password=password;
        this.email=email;
        this.name=name;
        this.address = address;
        this.addressNumber = addressNumber;
        this.detailAddress =detailAddress;
        this.phone=phone;
        money=0;
        earning=0;
        createDate=LocalDateTime.now();
        this.cart=cart;
        this.role=isSeller? "ROLE_SELLER":"ROLE_BUYER";
    }

    public void addMoney(int amount) {
        money+=amount;
    }
    public void useMoney(Integer useMoney){
        money-=useMoney;
    }

    public void addEarning(Integer totalPrice) {
        this.earning+=totalPrice;
    }

    public void modifyUserInfo(UserModifyDto modifyDto){
        this.email = modifyDto.getEmail();
        this.addressNumber = modifyDto.getAddressNumber();
        this.address = modifyDto.getAddress();
        this.detailAddress = modifyDto.getDetailAddress();
        this.phone = modifyDto.getPhone();
    }
}
