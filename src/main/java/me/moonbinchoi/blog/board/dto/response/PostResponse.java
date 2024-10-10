package me.moonbinchoi.blog.board.dto.response;

import lombok.Builder;
import lombok.Data;
import me.moonbinchoi.blog.board.domain.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public final class PostResponse {

    /* Metadata */
    private final Long id;

    private final LocalDateTime created;

    private final LocalDateTime updated;

    private final String author;

    private final List<TagResponse> tags;

    /* Body */
    private final String title;

    private final String content;



    @Builder
    private PostResponse(final Long id, final LocalDateTime created, final LocalDateTime updated, final String author, final Collection<TagResponse> tags, final String title, final String content) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.author = author;
        this.title = title;
        this.content = content;

        this.tags = new ArrayList<TagResponse>();
        this.tags.addAll(tags);
    }

    public static PostResponse from(final Post post) {
        List<TagResponse> tagResponses = new ArrayList<>();
        post.getPostTagRelations().forEach(relation -> tagResponses.add(TagResponse.from(relation.getTag())));

        return PostResponse.builder()
                .id(post.getId())
                .created(post.getCreated())
                .updated(post.getUpdated())
                .author(post.getAuthor())
                .tags(tagResponses)
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    //TODO: html 태그 제거에 Jsoup 사용할지 결정. 이미지 파일 헤더가 content에 포함되는지 확인
    public static PostResponse fromWithTruncatedContent(final Post post) {
        List<TagResponse> tagResponses = new ArrayList<>();
        post.getPostTagRelations().forEach(relation -> tagResponses.add(TagResponse.from(relation.getTag())));

        return PostResponse.builder()
                .id(post.getId())
                .created(post.getCreated())
                .updated(post.getUpdated())
                .author(post.getAuthor())
                .tags(tagResponses)
                .title(post.getTitle())
                .content(truncateContent(post.getContent()))
                .build();
    }

    private static String truncateContent(final String content) {
        final int limitLength = 50;
        if (content.length() > limitLength) {
            return content.substring(0, limitLength) + " ...";
        }
        return content;
    }
}
