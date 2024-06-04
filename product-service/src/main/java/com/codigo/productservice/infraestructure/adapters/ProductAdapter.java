package com.codigo.productservice.infraestructure.adapters;

import com.codigo.productservice.domain.aggregates.dto.CategoryDto;
import com.codigo.productservice.domain.aggregates.dto.ProductDto;
import com.codigo.productservice.domain.aggregates.request.ProductRequest;
import com.codigo.productservice.domain.aggregates.response.BaseResponse;
import com.codigo.productservice.domain.aggregates.response.ProductResponse;
import com.codigo.productservice.domain.ports.out.ProductServiceOut;
import com.codigo.productservice.infraestructure.client.CategoryClient;
import com.codigo.productservice.infraestructure.dao.ProductRepository;
import com.codigo.productservice.infraestructure.entity.Product;
import com.codigo.productservice.infraestructure.mapper.ConvertProduct;
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
public class ProductAdapter implements  ProductServiceOut{
    private final ProductRepository productRepository;
    private final CategoryClient categoryClient;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<BaseResponse> createOut(ProductRequest productRequest) {
        try {
            CategoryDto categoryDto = categoryClient.getCategory(productRequest.getCategoria());
            if(!isNull(categoryDto)){
                if(!categoryDto.getCondicion()){
                    return ResponseEntity
                            .ok(new BaseResponse<>(511,"Category Inactive", Optional.empty()));
                }
                Product product = Product.builder()
                        .nombre(productRequest.getNombre())
                        .descripcion(productRequest.getDescripcion())
                        .condicion(true)
                        .stock(productRequest.getStock())
                        .codigo(productRequest.getCodigo())
                        .imagen(productRequest.getImagen())
                        .idcategoria(categoryDto.getIdcategoria())
                        .build();
                product.setUsuaCreate("USER Admin");
                product.setDateCreate(getTime());
                ProductResponse productResponse = ConvertProduct.toProductResponse(productRepository.save(product),categoryDto);
                return ResponseEntity.ok(new BaseResponse<>(200,"Success", Optional.of(productResponse)));
            }
            return ResponseEntity
                    .ok(new BaseResponse<>(510,"Not Found Category", Optional.empty()));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdOut(Long id) {
        try{
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()){
                CategoryDto categoryDto = categoryClient.getCategory(product.get().getIdcategoria());
                ProductResponse productResponse = ConvertProduct.toProductResponse(product.get(),categoryDto);
                return ResponseEntity.ok(new BaseResponse<>(200,"Success", Optional.of(productResponse)));
            }
            return ResponseEntity
                    .ok(new BaseResponse<>(510,"Not Found Category", Optional.empty()));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getAllOut() {
        try {
            List<Product> products = productRepository.findAll();
            if(!products.isEmpty()){
                List<ProductResponse> productResponse = products.stream()
                        .map(x -> {
                            CategoryDto categoryDto = categoryClient.getCategory(x.getIdcategoria());
                            return ConvertProduct.toProductResponse(x,categoryDto);
                        })
                        .toList();
                return ResponseEntity.ok(new BaseResponse<>(200,"Success", Optional.of(productResponse)));
            }
            return ResponseEntity
                    .ok(new BaseResponse<>(510,"Not Found Category", Optional.empty()));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ResponseEntity<BaseResponse> updateOut(Long id, ProductRequest productRequest) {
        try{
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()){
                Product entity = getProduct(productRequest, product);
                CategoryDto categoryDto = categoryClient.getCategory(productRequest.getCategoria());
                ProductResponse productResponse = ConvertProduct.toProductResponse(productRepository.save(entity),categoryDto);
                return ResponseEntity.ok(new BaseResponse<>(200,"Success", Optional.of(productResponse)));
            }
            return ResponseEntity
                    .ok(new BaseResponse<>(510,"Not Found Category", Optional.empty()));
        }catch (Exception e){
            return null;
        }
    }

    private Product getProduct(ProductRequest productRequest, Optional<Product> product) {
        Product entity = product.get();
        entity.setCodigo(productRequest.getCodigo());
        entity.setIdcategoria(productRequest.getCategoria());
        entity.setDescripcion(productRequest.getDescripcion());
        entity.setImagen(productRequest.getImagen());
        entity.setStock(productRequest.getStock());
        entity.setPrecio(productRequest.getPrecio());
        entity.setNombre(productRequest.getNombre());
        entity.setUsuaModif("USER-MODIF");
        entity.setDateModif(getTime());
        return entity;
    }

    @Override
    public ResponseEntity<BaseResponse> deleteOut(Long id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()){
                Product entity = product.get();
                entity.setCondicion(false);
                CategoryDto categoryDto = categoryClient.getCategory(entity.getIdcategoria());
                ProductResponse productResponse = ConvertProduct.toProductResponse(productRepository.save(entity),categoryDto);
                return ResponseEntity.ok(new BaseResponse<>(200,"Success", Optional.of(productResponse)));
            }else{
                return ResponseEntity
                        .ok(new BaseResponse<>(510,"Not Found Category", Optional.empty()));
            }
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ProductDto getByIdOut(Long id) {
        try{
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()){
                return modelMapper.map(product.get(),ProductDto.class);
            }
            return new ProductDto();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void updateStockOut(String valor) {
        String[] valores = valor.split("-");
        Optional<Product> op = productRepository.findById(Long.parseLong(valores[0]));
        if (op.isPresent()){
            Product product = op.get();
            Integer cantidad = Integer.parseInt(valores[1]);
            product.setStock(product.getStock() - cantidad);
            productRepository.save(product);
        }
    }

    @Override
    public void resetStockOut(String valor) {
        String[] valores = valor.split("-");
        Optional<Product> op = productRepository.findById(Long.parseLong(valores[0]));
        if (op.isPresent()){
            Product product = op.get();
            Integer cantidad = Integer.parseInt(valores[1]);
            product.setStock(product.getStock() + cantidad);
            productRepository.save(product);
        }
    }

    private Timestamp getTime(){
        long currentTime =System.currentTimeMillis();
        return new Timestamp(currentTime);
    }
}
