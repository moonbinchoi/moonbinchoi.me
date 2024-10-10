package me.moonbinchoi.blog.board.dto.response;

import lombok.Builder;
import lombok.Data;
import me.moonbinchoi.blog.board.domain.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public final class CommentResponse {

    /* Metadata */
    private Long id;

    private final LocalDateTime created;

    private final LocalDateTime updated;

    private final String author;

    private final Boolean isDeleted;

    private final Boolean isPrivate;

    private final List<CommentResponse> children;

    /* Body */
    private final String body;

    @Builder
    private CommentResponse(final Long id, final LocalDateTime created, final LocalDateTime updated, final String author, final Boolean isDeleted, final Boolean isPrivate, final List<Comment> children, final String body) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.author = author;
        this.isDeleted = isDeleted;
        this.isPrivate = isPrivate;
        this.children = new ArrayList<>();
        this.body = body;

        children.forEach(comment -> this.children.add(CommentResponse.from(comment)));
    }

    public static CommentResponse from(final Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .author(comment.getAuthor())
                .isDeleted(comment.getIsDeleted())
                .isPrivate(comment.getIsPrivate())
                .children(comment.getChildren())
                .body(comment.getBody())
                .build();
    }

}
