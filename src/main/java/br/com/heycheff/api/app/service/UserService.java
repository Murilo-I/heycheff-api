package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.data.model.Role;
import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.data.repository.UserRepository;
import br.com.heycheff.api.util.exception.UserNotFoundException;
import br.com.heycheff.api.util.exception.UserRegistrationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static br.com.heycheff.api.util.mapper.TypeMapper.fromUser;

@Service
public class UserService {

    final UserRepository userRepository;
    final ReceiptRepository receiptRepository;

    public UserService(UserRepository userRepository, ReceiptRepository receiptRepository) {
        this.userRepository = userRepository;
        this.receiptRepository = receiptRepository;
    }

    @Transactional
    public User save(UserRequest request) {
        try {
            return userRepository.save(
                    User.builder().email(request.getEmail())
                            .username(request.getUsername())
                            .password(new BCryptPasswordEncoder()
                                    .encode(request.getPassword()))
                            .roles(Collections.singletonList(Role.USER))
                            .followersIds(Collections.emptyList())
                            .followingIds(Collections.emptyList())
                            .build()
            );
        } catch (Exception e) {
            throw new UserRegistrationException(e);
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponse findById(String userId) {
        var receiptCount = receiptRepository.findByOwnerId(
                userId, PageRequest.of(1, 1)
        ).getTotalElements();
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return fromUser(user, receiptCount);
    }
}
