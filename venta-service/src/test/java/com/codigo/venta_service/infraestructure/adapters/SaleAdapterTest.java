package com.codigo.venta_service.infraestructure.adapters;

import com.codigo.venta_service.domain.aggregates.dto.CustomerDto;
import com.codigo.venta_service.domain.aggregates.request.SaleRequest;
import com.codigo.venta_service.domain.aggregates.response.BaseResponse;
import com.codigo.venta_service.infraestructure.client.PersonClient;
import com.codigo.venta_service.infraestructure.client.ProductClient;
import com.codigo.venta_service.infraestructure.dao.SaleDetailRepository;
import com.codigo.venta_service.infraestructure.dao.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SaleAdapterTest {

    @Mock
    private SaleRepository saleRepository;
    @Mock
    private SaleDetailRepository saleDetailRepository;
    @Mock
    private PersonClient personClient;
    @Mock
    private ProductClient productClient;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private SaleAdapter saleAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOut1() {
        SaleRequest saleRequest = SaleRequest.builder().build();
        CustomerDto customerDto = CustomerDto.builder().build();
        when(personClient.getCustomerClient(2L)).thenReturn(customerDto);
        ResponseEntity<BaseResponse> response = saleAdapter.createOut(saleRequest);
        assertEquals(510, response.getBody().getCode());
        assertEquals("Not Found Customer", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testCreateOut2() {
        SaleRequest saleRequest = SaleRequest.builder()
                .idCliente(1L)
                .build();
        CustomerDto customerDto = CustomerDto.builder()
                .id(1L)
                .codCliente("222222")
                .build();
        when(personClient.getCustomerClient(saleRequest.getIdCliente())).thenReturn(customerDto);
        ResponseEntity<BaseResponse> response = saleAdapter.createOut(saleRequest);
        assertEquals(511, response.getBody().getCode());
        assertEquals("Not Found Operator", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

}
