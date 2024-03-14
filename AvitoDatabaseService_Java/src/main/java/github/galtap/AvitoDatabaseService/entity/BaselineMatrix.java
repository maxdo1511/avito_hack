package github.galtap.AvitoDatabaseService.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.NonNull;

import java.io.Serializable;

@Entity
@Table(name = "baseline_matrix_1")
public class BaselineMatrix  implements Serializable {

    private final long serializableId = 50454005040L;

    public BaselineMatrix(@NonNull MatrixId matrixId, Integer price) {
        this.matrixId = matrixId;
        this.price = price;
    }

    public BaselineMatrix() {
    }

    @Column(unique = true)
    private MatrixId matrixId;

    @Column(name = "price",nullable = false)
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
