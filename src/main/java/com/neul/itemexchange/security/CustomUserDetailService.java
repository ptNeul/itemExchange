package com.neul.itemexchange.security;

import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;

import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.exception.custom.UserException;
import com.neul.itemexchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findById(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    return new CustomUserDetails(user);
  }
}
