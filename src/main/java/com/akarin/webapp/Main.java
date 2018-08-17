package com.akarin.webapp;

import com.akarin.webapp.managers.DatabaseManager;
import com.akarin.webapp.storage.StorageProperties;
import com.akarin.webapp.storage.StorageService;
import com.akarin.webapp.util.SettingUp;
import com.akarin.webapp.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Main {
    private final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        Tools.createFolders();
        DatabaseManager.setDbLogin();
        SettingUp.prepareDatabase();
    }

    @Deprecated
    public static void oldMain(final String[] args) {
        final long tStart = System.currentTimeMillis();

        if (System.getenv("IS_HEROKU") == null) {
            Tools.cout("Debug screen enabled");
            Tools.coutln("No .env file specified, defaulting to port:5000");
            //port(5000);
        } else {
            Tools.coutln("Debug screen disabled");
            //port(Integer.valueOf(System.getenv("PORT")));
        }

        Tools.createFolders();
        //staticFiles.externalLocation("public");
        //staticFiles.expireTime(600L);

        //get(Reference.Web.ROOT, RootController.serveRootPage);
        //get(Reference.Web.TEXTBOARD_ROOT, TextboardController.serveTextboardHome);
        //get(Reference.Web.TEXTBOARD_BOARD, TextboardController.serveTextboardBoard);
        //get(Reference.Web.TEXTBOARD_BOARD_THREAD, TextboardController.serveTextboardThread);
        //get(Reference.Web.IMAGEPROCESSING_ROOT, ImageProcessingController.serveImageUpload);
        //get(Reference.Web.MANIFESTO_ROOT, ManifestoController.serveManifestoPage);
        //post(Reference.Web.TEXTBOARD_ROOT, TextboardController.handleCreateBoard);
        //post(Reference.Web.TEXTBOARD_BOARD, TextboardController.handleCreateThread);
        //post(Reference.Web.TEXTBOARD_BOARD_THREAD, TextboardController.handleCreatePost);
        //post(Reference.Web.IMAGEPROCESSING_ROOT, ImageProcessingController.handleImageUpload);

        //get("*", (req, res) -> {throw new Exception("Exceptions everywhere!");});

        DatabaseManager.setDbLogin();
        SettingUp.prepareDatabase();
        // SettingUp.createImageInfo();

        final long tEnd = System.currentTimeMillis();
        final long tDelta = tEnd - tStart;
        final double elapsedSeconds = tDelta / 1000.0;
        Tools.coutln("Server startup takes " + elapsedSeconds + " seconds.");
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
