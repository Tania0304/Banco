package com.unab.banca.Dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.unab.banca.Models.Administrador;

// Definición de la interfaz AdministradorDao
public interface AdministradorDao extends CrudRepository<Administrador,String> {
    //Operación de Autenticación (SELECT)
    @Transactional(readOnly=true)// Indica que la operación es de solo lectura y no afectará la integridad de la base de datos
    @Query(value="SELECT * FROM administrador WHERE id_administrador= :usuario AND clave_administrador= :clave", nativeQuery=true)
    
    // Método que realiza la autenticación de un administrador por medio de su usuario y clave
    public Administrador login(@Param("usuario") String usuario, @Param("clave") String clave); 
}
