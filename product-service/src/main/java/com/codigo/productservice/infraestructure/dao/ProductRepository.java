package com.codigo.productservice.infraestructure.dao;

import com.codigo.productservice.infraestructure.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
