package me.moonbinchoi.blog.testutils;

import me.moonbinchoi.blog.board.domain.Post;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public final class PostTestUtils {

    private static final String TITLE = "Post Title";
    private static final String CONTENT = "Post Content";
    private static final String AUTHOR = "TestAuthor";

    public static String getTitle() {
        return TITLE;
    }

    public static String getContent() {
        return CONTENT;
    }

    public static Post generatePostEntity() {
        return Post.builder()
                .title(TITLE)
                .content(CONTENT)
                .author(AUTHOR)
                .build();
    }
}
