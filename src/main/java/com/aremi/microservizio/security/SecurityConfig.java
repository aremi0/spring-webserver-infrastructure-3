package com.aremi.microservizio.security;

import com.aremi.microservizio.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
                                UtenteService utenteService) throws Exception {
        //auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(utenteService);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    /***
     * Questo bean AuthenticationManager serve necessariamente al Filtro di Autenticazione (che gestisce le richieste di
     * login.
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(Arrays.asList(jwtAuthenticationProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Autowired JwtAuthorizationFilter jwtAuthorizationFilter,
                                                   @Autowired JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagment ->
                        sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        /***
         * E' necessario che questo filtro non sia un bean perchè nonostante tutti i miei sforzi non sono riusito a garentire
         * che il bean da cui è dipendente (AuthenticationManager) venga creato prima del filtro. Perciò adesso questo Filtro
         * non è più @Component.
         */
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean());

        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);

        /***
         * L’ordine in cui aggiungi i filtri è importante. I filtri in Spring Security vengono applicati nell’ordine
         * in cui sono dichiarati. Quindi, se una richiesta soddisfa i criteri per più filtri, verrà gestita dal primo
         * filtro che incontra.
         *
         * Nel tuo caso, vuoi che il JwtAuthenticationFilter gestisca le richieste all’endpoint /api/login, e il
         * JwtAuthorizationFilter gestisca tutte le altre richieste. Quindi, dovresti aggiungere prima il
         * JwtAuthenticationFilter, e poi il JwtAuthorizationFilter. In questo modo, le richieste all’endpoint
         * /api/login verranno gestite dal JwtAuthenticationFilter e non raggiungeranno il JwtAuthorizationFilter
         */
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
