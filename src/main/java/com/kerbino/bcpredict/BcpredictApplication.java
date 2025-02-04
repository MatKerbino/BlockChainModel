package com.kerbino.bcpredict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BcpredictApplication { public static void main(String[] args) { SpringApplication.run(BcpredictApplication.class, args); } }
