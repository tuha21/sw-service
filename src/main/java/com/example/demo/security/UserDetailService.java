package com.example.demo.security;

import com.example.demo.common.consts.ErrorMessageConst;
import com.example.demo.repository.AccountRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var account = accountRepository.findByUsername(username);
            if (account != null) {
                return new User(account.getUsername(), account.getPassword(), Collections.emptyList());
            } else {
                throw new UsernameNotFoundException(ErrorMessageConst.AuthErrorMessage.ERROR_USERNAME_NOT_EXIST);
            }
        } catch (Exception e) {
            throw new InternalError(ErrorMessageConst.AuthErrorMessage.ERROR_INTERNAL_GET_USER);
        }
    }

}
