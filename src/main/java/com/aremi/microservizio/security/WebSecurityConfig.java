package com.aremi.microservizio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/***
 * E' una classe di configurazione che definisce le impostazioni di sicurezza per l’applicazione. Estende SecurityConfigurerAdapter
 * e viene utilizzata per configurare vari aspetti della sicurezza, come le regole di accesso alle risorse,
 * la configurazione del filtro JWT, la configurazione dell’entry point di autenticazione, ecc.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate").permitAll().
                anyRequest().authenticated().and().
                exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }*/

    /***
     * Questo metodo viene utilizzato per configurare le impostazioni di sicurezza. Configura Spring Security per disabilitare CSRF,
     * permettere l’accesso non autenticato all’endpoint /authenticate, richiedere l’autenticazione per tutti gli altri endpoint,
     * gestire le eccezioni di autenticazione con jwtAuthenticationEntryPoint, impostare la politica di gestione
     * della sessione su STATELESS (poiché JWT è stateless), e aggiungere jwtRequestFilter per gestire la validazione
     * e l’elaborazione dei token JWT
     */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/authorize")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sessionManagment ->
                        sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
