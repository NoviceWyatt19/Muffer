package io.github.wyatt.muffer.domain.member.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerBasic(
            @RequestBody SignUpReq req,
            HttpServletResponse res
    ) {
        authService.signUp(req);
        //NOTE query string 은 프론트에서 처리 후 url 에서 제거해야함.
        return ResponseEntity.status(HttpStatus.SEE_OTHER).header(HttpHeaders.LOCATION, "/?notify=signup-success").build();
    }

   @PostMapping("/sign-out")
    public void signOut() {
        Long memberId = 1L;
        authService.signOut(memberId);
    }

    /*
    @PostMapping("/reissue")
    public void reissue() {
        authService.reissue();
    }

    @PostMapping("/withdraw")
    public void withdraw() {
        authService.withdraw();
    }*/
}
