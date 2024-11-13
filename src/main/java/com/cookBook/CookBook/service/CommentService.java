package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.Comment;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        logger.info("CommentService initialized.");
    }

    public Comment addComment(Recipe recipe, User user, String content, int rating) {
        logger.info("Adding a new comment. Recipe ID: {}, User ID: {}, Rating: {}",
                recipe.getId(), user.getId(), rating);

        try {
            Comment comment = new Comment();
            comment.setRecipe(recipe);
            comment.setUser(user);
            comment.setContent(content);
            comment.setRating(rating);
            Comment savedComment = commentRepository.save(comment);
            logger.info("Comment added successfully. Comment ID: {}", savedComment.getId());
            return savedComment;
        } catch (Exception e) {
            logger.error("Error occurred while adding comment for Recipe ID: {}, User ID: {}",
                    recipe.getId(), user.getId(), e);
            throw e;
        }
    }

    public List<Comment> getCommentsByRecipe(Long recipeId) {
        logger.info("Fetching comments for Recipe ID: {}", recipeId);

        try {
            List<Comment> comments = commentRepository.findByRecipeId(recipeId);
            logger.info("Fetched {} comments for Recipe ID: {}", comments.size(), recipeId);
            return comments;
        } catch (Exception e) {
            logger.error("Error occurred while fetching comments for Recipe ID: {}", recipeId, e);
            throw e;
        }
    }
}
