package com.unab.banca;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Clase que extiende SpringBootServletInitializer para configurar la inicialización del servlet de Spring Boot
public class ServletInitializer extends SpringBootServletInitializer {

	// Método que configura la aplicación Spring para su inicialización
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// Especifica la clase principal de la aplicación Spring (BancaApplication en este caso)
		return application.sources(BancaApplication.class);
	}

}
