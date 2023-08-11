package com.unab.banca.Dao;
import com.unab.banca.Models.Transaccion;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransaccionDao extends CrudRepository< Transaccion, Integer> {
    //Operación para seleccionar transacciones de una cuenta en particular usando el identificador "Id_cuenta" y "idcta" (SELECT)
    @Transactional(readOnly=true)//No afecta integridad base de datos, indica que solo se realizara una lectura de la BBDD
    @Query(value="SELECT * FROM transaccion WHERE id_cuenta= :idcta", nativeQuery=true)
    public List<Transaccion> consulta_transaccion(@Param("idcta") String idcta); 
    
    
    //Operación Crear transacción por depósito o retiro
    @Transactional(readOnly=false)//El valor de "readOnly" es igual a "falso", indicando que es posible modificar en la BBDD
    @Modifying// indica el metodo actual realizará una modificacion en la BBDD 
    @Query(value="INSERT INTO transaccion(fecha_transaccion,valor_transaccion,tipo_transaccion,id_cuenta) VALUES (current_date(), :valor_transaccion, :tipo, :idcta)", nativeQuery=true)
    public void crear_transaccion(
        @Param("idcta") String idcta,
        @Param("valor_transaccion") Double valor_transaccion,
        @Param("tipo") String tipo);
}
