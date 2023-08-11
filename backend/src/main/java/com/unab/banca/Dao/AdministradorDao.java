package com.unab.banca.Dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.unab.banca.Models.Administrador;

public interface AdministradorDao extends CrudRepository<Administrador,String> {
    //Operación de Autentiiicación (SELECT)
    @Transactional(readOnly=true)//No afecta integridad base de datos
    @Query(value="SELECT * FROM administrador WHERE id_administrador= :usuario AND clave_administrador= :clave", nativeQuery=true)
    public Administrador login(@Param("usuario") String usuario, @Param("clave") String clave); 
}
