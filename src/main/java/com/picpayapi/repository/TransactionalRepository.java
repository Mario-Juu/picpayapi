package com.picpayapi.repository;

import com.picpayapi.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionalRepository extends JpaRepository<Transaction, Long>{
}
