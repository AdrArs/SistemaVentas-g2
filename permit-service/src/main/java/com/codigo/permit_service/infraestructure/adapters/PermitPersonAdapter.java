package com.codigo.permit_service.infraestructure.adapters;

import com.codigo.permit_service.domain.aggregates.dto.PermitDto;
import com.codigo.permit_service.domain.aggregates.dto.PermitPersonDto;
import com.codigo.permit_service.domain.aggregates.dto.PersonDto;
import com.codigo.permit_service.domain.aggregates.request.PermitPersonRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.domain.ports.out.PermitPersonOut;
import com.codigo.permit_service.infraestructure.client.PersonClient;
import com.codigo.permit_service.infraestructure.dao.PermitPersonRepository;
import com.codigo.permit_service.infraestructure.dao.PermitRepository;
import com.codigo.permit_service.infraestructure.entity.Permit;
import com.codigo.permit_service.infraestructure.entity.PermitPerson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class PermitPersonAdapter implements PermitPersonOut {
    private final PermitPersonRepository permitPersonRepository;
    private final PermitRepository permitRepository;
    private final ModelMapper modelMapper;
    private final PersonClient personClient;
    @Override
    public ResponseEntity<BaseResponse> createOut(PermitPersonRequest permitPersonRequest) {
        try {
            PersonDto personDto = personClient.getPerson(permitPersonRequest.getPersonId());
            if (isNull(personDto)){
                return ResponseEntity
                        .ok(new BaseResponse(414,"Invalid Person", Optional.empty()));
            }

            Optional<Permit> permit = permitRepository.findById(permitPersonRequest.getPermit());
            if(permit.isEmpty()){
                return ResponseEntity
                        .ok(new BaseResponse(419,"Invalid Permit", Optional.empty()));
            }
            PermitPerson permitPerson = getPermitPerson(permitPersonRequest,"CREATE", Optional.empty());
            if (isNull(permitPerson)){
                return ResponseEntity
                        .ok(new BaseResponse(414,"Invalid Request", Optional.empty()));
            }
            PermitPerson permitPersonNew = permitPersonRepository.save(permitPerson);
//            PermitDto permitDto = modelMapper.map(permit.get(),PermitDto.class);
            PermitPersonDto permitPersonDto = mapToPermitPersonDtoNew(permitPersonNew,personDto,permit.get());
//            PermitPersonDto permitPersonDto = PermitPersonDto.builder()
//                    .id(permitPersonNew.getId())
//                    .person(personDto)
//                    .permit(permitDto)
//                    .build();
            return ResponseEntity
                    .ok(new BaseResponse(200,"Success", Optional.of(permitPersonDto)));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity
                    .ok(new BaseResponse(414,"Invalid Request", Optional.empty()));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdOut(Long id) {
        try {
            Optional<PermitPerson> permitPerson = permitPersonRepository.findById(id);
            if (permitPerson.isEmpty()){
                return ResponseEntity
                        .ok(new BaseResponse(414,"Id Invalid", Optional.empty()));
            }
            PersonDto personDto = personClient.getPerson(permitPerson.get().getPersonaId());
            if (isNull(personDto)){
                return ResponseEntity
                        .ok(new BaseResponse(414,"Invalid Person", Optional.empty()));
            }

            Optional<Permit> permit = permitRepository.findById(permitPerson.get().getPermisoId());
            if(permit.isEmpty()){
                return ResponseEntity
                        .ok(new BaseResponse(419,"Invalid Permit", Optional.empty()));
            }
//            PermitDto permitDto = modelMapper.map(permit.get(),PermitDto.class);
            PermitPersonDto permitPersonDto = mapToPermitPersonDtoNew(permitPerson.get(),personDto,permit.get());
//            PermitPersonDto permitPersonDto = modelMapper.map(permitPerson.get(),PermitPersonDto.class);
            return ResponseEntity
                    .ok(new BaseResponse(200,"Success", Optional.of(permitPersonDto)));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity
                    .ok(new BaseResponse(414,"Invalid Request", Optional.empty()));
        }

    }

    @Override
    public ResponseEntity<BaseResponse> findPersonByIdOut(Long id) {
        List<PermitPerson> list = permitPersonRepository.findByPersonaId(id);
        if (list.isEmpty()){
            return ResponseEntity
                    .ok(new BaseResponse(414,"No content", Optional.empty()));
        }
        List<Permit> permits = list.stream()
                .map(l -> permitRepository.findById(l.getPermisoId()).orElse(null))
                .toList();
        List<PermitDto> permitDtos = permits.stream()
                .map(l -> modelMapper.map(l, PermitDto.class))
                .toList();
        return ResponseEntity
                    .ok(new BaseResponse(200,"Success", Optional.of(permitDtos)));
    }

    @Override
    public ResponseEntity<BaseResponse> getAllOut() {
        try {
            List<PermitPerson> list = permitPersonRepository.findAll();
            if(list.isEmpty()) {
                return ResponseEntity
                        .ok(new BaseResponse(200,"No content", Optional.empty()));
            }
            List<PermitPersonDto> permitPersonDtos = list.stream()
                    .map(this::mapToPermitPersonDto).toList();
            return ResponseEntity
                    .ok(new BaseResponse(200,"Success", Optional.of(permitPersonDtos)));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity
                    .ok(new BaseResponse(414,"Invalid Request", Optional.empty()));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> updateOut(Long id, PermitPersonRequest permitPersonRequest) {
        try {
            Optional<PermitPerson> permitPerson = permitPersonRepository.findById(id);
            if (permitPerson.isEmpty()) {
                return ResponseEntity
                        .ok(new BaseResponse(414,"Id Invalid", Optional.empty()));
            }
            PersonDto personDto = personClient.getPerson(permitPersonRequest.getPersonId());
            if (isNull(personDto)){
                return ResponseEntity
                        .ok(new BaseResponse(414,"Invalid Person", Optional.empty()));
            }
            Optional<Permit> permit = permitRepository.findById(permitPersonRequest.getPermit());
            if(permit.isEmpty()){
                return ResponseEntity
                        .ok(new BaseResponse(419,"Invalid Permit", Optional.empty()));
            }
            PermitPerson permitPersonEntity = getPermitPerson(permitPersonRequest,"UPDATE",permitPerson);
            PermitPerson permitPersonUpdate = permitPersonRepository.save(permitPersonEntity);
//        PermitPersonDto permitPersonDto = modelMapper.map(permitPersonRepository.save(permitPersonUpdate),PermitPersonDto.class);
            PermitPersonDto permitPersonDto = mapToPermitPersonDtoNew(permitPersonUpdate,personDto,permit.get());
            return ResponseEntity
                    .ok(new BaseResponse(200,"Success", Optional.of(permitPersonDto)));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity
                    .ok(new BaseResponse(414,"Invalid Request", Optional.empty()));
        }
    }

    private PermitPerson getPermitPerson(PermitPersonRequest permitPersonRequest, String action, Optional<PermitPerson> op) {
        return switch (action) {
            case "CREATE" -> PermitPerson.builder()
                    .personaId(permitPersonRequest.getPersonId())
                    .permisoId(permitPersonRequest.getPermit())
                    .build();
            case "UPDATE" -> PermitPerson.builder()
                    .id(op.get().getId())
                    .personaId(permitPersonRequest.getPersonId())
                    .permisoId(permitPersonRequest.getPermit())
                    .build();
            default -> new PermitPerson();
        };
    }

    private PermitPersonDto mapToPermitPersonDto(PermitPerson permitPerson) {
        PersonDto personDto = personClient.getPerson(permitPerson.getPersonaId());
        Optional<Permit> permit = permitRepository.findById(permitPerson.getPermisoId());
        PermitDto permitDto = modelMapper.map(permit.orElse(null), PermitDto.class);

        return PermitPersonDto.builder()
                .id(permitPerson.getId())
                .person(personDto)
                .permit(permitDto)
                .build();
    }

    private PermitPersonDto mapToPermitPersonDtoNew(PermitPerson permitPerson, PersonDto personDto, Permit permit) {
        PermitDto permitDto = modelMapper.map(permit, PermitDto.class);
        return PermitPersonDto.builder()
                .id(permitPerson.getId())
                .person(personDto)
                .permit(permitDto)
                .build();
    }

}
