package com.tanguri.shopping.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent",orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String username;
    private String comment;
    private LocalDateTime createDate;
    private Integer recommendCnt;
    private Integer reportCnt;
    // 필수 필드를 설정하는 생성자
    public Comment(User user, String username,Product product, String comment) {
        this.product= product;
        this.user = user;
        this.username = username;
        this.comment = comment;
        this.createDate = LocalDateTime.now();
        this.recommendCnt = 0;
        this.reportCnt = 0;
    }

    // 부모 댓글을 설정하는 메서드
    public void setParent(Comment parent) {
        this.parent = parent;
    }

    // 자식 댓글을 추가하는 메서드
    public void addChild(Comment child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void updateComment(String comment){
        this.comment=comment;
    }
    public void addRecommend(){
        this.recommendCnt+=1;
    }
    public void addReport(){
        this.reportCnt+=1;
    }
}
