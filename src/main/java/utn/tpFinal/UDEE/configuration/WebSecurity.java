package utn.tpFinal.UDEE.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import utn.tpFinal.UDEE.filter.JWTAuthorizationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/measurements").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/backoffice/**").hasAuthority("BACKOFFICE")
                .antMatchers("/client/**").hasAnyAuthority("BACKOFFICE","CLIENT")
                .anyRequest().authenticated(); //authenticated();permitAll()

                 /*.antMatchers(HttpMethod.POST, "/api/users").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/login").permitAll()
                .antMatchers(HttpMethod.GET,"/api/users").permitAll()
                .antMatchers(HttpMethod.GET,"/api/users/details").permitAll()
                .antMatchers(HttpMethod.GET,"/api/measurements").permitAll()*/
    }
}