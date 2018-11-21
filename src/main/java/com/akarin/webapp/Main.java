package com.akarin.webapp;

import com.akarin.webapp.managers.DatabaseManager;
import com.akarin.webapp.storage.FileManager;
import com.akarin.webapp.storage.StorageProperties;
import com.akarin.webapp.storage.StorageService;
import com.akarin.webapp.util.SettingUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@PropertySources({
        @PropertySource("classpath:application.properties")
})
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
        FileManager.createFolders();
        DatabaseManager.setDbLogin();
        SettingUp.prepareDatabases();
    }

    @Bean
    CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            logger.info("Beans provided by Spring Boot:");

            int counter = 1;
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                logger.info(counter++ + ". " + beanName);
            }

        };
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
