package me.moonbinchoi.blog.board.service.module;

import jakarta.persistence.EntityNotFoundException;
import me.moonbinchoi.blog.board.domain.Tag;
import me.moonbinchoi.blog.testutils.TagTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(TagCrudService.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TagCrudServiceTest {

    @Autowired
    private TagCrudService tagService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = TagTestUtils.generateTagEntity();
    }

    @Test
    void save() {
        //given

        //when
        final Tag saved = tagService.save(tag);

        //then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(TagTestUtils.getName());
    }

    @Nested
    class findById {

        @Test
        void Tag_found() {
            //given
            final Tag saved = tagService.save(tag);

            //when
            final Optional<Tag> found = tagService.findById(tag.getId());

            //then
            assertThat(found.isPresent()).isTrue();
            assertThat(found.get().getName()).isEqualTo(TagTestUtils.getName());
        }

        @Test
        void Tag_not_found_throws_EntityNotFoundException() {
            //given
            Long fakeId = 1L;

            //when
            final Optional<Tag> found = tagService.findById(fakeId);

            //then
            assertThat(found.isPresent()).isFalse();
        }
    }

    @Nested
    class findAll {

        @Test
        void Result_found() {
            //given
            final Tag saved1 = tagService.save(tag);
            final Tag saved2 = tagService.save(TagTestUtils.generateTagEntity());
            final List<Tag> expectedList = List.of(saved1, saved2);

            //when
            List<Tag> foundList = tagService.findAll();

            //then
            assertThat(foundList.size()).isEqualTo(expectedList.size());
            assertThat(foundList).containsAll(expectedList);
        }

        @Test
        void Result_not_found() {
            //given

            //when
            final List<Tag> foundList = tagService.findAll();

            //then
            assertThat(foundList).isEmpty();
        }

        @Test
        void Result_found_by_specific_ids() {
            //given
            final Tag saved1 = tagService.save(tag);
            final Tag saved2 = tagService.save(TagTestUtils.generateTagEntity());
            final Long savedId1 = saved1.getId();
            final Long savedId2 = saved2.getId();
            final List<Tag> expectedList = List.of(saved1, saved2);

            //when
            final List<Tag> foundList = tagService.findAll(List.of(savedId1, savedId2));

            //then
            assertThat(foundList.size()).isEqualTo(expectedList.size());
            assertThat(foundList).containsAll(expectedList);
        }

        @Test
        void Result_not_found_by_specific_ids_throws_EntityNotFoundException() {
            //given
            final Long fakeId1 = 1L;
            final Long fakeId2 = 2L;

            //when
            final List<Tag> foundList = tagService.findAll(List.of(fakeId1, fakeId2));

            //then
            assertThat(foundList).isEmpty();
        }
    }

    @Nested
    class update {

        @Test
        void Success() {
            //given
            final Tag saved = tagService.save(tag);
            final Long savedId = saved.getId();
            final Tag toUpdate = TagTestUtils.generateTagEntity();
            toUpdate.setName("Updated tag");

            //when
            final Tag updated = tagService.update(savedId, toUpdate);

            //then
            assertThat(updated.getName()).isEqualTo("Updated tag");
        }

        @Test
        void Tag_not_found_throws_EntityNotFoundException() {
            //given
            final Long fakeId = 1L;
            final Tag toUpdate = TagTestUtils.generateTagEntity();
            toUpdate.setName("Updated tag");

            //when, then
            assertThrows(EntityNotFoundException.class, () -> tagService.update(fakeId, toUpdate));
        }
    }

    @Nested
    class deleteById {

        @Test
        void Success() {
            //given
            final Tag saved = tagService.save(tag);
            final Long savedId = saved.getId();

            //when
            tagService.deleteById(savedId);

            //then
            assertThat(tagService.findById(savedId)).isEmpty();
        }

        @Test
        void Tag_not_found_throws_EntityNotFoundException() {
            //given
            final Long fakeId = 1L;

            //when, then
            assertThrows(EntityNotFoundException.class, () -> tagService.deleteById(fakeId));
        }
    }

    @Nested
    class existsById {

        @Test
        void Tag_found() {
            //given
            final Tag saved = tagService.save(tag);

            //when
            final boolean b = tagService.existsById(saved.getId());

            //then
            assertThat(b).isTrue();
        }

        @Test
        void Tag_not_found() {
            //given
            Long fakeId = 1L;

            //when
            boolean b = tagService.existsById(fakeId);

            //then
            assertThat(b).isFalse();
        }
    }

    @Nested
    class existsAllById {

        @Test
        void Tag_found() {
            //given
            final Tag saved1 = tagService.save(tag);
            final Tag saved2 = tagService.save(TagTestUtils.generateTagEntity());
            final Long savedId1 = saved1.getId();
            final Long savedId2 = saved2.getId();

            //when
            boolean b = tagService.existsAllById(List.of(savedId1, savedId2));

            //then
            assertThat(b).isTrue();
        }

        @Test
        void Tag_not_found() {
            //given
            final Long fakeId1 = 1L;
            final Long fakeId2 = 2L;

            //when
            boolean b = tagService.existsAllById(List.of(fakeId1, fakeId2));

            //then
            assertThat(b).isFalse();
        }
    }
}
