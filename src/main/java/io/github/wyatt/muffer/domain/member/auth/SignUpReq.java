package io.github.wyatt.muffer.domain.member.auth;

import io.github.wyatt.muffer.domain.member.Member;
import io.github.wyatt.muffer.domain.member.Role;

public record SignUpReq(
        String name,
        String nickname,
        String email,
        String password,
        String phone,
        String address
) {

    public Member toEntity(String encodedPassword, Role role) {
        return Member.create(
                role, name, nickname, email,
                encodedPassword, phone, address
        );
    }
}
