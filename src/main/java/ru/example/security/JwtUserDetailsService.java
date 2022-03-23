package ru.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.user.User;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.JwtUserMapper;
import ru.example.service.UserService;

import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final JwtUserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String studentNumber) throws UsernameNotFoundException {
        User user = userService.getUserByStudentNumber(studentNumber);

        Optional.ofNullable(user)
                .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));

        return mapper.map(user);
    }
}
