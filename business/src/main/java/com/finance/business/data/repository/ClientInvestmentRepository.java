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


    @Query("select c from ClientInvestment AS c  " +
            " JOIN FETCH c.idClient  " +
            " WHERE  c.idClient.id = :idClient ")
    List<ClientInvestment> findClientInvestmentsList(@Param("idClient") Integer idClient);


    @Query("select c from ClientInvestment  as c " +
            " LEFT JOIN FETCH c.idClient " +
            " WHERE  c.idClient.id = :idClient and c.statusOfPayment = :statusOfPayment ")
    List<ClientInvestment> findClientInvestmentsListWithPaymentStatus(@Param("idClient") Integer idClient,
                                                                      @Param("statusOfPayment") Boolean statusOfPayment);


    @Query(value = "select c  from ClientInvestment  as c " +
            " JOIN FETCH c.idClient WHERE  c.idClient.id = :idClient  " +
            " and  c.statusOfPayment = false ")
    List<ClientInvestment> countNonPaidInvestment(Integer idClient);

    @Query(value = "select c  from ClientInvestment  as c " +
            " JOIN FETCH c.idClient WHERE  c.idClient.id = :idClient  ")
    List<ClientInvestment> getClientInvestments(Integer idClient);


    /**
     * COALESCE recupera il primo elemento 'non null' , in questo caso se  'firstName' e null fa la join con tutti i nomi
     */
/*    @Query("select c from ClientInvestment AS c " +
            " LEFT JOIN Investment invests ON c.idInvestment = invests.id " +
            " LEFT JOIN Client clients ON c.idClient = clients.id " +
            " WHERE clients.firstName = COALESCE(:firstName, clients.firstName) "+
            " AND clients.lastName = COALESCE(:lastName, clients.lastName)"
    )*/
    @Query("select c from ClientInvestment AS c " +
            "LEFT JOIN Investment invests ON c.idInvestment = invests.id " +
            "LEFT JOIN Client clients ON c.idClient = clients.id " +
            /** Se e null NON cerca nella join (se esegue ricerca nella join sul db cercherebbe il valore a null, non ci serve)
             * LA CONDIZIONE NON VIENE ESEGUITA SE IL VALORE E NULL */
            "WHERE (:firstName is null OR clients.firstName = :firstName) " +
            "AND (:lastName is null OR clients.lastName = :lastName) " +
            "AND (:investmentName is null OR invests.name = :investmentName) " +
            "AND (:statusOfPayment is null OR c.statusOfPayment = :statusOfPayment) ")
    List<ClientInvestment> findClientInvestmentByIdClientAndIdInvestmentAndStatusOfPayment(@Param("firstName") String firstName, @Param("lastName") String lastName,
                                                                                           @Param("investmentName") String investmentName,@Param("statusOfPayment")Boolean statusOfPayment);
}
