package com.codigo.personservice.infraestructure.adapters;

import com.codigo.personservice.domain.aggregates.constant.Constant;
import com.codigo.personservice.domain.aggregates.dto.*;
import com.codigo.personservice.domain.aggregates.request.PersonRequest;
import com.codigo.personservice.domain.aggregates.request.SupplierRequest;
import com.codigo.personservice.domain.aggregates.response.BaseResponse;
import com.codigo.personservice.domain.ports.out.PersonServiceOut;
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
import com.codigo.personservice.infraestructure.util.Util;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class PersonAdapter implements PersonServiceOut {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final RedisService redisService;
    private final ReniecClient reniecClient;
    private final CustomerRepository customerRepository;
    private final OperatorRepository operatorRepository;
    private final SupplierRepository supplierRepository;

    @Value("${token.client}")
    private String token;

    @Override
    public ResponseEntity<BaseResponse> createSupplierOut(SupplierRequest supplierRequest) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResponse> createCustomerOrOperatorOut(PersonRequest personRequest, String tipo) {
        boolean exists = personRepository.existsByNumeroDocumento(personRequest.getNumDoc());
        if(exists){
            return ResponseEntity
                    .ok(new BaseResponse(410,"NumDoc exists", Optional.empty()));
        }else{
            Person person = getPerson(personRequest,Constant.ACTION_CREATE,null);
            if(isNull(person)){
                return ResponseEntity
                        .ok(new BaseResponse(550,"Person invalid", Optional.empty()));
            }
            try{
                person = personRepository.save(person);
                PersonDto personDto = modelMapper.map(person,PersonDto.class);
                switch (tipo){
                    case "CUSTOMER":
                        Customer customer = Customer.builder()
                                .codCliente(customerRepository.generateCodCustomer())
                                .persona(person)
                                .build();
                        CustomerDto customerDto = modelMapper.map(customerRepository.save(customer),CustomerDto.class);
                        CustomerResponseDto customerResponseDto = modelMapper.map(personDto,CustomerResponseDto.class);
                        customerResponseDto.setCodCliente(customerDto.getCodCliente());
                        return ResponseEntity
                                .ok(new BaseResponse(200,"Success", Optional.of(customerResponseDto)));
                    case "OPERATOR":
                        Operator operator = Operator.builder()
                                .codEmpleado(operatorRepository.generateCodOperator())
                                .persona(person)
                                .build();
                        OperatorDto operatorDto = modelMapper.map(operatorRepository.save(operator),OperatorDto.class);
                        OperatorResponseDto operatorResponseDto = modelMapper.map(personDto,OperatorResponseDto.class);
                        operatorResponseDto.setCodEmpleado(operatorDto.getCodEmpleado());
                        return ResponseEntity
                                .ok(new BaseResponse(200,"Success", Optional.of(operatorResponseDto)));
                    default:
                        return ResponseEntity
                                .ok(new BaseResponse(550,"Person invalid", Optional.empty()));
                }
            }catch (Exception e){
//                controlar error generado por desconexion en bd
                return null;
            }
        }
    }

    private Person getPerson(PersonRequest personRequest , String action , Long id) {
//        Evaluar caso en donde se envia dni invalido
        ReniecDto reniecDto = getExecReniec(personRequest.getNumDoc());
        Person person = Person.builder()
                .nombre(reniecDto.getNombres()+" "+reniecDto.getApellidoPaterno()+" "+reniecDto.getApellidoMaterno())
                .tipoDocumento(reniecDto.getTipoDocumento())
                .numeroDocumento(reniecDto.getNumeroDocumento())
//                .correo(reniecDto.getApellidoPaterno().substring(0,2).toLowerCase()+reniecDto.getNumeroDocumento()+"@example.com")
                .correo(personRequest.getCorreo())
                .estado(true)
                .clave(new BCryptPasswordEncoder().encode(personRequest.getClave()))
//                .clave(new BCryptPasswordEncoder().encode("123"))
                .direccion(personRequest.getDirecion())
                .telefono(personRequest.getTelefono())
                .build();
        switch (action){
            case "CREATE":
                person.setUsuaCreate("ADMIN");
                person.setDateCreate(getTime());
                break;
            case "UPDATE":
                Optional<Person> op = personRepository.findById(id);
                if (op.isPresent()){
                    person.setIdpersona(id);
                    person.setUsuaCreate(op.get().getUsuaCreate());
                    person.setDateCreate(op.get().getDateCreate());
                    person.setUsuaModif("USER UPDATE");
                    person.setDateModif(getTime());
                    person.setUsuaDelet(op.get().getUsuaDelet());
                    person.setDateDelet(op.get().getDateDelet());
                }else {
                    return null;
                }
                break;
        }
        return person;
    }

    private ReniecDto getExecReniec(String numDoc){
        String authorization = "Bearer "+token;
        return reniecClient.getInfoReniec(numDoc,authorization);
    }

    private Timestamp getTime(){
        long currentTime =System.currentTimeMillis();
        return new Timestamp(currentTime);
    }

    @Override
    public ResponseEntity<BaseResponse> findByNumDocOut(String num) {
            Optional<Person> person = personRepository.findByNumeroDocumento(num);
            if (person.isPresent()){
                Long idPersona = person.get().getIdpersona();
                Optional<Customer> customer = customerRepository.findByPersonaIdpersona(idPersona);
                if (customer.isEmpty()){
                    Optional<Operator> operator = operatorRepository.findByPersonaIdpersona(idPersona);
                    if (operator.isEmpty()){
                        Optional<Supplier> supplier = supplierRepository.findByPersonaIdpersona(idPersona);
                        if(supplier.isEmpty()){
                            return ResponseEntity
                                    .ok(new BaseResponse(550,"NumDoc invalid", Optional.empty()));
                        }else {
                            SupplierResponseDto responseDto = modelMapper.map(person.get(), SupplierResponseDto.class);
                            responseDto.setEmpresa(supplier.get().getEmpresa());
                            return ResponseEntity
                                    .ok(new BaseResponse(200,"Success", Optional.of(responseDto)));
                        }
                    }else {
                        OperatorResponseDto responseDto = modelMapper.map(person.get(),OperatorResponseDto.class);
                        responseDto.setCodEmpleado(operator.get().getCodEmpleado());
                        return ResponseEntity
                                .ok(new BaseResponse(200,"Success", Optional.of(responseDto)));
                    }
                }else{
                    CustomerResponseDto responseDto = modelMapper.map(person.get(),CustomerResponseDto.class);
                    responseDto.setCodCliente(customer.get().getCodCliente());
                    return ResponseEntity
                            .ok(new BaseResponse(200,"Success", Optional.of(responseDto)));
                }
            }else {
                return ResponseEntity
                        .ok(new BaseResponse(550,"NumDoc NotExist", Optional.empty()));
            }
    }

    @Override
    public ResponseEntity<BaseResponse> getAllSupplierOut() {
        List<Supplier> list = supplierRepository.findAll();
        if (list.isEmpty())
            return ResponseEntity
                    .ok(new BaseResponse(550,"Lista vacía", Optional.empty()));

        List<SupplierResponseDto> supplierResponseDtoList = list.stream()
                .map(l -> {
                    PersonDto personDto = modelMapper.map(l.getPersona(),PersonDto.class);
                    SupplierResponseDto response = modelMapper.map(personDto,SupplierResponseDto.class);
                    response.setEmpresa(l.getEmpresa());
                    return response;
                })
                .toList();
        return ResponseEntity
                .ok(new BaseResponse(200,"Success", Optional.of(supplierResponseDtoList)));
    }

    @Override
    public ResponseEntity<BaseResponse> getAllCustomerOut() {
        List<Customer> list = customerRepository.findAll();
        if (list.isEmpty())
            return ResponseEntity
                    .ok(new BaseResponse(550,"Lista vacía", Optional.empty()));

        List<CustomerResponseDto> customerResponseDtoList = list.stream()
                .map(l -> {
                    PersonDto personDto = modelMapper.map(l.getPersona(),PersonDto.class);
                    CustomerResponseDto response = modelMapper.map(personDto,CustomerResponseDto.class);
                    response.setCodCliente(l.getCodCliente());
                    return response;
                })
                .toList();
        return ResponseEntity
                .ok(new BaseResponse(200,"Success", Optional.of(customerResponseDtoList)));
    }

    @Override
    public ResponseEntity<BaseResponse> getAllOperatorOut() {
        List<Operator> list = operatorRepository.findAll();
        if (list.isEmpty())
            return ResponseEntity
                    .ok(new BaseResponse(550,"Lista vacía", Optional.empty()));

        List<OperatorResponseDto> operatorResponseDtoList = list.stream()
                .map(l -> {
                    PersonDto personDto = modelMapper.map(l.getPersona(),PersonDto.class);
                    OperatorResponseDto response = modelMapper.map(personDto,OperatorResponseDto.class);
                    response.setCodEmpleado(l.getCodEmpleado());
                    return response;
                })
                .toList();
        return ResponseEntity
                .ok(new BaseResponse(200,"Success", Optional.of(operatorResponseDtoList)));
    }

    @Override
    public ResponseEntity<BaseResponse> findCustomerByNumDocOut(String num) {
            String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENERCUSTOMER+num);
            if(!isNull(redisInfo)){
                CustomerResponseDto customerResponseDto = Util.convertJsonToDto(redisInfo,CustomerResponseDto.class);
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(customerResponseDto)));
            }
            Optional<Person> person = personRepository.findByNumeroDocumento(num);
            if (person.isPresent()){
                Long idPersona = person.get().getIdpersona();
                Optional<Customer> customer = customerRepository.findByPersonaIdpersona(idPersona);
                if (customer.isEmpty()){
                    return ResponseEntity
                                    .ok(new BaseResponse(550,"Customer not register", Optional.empty()));
                }else{
                    CustomerResponseDto responseDto = modelMapper.map(person.get(),CustomerResponseDto.class);
                    responseDto.setCodCliente(customer.get().getCodCliente());
                    String json = Util.convertDtoToJson(responseDto);
                    redisService.saveInRedis(Constant.REDIS_KEY_OBTENERCUSTOMER+num,json,10);
                    return ResponseEntity
                            .ok(new BaseResponse(200,"Success", Optional.of(responseDto)));
                }
            }else {
                return ResponseEntity
                        .ok(new BaseResponse(550,"NumDoc NotExists", Optional.empty()));
            }
    }

    @Override
    public ResponseEntity<BaseResponse> findOperatorByNumDocOut(String num) {
            String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENEROPERATOR+num);
            if(!isNull(redisInfo)){
                OperatorResponseDto operatorResponseDto = Util.convertJsonToDto(redisInfo,OperatorResponseDto.class);
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(operatorResponseDto)));
            }
            Optional<Person> person = personRepository.findByNumeroDocumento(num);
            if (person.isPresent()){
                Long idPersona = person.get().getIdpersona();
                Optional<Operator> operator = operatorRepository.findByPersonaIdpersona(idPersona);
                if (operator.isEmpty()){
                    return ResponseEntity
                                    .ok(new BaseResponse(550,"NumDoc invalid", Optional.empty()));
                }else{
                    OperatorResponseDto responseDto = modelMapper.map(person.get(),OperatorResponseDto.class);
                    responseDto.setCodEmpleado(operator.get().getCodEmpleado());
                    String json = Util.convertDtoToJson(responseDto);
                    redisService.saveInRedis(Constant.REDIS_KEY_OBTENEROPERATOR+num,json,10);
                    return ResponseEntity
                            .ok(new BaseResponse(200,"Success", Optional.of(responseDto)));
                }
            }else {
                return ResponseEntity
                        .ok(new BaseResponse(550,"NumDoc NotExists", Optional.empty()));
            }
    }

    @Override
    public ResponseEntity<BaseResponse> findSupplierByNumDocOut(String num) {
            String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENERSUPPLIER+num);
            if(!isNull(redisInfo)){
                SupplierResponseDto supplierResponseDto = Util.convertJsonToDto(redisInfo,SupplierResponseDto.class);
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(supplierResponseDto)));
            }
            Optional<Person> person = personRepository.findByNumeroDocumento(num);
            if (person.isPresent()){
                Long idPersona = person.get().getIdpersona();
                Optional<Supplier> supplier = supplierRepository.findByPersonaIdpersona(idPersona);
                if (supplier.isEmpty()){
                    return ResponseEntity
                                    .ok(new BaseResponse(550,"NumDoc invalid", Optional.empty()));
                }else{
                    SupplierResponseDto responseDto = modelMapper.map(person.get(), SupplierResponseDto.class);
                    responseDto.setEmpresa(supplier.get().getEmpresa());
                    String json = Util.convertDtoToJson(responseDto);
                    redisService.saveInRedis(Constant.REDIS_KEY_OBTENERSUPPLIER+num,json,10);
                    return ResponseEntity
                            .ok(new BaseResponse(200,"Success", Optional.of(responseDto)));
                }
            }else {
                return ResponseEntity
                        .ok(new BaseResponse(550,"NumDoc NotExists", Optional.empty()));
            }
    }

    @Override
    public ResponseEntity<BaseResponse> updateCustomerOrOperatorIn(Long id,PersonRequest personRequest, String tipo ) {
        Optional<Person> person = personRepository.findById(id);
        if(person.isEmpty()){
            return ResponseEntity
                    .ok(new BaseResponse(410,"NumDoc exists", Optional.empty()));
        }else{
            Person personUpdate = getPerson(personRequest, Constant.ACTION_UPDATE, id);
            if (isNull(personUpdate)){
                return ResponseEntity
                        .ok(new BaseResponse(550,"Person invalid", Optional.empty()));
            }
            try{
                PersonDto personDto = modelMapper.map(personRepository.save(personUpdate),PersonDto.class);
                switch (tipo){
                    case "CUSTOMER":
                        Optional<Customer> customer = customerRepository.findByPersonaIdpersona(id);
                        if(customer.isPresent()){
                            CustomerResponseDto customerResponseDto = modelMapper.map(personDto,CustomerResponseDto.class);
                            customerResponseDto.setCodCliente(customer.get().getCodCliente());
                            return ResponseEntity
                                    .ok(new BaseResponse(200,"Success", Optional.of(customerResponseDto)));
                        }
                    case "OPERATOR":
                        Optional<Operator> operator = operatorRepository.findByPersonaIdpersona(id);
                        if(operator.isPresent()){
                            OperatorResponseDto operatorResponseDto = modelMapper.map(personDto,OperatorResponseDto.class);
                            operatorResponseDto.setCodEmpleado(operator.get().getCodEmpleado());
                            return ResponseEntity
                                    .ok(new BaseResponse(200,"Success", Optional.of(operatorResponseDto)));
                        }
                    default:
                        return ResponseEntity
                                .ok(new BaseResponse(550,"Person invalid", Optional.empty()));
                }
            }catch (Exception e){
//                controlar error generado por desconexion en bd
                return null;
            }
        }
    }

    @Override
    public PersonDto getPersonClientOut(Long id) {
        try{
            Optional<Person> person = personRepository.findById(id);
            if (person.isPresent()){
                PersonDto personDto = modelMapper.map(person.get(),PersonDto.class);
                return personDto;
            }
            return new PersonDto();
        }catch (Exception e){
            return new PersonDto();
        }
    }

    @Override
    public OperatorDto getOperatorClientOut(Long id) {
        Optional<Operator> operator = operatorRepository.findById(id);
        if(operator.isPresent()){
            PersonDto personDto = modelMapper.map(operator.get().getPersona(),PersonDto.class);
            return  OperatorDto.builder()
                    .id(operator.get().getId())
                    .codEmpleado(operator.get().getCodEmpleado())
                    .persona(personDto)
                    .build();
        }
        return null;
    }

    @Override
    public CustomerDto getCustomerClientOut(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            PersonDto personDto = modelMapper.map(customer.get().getPersona(),PersonDto.class);
            return  CustomerDto.builder()
                    .id(customer.get().getId())
                    .codCliente(customer.get().getCodCliente())
                    .persona(personDto)
                    .build();
        }
        return null;
    }
}
