package org.example.produktylist.Service;

import org.example.produktylist.Entity.MyUserDetails;
import org.example.produktylist.Entity.User;
import org.example.produktylist.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serwis obsługujący użytkowników.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("Not found : "+username);
        return new MyUserDetails(user.get());
    }


}
