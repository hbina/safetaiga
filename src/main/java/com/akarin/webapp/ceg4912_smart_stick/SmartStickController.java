package com.akarin.webapp.ceg4912_smart_stick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

class SmartStickController {

    private static final Logger logger = LoggerFactory.getLogger(SmartStickController.class);

    @RequestMapping(path = "/ceg4912/user/{userid}/location")
    public void receiveUserLocation(@RequestParam(value = "longitude") int userLongitude, @RequestParam(value = "latitude") int userLatitude) {
        logger.info("longitude:" + userLongitude + " latitude:" + userLatitude);
    }
}
