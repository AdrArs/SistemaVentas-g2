package com.codigo.permit_service.infraestructure.adapters;

import com.codigo.permit_service.domain.aggregates.dto.PermitAllDto;
import com.codigo.permit_service.domain.aggregates.request.PermitRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.domain.ports.out.PermitServiceOut;
import com.codigo.permit_service.infraestructure.dao.PermitRepository;
import com.codigo.permit_service.infraestructure.entity.Permit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class PermitAdapter implements PermitServiceOut {

    private final PermitRepository permitRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<BaseResponse> createOut(PermitRequest permitRequest) {
        Permit permit = getPermit(permitRequest,"CREATE",Optional.empty());
        if (isNull(permit)){
            return ResponseEntity
                    .ok(new BaseResponse<>(414,"Not Found Permit", Optional.empty()));
        }
        PermitAllDto permitDto = modelMapper.map(permitRepository.save(permit), PermitAllDto.class);
        return ResponseEntity
                .ok(new BaseResponse<>(200,"Success", Optional.of(permitDto)));
    }

    private Permit getPermit(PermitRequest permitRequest, String action , Optional<Permit> op) {
        Permit permit = Permit
                .builder()
                .permiso(permitRequest.getPermit())
                .build();
        switch (action){
            case "CREATE":
                permit.setUsuaCreate("ADMIN");
                permit.setDateCreate(getTime());
                break;
            case "UPDATE":
                if (op.isPresent()){
                    permit.setIdpermiso(op.get().getIdpermiso());
                    permit.setUsuaCreate(op.get().getUsuaCreate());
                    permit.setDateCreate(op.get().getDateCreate());
                    permit.setUsuaModif("MODIFY");
                    permit.setDateModif(getTime());
                    permit.setUsuaDelet(op.get().getUsuaDelet());
                    permit.setDateDelet(op.get().getDateDelet());
                }else {
                    return null;
                }
                break;
            default:return null;
        }
        return permit;
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdOut(Long id) {
        Optional<Permit> op = permitRepository.findById(id);
        if (op.isEmpty()){
            return ResponseEntity
                    .ok(new BaseResponse<>(414,"Not found Permit", Optional.empty()));
        }
        Permit permit = op.get();
        PermitAllDto permitDto = modelMapper.map(permit, PermitAllDto.class);
        return ResponseEntity
                .ok(new BaseResponse<>(200,"Success", Optional.of(permitDto)));
    }

    @Override
    public ResponseEntity<BaseResponse> getAllOut() {
        List<Permit> list = permitRepository.findAll();
        if (list.isEmpty()){
            return ResponseEntity
                    .ok(new BaseResponse<>(414,"Not Content", Optional.empty()));
        }
        List<PermitAllDto> permitDtos = list.stream()
                .map(l -> modelMapper.map(l, PermitAllDto.class))
                .toList();
        return ResponseEntity
                .ok(new BaseResponse<>(200,"Success", Optional.of(permitDtos)));
    }

    @Override
    public ResponseEntity<BaseResponse> updateOut(Long id, PermitRequest permitRequest) {
        Optional<Permit> op = permitRepository.findById(id);
        if(op.isEmpty()){
            return ResponseEntity
                    .ok(new BaseResponse<>(414,"Not found Permit", Optional.empty()));
        }
        Permit permit = getPermit(permitRequest,"UPDATE",op);
        if (isNull(permit)){
            return ResponseEntity
                    .ok(new BaseResponse<>(414,"Error in Permit", Optional.empty()));
        }
        PermitAllDto permitDto = modelMapper.map(permitRepository.save(permit), PermitAllDto.class);
        return ResponseEntity
                .ok(new BaseResponse<>(200,"Success", Optional.of(permitDto)));
    }

    private Timestamp getTime(){
        long currentTime =System.currentTimeMillis();
        return new Timestamp(currentTime);
    }

}
