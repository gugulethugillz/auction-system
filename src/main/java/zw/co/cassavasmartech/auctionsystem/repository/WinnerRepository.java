package zw.co.cassavasmartech.auctionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Winner;

import java.util.List;
import java.util.Optional;

@Repository
public interface WinnerRepository extends JpaRepository<Winner, Long> {
    //SELECT * FROM winner w LEFT OUTER JOIN user u ON u.id = w.user_id WHERE u.username = ?
    @Query("SELECT w FROM Winner w WHERE w.user.username = :username")
    List<Winner> getByUsername(@Param("username") String username);


}
