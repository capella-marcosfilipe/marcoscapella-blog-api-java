package br.com.marcoscapella.blog.services;

import br.com.marcoscapella.blog.dtos.PostRequestDTO;
import br.com.marcoscapella.blog.dtos.PostResponseDTO;
import br.com.marcoscapella.blog.dtos.AuthorDTO;
import br.com.marcoscapella.blog.dtos.TagDTO;
import br.com.marcoscapella.blog.models.Post;
import br.com.marcoscapella.blog.models.Tag;
import br.com.marcoscapella.blog.models.User;
import br.com.marcoscapella.blog.repositories.PostRepo;
import br.com.marcoscapella.blog.repositories.TagRepo;
import br.com.marcoscapella.blog.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired private PostRepo postRepo;
    @Autowired private TagRepo tagRepo;
    @Autowired private UserRepo userRepo;

    public List<Post> getAllPosts() {
        return (List<Post>)postRepo.findAll();
    }

    public Optional<Post> getPostBySlug(String slug) {
        return postRepo.findBySlug(slug);
    }

    public Post createPost(PostRequestDTO dto) {
        Post post = new Post();

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPublished(dto.getPublished());

        // Fetch author from DB using the ID from DTO
        User author = userRepo.findById(dto.getAuthor().getId()).orElseThrow(() -> new RuntimeException("Author not found"));
        post.setAuthor(author);

        // Check if the post object have tags (not null and not empty)
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            // Resolve tag names into actual Tag entities from the DB
            Set<Tag> resolvedTags = dto.getTags().stream().map(tag -> {
                // Find a tag in the DB with the same name
                return tagRepo.findByName(tag.getName())
                        .orElseGet(() -> { // if the tag doesn't exist, (...)
                    // (...) create a new tag entity
                    Tag newTag = new Tag();
                    newTag.setName(tag.getName());

                    // Save the new tag to the DB so it gets an ID.
                    return tagRepo.save(newTag);
                });
            }).collect(Collectors.toSet());

            // Associate all the resolved tag entities (old and new) with the post.
            post.setTags(resolvedTags);
        }

        // Creating slug name
        post.setSlug(
                post.getTitle().toLowerCase().replaceAll(" ", "-")
        );

        // Set createdAt and updatedAt
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        // Finally save the post
        return postRepo.save(post);
    }

    public PostResponseDTO convertToResponseDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSlug(post.getSlug());
        dto.setContent(post.getContent());
        dto.setPublished(post.getPublished());

        // Author
        User author = post.getAuthor();
        dto.setAuthor(new AuthorDTO(author.getId(), author.getUsername()));

        // Tags
        Set<TagDTO> tagDTOs = post.getTags().stream()
                .map(tag -> new TagDTO(tag.getName()))
                .collect(Collectors.toSet());
        dto.setTags(tagDTOs);

        dto.setCreatedAt(post.getCreatedAt().toString());
        dto.setUpdatedAt(post.getUpdatedAt().toString());

        return dto;
    }

    public Post updatePost(Post post) {
        post.setUpdatedAt(LocalDateTime.now());
        return postRepo.save(post);
    }

    public void deletePost(Long id) {
        if (!postRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepo.deleteById(id);
    }

    public boolean existsById(Long id) {
        return postRepo.existsById(id);
    }
}
