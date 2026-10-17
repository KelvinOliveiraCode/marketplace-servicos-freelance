package com.kelvinoliveira.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API do marketplace de servi\u00e7os freelance.
 * Exp\u00f5e an\u00fancias, propostas e contrata\u00e7\u00f5es via REST.
 */
@SpringBootApplication
public class MarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }

}
