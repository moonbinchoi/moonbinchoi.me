package me.moonbinchoi.blog.board.service.component;

import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.domain.Tag;
import me.moonbinchoi.blog.board.repository.TagRepository;
import me.moonbinchoi.blog.testutils.PostTestUtils;
import me.moonbinchoi.blog.testutils.TagTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(PostService.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private TagRepository tagRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = PostTestUtils.generatePostEntity();

        final Tag tag = TagTestUtils.generateTagEntity();
        tagRepository.save(tag);
        post.addTag(tag);
    }

}