package imrbp.restfulapi.service;

import imrbp.restfulapi.model.RegisterUserRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    @Autowired
    private Validator validator;

    public void validate(Object request) {
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(request);
        if (constraintViolationSet.size() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, constraintViolationSet.stream().map(constraintVioldation ->
                    String.format("%s: %s", constraintVioldation.getPropertyPath(), constraintVioldation.getMessage())).collect(Collectors.joining(", ")));
        }
    }
}