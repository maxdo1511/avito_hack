package github.galtap.AvitoDatabaseService.service;

import github.galtap.AvitoDatabaseService.entity.BaselineMatrix;
import github.galtap.AvitoDatabaseService.entity.DiscountMatrix;
import github.galtap.AvitoDatabaseService.entity.MatrixId;
import github.galtap.AvitoDatabaseService.repository.BaselineMatrixRepository;
import github.galtap.AvitoDatabaseService.repository.DiscountMatrixRepository;
import github.galtap.rgpc.MatrixPriceServiceOuterClass;
import io.grpc.stub.StreamObserver;
import jakarta.validation.Valid;
import lombok.NonNull;

public class MatrixService extends github.galtap.rgpc.MatrixPriceServiceGrpc.MatrixPriceServiceImplBase {
    private final BaselineMatrixRepository baselineMatrixRepository;
    private final DiscountMatrixRepository discountMatrixRepository;

    public MatrixService(@NonNull BaselineMatrixRepository baselineMatrixRepository, @NonNull DiscountMatrixRepository discountMatrixRepository) {

        this.baselineMatrixRepository = baselineMatrixRepository;
        this.discountMatrixRepository = discountMatrixRepository;
    }

    @Override
    public void addPrice(github.galtap.rgpc.MatrixPriceServiceOuterClass.Matrix request, StreamObserver<MatrixPriceServiceOuterClass.RequestState> responseObserver) {
        MatrixPriceServiceOuterClass.MatrixId requestMatrixId = request.getMatrixId();
        @Valid MatrixId matrixEntity = new MatrixId(requestMatrixId.getMicroCategoryId(),requestMatrixId.getLocationCategoryId());
        switch (requestMatrixId.getTableType()){
            case BASELINE_MATRIX -> {
                if(baselineMatrixRepository.findById(matrixEntity).isEmpty()){
                    @Valid BaselineMatrix baselineMatrix = new BaselineMatrix(matrixEntity,request.getPrice());
                    baselineMatrixRepository.save(baselineMatrix);
                    responseObserver.onNext(MatrixPriceServiceOuterClass.RequestState.newBuilder().setMessageState(0).build());
                } else{
                    responseObserver.onNext(MatrixPriceServiceOuterClass.RequestState.newBuilder().setMessageState(1).build());
                    throw new RuntimeException(String.format("Matrix Id with data such as microCategory = %s and locationCategoryId = %s  already exists.", matrixEntity.getMicroCategoryId(),matrixEntity.getLocationId()));
                }
            }
            case DISCOUNT_MATRIX -> {
                if (discountMatrixRepository.findById(matrixEntity).isEmpty()) {
                    @Valid DiscountMatrix discountMatrix = new DiscountMatrix(matrixEntity, request.getPrice());
                    discountMatrixRepository.save(discountMatrix);
                    responseObserver.onNext(MatrixPriceServiceOuterClass.RequestState.newBuilder().setMessageState(0).build());
                } else{
                    responseObserver.onNext(MatrixPriceServiceOuterClass.RequestState.newBuilder().setMessageState(1).build());
                    throw new RuntimeException(String.format("Matrix Id with data such as microCategory = %s and locationCategoryId = %s  already exists.", matrixEntity.getMicroCategoryId(), matrixEntity.getLocationId()));
                }
            }
            default -> {
                responseObserver.onNext(MatrixPriceServiceOuterClass.RequestState.newBuilder().setMessageState(1).build());
                throw new RuntimeException("Invalid request. Invalid matrix table type");
            }
        }
    }

    @Override
    public void updatePrice(MatrixPriceServiceOuterClass.Matrix request, StreamObserver<MatrixPriceServiceOuterClass.RequestState> responseObserver) {
    }

    @Override
    public void deletePrice(MatrixPriceServiceOuterClass.MatrixId request, StreamObserver<MatrixPriceServiceOuterClass.RequestState> responseObserver) {
    }

    @Override
    public void getPrice(MatrixPriceServiceOuterClass.MatrixId request, StreamObserver<MatrixPriceServiceOuterClass.Matrix> responseObserver) {

    }

    @Override
    public void getAllPrices(MatrixPriceServiceOuterClass.Empty request, StreamObserver<MatrixPriceServiceOuterClass.MatrixList> responseObserver) {

    }
}
