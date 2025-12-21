package io.github.wyatt.muffer.domain.member;

import io.github.wyatt.muffer.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;
    private String nickname;
    private String email;
    private String password;
    private String phone;
    private String address;
    private boolean isLocked;

    private String profileImage;

    @Builder
    private Member(
            Role role, String name, String nickname, String email,
            String password, String phone, String address, String profileImage,
            boolean isLocked) {
        this.role = role;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.profileImage = profileImage;
        this.isLocked = isLocked;
    }

    public static Member create(
            Role role, String name, String nickname, String email,
            String password, String phone, String address) {
        return Member.builder()
                .role(role)
                .name(name)
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .profileImage(null)
                .address(address)
                .password(password)
                .isLocked(false)
                .build();
    }
}
