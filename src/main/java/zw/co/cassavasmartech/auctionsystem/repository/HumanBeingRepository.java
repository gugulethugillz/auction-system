package zw.co.cassavasmartech.auctionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.auctionsystem.integrations.camel.HumanBeing;
import zw.co.cassavasmartech.auctionsystem.model.Winner;

import java.util.List;

@Repository
public interface HumanBeingRepository extends JpaRepository<HumanBeing, Long> {

}
