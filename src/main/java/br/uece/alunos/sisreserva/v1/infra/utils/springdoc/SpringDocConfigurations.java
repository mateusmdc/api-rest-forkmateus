package br.uece.alunos.sisreserva.v1.infra.utils.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("SisReserva API")
                        .description("API Rest da aplicação SisReserva")
                        .contact(new Contact()
                                .name("Andre Rocha")
                                .email("andre.lucio@aluno.uece.br"))
                        .license(new License()
                                .name("Common License")
                                .url("http://url.com/api/licenca")));
    }

}
