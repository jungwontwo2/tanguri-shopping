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
        return commentRepository.findByProductIdWithChildren(productId);
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

    public void writeReply(Long id, Long userId, String comment, Long parentId,Long productId) {
        Comment findComment = commentRepository.findById(id).orElse(null);
        User user = userService.findUser(userId);
        Comment commentEntity = new Comment(user, user.getUsername(), findComment.getProduct(), comment);
        commentEntity.setParent(findComment);
        commentRepository.save(commentEntity);
    }

    public void likeComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        comment.addRecommend();
        commentRepository.save(comment);
    }

    public void reportComment(Long id) {
        commentRepository.findById(id).orElse(null).addReport();
    }
}
