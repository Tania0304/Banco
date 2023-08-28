package com.unab.banca.Dao;
import com.unab.banca.Models.Cuenta;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Anotación que indica que la clase es un componente de acceso a datos (DAO)
@Repository
// Definición de la interfaz CuentaDao
public interface CuentaDao  extends CrudRepository<Cuenta, String> {
    //Operación para seleccionar cuentas de un cliente 
    @Transactional(readOnly=true)// Indica que la operación es de solo lectura y no afectará la integridad de la base de datos
    @Query(value="SELECT * FROM cuenta WHERE id_cliente= :idc", nativeQuery=true)
    // Método que consulta las cuentas de un cliente por medio de su ID de cliente
    public List<Cuenta> consulta_cuenta(@Param("idc") String idc); 
   
    //Operación Depósito
    @Transactional(readOnly=false)// Indica que la operación no es de solo lectura y puede afectar la base de datos
    @Modifying// Indica que la operación modificará la base de datos
    @Query(value="UPDATE cuenta SET saldo_cuenta=saldo_cuenta + :valor_deposito WHERE id_cuenta like :idcta", nativeQuery=true)
    // Método que realiza un depósito en una cuenta específica
    public void deposito(@Param("idcta") String idcta,@Param("valor_deposito") Double valor_deposito); 
    
    //Operación Retiro
    @Transactional(readOnly=false)// Indica que la operación no es de solo lectura y puede afectar la base de datos
    @Modifying// Indica que la operación modificará la base de datos
    @Query(value="UPDATE cuenta SET saldo_cuenta=saldo_cuenta - :valor_retiro WHERE id_cuenta like :idcta", nativeQuery=true)
    // Método que realiza un retiro de una cuenta específica
    public void retiro(@Param("idcta") String idcta,@Param("valor_retiro") Double valor_retiro); 
}
