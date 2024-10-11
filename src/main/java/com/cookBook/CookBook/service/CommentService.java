package com.cookBook.CookBook.service;

import com.cookBook.CookBook.model.Comment;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Dodanie komentarza do przepisu
    public Comment addComment(Recipe recipe, User user, String content, int rating) {
        Comment comment = new Comment();
        comment.setRecipe(recipe);
        comment.setUser(user);
        comment.setContent(content);
        comment.setRating(rating);
        return commentRepository.save(comment);
    }

    // Pobranie komentarzy dla przepisu
    public List<Comment> getCommentsByRecipe(Long recipeId) {
        return commentRepository.findByRecipeId(recipeId);
    }
}
