package com.cookBook.CookBook.repository;

import com.cookBook.CookBook.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeId(Long recipeId);  // Pobiera komentarze dla danego przepisu
}
