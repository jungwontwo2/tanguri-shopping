package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.entity.Comment;
import com.tanguri.shopping.domain.entity.Product;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ProductService productService;

    public void writeComment(String commentText,Long productId,Long userId){
        User user = userService.findUser(userId);
        Product product = productService.getProduct(productId);
        Comment comment = new Comment(user, user.getUsername(),product, commentText);
        commentRepository.save(comment);
    }
    public List<Comment> getCommentsByProductId(Long productId){
        return commentRepository.findByProductId(productId);
    }

    public void updateComment(String updatedComment, Long id) {
        try{
            Optional<Comment> optionalComment = commentRepository.findById(id);
            Comment comment = optionalComment.get();
            System.out.println("comment.getComment() = " + comment.getComment());

            comment.updateComment(updatedComment);
            commentRepository.save(comment);
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        System.out.println("comment update complete");
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
