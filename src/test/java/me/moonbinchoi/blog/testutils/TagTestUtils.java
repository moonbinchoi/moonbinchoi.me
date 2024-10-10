package me.moonbinchoi.blog.testutils;

import me.moonbinchoi.blog.board.domain.Tag;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public final class TagTestUtils {

    private static final String NAME = "Tag Title";

    public static String getName() {
        return NAME;
    }

    public static Tag generateTagEntity() {
        return new Tag(NAME);
    }

}
