package me.moonbinchoi.blog.global.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    ADMIN,
    MEMBER,
    GUEST
}
