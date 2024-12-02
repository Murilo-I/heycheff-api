package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.request.FollowRequest;
import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.FollowResponse;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.app.usecase.UserUseCase;
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
public class UserService implements UserUseCase {

    final UserRepository userRepository;
    final ReceiptRepository receiptRepository;

    public UserService(UserRepository userRepository, ReceiptRepository receiptRepository) {
        this.userRepository = userRepository;
        this.receiptRepository = receiptRepository;
    }

    @Override
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

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponse findById(String userId) {
        var receiptCount = receiptRepository.findByOwnerId(
                userId, PageRequest.of(1, 1)
        ).getTotalElements();
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return fromUser(user, receiptCount);
    }

    @Override
    @Transactional
    public FollowResponse follow(FollowRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);
        var userToFollow = userRepository.findById(request.getUserToFollowId())
                .orElseThrow(() -> new UserNotFoundException("Following ID Not Found!"));

        var userFollowing = user.getFollowingIds();
        boolean followingNotRemoved = !userFollowing.removeIf(request.getUserToFollowId()::equals);
        if (followingNotRemoved)
            userFollowing.add(request.getUserToFollowId());

        user.setFollowingIds(userFollowing);
        userRepository.save(user);

        var userFollowers = userToFollow.getFollowersIds();
        boolean followerNotRemoved = !userFollowers.removeIf(request.getUserId()::equals);
        if (followerNotRemoved)
            userFollowers.add(request.getUserId());

        userToFollow.setFollowersIds(userFollowers);
        userRepository.save(userToFollow);

        return new FollowResponse(userFollowing);
    }
}
