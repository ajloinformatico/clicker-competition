package es.lojo.clickercompetition.demo;

import es.lojo.clickercompetition.demo.Security.JWTAuthorizationFilter;
import es.lojo.clickercompetition.demo.model.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //Permit all request
    @EnableWebSecurity
    @Configuration
    static
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            Role player = new Role("player");
            Role coach = new Role("coach");
            Role president = new Role("president");

            //entities in plural are allowed to all to see statistics
            //players and coachs can work with player and team and other entities only for president
            http.cors().and().csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(getApplicationContext()), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                        .antMatchers("/login/**").permitAll()
                        .antMatchers("/rol/**").authenticated()
                        .antMatchers("/players/**").authenticated()
                        .antMatchers("/teams/**").authenticated()
                        .antMatchers("/cities/**").authenticated()
                        .antMatchers("/communities/**").authenticated()
                        .antMatchers("/countries/**").authenticated()
                        .antMatchers("/player/**").hasAnyRole("coach","player")
                        .antMatchers("/team/**").hasRole("coach")
                        .antMatchers("/city/**").hasRole("president")
                        .antMatchers("/community/**").hasRole("president")
                        .antMatchers("/country/**").hasRole("president")
                        .antMatchers("/").permitAll();
        }

        @Bean
        public PasswordEncoder getPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }

}
