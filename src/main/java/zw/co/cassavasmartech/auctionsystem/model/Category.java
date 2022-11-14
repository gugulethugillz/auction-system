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

/**
 * Created by alfred on 18 September 2020
 * Create a createPage, viewPage, editPage, Controller, CRUD Frontend, create Category service interface and implementation
 */
@Data
@NoArgsConstructor
@Entity
//Tinashe, Calvin

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
