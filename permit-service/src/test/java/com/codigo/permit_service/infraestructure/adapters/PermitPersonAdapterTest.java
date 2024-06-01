package com.codigo.permit_service.infraestructure.adapters;

import com.codigo.permit_service.domain.aggregates.dto.PersonDto;
import com.codigo.permit_service.domain.aggregates.request.PermitPersonRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.infraestructure.client.PersonClient;
import com.codigo.permit_service.infraestructure.dao.PermitPersonRepository;
import com.codigo.permit_service.infraestructure.dao.PermitRepository;
import com.codigo.permit_service.infraestructure.entity.Permit;
import com.codigo.permit_service.infraestructure.entity.PermitPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class PermitPersonAdapterTest {

    @Mock
    private PermitPersonRepository permitPersonRepository;
    @Mock
    private PermitRepository permitRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PersonClient personClient;
    @InjectMocks
    private PermitPersonAdapter permitPersonAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOutIsNull() {
        PermitPersonRequest request = PermitPersonRequest.builder().build();
        when(personClient.getPerson(request.getPersonId())).thenReturn(null);
        ResponseEntity<BaseResponse> response = permitPersonAdapter.createOut(request);
        assertEquals(414, response.getBody().getCode());
        assertEquals("Invalid Person", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testCreateOutIsEmpty() {
        PermitPersonRequest request = PermitPersonRequest.builder().build();
        PersonDto personDto = PersonDto.builder().nombre("nombre-test").tipoDocumento("tipo-doc").build();
        when(personClient.getPerson(request.getPersonId())).thenReturn(personDto);
        when(permitRepository.findById(request.getPermit())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = permitPersonAdapter.createOut(request);
        assertEquals(419, response.getBody().getCode());
        assertEquals("Invalid Permit", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindByIdOutIsEmpty() {
        when(permitPersonRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = permitPersonAdapter.findByIdOut(anyLong());
        assertEquals(414, response.getBody().getCode());
        assertEquals("Id Invalid", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindByIdOutIsNull() {
        PermitPerson permitPerson = PermitPerson.builder().id(1L).permisoId(2L).build();
        when(permitPersonRepository.findById(anyLong())).thenReturn(Optional.of(permitPerson));
        ResponseEntity<BaseResponse> response = permitPersonAdapter.findByIdOut(anyLong());
        assertEquals(414, response.getBody().getCode());
        assertEquals("Invalid Person", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindByIdOutIsPermitEmpty() {
        PermitPerson permitPerson = PermitPerson.builder().id(1L).permisoId(2L).build();
        when(permitPersonRepository.findById(anyLong())).thenReturn(Optional.of(permitPerson));
        PersonDto personDto = PersonDto.builder().nombre("nombre-test").tipoDocumento("tipo-doc").build();
        when(personClient.getPerson(permitPerson.getPersonaId())).thenReturn(personDto);
        Permit permit = Permit.builder().idpermiso(1L).permiso("permiso-test").build();
        when(permitRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = permitPersonAdapter.findByIdOut(anyLong());
        assertEquals(419, response.getBody().getCode());
        assertEquals("Invalid Permit", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindByIdOutIsPermitNotEmpty() {
        PermitPerson permitPerson = PermitPerson.builder().id(1L).permisoId(2L).build();
        when(permitPersonRepository.findById(anyLong())).thenReturn(Optional.of(permitPerson));
        PersonDto personDto = PersonDto.builder().nombre("nombre-test").tipoDocumento("tipo-doc").build();
        when(personClient.getPerson(permitPerson.getPersonaId())).thenReturn(personDto);
        Permit permit = Permit.builder().idpermiso(1L).permiso("permiso-test").build();
        when(permitRepository.findById(anyLong())).thenReturn(Optional.of(permit));
        ResponseEntity<BaseResponse> response = permitPersonAdapter.findByIdOut(anyLong());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testGetAllOutIsEmpty() {
        List<PermitPerson> permitPeople = new ArrayList<>();
        when(permitPersonRepository.findAll()).thenReturn(permitPeople);
        ResponseEntity<BaseResponse> response = permitPersonAdapter.getAllOut();
        assertEquals(200, response.getBody().getCode());
        assertEquals("No content", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllOutIsNotEmpty() {
        List<PermitPerson> permitPeople = new ArrayList<>();
        permitPeople.add(new PermitPerson());
        when(permitPersonRepository.findAll()).thenReturn(permitPeople);
        ResponseEntity<BaseResponse> response = permitPersonAdapter.getAllOut();
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testFindPersonByIdOutIsEmpty() {
        List<PermitPerson> permitPeople = new ArrayList<>();
        when(permitPersonRepository.findAll()).thenReturn(permitPeople);
        ResponseEntity<BaseResponse> response = permitPersonAdapter.findPersonByIdOut(anyLong());
        assertEquals(414, response.getBody().getCode());
        assertEquals("No content", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindPersonByIdOutIsNotEmpty() {
        List<PermitPerson> permitPeople = new ArrayList<>();
        permitPeople.add(new PermitPerson());
        when(permitPersonRepository.findByPersonaId(anyLong())).thenReturn(permitPeople);
        ResponseEntity<BaseResponse> response = permitPersonAdapter.findPersonByIdOut(anyLong());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testUpdateOutIsEmpty() {
        PermitPersonRequest request = PermitPersonRequest.builder().build();
        when(permitPersonRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = permitPersonAdapter.updateOut(anyLong(), request);
        assertEquals(414, response.getBody().getCode());
        assertEquals("Id Invalid", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testUpdateOutIsNull() {
        PermitPerson permitPerson = PermitPerson.builder().id(1L).permisoId(2L).build();
        PermitPersonRequest request = PermitPersonRequest.builder().build();
//        PersonDto personDto = PersonDto.builder().nombre("nombre-test").tipoDocumento("tipo-doc").build();
        when(permitPersonRepository.findById(anyLong())).thenReturn(Optional.of(permitPerson));
        when(personClient.getPerson(request.getPersonId())).thenReturn(null);
        ResponseEntity<BaseResponse> response = permitPersonAdapter.updateOut(anyLong(), request);
        assertEquals(414, response.getBody().getCode());
        assertEquals("Invalid Person", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testUpdateOutPermitIsEmpty() {
        PermitPerson permitPerson = PermitPerson.builder().id(1L).permisoId(2L).build();
        PermitPersonRequest request = PermitPersonRequest.builder().build();
        PersonDto personDto = PersonDto.builder().nombre("nombre-test").tipoDocumento("tipo-doc").build();
        Permit permit = Permit.builder().idpermiso(1L).permiso("permiso-test").build();
        when(permitPersonRepository.findById(anyLong())).thenReturn(Optional.of(permitPerson));
        when(personClient.getPerson(request.getPersonId())).thenReturn(personDto);
        when(permitRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> response = permitPersonAdapter.updateOut(anyLong(), request);
        assertEquals(419, response.getBody().getCode());
        assertEquals("Invalid Permit", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testUpdateOutPermitIsNotEmpty() {
        PermitPerson permitPerson = PermitPerson.builder().id(1L).permisoId(2L).build();
        PermitPersonRequest request = PermitPersonRequest.builder().build();
        PersonDto personDto = PersonDto.builder().nombre("nombre-test").tipoDocumento("tipo-doc").build();
        Permit permit = Permit.builder().idpermiso(1L).permiso("permiso-test").build();
        when(permitPersonRepository.findById(anyLong())).thenReturn(Optional.of(permitPerson));
        when(personClient.getPerson(request.getPersonId())).thenReturn(personDto);
        when(permitRepository.findById(request.getPermit())).thenReturn(Optional.of(permit));
        when(permitPersonRepository.save(permitPerson)).thenReturn(permitPerson);
        ResponseEntity<BaseResponse> response = permitPersonAdapter.updateOut(anyLong(), request);
        assertEquals(414, response.getBody().getCode());
        assertEquals("Invalid Request", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }
}