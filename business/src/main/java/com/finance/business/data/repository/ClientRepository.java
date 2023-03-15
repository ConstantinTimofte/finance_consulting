package com.finance.business.data.repository;

import com.finance.business.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select c from Client c " +
            " where (:searchTerm is null OR c.firstName = :searchTerm )  " +
            " OR (:searchTerm is not null AND ( lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "                                   or lower(c.firstName) like lower(concat('%', :searchTerm, '%')) ) ) ")
    List<Client> search(@Param("searchTerm") String searchTerm);

    Client findClientByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}