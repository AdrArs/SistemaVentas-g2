package com.codigo.personservice.infraestructure.adapters;

import com.codigo.personservice.domain.aggregates.constant.Constant;
import com.codigo.personservice.domain.aggregates.dto.CustomerResponseDto;
import com.codigo.personservice.domain.aggregates.dto.OperatorResponseDto;
import com.codigo.personservice.domain.aggregates.dto.PersonDto;
import com.codigo.personservice.domain.aggregates.dto.SupplierResponseDto;
import com.codigo.personservice.domain.aggregates.request.PersonRequest;
import com.codigo.personservice.domain.aggregates.response.BaseResponse;
import com.codigo.personservice.infraestructure.client.ReniecClient;
import com.codigo.personservice.infraestructure.dao.CustomerRepository;
import com.codigo.personservice.infraestructure.dao.OperatorRepository;
import com.codigo.personservice.infraestructure.dao.PersonRepository;
import com.codigo.personservice.infraestructure.dao.SupplierRepository;
import com.codigo.personservice.infraestructure.entity.Customer;
import com.codigo.personservice.infraestructure.entity.Operator;
import com.codigo.personservice.infraestructure.entity.Person;
import com.codigo.personservice.infraestructure.entity.Supplier;
import com.codigo.personservice.infraestructure.redis.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

//    @Test
//    void testCreateCustomerOrOperatorOutInvalid() {
//        PersonRequest request = PersonRequest.builder()
//                .numDoc("12345678")
//                .direcion("direction-test")
//                .telefono("22222222")
//                .correo("nothing@example.com")
//                .clave("11111111111111111")
//                .build();
//        Person person = Person.builder().build();
//        PersonDto personDto = PersonDto.builder().build();
//        when(personRepository.existsByNumeroDocumento(request.getNumDoc())).thenReturn(false);
//        when(personRepository.save(person)).thenReturn(person);
//        when(modelMapper.map(person, PersonDto.class)).thenReturn(personDto);
//        ResponseEntity<BaseResponse> response = personAdapter.createCustomerOrOperatorOut(request, "CUSTOMER");
//        assertEquals(200, response.getBody().getCode());
//        assertEquals("Success", response.getBody().getMessage());
//    }

    @Test
    void testFindNumDocOut1() {
        Person person = Person.builder().idpersona(1L).nombre("leviatan")
                .tipoDocumento("dni").telefono("1111111111").numeroDocumento("12345678").build();
        when(personRepository.findByNumeroDocumento("12345678")).thenReturn(Optional.of(person));
        when(customerRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.empty());
        when(operatorRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.empty());
        when(supplierRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> response = personAdapter.findByNumDocOut("12345678");
        assertEquals(550, response.getBody().getCode());
        assertEquals("NumDoc invalid", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindNumDocOut2() {
        Person person = Person.builder().idpersona(1L).nombre("leviatan")
                .tipoDocumento("dni").telefono("1111111111").numeroDocumento("12345678").build();
        when(personRepository.findByNumeroDocumento("12345678")).thenReturn(Optional.of(person));
        when(customerRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.empty());
        when(operatorRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.empty());
        Supplier supplier = Supplier.builder().build();
        when(supplierRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.of(supplier));
        SupplierResponseDto responseDto = SupplierResponseDto.builder().build();
        when(modelMapper.map(person, SupplierResponseDto.class)).thenReturn(responseDto);

        ResponseEntity<BaseResponse> response = personAdapter.findByNumDocOut("12345678");
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testFindNumDocOut3() {
        Person person = Person.builder().idpersona(1L).nombre("leviatan")
                .tipoDocumento("dni").telefono("1111111111").numeroDocumento("12345678").build();
        when(personRepository.findByNumeroDocumento("12345678")).thenReturn(Optional.of(person));
        when(customerRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.empty());
        Operator operator = Operator.builder().build();
        when(operatorRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.of(operator));
        OperatorResponseDto responseDto = OperatorResponseDto.builder().build();
        when(modelMapper.map(person, OperatorResponseDto.class)).thenReturn(responseDto);
        ResponseEntity<BaseResponse> response = personAdapter.findByNumDocOut("12345678");
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testFindNumDocOut4() {
        Person person = Person.builder().idpersona(1L).nombre("leviatan")
                .tipoDocumento("dni").telefono("1111111111").numeroDocumento("12345678").build();
        when(personRepository.findByNumeroDocumento("12345678")).thenReturn(Optional.of(person));
        Customer customer = Customer.builder().build();
        when(customerRepository.findByPersonaIdpersona(anyLong())).thenReturn(Optional.of(customer));
        CustomerResponseDto responseDto = CustomerResponseDto.builder().build();
        when(modelMapper.map(person, CustomerResponseDto.class)).thenReturn(responseDto);
        ResponseEntity<BaseResponse> response = personAdapter.findByNumDocOut("12345678");
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testFindNumDocOut5() {
        when(personRepository.findByNumeroDocumento("12345678")).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = personAdapter.findByNumDocOut("12345678");
        assertEquals(550, response.getBody().getCode());
        assertEquals("NumDoc NotExist", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllSupplierOut1() {
        List<Supplier> suppliers = new ArrayList<>();
        when(supplierRepository.findAll()).thenReturn(suppliers);
        ResponseEntity<BaseResponse> response = personAdapter.getAllSupplierOut();
        assertEquals(550, response.getBody().getCode());
        assertEquals("Lista vacía", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllCustomerOut1() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customers);
        ResponseEntity<BaseResponse> response = personAdapter.getAllSupplierOut();
        assertEquals(550, response.getBody().getCode());
        assertEquals("Lista vacía", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllOperatorOut1() {
        List<Operator> operators = new ArrayList<>();
        when(operatorRepository.findAll()).thenReturn(operators);
        ResponseEntity<BaseResponse> response = personAdapter.getAllSupplierOut();
        assertEquals(550, response.getBody().getCode());
        assertEquals("Lista vacía", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindCustomerByNumDocOut1() {
        when(redisService.getFromRedis(Constant.REDIS_KEY_OBTENERCUSTOMER+"5")).thenReturn(null);
        when(personRepository.findByNumeroDocumento("5")).thenReturn(Optional.empty());
        when(customerRepository.findByPersonaIdpersona(5L)).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = personAdapter.findCustomerByNumDocOut("12345678");
        assertEquals(550, response.getBody().getCode());
        assertEquals("NumDoc NotExists", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

}