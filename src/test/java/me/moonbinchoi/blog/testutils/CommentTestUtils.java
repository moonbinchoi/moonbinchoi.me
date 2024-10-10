package me.moonbinchoi.blog.testutils;

import me.moonbinchoi.blog.board.domain.Comment;
import me.moonbinchoi.blog.board.domain.Post;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public final class CommentTestUtils {

    private static final String AUTHOR = "TestAuthor";
    private static final String BODY = "This is test comment";

    public static String getAuthor() {
        return AUTHOR;
    }

    public static String getBody() {
        return BODY;
    }

    public static Comment generateCommentEntity(Post post) {
        return Comment.builder()
                .post(post)
                .author(AUTHOR)
                .body(BODY)
                .isPrivate(false)
                .build();
    }
}
