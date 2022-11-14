package zw.co.cassavasmartech.auctionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.projections.AssetReportEntry;
import zw.co.cassavasmartech.auctionsystem.projections.PaymentReportEntry;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset,Long> {
    List<Asset> findByName(String name);
    List<Asset> findByCategoryId(Long id);
    @Query(value = "SELECT * from asset  assets where assets.name like %:keyWord% or assets.description" +
            " like %:keyWord%", nativeQuery = true)
    List<Asset> SearchByKeyWord(@Param("keyWord") String keyWord);

    @Query(nativeQuery = true, value = "SELECT " +
            "ains.image_names AS picture, " +
            "a.name AS name, " +
            "c.name AS categoryName, " +
            "a.bid_start_price AS startBidValue, " +
            "b.value AS winningBid, " +
            "a.status AS status, " +
            "(SELECT COUNT(bd.id) FROM bid bd WHERE bd.asset_id = a.id) AS numberOfBids, " +
            "a.bid_start_date AS assetBidStartDate, " +
            " " +
            "a.bid_end_date AS assetBidEndDate " +
            "FROM asset a " +
            "    LEFT OUTER JOIN category c ON a.category_id = c.id " +
            "    LEFT OUTER JOIN bid b ON b.asset_id = a.id " +
            "    LEFT OUTER JOIN asset_image_names ains ON ains.asset_id = a.id " +
            "     WHERE b.value = (SELECT MAX(b1.value) FROM bid b1 WHERE b1.asset_id = a.id) " +
            "     AND ains.image_names = (SELECT MIN(ain.image_names) FROM asset_image_names ain WHERE ain.asset_id = a.id) " +
            "       AND a.date_created BETWEEN :start AND :end ")
    List<AssetReportEntry> getAssetReportEntries(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);
}
