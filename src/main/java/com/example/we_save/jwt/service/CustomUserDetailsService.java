package com.example.we_save.jwt.service;

import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import com.example.we_save.jwt.dto.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //DB에서 조회
        Long userId = Long.valueOf(username);
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(user.orElse(null));
        }
        return null;
    }
}
