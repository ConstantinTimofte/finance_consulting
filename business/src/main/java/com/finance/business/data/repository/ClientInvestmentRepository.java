package com.finance.business.data.repository;

import com.finance.business.data.entity.ClientInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @see https://www.baeldung.com/jpa-join-types#:~:text=First%20of%20all%2C%20JPA%20only%20creates%20an%20implicit,be%20easier%20to%20know%20what%20is%20going%20on.
 * https://www.baeldung.com/jpa-join-types
 */


@Repository
public interface ClientInvestmentRepository extends JpaRepository<ClientInvestment, Integer> {

    @Query("select  c from ClientInvestment AS c  " +
            " JOIN FETCH c.idClient  " +
            " JOIN FETCH c.idInvestment " +
            " WHERE  c.idClient.id = :idClient and  c.idInvestment.id = :idInvestment")
   ClientInvestment findClientInvestmentsList(@Param("idClient") Integer idClient, @Param("idInvestment") Integer idInvestment);


    @Query("select  c from ClientInvestment AS c  " +
            " JOIN FETCH c.idClient  " +
            " WHERE  c.idClient.id = :idClient ")
    List<ClientInvestment> findClientInvestmentsList(@Param("idClient") Integer idClient);
}
