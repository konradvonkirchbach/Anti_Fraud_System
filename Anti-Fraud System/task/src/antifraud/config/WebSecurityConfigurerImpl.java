package antifraud.config;

import antifraud.model.user.RoleEnum;
import antifraud.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private UserDetailServiceImpl userDetailService;

    @Autowired
    public WebSecurityConfigurerImpl(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                                     UserDetailServiceImpl userDetailService) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(getEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .antMatchers("/actuator/shutdown").permitAll() // needs to run test
                // other matchers
                .mvcMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasRole(RoleEnum.MERCHANT.name())
                .mvcMatchers("/api/auth/list").hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.SUPPORT.name())
                .mvcMatchers("/api/antifraud/suspicious-ip").hasAnyRole(RoleEnum.SUPPORT.name())
                .mvcMatchers("/api/antifraud/suspicious-ip/**").hasAnyRole(RoleEnum.SUPPORT.name())
                .mvcMatchers("/api/antifraud/stolencard").hasAnyRole(RoleEnum.SUPPORT.name())
                .mvcMatchers("/api/antifraud/feedback").hasAnyRole(RoleEnum.SUPPORT.name())
                .mvcMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasAnyRole(RoleEnum.SUPPORT.name())
                .mvcMatchers("/api/antifraud/stolencard/**").hasAnyRole(RoleEnum.SUPPORT.name())
                .mvcMatchers("/api/antifraud/history/**").hasAnyRole(RoleEnum.SUPPORT.name())
                .mvcMatchers("/api/auth/role").hasAnyRole(RoleEnum.ADMINISTRATOR.name())
                .mvcMatchers("/api/auth/access").hasAnyRole(RoleEnum.ADMINISTRATOR.name())
                .mvcMatchers("/api/auth/user/**").hasAnyRole(RoleEnum.ADMINISTRATOR.name())
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}

