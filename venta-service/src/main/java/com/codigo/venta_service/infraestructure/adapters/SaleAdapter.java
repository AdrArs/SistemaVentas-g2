package com.codigo.venta_service.infraestructure.adapters;

import com.codigo.venta_service.domain.aggregates.dto.*;
import com.codigo.venta_service.domain.aggregates.request.DetalleRequest;
import com.codigo.venta_service.domain.aggregates.request.SaleRequest;
import com.codigo.venta_service.domain.aggregates.response.BaseResponse;
import com.codigo.venta_service.domain.aggregates.response.SaleResponse;
import com.codigo.venta_service.domain.ports.out.SaleServiceOut;
import com.codigo.venta_service.infraestructure.client.PersonClient;
import com.codigo.venta_service.infraestructure.client.ProductClient;
import com.codigo.venta_service.infraestructure.dao.SaleDetailRepository;
import com.codigo.venta_service.infraestructure.dao.SaleRepository;
import com.codigo.venta_service.infraestructure.entity.Sale;
import com.codigo.venta_service.infraestructure.entity.SaleDetail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class SaleAdapter implements SaleServiceOut {
    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final PersonClient personClient;
    private final ProductClient productClient;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ResponseEntity<BaseResponse> createOut(SaleRequest saleRequest) {

        CustomerDto customerDto = personClient.getCustomerClient(saleRequest.getIdCliente());
        if (isNull(customerDto)){
            return ResponseEntity
                    .ok(new BaseResponse<>(510,"Not Found Customer", Optional.empty()));
        }
        OperatorDto operatorDto = personClient.getOperatorClient(saleRequest.getIdVendedor());
        if (isNull(operatorDto)){
            return ResponseEntity
                    .ok(new BaseResponse<>(511,"Not Found Operator", Optional.empty()));
        }

        List<SaleDetail> list = new ArrayList<>();
        float total = 0f;
        for ( DetalleRequest l: saleRequest.getDetalleRequest()) {
            SaleDetail detail;
            Long idProducto = l.getIdproducto();
            ProductDto product = productClient.getProductClient(idProducto);
            if (isNull(product)) {
                throw new RuntimeException(String.format("El producto con id: %s  no existe", idProducto));
            }
            Integer cantidad = l.getCantidad();
            if (product.getStock() < cantidad) {
                throw new RuntimeException(String.format("No existe stock disponible del producto con id: %s , solo tenemos %s unidad(es)", idProducto, product.getStock()));
            }
            Random random = new Random();
            int descuento = Math.round(5 + (10 * random.nextFloat()));

            Float desc = (float) (descuento*0.01);
            Float precioInicial = cantidad * product.getPrecio();
            Float valorDescuento = precioInicial * desc;
            float subTotal = precioInicial - valorDescuento;
            detail = SaleDetail.builder()
                    .idproducto(idProducto)
                    .cantidad(cantidad)
                    .descuento(valorDescuento)
                    .precioVenta(subTotal)
                    .build();
            list.add(detail);
            total += subTotal;
        }

        Sale sale = Sale.builder()
                .tipoComprobante(saleRequest.getTipoComprobante())
                .fechaHora(getTime())
                .idCliente(customerDto.getId())
                .idVendedor(operatorDto.getId())
                .igv((float) (0.18*total))
                .total(total)
                .estado("Registrado")
                .build();
        if(sale.getTipoComprobante().equalsIgnoreCase("BOLETA")){
            sale.setNumComprobante(saleRepository.generateNumBoleta());
        } else if (sale.getTipoComprobante().equalsIgnoreCase("FACTURA")) {
            sale.setNumComprobante(saleRepository.generateNumFactura());
        } else{
            throw new RuntimeException("Tipo de Comprobante incorrecto");
        }
        Sale entity =saleRepository.save(sale);
        SaleDto dto = modelMapper.map(entity,SaleDto.class);
        List<SaleDetailDto> saleDetailDtoList = new ArrayList<>();
        for (SaleDetail s :list){
            s.setVenta(entity);
            saleDetailDtoList.add(modelMapper.map(saleDetailRepository.save(s),SaleDetailDto.class));
            String valor = s.getIdproducto()+"-"+s.getCantidad();
            productClient.updateStock(valor);
        }
        SaleResponse response = modelMapper.map(dto,SaleResponse.class);
        response.setSaleDetailDtoList(saleDetailDtoList);
        return ResponseEntity
                .ok(new BaseResponse<>(200,"Success", Optional.of(response)));
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdOut(Long id) {
        Optional<Sale> optionalSale = saleRepository.findById(id);
        if (optionalSale.isPresent()) {
            Sale sale = optionalSale.get();
            SaleDto saleDto = modelMapper.map(sale, SaleDto.class);
            List<SaleDetail> saleDetails = saleDetailRepository.findByVenta(sale);
            List<SaleDetailDto> saleDetailDtos = saleDetails.stream()
                    .map(detail -> modelMapper.map(detail, SaleDetailDto.class))
                    .collect(Collectors.toList());
            SaleResponse response = modelMapper.map(saleDto,SaleResponse.class);
            response.setSaleDetailDtoList(saleDetailDtos);
            return ResponseEntity.ok(new BaseResponse<>(200, "Success", Optional.of(response)));
        } else {
            return ResponseEntity.ok(new BaseResponse<>(404, "Sale not found", Optional.empty()));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getAllOut() {
        List<Sale> sales = saleRepository.findAll();

        if (sales.isEmpty()){
            return ResponseEntity.ok(new BaseResponse<>(404, "Sale Empty", Optional.empty()));
        }
        List<SaleResponse> saleResponses = new ArrayList<>();
        SaleResponse response = null;
        for (Sale sale : sales) {
            SaleDto saleDto = modelMapper.map(sale, SaleDto.class);
            List<SaleDetail> saleDetails = saleDetailRepository.findByVenta(sale);
            List<SaleDetailDto> saleDetailDtos = saleDetails.stream()
                    .map(detail -> modelMapper.map(detail, SaleDetailDto.class))
                    .collect(Collectors.toList());
            response = modelMapper.map(saleDto,SaleResponse.class);
            response.setSaleDetailDtoList(saleDetailDtos);
            saleResponses.add(response);
        }
        return ResponseEntity.ok(new BaseResponse<>(200, "Success", Optional.of(saleResponses)));
    }

    @Transactional
    @Override
    public ResponseEntity<BaseResponse> updateOut(Long id, SaleRequest saleRequest) {
        Optional<Sale> optionalSale = saleRepository.findById(id);
        if (optionalSale.isEmpty()) {
            return ResponseEntity.ok(new BaseResponse<>(404, "Sale not found", Optional.empty()));
        }

        Sale existingSale = optionalSale.get();

        CustomerDto customerDto = personClient.getCustomerClient(saleRequest.getIdCliente());
        if (isNull(customerDto)) {
            return ResponseEntity.ok(new BaseResponse<>(510, "Not Found Customer", Optional.empty()));
        }

        OperatorDto operatorDto = personClient.getOperatorClient(saleRequest.getIdVendedor());
        if (isNull(operatorDto)) {
            return ResponseEntity.ok(new BaseResponse<>(511, "Not Found Operator", Optional.empty()));
        }


        List<SaleDetail> existingDetails = saleDetailRepository.findByVenta(existingSale);
        for (SaleDetail detail : existingDetails) {
            productClient.resetStock(detail.getIdproducto() + "-" + detail.getCantidad());
        }


        saleDetailRepository.deleteByVentaId(existingSale.getIdventa());


        List<SaleDetail> newDetails = new ArrayList<>();
        float total = 0f;
        for (DetalleRequest l : saleRequest.getDetalleRequest()) {
            SaleDetail detail;
            Long idProducto = l.getIdproducto();
            ProductDto product = productClient.getProductClient(idProducto);
            if (isNull(product)) {
                throw new RuntimeException(String.format("El producto con id: %s no existe", idProducto));
            }
            Integer cantidad = l.getCantidad();
            if (product.getStock() < cantidad) {
                throw new RuntimeException(String.format("No existe stock disponible del producto con id: %s, solo tenemos %s unidad(es)", idProducto, product.getStock()));
            }
            Random random = new Random();
            int descuento = Math.round(5 + (10 * random.nextFloat()));

            Float desc = descuento * 0.01f;
            Float precioInicial = cantidad * product.getPrecio();
            Float valorDescuento = precioInicial * desc;
            float subTotal = precioInicial - valorDescuento;
            detail = SaleDetail.builder()
                    .idproducto(idProducto)
                    .cantidad(cantidad)
                    .descuento(valorDescuento)
                    .precioVenta(subTotal)
                    .build();
            newDetails.add(detail);
            total += subTotal;
        }


        existingSale.setTipoComprobante(saleRequest.getTipoComprobante());
        existingSale.setFechaHora(getTime());
        existingSale.setIdCliente(customerDto.getId());
        existingSale.setIdVendedor(operatorDto.getId());
        existingSale.setIgv(total * 0.18f);
        existingSale.setTotal(total);
        existingSale.setEstado("Actualizado");

        if (existingSale.getTipoComprobante().equalsIgnoreCase("BOLETA")) {
            existingSale.setNumComprobante(saleRepository.generateNumBoleta());
        } else if (existingSale.getTipoComprobante().equalsIgnoreCase("FACTURA")) {
            existingSale.setNumComprobante(saleRepository.generateNumFactura());
        } else {
            throw new RuntimeException("Tipo de Comprobante incorrecto");
        }

        Sale updatedSale = saleRepository.save(existingSale);


        List<SaleDetailDto> saleDetailDtoList = new ArrayList<>();
        for (SaleDetail s : newDetails) {
            s.setVenta(updatedSale);
            saleDetailDtoList.add(modelMapper.map(saleDetailRepository.save(s), SaleDetailDto.class));
            String valor = s.getIdproducto() + "-" + s.getCantidad();
            productClient.updateStock(valor);
        }

        SaleDto dto = modelMapper.map(updatedSale, SaleDto.class);
        SaleResponse response = modelMapper.map(dto, SaleResponse.class);
        response.setSaleDetailDtoList(saleDetailDtoList);
        return ResponseEntity.ok(new BaseResponse(200, "Sale updated successfully", Optional.of(response)));
    }


    @Override
    public ResponseEntity<BaseResponse> deleteOut(Long id) {
        Optional<Sale> optionalSale = saleRepository.findById(id);
        if (optionalSale.isPresent()) {
            if (optionalSale.get().getEstado().equalsIgnoreCase("CANCELADO")){
                return ResponseEntity.ok(new BaseResponse<>(404, "La venta ya se encuentra cancelada", Optional.empty()));
            }
            optionalSale.get().setEstado("CANCELADO");
            List<SaleDetail> saleDetails = saleDetailRepository.findByVenta(optionalSale.get());
            for (SaleDetail detail : saleDetails) {
                productClient.resetStock(detail.getIdproducto()+"-"+detail.getCantidad());
            }
            SaleDto saleDto = modelMapper.map(saleRepository.save(optionalSale.get()),SaleDto.class);
            return ResponseEntity.ok(new BaseResponse<>(200, "Sale deleted successfully", Optional.of(saleDto)));
        } else {
            return ResponseEntity.ok(new BaseResponse<>(404, "Sale not found", Optional.empty()));
        }
    }

    private Timestamp getTime(){
        long currentTime =System.currentTimeMillis();
        return new Timestamp(currentTime);
    }
}
