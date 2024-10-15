package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.Comment;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.service.CommentService;
import com.cookBook.CookBook.service.RecipeService;
import com.cookBook.CookBook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

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
        User currentUser = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie został znaleziony"));

        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new RuntimeException("Przepis nie został znaleziony"));

        comment.setRecipe(recipe);
        comment.setUser(currentUser);

        Comment savedComment = commentService.addComment(recipe, currentUser, comment.getContent(), comment.getRating());

        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<List<Comment>> getCommentsByRecipe(@PathVariable Long recipeId) {
        List<Comment> comments = commentService.getCommentsByRecipe(recipeId);
        return ResponseEntity.ok(comments);
    }
}


