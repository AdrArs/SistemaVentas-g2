package com.codigo.categoryservice.infraestructure.dao;

import com.codigo.categoryservice.infraestructure.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRespository extends JpaRepository<Category,Long> {
    boolean existsByNombre(String nombre);
}
