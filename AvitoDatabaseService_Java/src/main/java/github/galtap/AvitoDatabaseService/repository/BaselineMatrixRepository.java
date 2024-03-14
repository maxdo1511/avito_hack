package github.galtap.AvitoDatabaseService.repository;

import github.galtap.AvitoDatabaseService.entity.BaselineMatrix;
import github.galtap.AvitoDatabaseService.entity.MatrixId;
import org.springframework.data.repository.CrudRepository;

public interface BaselineMatrixRepository extends CrudRepository<BaselineMatrix, MatrixId> {
}
