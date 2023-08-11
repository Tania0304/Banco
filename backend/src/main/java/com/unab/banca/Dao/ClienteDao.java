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


//Indica que la entidad cliente, sera utilizada para interactuar con el codigo de ClienteDao el cual administra la base de datos
@Repository 

//Se indica que la interfaz clienteDAO heredara los metodos de CrudRepository del modelo de trabajo JPA y posibilitara realizar acciones tipo CRUD en la clase Cliente
public interface ClienteDao extends CrudRepository< Cliente, String>  {
    
  

    //Operación de Autenticación (SELECT)
    @Transactional(readOnly=true)//No afecta integridad base de datos, solo generara una lectura de la BBDD
    @Query(value="SELECT * FROM cliente WHERE id_cliente= :usuario AND clave_cliente= :clave", nativeQuery=true)
    
    //"login" generara una operacion de autenticacion del usuario y clave, utilizando el @Query para comparar los datos ingresados con la BBDD
    //Metodo que establece parametros "usuario" y "cliente", los cuales pasaran su informacion a los marcadores de posicion ":usuario" y ":clave" 
    //y estos indicaran donde se colocaran los valores asignados dentro de la consulta SQL
    public Cliente login(@Param("usuario") String usuario, @Param("clave") String clave);

    
    // @Transactional(readOnly = true)
    // @Query(value = "SELECT COUNT(*) FROM cliente", nativeQuery=true)
    // public Long countUsers();
   
    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "INSERT INTO cliente (id_cliente, nombre_cliente, clave_cliente) VALUES (:usuario, :nombre, :clave)", nativeQuery = true)
    public int registrarse(@Param("usuario") String usuario, @Param("nombre") String nombre, @Param("clave") String clave);
}
