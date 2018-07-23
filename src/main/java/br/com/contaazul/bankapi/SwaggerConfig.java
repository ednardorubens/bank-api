package br.com.contaazul.bankapi;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe de configuração da documentação da API
 * @author Ednardo Rubens
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("br.com.contaazul.bankapi.controller"))
            .paths(PathSelectors.any()).build().useDefaultResponseMessages(false)
            .apiInfo(new ApiInfo("Bank API", "O objetivo desta API é disponibilizar uma interface REST para geração de boletos.", 
                "v1", "Terms of service", new Contact("Ednardo Rubens", "https://github.com/ednardorubens", "ednardorubens@hotmail.com"), 
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList()));
    }
}
