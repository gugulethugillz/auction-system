package zw.co.cassavasmartech.auctionsystem.service.impl;

import com.sun.istack.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.repository.AssetRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.AssetService;
import zw.co.cassavasmartech.auctionsystem.storage.StorageService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final StorageService storageService;

    @Override
    public void deleteAssetById(Long id) {
        assetRepository.deleteById(id);
        return;
    }

    @Override
    public Optional<Asset> updateAsset(Asset asset, @Nullable String fileName) {
        if (fileName != "") {
            boolean removed = asset.getImageNames().removeIf(name -> name.equals(fileName));
            if (removed) {
                storageService.deleteImageFile(fileName);
            }
        }

        return Optional.ofNullable(assetRepository.save(asset));
    }

    @Override
    public List<Asset> searchByKeyWord(String keyWord) {
        return assetRepository.SearchByKeyWord(keyWord);
    }

    @Override
    public AssetRepository getRepository() {
        return assetRepository;
    }

    @Override
    public Optional<Asset> findById(Long id) {
        return assetRepository.findById(id);
    }

    @Override
    public List<Asset> findByName(String name) {
        return assetRepository.findByName(name);
    }

    @Override
    public Optional<Asset> createAsset(Asset asset) {
        return Optional.of(assetRepository.save(asset));
    }

    @Override
    public List<Asset> findAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public List<Asset> findByCategoryId(Long id) {
        return assetRepository.findByCategoryId(id);
    }
}