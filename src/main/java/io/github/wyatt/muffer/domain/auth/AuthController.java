package io.github.wyatt.muffer.domain.auth;

import io.github.wyatt.muffer.domain.auth.request.SignUpReq;
import io.github.wyatt.muffer.domain.auth.service.AuthService;
import io.github.wyatt.muffer.global.auth.utils.TokenCookieFactory;
import io.github.wyatt.muffer.domain.auth.token.TokenType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerMember(
            @RequestBody SignUpReq req,
            HttpServletResponse res
    ) {
        authService.signUp(req);
        //NOTE query string 은 프론트에서 처리 후 url 에서 제거해야함.
        return ResponseEntity.status(HttpStatus.SEE_OTHER).header(HttpHeaders.LOCATION, "/?notify=signup-success").build();
    }

    // logout
   @PostMapping("/sign-out")
    public void logout(
           @CookieValue(name = "REFRESH_TOKEN", required = false) String refreshToken,
           HttpServletResponse response
   ) {
        if (refreshToken != null) authService.logout(refreshToken);

       ResponseCookie expiredCookie = TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN.name());
       response.addHeader("Set-Cookie", expiredCookie.toString());
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
