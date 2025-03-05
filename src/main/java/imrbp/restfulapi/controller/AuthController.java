package imrbp.restfulapi.controller;

import imrbp.restfulapi.model.LoginUserRequest;
import imrbp.restfulapi.model.RegisterUserRequest;
import imrbp.restfulapi.model.TokenResponse;
import imrbp.restfulapi.model.WebResponse;
import imrbp.restfulapi.service.AuthService;
import imrbp.restfulapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse token = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(token).build();
    }
}
