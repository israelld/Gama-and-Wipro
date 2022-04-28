package dev.israelld.baseBank.repository;

import dev.israelld.baseBank.model.Account;
import dev.israelld.baseBank.model.Historic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricRepository extends JpaRepository<Historic, Long> {
    List<Historic> findByAccount(Account account);
}
