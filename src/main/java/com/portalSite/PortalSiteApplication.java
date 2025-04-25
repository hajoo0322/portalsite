package com.portalSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PortalSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalSiteApplication.class, args);
	}

}
