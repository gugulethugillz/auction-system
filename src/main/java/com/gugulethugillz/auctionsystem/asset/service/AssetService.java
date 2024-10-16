package com.gugulethugillz.auctionsystem.asset.service;

import com.gugulethugillz.auctionsystem.asset.model.Asset;
import com.gugulethugillz.auctionsystem.asset.repository.AssetRepository;
import com.sun.istack.Nullable;

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
