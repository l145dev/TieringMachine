package be.odoo.tieringmachine.repository;

import be.odoo.tieringmachine.domain.Log;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByUserIdOrderByLogTimeDesc(Long userId);
}
