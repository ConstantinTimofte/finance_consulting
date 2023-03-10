package com.investing.tracker.repo;

import com.investing.tracker.model.entity.InvestingTrk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/** https://www.objectdb.com/java/jpa/query/jpql/delete*/
public interface TrackerRepository extends JpaRepository<InvestingTrk, Integer> {


    @Query(" SELECT invest FROM InvestingTrk invest " +
            "WHERE (:firstName IS NULL OR invest.firstName = :firstName) " +
            "AND (:lastName IS NULL OR invest.lastName = :lastName) " +
            "AND (:investingName IS NULL OR invest.investingName = :investingName) ORDER BY invest.dayPay asc ")
    List<InvestingTrk> search(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("investingName") String investingName);


/*   @Query(" SELECT InvestingTrk FROM InvestingTrk invest " +
            "WHERE (firstName IS NULL OR invest.firstName = :firstName) " +
            "AND (lastName IS NULL OR invest.lastName = :lastName) " +
            "AND (investingName IS NULL OR invest.investingName = :investingName) " +
            "AND (inDate IS NULL OR invest.dayPay >= :inDate ) ")
    List<InvestingTrk> searchWhitInitialDate(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("investingName") String investingName, @Param("initialDate") LocalDate inDate);*/


}
