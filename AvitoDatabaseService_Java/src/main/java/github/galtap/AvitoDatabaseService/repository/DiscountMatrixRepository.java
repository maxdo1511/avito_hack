package github.galtap.AvitoDatabaseService.repository;

import github.galtap.AvitoDatabaseService.entity.DiscountMatrix;
import github.galtap.AvitoDatabaseService.entity.MatrixId;
import org.springframework.data.repository.CrudRepository;

public interface DiscountMatrixRepository extends CrudRepository<DiscountMatrix, MatrixId> {
}
