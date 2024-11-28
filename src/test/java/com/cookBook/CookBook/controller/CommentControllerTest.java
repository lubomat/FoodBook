package com.cookBook.CookBook.controller;

import com.cookBook.CookBook.model.Comment;
import com.cookBook.CookBook.model.Recipe;
import com.cookBook.CookBook.model.User;
import com.cookBook.CookBook.service.CommentService;
import com.cookBook.CookBook.service.RecipeService;
import com.cookBook.CookBook.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private RecipeService recipeService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentController commentController;

    private User mockUser;
    private Recipe mockRecipe;
    private Comment mockComment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        // Mock recipe
        mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        mockRecipe.setName("Test Recipe");

        // Mock comment
        mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setContent("Test comment");
        mockComment.setRating(5);
        mockComment.setUser(mockUser);
        mockComment.setRecipe(mockRecipe);
    }

    @Test
    void testAddComment() {
        // Mock authentication and services
        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(mockRecipe));
        when(commentService.addComment(mockRecipe, mockUser, "Test comment", 5)).thenReturn(mockComment);

        // Prepare input comment
        Comment inputComment = new Comment();
        inputComment.setContent("Test comment");
        inputComment.setRating(5);

        // Call the controller method
        ResponseEntity<Comment> response = commentController.addComment(1L, inputComment, authentication);

        // Verify and assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test comment", response.getBody().getContent());
        assertEquals(5, response.getBody().getRating());

        verify(userService, times(1)).findByUsername("testuser");
        verify(recipeService, times(1)).getRecipeById(1L);
        verify(commentService, times(1)).addComment(mockRecipe, mockUser, "Test comment", 5);
    }

    @Test
    void testGetCommentsByRecipe() {
        // Mock comment service
        List<Comment> comments = Arrays.asList(mockComment);
        when(commentService.getCommentsByRecipe(1L)).thenReturn(comments);

        // Call the controller method
        ResponseEntity<List<Comment>> response = commentController.getCommentsByRecipe(1L);

        // Verify and assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Test comment", response.getBody().get(0).getContent());

        verify(commentService, times(1)).getCommentsByRecipe(1L);
    }
}
