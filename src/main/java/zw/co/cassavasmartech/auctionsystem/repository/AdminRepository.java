package zw.co.cassavasmartech.auctionsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.Person;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends PersonBaseRepository<Admin> {
    Optional<Admin> findByUsername(String username);
    @Query("SELECT p FROM HumanBeing p WHERE LOWER(p.firstName) LIKE LOWER(:searchPhrase)")
    List<? extends Person> search(@Param("searchPhrase") String searchPhrase);
}
