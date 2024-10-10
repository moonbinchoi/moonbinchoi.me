package me.moonbinchoi.blog.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@ToString
public abstract class BoardEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated = LocalDateTime.now();


    @PreUpdate
    public void preUpdate() {
        this.updated = LocalDateTime.now();
    }

}
