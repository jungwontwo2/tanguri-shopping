package com.tanguri.shopping.controller;

import com.tanguri.shopping.AuthenticationHelper;
import com.tanguri.shopping.domain.dto.comments.WriteCommentDto;
import com.tanguri.shopping.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final AuthenticationHelper authenticationHelper;

    @PostMapping("/comment/write")
    public String writeComment(@ModelAttribute("Comment") WriteCommentDto writeCommentDto, @RequestParam("productId") Long productId) {
        Long userId = authenticationHelper.getAuthenticatedUserId();
        if (userId != null) {
            commentService.writeComment(writeCommentDto.getComment(), productId, userId);
        }
        // 댓글 저장 로직 추가

        // 원래 상품 페이지로 리다이렉트
        return "redirect:/product/" + productId;
    }

    @PostMapping("/comment/update/{id}")
    public String updateComment(@PathVariable Long id,
                                @RequestParam("text") String updatedComment,
                                @RequestParam("productId") Long productId) {
        commentService.updateComment(updatedComment, id);
        return "redirect:/product/" + productId;
    }

    @PostMapping("comment/delete/{id}")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam("productId") Long productId) {
        commentService.deleteComment(id);
        return "redirect:/product/" + productId;
    }

    @PostMapping("/comment/write/{id}")
    public String writeReply(@PathVariable("id") Long id, @RequestParam String comment,
                             @RequestParam Long parentId, @RequestParam Long productId) {
        Long userId = authenticationHelper.getAuthenticatedUserId();
        commentService.writeReply(id, userId, comment, parentId, productId);
        return "redirect:/product/" + productId;
    }

    @PostMapping("/comment/like/{id}")
    public String likeComment(@PathVariable Long id,@RequestParam Long productId){
        System.out.println("comment/like");
        commentService.likeComment(id);
        return "redirect:/product/"+productId;
    }

    @PostMapping("/comment/report/{id}")
    public String reportComment(@PathVariable Long id,@RequestParam Long productId){
        System.out.println("comment/report");
        commentService.reportComment(id);
        return "redirect:/product/"+productId;
    }
}
