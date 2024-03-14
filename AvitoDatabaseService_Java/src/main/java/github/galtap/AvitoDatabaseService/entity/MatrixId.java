package github.galtap.AvitoDatabaseService.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class MatrixId implements Serializable {
    private final long serializableId = 40200434020342L;
    @Min(value = 0, message = "microCategoryId must be greater than or equal to 0")
    @Column(name = "microcategory_id",nullable = false)
    private Integer microCategoryId;

    @Min(value = 0, message = "locationId must be greater than or equal to 0")
    @Column(name = "location_id",nullable = false)
    private Integer locationId;


    public MatrixId(int microCategoryId, int locationId) {
        this.microCategoryId = microCategoryId;
        this.locationId = locationId;
    }

    public int getMicroCategoryId() {
        return microCategoryId;
    }

    public void setMicroCategoryId(int microCategoryId) {
        this.microCategoryId = microCategoryId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixId matrixId = (MatrixId) o;
        return Objects.equals(microCategoryId, matrixId.microCategoryId) && Objects.equals(locationId, matrixId.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(microCategoryId, locationId);
    }
}
