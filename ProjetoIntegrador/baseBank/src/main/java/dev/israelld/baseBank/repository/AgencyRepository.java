package dev.israelld.baseBank.repository;

import dev.israelld.baseBank.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Agency findByAgencyName(String agencyName);

}
