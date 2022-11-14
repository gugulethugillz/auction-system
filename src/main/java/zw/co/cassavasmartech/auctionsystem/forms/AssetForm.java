package zw.co.cassavasmartech.auctionsystem.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import zw.co.cassavasmartech.auctionsystem.model.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetForm {

    private Long Id;
    @NotBlank(message = "Asset name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    //2020-09-29T00:54
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime bidStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime bidEndDate;

    @NotNull(message = "Bid Start Price is required")
    private BigDecimal bidStartPrice;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private MultipartFile imageFile;


    public Asset getAsset(Category category) {
        Asset asset = new Asset();
        asset.setName(name);
        asset.setDescription(description);
        asset.setBidStartDate(Optional.ofNullable(bidStartDate).map(bsd -> bsd.atOffset(ZoneOffset.UTC)).orElse(null));
        asset.setBidEndDate(Optional.ofNullable(bidEndDate).map(bed -> bed.atOffset(ZoneOffset.UTC)).orElse(null));
        asset.setBidStartPrice(bidStartPrice);
        asset.setCategory(category);
        return asset;
    }

    public static AssetForm createAssetForm(Asset asset) {
        if (asset == null) return AssetForm.builder().build();
        AssetForm assetForm = AssetForm.builder()
                .name(asset.getName())
                .description(asset.getDescription())
                .bidStartDate(asset.getBidStartDate().toLocalDateTime())
                .bidEndDate(asset.getBidEndDate().toLocalDateTime())
                .bidStartPrice(asset.getBidStartPrice())
                .categoryId(asset.getCategory().getId())
                .build();
        return assetForm;
    }


}
