package github.galtap.AvitoDatabaseService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

import java.io.Serializable;

@Entity
@Table(name = "discount_matrix_1")
public class DiscountMatrix implements Serializable {

    private final long serializableId = 540504050040450440L;

    public DiscountMatrix(MatrixId matrixId, Integer price) {
        this.matrixId = matrixId;
        this.price = price;
    }

    public DiscountMatrix() {
    }

    @Column(unique = true)
    private MatrixId matrixId;

    @Column(nullable = false)
    @Min(value = 0, message = "price must be greater than or equal to 0")
    private Integer price;

    @EmbeddedId
    public MatrixId getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(MatrixId matrixId) {
        this.matrixId = matrixId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
