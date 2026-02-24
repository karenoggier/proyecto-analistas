package com.seminario.ms_pago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsPagoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPagoApplication.class, args);
	}

}
