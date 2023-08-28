package com.unab.banca.Dao;
import com.unab.banca.Models.Transaccion;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Anotación que indica que la clase es un componente de acceso a datos (DAO)
@Repository
// Definición de la interfaz TransaccionDao
public interface TransaccionDao extends CrudRepository< Transaccion, Integer> {
    //Operación para seleccionar transacciones de una cuenta en particular usando el identificador "Id_cuenta" y "idcta" (SELECT)
    @Transactional(readOnly=true)// Indica que la operación es de solo lectura y no afectará la integridad de la base de datos
    @Query(value="SELECT * FROM transaccion WHERE id_cuenta= :idcta", nativeQuery=true)
    // Método para consultar transacciones de una cuenta específica
    public List<Transaccion> consulta_transaccion(@Param("idcta") String idcta); 
    
    
    //Operación Crear transacción por depósito o retiro
    @Transactional(readOnly=false)// Indica que la operación no es de solo lectura y puede afectar la base de datos
    @Modifying// Indica que la operación modificará la base de datos
    @Query(value="INSERT INTO transaccion(fecha_transaccion,valor_transaccion,tipo_transaccion,id_cuenta) VALUES (current_date(), :valor_transaccion, :tipo, :idcta)", nativeQuery=true)
    // Método para crear una nueva transacción por depósito o retiro
    public void crear_transaccion(
        @Param("idcta") String idcta,
        @Param("valor_transaccion") Double valor_transaccion,
        @Param("tipo") String tipo);
}
