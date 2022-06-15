package ru.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.example.dao.entity.user.Role;
import ru.example.security.jwt.JwtConfigurer;
import ru.example.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api.endpoints.permitAll}")
    private String[] permitAll;

    @Value("${api.endpoints.roleAdmin}")
    private String[] roleAdmin;

    @Value("${api.endpoints.roleStudent}")
    private String[] roleStudent;

    @Value("${api.endpoints.roleDecanat}")
    private String[] roleDecanat;

    @Value("${api.endpoints.graphics}")
    private String[] graphics;

    @Value("${api.endpoints.tables}")
    private String[] tables;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(permitAll)
                .permitAll()
                .antMatchers(roleAdmin)
                .hasAuthority(Role.ADMIN.name())
                .antMatchers(roleStudent)
                .hasAuthority(Role.STUDENT.name())
                .antMatchers(roleDecanat)
                .hasAuthority(Role.DECANAT.name())
                .antMatchers(graphics)
                .hasAnyAuthority(Role.DECANAT.name(), Role.RECTORAT.name(), Role.TEACHER.name(), Role.CURATOR.name())
                .antMatchers(tables)
                .hasAnyAuthority(Role.DECANAT.name(), Role.RECTORAT.name(), Role.TEACHER.name(), Role.CURATOR.name())
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
