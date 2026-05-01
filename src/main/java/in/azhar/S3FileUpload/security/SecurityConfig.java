package in.azhar.S3FileUpload.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter filter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // public -> no authentication required
                        .requestMatchers("/auth/**").permitAll()


                        //-----------------------------FILE APIs-----------------------------
                        // ADMIN access only (upload/update/delete)
                        .requestMatchers("/files/upload").hasRole("ADMIN")
                        .requestMatchers("/files/update/**").hasRole("ADMIN")
                        .requestMatchers("/files/delete/**").hasRole("ADMIN")

                        // USER + ADMIN access
                        .requestMatchers("/files/**").hasAnyRole("ADMIN", "USER")


                        //------------------------------COURSE APIs-----------------------------
                        // USER + ADMIN access
                        .requestMatchers(HttpMethod.GET, "/courses/**").hasAnyRole("ADMIN", "USER")

                        // ADMIN access only (create/update/delete)
                        .requestMatchers("/courses").hasRole("ADMIN")
                        .requestMatchers("/courses/**").hasRole("ADMIN")


                        //-----------------------------ORDER APIs-----------------------------
                        // USER + ADMIN (order create & success)
                        .requestMatchers("/orders/**").hasAnyRole("ADMIN", "USER")


                        //----------------------ADMIN APIs-----------------------------
                        // ADMIN access only (activate/deactivate users)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}