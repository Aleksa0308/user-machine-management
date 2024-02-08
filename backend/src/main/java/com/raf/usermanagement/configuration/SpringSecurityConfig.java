package com.raf.usermanagement.configuration;

import com.raf.usermanagement.filters.JwtFilter;
import com.raf.usermanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SpringSecurityConfig(UserService userService, JwtFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/users/all", "/api/users/get/**").hasAuthority("can_read_users")
                .antMatchers("/api/users/update/**").hasAuthority("can_update_users")
                .antMatchers("/api/users/delete/**").hasAuthority("can_delete_users")
                .antMatchers("/api/users/register").hasAuthority("can_create_users")
                .antMatchers("/api/vacuums/get/**").hasAuthority("can_search_vacuums")
                .antMatchers("/api/vacuums/errors/**").hasAuthority("can_search_vacuums")
                .antMatchers("/api/vacuums/start/**").hasAuthority("can_start_vacuums")
                .antMatchers("/api/vacuums/stop/**").hasAuthority("can_stop_vacuums")
                .antMatchers("/api/vacuums/discharge/**").hasAuthority("can_discharge_vacuums")
                .antMatchers("/api/vacuums/create/**").hasAuthority("can_create_vacuums")
                .antMatchers("/api/vacuums/destroy/**").hasAuthority("can_destroy_vacuums")
                .antMatchers("/api/vacuums/schedule/**").hasAuthority("can_schedule_vacuums")
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200"); // Add your frontend's origin
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
