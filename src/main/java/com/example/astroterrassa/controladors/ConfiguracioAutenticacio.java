package com.example.astroterrassa.controladors;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.services.AuthFailureHandler;
import com.example.astroterrassa.services.AuthSuccessHandler;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Indica al sistema que és una classe de configuració
@EnableWebSecurity //Habilita la seguretat web
public class ConfiguracioAutenticacio {

    @Autowired
    private UserDetailsService userDetailsService; //Objecte per recuperar l'usuari

    @Autowired
    private UserRepository userDao;

    @Autowired
    public void autenticacio(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean //L'indica al sistema que el mètode és un Bean, en aquest cas perquè crea un objecte de la classe HttpSecurity
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests((requests) -> requests
                        //En el nostre cas el mètode hasAnyAuthority fa el mateix que HasAnyRoles, o hasAuthority el mateix que hasRol, però en aquesta nova versió per autoritzar els usuaris, els mètodes
                        //dels rols, normalment donen problemes, els Authority, no.
                        .requestMatchers("/iniciGossos/**", "/enviar/**", "/eliminar", "guardarGos/**")
                        .hasAnyAuthority("veterinari", "pacient","admin").requestMatchers("/registre/**").permitAll().requestMatchers("/llistaUsuaris/**").hasAnyAuthority("admin") //URL iniciGossos on pot accedir el rol de veterinari o pacient
                        .requestMatchers("/login/**").permitAll().anyRequest().authenticated() //Qualsevol altre sol.licitud que no coincideixi amb les regles anteriors cal autenticació
                )

                .formLogin((form) -> form //Objecte que representa el formulari de login personalitzat que utilitzarem
                        .loginPage("/login")//Pàgina on es troba el formulari per fer login personalitzat
                        .failureHandler(authenticationFailureHandler())
                        .successHandler(authenticationSuccessHandler())
                        .permitAll() //Permet acceddir a tothom
                )

                .exceptionHandling((exception) -> exception //Quan es produeix una excepcció 403, accés denegat, mostrem el nostre missatge
                        .accessDeniedPage("/errors/error403"))
                .build();

    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthSuccessHandler();
    }

}