package be.odoo.tieringmachine.repository;

import be.odoo.tieringmachine.domain.Tier;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository extends JpaRepository<Tier, Long> {

    Optional<Tier> findByName(String name);
}
