package br.com.marcoscapella.blog.repositories;

import br.com.marcoscapella.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.tags WHERE p.id = :id")
    Optional<Post> findByIdWithTags(@Param("id") Long id);
}
