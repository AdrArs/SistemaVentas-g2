package com.codigo.personservice.infraestructure.adapters;

import com.codigo.personservice.domain.aggregates.request.PersonRequest;
import com.codigo.personservice.domain.aggregates.response.BaseResponse;
import com.codigo.personservice.infraestructure.client.ReniecClient;
import com.codigo.personservice.infraestructure.dao.CustomerRepository;
import com.codigo.personservice.infraestructure.dao.OperatorRepository;
import com.codigo.personservice.infraestructure.dao.PersonRepository;
import com.codigo.personservice.infraestructure.dao.SupplierRepository;
import com.codigo.personservice.infraestructure.entity.Person;
import com.codigo.personservice.infraestructure.redis.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class PersonAdapterTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RedisService redisService;
    @Mock
    private ReniecClient reniecClient;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OperatorRepository operatorRepository;
    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private PersonAdapter personAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomerOrOperatorOut() {
        PersonRequest request = PersonRequest.builder().numDoc("12345678").build();
        when(personRepository.existsByNumeroDocumento(request.getNumDoc())).thenReturn(true);
        ResponseEntity<BaseResponse> response = personAdapter.createCustomerOrOperatorOut(request, anyString());
        assertEquals(410, response.getBody().getCode());
        assertEquals("NumDoc exists", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testCreateCustomerOrOperatorOutInvalid() {
        PersonRequest request = PersonRequest.builder()
                .numDoc("12345678")
                .direcion("direction-test")
                .telefono("22222222")
                .correo("nothing@example.com")
                .clave(anyString())
                .build();
        when(personRepository.existsByNumeroDocumento(request.getNumDoc())).thenReturn(false);
        ResponseEntity<BaseResponse> response = personAdapter.createCustomerOrOperatorOut(request, "CUSTOMER");
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
    }


}