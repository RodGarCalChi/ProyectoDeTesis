package org.example.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de Desarrollo");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.almacenfarmaceutico.com");
        prodServer.setDescription("Servidor de Producción");

        Contact contact = new Contact();
        contact.setName("Equipo de Desarrollo");
        contact.setEmail("soporte@almacenfarmaceutico.com");

        License license = new License();
        license.setName("Apache 2.0");
        license.setUrl("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("API Sistema de Almacén Farmacéutico")
                .version("1.0.0")
                .description("API REST para gestión de almacén farmacéutico con jerarquía completa: " +
                        "Operador Logístico → Almacén → Zona → Lote → Palet → Caja → Productos")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(Arrays.asList(devServer, prodServer));
    }
}
