package me.moonbinchoi.blog.board.service.component;

import jakarta.persistence.EntityNotFoundException;
import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.domain.Tag;
import me.moonbinchoi.blog.board.repository.PostRepository;
import me.moonbinchoi.blog.board.service.module.CommentCrudService;
import me.moonbinchoi.blog.board.service.module.PostCrudService;
import me.moonbinchoi.blog.board.service.module.PostTagRelationReadOnlyService;
import me.moonbinchoi.blog.board.service.module.TagCrudService;
import me.moonbinchoi.blog.testutils.PostTestUtils;
import me.moonbinchoi.blog.testutils.TagTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Import({TagService.class, TagCrudService.class, PostCrudService.class, PostTagRelationReadOnlyService.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagCrudService tagCrudService;

    @Autowired
    private PostCrudService postCrudService;

    private Post post;
    private Tag tag;

    @BeforeEach
    void setUp() {
        post = PostTestUtils.generatePostEntity();
        postCrudService.save(post);

        tag = TagTestUtils.generateTagEntity();
        tagCrudService.save(tag);
        post.addTag(tag);
    }


    @Nested
    class deleteTag {

        @Test
        void Success() {
            //given

            //when
            tagService.deleteTag(tag.getId());

            //then
            assertThat(tagService.findTag(tag.getId())).isEmpty();
            assertThat(post.getTags()).isEmpty();
        }

        @Test
        void Tag_not_found_throws_EntityNotFoundException() {
            //given
            Long fakeId = 1L;

            //when, then
            assertThrows(EntityNotFoundException.class, () -> tagService.deleteTag(fakeId));
            assertThat(tagService.findTag(tag.getId())).isPresent();
            assertThat(post.getTags()).containsOnly(tag);
        }
    }
}