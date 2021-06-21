package utn.tpFinal.UDEE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
/*import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;*/

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"test"} , exclude = JpaRepositoriesAutoConfiguration.class)
@EntityScan({"utn.tpFinal.UDEE.model"})
@ComponentScan({"utn.tpFinal.UDEE.configuration","utn.tpFinal.UDEE.filter","utn.tpFinal.UDEE.controller","utn.tpFinal.UDEE.exceptions","utn.tpFinal.UDEE.repository","utn.tpFinal.UDEE.service","utn.tpFinal.UDEE.util"})
@EnableJpaRepositories("utn.tpFinal.UDEE.repository")
public class UtnElectricEnergyDistributionApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UtnElectricEnergyDistributionApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UtnElectricEnergyDistributionApplication.class);
	}
}
