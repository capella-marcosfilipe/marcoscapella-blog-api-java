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
import br.com.marcoscapella.blog.utils.SlugUtil;
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

    public PostResponseDTO createPost(PostRequestDTO dto) {
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
        post.setSlug(SlugUtil.toSlug(post.getTitle()));

        // Set createdAt and updatedAt
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        // Finally save the post
        Post savedPost = postRepo.save(post);

        return convertToResponseDTO(savedPost);
    }

    public PostResponseDTO convertToResponseDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSlug(post.getSlug());
        dto.setContent(post.getContent());
        dto.setPublished(post.getPublished());

        // Author
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(post.getAuthor().getId());
        authorDTO.setUsername(post.getAuthor().getUsername());
        authorDTO.setEmail(post.getAuthor().getEmail());
        dto.setAuthor(authorDTO);

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

    public Post updatePostPartially(Long id, PostRequestDTO updatedFields) {
        Post existingPost = postRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (updatedFields.getTitle() != null) {
            existingPost.setTitle(updatedFields.getTitle());
            existingPost.setSlug(SlugUtil.toSlug(updatedFields.getTitle())); // se quiser atualizar o slug também
        }

        if (updatedFields.getContent() != null) {
            existingPost.setContent(updatedFields.getContent());
        }

        if (updatedFields.getPublished() != null) {
            existingPost.setPublished(updatedFields.getPublished());
        }

        if (updatedFields.getAuthor() != null) {
            User author = userRepo.findById(updatedFields.getAuthor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
            existingPost.setAuthor(author);
        }

        if (updatedFields.getTags() != null) {
            Set<Tag> tags = updatedFields.getTags().stream().map(dto ->
                    tagRepo.findById(dto.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found: " + dto.getId()))
            ).collect(Collectors.toSet());
            existingPost.setTags(tags);
        }

        return postRepo.save(existingPost);
    }

    public void deletePost(Long id) {
        Post post = postRepo.findByIdWithTags(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Saving the tags to check if they're orphans after deleting
        Set<Tag> tagsToCheck = post.getTags();

        postRepo.deleteById(id);

        // Deleting orphan tags
        for (Tag tag : tagsToCheck) {
            if (tag.getPosts().isEmpty()) {
                tagRepo.delete(tag);
            }
        }

    }

    public boolean existsById(Long id) {
        return postRepo.existsById(id);
    }
}
