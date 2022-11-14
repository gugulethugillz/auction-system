package zw.co.cassavasmartech.auctionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;
import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.StatusHistory;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

}
