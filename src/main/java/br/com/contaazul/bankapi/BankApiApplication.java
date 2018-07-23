package br.com.contaazul.bankapi;

import javax.validation.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Classe principal da aplicação
 * @author Ednardo Rubens
 */
@SpringBootApplication
public class BankApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApiApplication.class, args);
    }

    @Bean
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }
    
}
