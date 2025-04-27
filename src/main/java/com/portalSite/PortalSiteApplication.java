package com.portalSite;

import com.portalSite.security.JwtSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties({JwtSecurityProperties.class})
public class PortalSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalSiteApplication.class, args);
	}

}
