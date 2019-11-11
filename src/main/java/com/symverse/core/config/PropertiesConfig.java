package com.symverse.core.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


@Component
public class PropertiesConfig implements EnvironmentPostProcessor{

	static final Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);
	
	private static final String SERVICE_MODE = Optional.ofNullable(System.getProperty("SERVICE_MODE")).orElse("mainnet").toLowerCase();
	
	
	
	private final PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

	@Override	
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		logger.debug("SERVICE_MODE: " + SERVICE_MODE);
		
		Resource path = new ClassPathResource("properties/application-" +  SERVICE_MODE + ".properties");

		List<PropertySource<?>> propertySource = loadProps(path);
		environment.getPropertySources().addFirst(propertySource.get(0));
	}

	private List<PropertySource<?>> loadProps(Resource path) {
		if (!path.exists()) {
			throw new IllegalArgumentException("Resource " + path + " does not exist");
		}
		try {
			return this.loader.load("properties", path);
		} catch (IOException ex) {
			throw new IllegalStateException("Failed to load props configuration from " + path, ex);
		}
	}
}
