package ie.lukeandella.wedding.configuration;

import ie.lukeandella.wedding.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
//                .antMatchers("/new").hasAnyAuthority("ADMIN")
//                .antMatchers("/update/**").hasAnyAuthority("ADMIN")
                .antMatchers("/new").hasAuthority("ADMIN")
                .antMatchers("/update/**").hasAuthority("ADMIN")
                .antMatchers("/delete/**").hasAuthority("ADMIN")
                .antMatchers("/users/**").hasAuthority("ADMIN")
                .antMatchers("/reserve/**").hasAuthority("MEMBER")
                .antMatchers("/cancel-reservation/**").hasAuthority("MEMBER")
                .antMatchers("/register").hasAuthority("VISITOR")              //register page
                .antMatchers("/process-registration").hasAuthority("VISITOR")  //process registration page
                .antMatchers("/verify").hasAuthority("VISITOR")                //verify registration
                .antMatchers("/verify/success").hasAuthority("VISITOR")         //verify success
                .antMatchers("/verify/failure").hasAuthority("VISITOR")        //verify failure
                .antMatchers("/").permitAll()                       //landing page
                .anyRequest().fullyAuthenticated()                              //Everything else is only accessible to authenticated users
            .and()
                .formLogin()                             //overrides the default Spring Boot login page
                .loginPage("/login").permitAll()    //makes the custom login page accessible to anyone.
                .defaultSuccessUrl("/home", true)   //where users are directed after successful login
                .failureUrl("/invalid-credential")
            .and()
                //logout is also accessible to anyone
                .logout().permitAll()
            .and()
                .exceptionHandling().accessDeniedPage("/403");
    }

    //This permits access to the css and images folders (both located inside the resources folder)
    @Override
    public void configure(WebSecurity web) throws Exception {
        // Solve the problem of static resources being intercepted
        web.ignoring().antMatchers("/css/**", "/images/**");
    }

}
