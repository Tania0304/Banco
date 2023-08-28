package com.unab.banca.Dao;
import com.unab.banca.Models.Cliente;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


// Anotación que indica que la clase es un componente de acceso a datos (DAO)
@Repository 

// Definición de la interfaz ClienteDao
public interface ClienteDao extends CrudRepository< Cliente, String>  {
    
  

    //Operación de Autenticación (SELECT)
    @Transactional(readOnly=true)// Indica que la operación es de solo lectura y no afectará la integridad de la base de datos
    @Query(value="SELECT * FROM cliente WHERE id_cliente= :usuario AND clave_cliente= :clave", nativeQuery=true)
    // Método que realiza la autenticación de un cliente por medio de su usuario y clave
    public Cliente login(@Param("usuario") String usuario, @Param("clave") String clave);

   // Operación de Registro (INSERT)
    @Transactional(readOnly = false) // Indica que la operación no es de solo lectura y puede afectar la base de datos
    @Modifying // Indica que la operación modificará la base de datos
    @Query(value = "INSERT INTO cliente (id_cliente, nombre_cliente, clave_cliente) VALUES (:usuario, :nombre, :clave)", nativeQuery = true)
    // Método que permite a un cliente registrarse en la base de datos
    public int registrarse(@Param("usuario") String usuario, @Param("nombre") String nombre, @Param("clave") String clave);
}
