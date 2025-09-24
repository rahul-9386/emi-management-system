package com.supernova.emims.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Main application configuration
 * Imports all necessary configurations and enables component scanning
 *
 * Sonar-compliant: Proper configuration structure
 */
@Configuration
@ComponentScan(basePackages = "com.supernova.emims")
@Import(DatabaseConfig.class)
public class ApplicationConfig {

    // Main application configuration
    // All components are scanned from com.supernova.emims package
    // Database configuration is imported from DatabaseConfig
}
