package br.com.techchallenge.foodsys.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "FoodSys API",
                version = "v1",
                description = "API responsável por autenticação, cadastro de usuários, pedidos e gerenciamento de produtos.",
                contact = @Contact(name = "Equipe FoodSys", email = "javafiappos@gmail.com"),
                license = @License(name = "Apache 2.0", url = "http://springdoc.org")
        )
)
public class OpenApiConfig {
}
