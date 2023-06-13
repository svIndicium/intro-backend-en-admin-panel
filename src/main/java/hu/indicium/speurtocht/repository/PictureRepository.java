package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
	@Query("select p from Picture p where p.id in ?1")
	List<Picture> findByIdIn(Collection<Long> ids);

}
