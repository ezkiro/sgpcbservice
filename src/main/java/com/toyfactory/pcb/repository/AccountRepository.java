package com.toyfactory.pcb.repository;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.toyfactory.pcb.domain.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}
