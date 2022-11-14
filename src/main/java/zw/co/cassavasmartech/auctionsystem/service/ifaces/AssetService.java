package zw.co.cassavasmartech.auctionsystem.service.ifaces;

import com.sun.istack.Nullable;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.repository.AssetRepository;
import zw.co.cassavasmartech.auctionsystem.repository.BidRepository;

import java.util.List;
import java.util.Optional;

public interface AssetService {
    AssetRepository getRepository();
    Optional<Asset> findById(Long id);
    List<Asset> findByName(String name);
    Optional<Asset> createAsset(Asset asset);
    List<Asset> findAllAssets();
    void deleteAssetById(Long id);
    Optional<Asset> updateAsset(Asset asset,@Nullable String fileName);
    List<Asset> searchByKeyWord(String keyWord);
//    List<Asset> findAllAssetsByName(String name);
    List<Asset> findByCategoryId(Long id);
}
