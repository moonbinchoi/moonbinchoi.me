package me.moonbinchoi.blog.board.dto.response;

import lombok.Data;
import me.moonbinchoi.blog.board.domain.Tag;

@Data
public final class TagResponse {

    private final Long id;

    private final String name;

    private TagResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static TagResponse of(final Long id, final String name) {
        return new TagResponse(id, name);
    }

    public static TagResponse from(final Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
