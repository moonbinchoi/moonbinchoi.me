package me.moonbinchoi.blog.board.service.module;

import jakarta.persistence.EntityNotFoundException;
import me.moonbinchoi.blog.board.domain.Comment;
import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.repository.PostRepository;
import me.moonbinchoi.blog.testutils.CommentTestUtils;
import me.moonbinchoi.blog.testutils.PostTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(CommentCrudService.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentCrudServiceTest {

    @Autowired
    CommentCrudService commentCrudService;

    @Autowired
    PostRepository postRepository;

    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        post = PostTestUtils.generatePostEntity();
        postRepository.save(post);
        comment = CommentTestUtils.generateCommentEntity(post);
    }

    @Test
    void save() {
        //given

        //when
        final Comment saved = commentCrudService.save(comment);

        //then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAuthor()).isEqualTo(CommentTestUtils.getAuthor());
        assertThat(saved.getBody()).isEqualTo(CommentTestUtils.getBody());
        assertThat(saved.getIsPrivate()).isFalse();
    }

    @Nested
    class findById {

        @Test
        void Success() {
            //given
            final Comment saved = commentCrudService.save(comment);

            //when
            final Optional<Comment> found = commentCrudService.findById(comment.getId());

            //then
            assertThat(found).isPresent();
            assertThat(found.get()).isEqualTo(saved);
        }

        @Test
        void Comment_not_found() {
            //given
            final Long fakeId = 1L;

            //when
            final Optional<Comment> found = commentCrudService.findById(fakeId);

            //then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    class findAll {

        @Test
        void Results_found() {
            //given
            final Comment saved1 = commentCrudService.save(comment);
            final Comment saved2 = commentCrudService.save(CommentTestUtils.generateCommentEntity(post));
            final List<Comment> expectedList = List.of(saved1, saved2);

            //when
            final List<Comment> foundList = commentCrudService.findAll();

            //then
            assertThat(foundList).hasSameElementsAs(expectedList);
        }

        @Test
        void Results_not_found() {
            //given

            //when
            final List<Comment> foundList = commentCrudService.findAll();

            //then
            assertThat(foundList).isEmpty();
        }
    }

    @Nested
    class update {

        @Test
        void Success() {
            //given
            final Comment saved = commentCrudService.save(comment);
            final Comment toUpdate = CommentTestUtils.generateCommentEntity(post);
            toUpdate.setBody("This is updated comment.");
            toUpdate.setIsPrivate(true);

            //when
            final Comment updated = commentCrudService.update(saved.getId(), toUpdate);

            //then
            assertThat(updated.getAuthor()).isEqualTo(toUpdate.getAuthor());
            assertThat(updated.getBody()).isEqualTo(toUpdate.getBody());
            assertThat(updated.getIsPrivate()).isEqualTo(toUpdate.getIsPrivate());
        }

        @Test
        void Comment_not_found_throws_EntityNotFoundException() {
            final Long fakeId = 1L;
            final Comment toUpdate = CommentTestUtils.generateCommentEntity(post);
            toUpdate.setBody("This is updated comment.");
            toUpdate.setIsPrivate(true);

            //when, then
            assertThrows(EntityNotFoundException.class, () -> commentCrudService.update(fakeId, toUpdate));
        }

        @Test
        void Comment_author_and_request_author_not_matched() {
            final Comment saved = commentCrudService.save(comment);
            final Comment toUpdate = Comment.builder()
                    .post(post)
                    .author("AnotherAuthor")
                    .body("This is updated comment.")
                    .isPrivate(true)
                    .build();

            //when, then
            assertThrows(IllegalArgumentException.class, () -> commentCrudService.update(saved.getId(), toUpdate));
        }
    }

    @Nested
    class deleteById {

        @Test
        void Success() {
            //given
            final Comment saved = commentCrudService.save(comment);
            final Long commentId = saved.getId();

            //when
            commentCrudService.deleteById(commentId);

            //then
            assertThat(commentCrudService.findById(commentId)).isEmpty();
        }

        @Test
        void Comment_not_found_throws_EntityNotFoundException() {
            //given
            final Long fakeId = 1L;

            //when, then
            assertThrows(EntityNotFoundException.class, () -> commentCrudService.deleteById(fakeId));
        }
    }

    @Nested
    class existsById {

        @Test
        void Comment_found() {
            //given
            final Comment saved = commentCrudService.save(comment);

            //when
            final boolean b = commentCrudService.existsById(saved.getId());

            //then
            assertThat(b).isTrue();
        }

        @Test
        void Comment_not_found() {
            //given
            final Long fakeId = 1L;

            //when
            final boolean b = commentCrudService.existsById(fakeId);

            //then
            assertThat(b).isFalse();
        }
    }
}