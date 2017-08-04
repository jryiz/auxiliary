package com.auxiliary;

import com.auxiliary.utils.MailPropertiesUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class AuxiliaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuxiliaryApplication.class, args);
	}
}
