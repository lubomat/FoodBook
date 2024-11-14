package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.Comment;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.service.CommentService;
import com.cookBook.CookBook.service.RecipeService;
import com.cookBook.CookBook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Secured("ROLE_USER")
    @PostMapping("/{recipeId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long recipeId,
                                              @RequestBody Comment comment,
                                              Authentication authentication) {
        logger.info("Received request to add a comment for recipeId: {}", recipeId);

        if (comment.getRating() == 0) {
            logger.error("Rating is missing or invalid.");
            return ResponseEntity.badRequest().body(null);
        }

        try {
            User currentUser = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> {
                        logger.error("User not found: {}", authentication.getName());
                        return new RuntimeException("User not found.");
                    });

            logger.info("Authenticated user: {}", currentUser.getUsername());

            Recipe recipe = recipeService.getRecipeById(recipeId)
                    .orElseThrow(() -> {
                        logger.error("Recipe not found with ID: {}", recipeId);
                        return new RuntimeException("Recipe not found.");
                    });

            comment.setRecipe(recipe);
            comment.setUser(currentUser);

            Comment savedComment = commentService.addComment(recipe, currentUser, comment.getContent(), comment.getRating());
            logger.info("Comment successfully added for recipeId: {}, by user: {}", recipeId, currentUser.getUsername());

            return ResponseEntity.ok(savedComment);

        } catch (Exception e) {
            logger.error("Error while adding comment for recipeId: {}", recipeId, e);
            throw e;
        }
    }


    @GetMapping("/{recipeId}")
    public ResponseEntity<List<Comment>> getCommentsByRecipe(@PathVariable Long recipeId) {
        logger.info("Received request to fetch comments for recipeId: {}", recipeId);

        try {
            List<Comment> comments = commentService.getCommentsByRecipe(recipeId);
            logger.info("Successfully fetched {} comments for recipeId: {}", comments.size(), recipeId);

            return ResponseEntity.ok(comments);

        } catch (Exception e) {
            logger.error("Error while fetching comments for recipeId: {}", recipeId, e);
            throw e;
        }
    }
}


