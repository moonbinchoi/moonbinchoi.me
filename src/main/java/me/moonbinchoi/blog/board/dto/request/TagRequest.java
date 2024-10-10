package me.moonbinchoi.blog.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.moonbinchoi.blog.board.domain.Tag;

public abstract class TagRequest {

    @Data
    public static class Create {

        @NotBlank
        String name;

        public Tag toEntity() {
            return new Tag(name);
        }
    }

    @Data
    public static class Read {

        @NotNull
        Long id;

    }

    @Data
    public static class Update {

        @NotBlank
        String name;

        public Tag toEntity() {
            return new Tag(name);
        }
    }

    @Data
    public static class Delete {

        @NotNull
        Long id;

    }
}
