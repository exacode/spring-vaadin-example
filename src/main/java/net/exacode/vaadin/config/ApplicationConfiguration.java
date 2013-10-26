package net.exacode.vaadin.config;


import net.exacode.vaadin.ApplicationInitializer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan(basePackageClasses = ApplicationInitializer.class)
public class ApplicationConfiguration {

}
