package me.moonbinchoi.blog.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.moonbinchoi.blog.board.domain.Comment;
import me.moonbinchoi.blog.board.domain.Post;

/**
 * toEntity();
 * Convert to entity MUST be done at the COMMAND SERVICE LAYER
 */
public abstract class CommentRequest {

    @Data
    public static class Create {

        @NotNull
        Long postId;

        @NotBlank
        String author;

        @NotNull
        Boolean isPrivate;

        @NotBlank
        String body;

        public Comment toEntity(final Post foundPost) {
            return Comment.builder()
                    .post(foundPost)
                    .author(author)
                    .isPrivate(isPrivate)
                    .body(body)
                    .build();
        }

    }


    @Data
    public static class CreateReply {

        @NotNull
        Long postId;

        @NotBlank
        String author;

        @NotNull
        Boolean isPrivate;

        @NotNull
        Long parentCommentId;

        @NotBlank
        String body;

        public Comment toEntity(final Post foundPost, final Comment foundParentComment) {
            return Comment.builder()
                    .post(foundPost)
                    .author(author)
                    .isPrivate(isPrivate)
                    .parent(foundParentComment)
                    .body(body)
                    .build();
        }
    }

    @Data
    public static class Update {

        @NotNull
        Long postId;

        @NotNull
        Long commentId;

        @NotNull
        Boolean isPrivate;

        @NotBlank
        String body;

        public Comment toEntity() {
            return Comment.builder()
                    .isPrivate(isPrivate)
                    .body(body)
                    .build();
        }
    }

    @Data
    public static class Delete {

        @NotNull
        Long postId;

        @NotNull
        Long commentId;

        public Comment toEntity(final Post foundPost) {
            return Comment.builder()
                    .post(foundPost)
                    .build();
        }
    }
}
