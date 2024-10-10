package me.moonbinchoi.blog.board.service.module;

import jakarta.persistence.EntityNotFoundException;
import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.domain.Tag;
import me.moonbinchoi.blog.board.repository.TagRepository;
import me.moonbinchoi.blog.testutils.PostTestUtils;
import me.moonbinchoi.blog.testutils.TagTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(PostCrudService.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostCrudServiceTest {

    @Autowired
    private PostCrudService postCrudService;

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


    @Test
    void save() {
        //given

        //when
        final Post savedPost = postCrudService.save(post);

        //then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(PostTestUtils.getTitle());
        assertThat(savedPost.getContent()).isEqualTo(PostTestUtils.getContent());
    }

    @Nested
    class findById {

        @Test
        void Success() {
            //given
            final Post saved = postCrudService.save(post);
            Long savedId = saved.getId();

            //when
            final Optional<Post> result1 = postCrudService.findById(savedId);

            //then
            assertThat(result1).isPresent();
            assertThat(result1.get()).isEqualTo(saved);
        }

        @Test
        void Post_not_found() {
            //given
            Long fakeId = 1L;

            //when
            final Optional<Post> result = postCrudService.findById(fakeId);

            //then
            assertThat(result).isNotPresent();
        }
    }

    @Nested
    class findAll {

        @Test
        void Results_found() {
            //given
            final Post saved1 = postCrudService.save(post);
            final Post newPost = PostTestUtils.generatePostEntity();
            final Post saved2 = postCrudService.save(newPost);
            final List<Post> expected = List.of(saved1, saved2);

            //when
            List<Post> foundList = postCrudService.findAll();

            //then
            assertThat(foundList).hasSameElementsAs(expected);
        }

        @Test
        void Results_not_found() {
            //given

            //when
            List<Post> foundList = postCrudService.findAll();

            //then
            assertThat(foundList).isEmpty();
        }

        @Test
        void PageRequest_Success() {
            //given
            final Post saved1 = postCrudService.save(post);
            final Post newPost = PostTestUtils.generatePostEntity();
            final Post saved2 = postCrudService.save(newPost);
            final List<Post> expected = List.of(saved2, saved1);

            final int pageSize = 5;
            PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("created").descending());
            Page<Post> expectedPage = new PageImpl<>(expected, pageRequest, expected.size());

            //when
            final Page<Post> result = postCrudService.findAll(pageRequest);

            //then
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(expectedPage.getTotalElements());
            assertThat(result.getContent()).isEqualTo(expectedPage.getContent());
        }

        @Test
        void PageRequest_not_found() {
            final int pageSize = 5;
            PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("created").descending());
            Page<Post> expectedPage = new PageImpl<>(List.of(), pageRequest, 0);

            //when
            final Page<Post> result = postCrudService.findAll(pageRequest);

            //then
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(expectedPage.getTotalElements());
            assertThat(result.getContent()).isEqualTo(expectedPage.getContent());
        }
    }

    @Nested
    class update {

        @Test
        void Success() {
            //given
            final Post saved = postCrudService.save(post);
            final Long postId = saved.getId();
            final Tag tag = TagTestUtils.generateTagEntity();
            final Set<Tag> originTags = saved.getTags();
            Set<Tag> updateTags = new HashSet<>();

            tag.setName("Updated Tag");
            tagRepository.save(tag);
            updateTags.add(tag);

            final Post toUpdate = PostTestUtils.generatePostEntity();
            toUpdate.setTitle("Updated Title");
            toUpdate.setContent("Updated Content");
            toUpdate.updateTags(updateTags);

            //when
            final Post updated = postCrudService.update(postId, toUpdate);

            //then
            assertThat(updated.getTitle()).isEqualTo(toUpdate.getTitle());
            assertThat(updated.getContent()).isEqualTo(toUpdate.getContent());
            assertThat(updated.getTags()).isEqualTo(updateTags);
            assertThat(updated.getTags()).doesNotContainAnyElementsOf(originTags);
        }

        @Test
        void Post_not_found_throws_EntityNotFoundException() {
            //given
            final Long fakeId = 1L;
            final Post toUpdate = PostTestUtils.generatePostEntity();

            //when, then
            assertThrows(EntityNotFoundException.class, () -> postCrudService.update(fakeId, toUpdate));
        }

    }

    @Nested
    class deleteById {

        @Test
        void Success() {
            //given
            final Post saved = postCrudService.save(post);
            final Long postId = saved.getId();

            //when
            postCrudService.deleteById(postId);

            //then
            assertThat(postCrudService.findById(postId)).isEmpty();
        }

        @Test
        void Post_not_found_throws_EntityNotFoundException() {
            //given
            final Long fakeId = 1L;

            //when, then
            assertThrows(EntityNotFoundException.class, () -> postCrudService.deleteById(fakeId));
        }
    }

    @Nested
    class existsById {
        @Test
        void Post_found() {
            //given
            final Post saved = postCrudService.save(post);
            final Long postId = saved.getId();

            //when
            final boolean result = postCrudService.existsById(postId);

            //then
            assertThat(result).isTrue();
        }

        @Test
        void Post_not_found() {
            //given

            Long fakeId = 1L;
            final Post saved = postCrudService.save(post);

            //when
            final boolean result = postCrudService.existsById(fakeId);

            //then
            assertThat(result).isFalse();
        }
    }
}