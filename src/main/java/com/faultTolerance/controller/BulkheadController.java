package com.faultTolerance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@RestController
public class BulkheadController {

	Logger logger = LoggerFactory.getLogger(BulkheadController.class);

    @GetMapping("/getMessageBH")
    @Bulkhead(name = "getMessageBH", fallbackMethod = "getMessageBHFallBack")
    public ResponseEntity<String> getMessageBH(@RequestParam(value="name", defaultValue = "Hello") String name){
    	logger.info("getMessageBH() call starts here");
       return ResponseEntity.ok().body("Message from getMessageBH() :" +name);
    }

    public ResponseEntity<String> getMessageBHFallBack(RequestNotPermitted exception) {

       logger.info("Bulkhead has applied, So no further calls are getting accepted");

       return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
      .body("Too many requests : No further request will be accepted. Plese try after sometime");
    }
}
