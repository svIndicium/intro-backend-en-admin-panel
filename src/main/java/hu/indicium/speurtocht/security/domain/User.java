package hu.indicium.speurtocht.security.domain;

import hu.indicium.speurtocht.domain.Team;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_users")
public class User implements UserDetails {

	@Id
	private String username;

	private String password;

	@OneToOne
	private Team team;

	private UserRole role;

	public static User createParticipant(Team team, String password) {
		return new User(team.getName(), password, team, UserRole.PARTICIPANT);
	}

	public static User createAdmin(String username, String password) {
		return new User(username, password, null, UserRole.ADMIN);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
