package io.github.wyatt.muffer.domain.member.auth;

import io.github.wyatt.muffer.domain.member.Member;
import io.github.wyatt.muffer.domain.member.MemberRepo;
import io.github.wyatt.muffer.domain.member.Role;
import io.github.wyatt.muffer.global.auth.paseto.PasetoConfig;
import io.github.wyatt.muffer.global.config.ErrorCode;
import io.github.wyatt.muffer.global.exceptions.BusinessNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepo mRepo;
    private final PasetoConfig tokenProvider;
    private final PasswordEncoder pwdEncoder;

    public void signUp(SignUpReq req) {
        String encodedPassword = pwdEncoder.encode(req.password());
        log.info("create member -> {}", req);
        Member member = req.toEntity(encodedPassword, Role.USER);
        mRepo.save(member);
    }

    public void signOut(Long memberId) {
        Member member = mRepo.findById(memberId).orElseThrow(
                () -> new BusinessNotFoundException(ErrorCode.NOT_FOUND_DATA)
        );
        log.info("deactivate member -> {}", member);
        member.deactivated();
    }
}
