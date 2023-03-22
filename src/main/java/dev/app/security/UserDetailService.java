package dev.app.security;

import dev.app.common.consts.ErrorMessageConst;
import dev.app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
