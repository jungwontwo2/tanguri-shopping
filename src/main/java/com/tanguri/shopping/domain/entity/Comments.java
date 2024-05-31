package com.tanguri.shopping.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comments parent;
    @OneToMany(mappedBy = "parent",orphanRemoval = true)
    private List<Comments> children = new ArrayList<>();
    private String username;
    private String comment;
    private LocalDateTime createDate;
    private Integer recommendCnt;
    private Integer reportCnt;
}
