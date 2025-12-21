package io.github.wyatt.muffer.domain.member.auth;

import io.github.wyatt.muffer.domain.member.MemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepo memberRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(memberRepo.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("can not found user")
        ));
    }
}
