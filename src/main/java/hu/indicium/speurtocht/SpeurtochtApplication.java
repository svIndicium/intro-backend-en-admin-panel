package hu.indicium.speurtocht;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Speurtocht 88",
				description = """
						Speurtocht 88 is developed by study association Indicium to introduce new students to the city and to get to know their fellow students.\n
						The groups of students will play 2 games parallel. The first game is where they need to find a location given a picture of said location. The second game is a Crazy 88 game. It's a game where students try to complete as much or all 88 challenges given by the organizers.\n
						For organizers Speurtocht 88 provides a simple solution to review and approve submissions.
						"""
		)
)
@SecurityScheme(name = "speurtocht-88", type = SecuritySchemeType.HTTP, scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class SpeurtochtApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpeurtochtApplication.class, args);
	}
}
