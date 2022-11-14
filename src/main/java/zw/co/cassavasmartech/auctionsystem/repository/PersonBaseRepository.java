package zw.co.cassavasmartech.auctionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.model.User;

import java.util.List;

@NoRepositoryBean
public interface PersonBaseRepository<T extends Person> extends JpaRepository<T, Long> {
}
