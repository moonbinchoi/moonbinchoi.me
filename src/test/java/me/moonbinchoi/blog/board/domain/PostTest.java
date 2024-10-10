package me.moonbinchoi.blog.board.domain;

import me.moonbinchoi.blog.board.service.module.TagCrudService;
import me.moonbinchoi.blog.testutils.PostTestUtils;
import me.moonbinchoi.blog.testutils.TagTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TagCrudService.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostTest {

    @Autowired
    private TagCrudService tagCrudService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = PostTestUtils.generatePostEntity();
        final Tag tag = TagTestUtils.generateTagEntity();
        tagCrudService.save(tag);
        post.addTag(tag);
    }


    @Nested
    class removeTag {

        @Test
        void Success() {
            //given
            Tag tag = post.getTags().iterator().next();

            //when
            post.removeTag(tag);

            //then
            assertThat(post.getTags()).isEmpty();
        }

        @Test
        void Tag_relation_not_found() {
            //given
            final Tag tag = post.getTags().iterator().next();
            final Tag fakeTag = TagTestUtils.generateTagEntity();
            tagCrudService.save(fakeTag);

            //when
            post.removeTag(fakeTag);

            //then
            assertThat(post.getTags().size()).isEqualTo(1);
            assertThat(post.getTags()).contains(tag);
        }
    }

    @Nested
    class updateTags {

        @Test
        void Update_nothing() {
            //given
            final Tag tag = post.getTags().iterator().next();

            //when
            post.updateTags(Set.of(tag));

            //then
            assertThat(post.getTags()).containsOnly(tag);
        }

        @Test
        void Replace_all_to_new_tags() {
            //given
            final Tag newTag1 = TagTestUtils.generateTagEntity();
            final Tag newTag2 = TagTestUtils.generateTagEntity();
            newTag1.setName("Updated Tag 1");
            newTag2.setName("Updated Tag 2");

            //when
            post.updateTags(Set.of(newTag1, newTag2));

            //then
            assertThat(post.getTags()).containsOnly(newTag1, newTag2);
        }

        @Test
        void Update_until_preserve_origin_tags() {
            //given
            final Tag originalTag = post.getTags().iterator().next();
            final Tag newTag1 = TagTestUtils.generateTagEntity();
            final Tag newTag2 = TagTestUtils.generateTagEntity();
            newTag1.setName("Updated Tag 1");
            newTag2.setName("Updated Tag 2");

            //when
            post.updateTags(Set.of(originalTag, newTag1, newTag2));

            //then
            assertThat(post.getTags()).containsOnly(originalTag, newTag1, newTag2);
        }
    }
}