package com.codigo.permit_service.infraestructure.adapters;

import com.codigo.permit_service.domain.aggregates.dto.PermitAllDto;
import com.codigo.permit_service.domain.aggregates.request.PermitRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.infraestructure.dao.PermitRepository;
import com.codigo.permit_service.infraestructure.entity.Permit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PermitAdapterTest {

    @Mock
    private PermitRepository permitRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private PermitAdapter permitAdapter;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOut() {
        PermitRequest request = PermitRequest.builder().build();
        Permit permit = Permit.builder().idpermiso(1L).permiso("permiso-test").build();
        PermitAllDto permitAllDto = PermitAllDto.builder().usuaCreate("ADMIN").idpermiso(1L).permiso("permiso-test").build();
        when(modelMapper.map(permitRepository.save(permit), PermitAllDto.class)).thenReturn(permitAllDto);
        ResponseEntity<BaseResponse> response = permitAdapter.createOut(request);
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testFindByIdOut() {
        Permit permit = Permit.builder().idpermiso(1L).permiso("permiso-test").build();
        when(permitRepository.findById(anyLong())).thenReturn(Optional.of(permit));
        PermitAllDto permitAllDto = PermitAllDto.builder().usuaCreate("ADMIN").idpermiso(1L).permiso("permiso-test").build();
        when(modelMapper.map(permit, PermitAllDto.class)).thenReturn(permitAllDto);
        ResponseEntity<BaseResponse> response = permitAdapter.findByIdOut(anyLong());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testFindByIdOutEmpty() {
        when(permitRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = permitAdapter.findByIdOut(anyLong());
        assertEquals(414, response.getBody().getCode());
        assertEquals("Not found Permit", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllOutEmpty() {
        List<Permit> permits = new ArrayList<>();
        when(permitRepository.findAll()).thenReturn(permits);
        ResponseEntity<BaseResponse> response = permitAdapter.getAllOut();
        assertEquals(414, response.getBody().getCode());
        assertEquals("Not Content", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllOutNotEmpty() {
        List<Permit> permits = new ArrayList<>();
        permits.add(new Permit());
        when(permitRepository.findAll()).thenReturn(permits);
        ResponseEntity<BaseResponse> response = permitAdapter.getAllOut();
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testUpdateOutEmpty() {
        PermitRequest request = PermitRequest.builder().build();
        when(permitRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = permitAdapter.updateOut(anyLong(), request);
        assertEquals(414, response.getBody().getCode());
        assertEquals("Not found Permit", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testUpdateOutIsNotEmpty() {
        PermitRequest request = PermitRequest.builder().build();
        Permit permit = Permit.builder().idpermiso(1L).permiso("permiso-test").build();
        PermitAllDto permitAllDto = PermitAllDto.builder().usuaCreate("ADMIN").idpermiso(1L).permiso("permiso-test").build();
        when(permitRepository.findById(anyLong())).thenReturn(Optional.of(permit));
        when(modelMapper.map(permitRepository.save(permit), PermitAllDto.class)).thenReturn(permitAllDto);
        ResponseEntity<BaseResponse> response = permitAdapter.updateOut(anyLong(), request);
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }
}