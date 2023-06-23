package hu.indicium.speurtocht;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "speurtocht-88"
		)
)
public class SpeurtochtApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpeurtochtApplication.class, args);
	}
}
