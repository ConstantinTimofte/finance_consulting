package com.finance.business.data.repository;

import com.finance.business.data.entity.Investment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {
    @EntityGraph(attributePaths = "clientInvestments")
    Investment findInvestmentByName(@Param("name") String name);


    @Query("select inv FROM Investment inv " +
            " JOIN FETCH inv.clientInvestments where inv.name = :name")
    Investment findInvestmentByNameJOINTEST(@Param("name") String name);

    @Query("select inv from Investment  inv where inv.id not in (:list) ")
    List<Investment> all(@Param("list") List<Integer> cc);
}
