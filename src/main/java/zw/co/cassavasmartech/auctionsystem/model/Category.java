package zw.co.cassavasmartech.auctionsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

public class Category extends BaseEntity {
    private String name;

    @Override
    public String toString() {
        return "Category{" +
                "CategoryName='" + name + '\'' +
                '}';
    }

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Asset> assets;

}
