package io.github.wyatt.muffer.domain.member.auth;

import io.github.wyatt.muffer.domain.auth.service.AuthService;
import io.github.wyatt.muffer.domain.auth.request.SignUpReq;
import io.github.wyatt.muffer.domain.member.Member;
import io.github.wyatt.muffer.domain.member.MemberRepo;
import io.github.wyatt.muffer.domain.member.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepo memberRepo;

    Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.create(
                Role.USER, "test member", "test nickname", "test email",
                "test password", "test phone", "test address"
        );
        memberRepo.save(testMember);
    }

    @Test
    @DisplayName("Sign up test")
    void signUp() {
        SignUpReq req = new SignUpReq(
                "sign up test name", "sign up test nickname",
                "sign up test email", "signup_password", "sign up test phone", "sign up test address"
        );
        authService.signUp(req);
        assertThat(memberRepo.count(), is(2L));

        Member member = memberRepo.findByEmail(req.email()).get();
        assertThat(ObjectUtils.isEmpty(member), is(false));
        assertThat(member.getRole(), is(Role.USER));

    }
}