package org.example.produktylist.Utils;

import org.example.produktylist.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/product/add/**", "/product/edit/**", "/category/**", "/orders/**").hasRole("ADMIN")
                .antMatchers("/product/details/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/").permitAll()
                .and()
                .formLogin()
                    .defaultSuccessUrl("/product/", true)
//                    .successHandler(new CustomAuthenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}
