package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import db.model.Products;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@With
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Products {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("categoryTitle")
    private String categoryTitle;


}
