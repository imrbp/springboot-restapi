package imrbp.restfulapi.service;

import imrbp.restfulapi.entity.User;
import imrbp.restfulapi.model.LoginUserRequest;
import imrbp.restfulapi.model.TokenResponse;
import imrbp.restfulapi.repository.UserRepository;
import imrbp.restfulapi.security.BCrypt;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername()).orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next24Hours());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    private Long next24Hours() {
        return System.currentTimeMillis() + (24 * 60 * 60 * 1000);
    }
}
