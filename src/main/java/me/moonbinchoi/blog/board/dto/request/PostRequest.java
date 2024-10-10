package me.moonbinchoi.blog.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.moonbinchoi.blog.board.domain.Post;

import java.util.HashSet;
import java.util.Set;

public abstract class PostRequest {

    @Data
    public final static class Create {

        @NotBlank
        String author;

        @NotBlank
        String title;

        @NotBlank
        String content;

        @NotNull
        Set<Long> tagIds = new HashSet<>();

        public Post toEntity() {
            return Post.builder()
                    .author(author)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Data
    public final static class Read {

        @NotNull
        Long postId;

    }

    @Data
    public final static class Update {

        @NotNull
        Long postId;

        @NotBlank
        String title;

        @NotBlank
        String content;

        @NotNull
        Set<Long> tagIds;

        public Post toEntity() {
            return Post.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Data
    public final static class Delete {

        @NotNull
        Long postId;

    }
}
