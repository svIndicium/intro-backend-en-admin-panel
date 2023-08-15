package hu.indicium.speurtocht.security;

import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

	private final TeamRepository teamRepository;

	public Team getTeam() {
		return this.teamRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
	}

}
