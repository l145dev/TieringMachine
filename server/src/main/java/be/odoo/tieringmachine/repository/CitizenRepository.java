package be.odoo.tieringmachine.repository;

import be.odoo.tieringmachine.domain.Citizen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    List<Citizen> findAllByOrderByTotalPointsDesc();

    Optional<Citizen> findByUsername(String username);
}
