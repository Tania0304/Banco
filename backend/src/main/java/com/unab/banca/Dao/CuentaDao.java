package com.unab.banca.Dao;
import com.unab.banca.Models.Cuenta;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
//Al realizar una extends de CrudRespository se establece que se utilizara la entidad "Cuenta" y el "ID" definido en la tabla es de tipo String
public interface CuentaDao  extends CrudRepository<Cuenta, String> {
    //Operación para seleccionar cuentas de un cliente 
    //Se busca un cliente en particular por medio del campo idc = id del cliente, dentro de la tabla cuenta(SELECT)
    @Transactional(readOnly=true)//No afecta integridad base de datos, la operacion solo realizara lectura en la base de datos
    @Query(value="SELECT * FROM cuenta WHERE id_cliente= :idc", nativeQuery=true)
    public List<Cuenta> consulta_cuenta(@Param("idc") String idc); 
   
    //Operación Depósito
    //Se actualiza el valor de la cuenta, sumando el saldo y el deposito, siempre y cuando el id de la cuenta coincidan en la informacion sumistrada por el cliente
    @Transactional(readOnly=false)//El valor de "readOnly" es igual a "falso", indicando que es posible modificar en la BBDD
    @Modifying// indica el metodo actual realizará una modificacion en la BBDD 
    @Query(value="UPDATE cuenta SET saldo_cuenta=saldo_cuenta + :valor_deposito WHERE id_cuenta like :idcta", nativeQuery=true)
    public void deposito(@Param("idcta") String idcta,@Param("valor_deposito") Double valor_deposito); 
    
    //Operación Retiro
    //Se actualiza el valor de la cuenta, restando el saldo y el vamor de retiro de la cuenta especificada con el id de la cuenta (id_cuenta = idcta)
    @Transactional(readOnly=false)
    @Modifying// indica el metodo actual realizará una modificacion en la BBDD 
    @Query(value="UPDATE cuenta SET saldo_cuenta=saldo_cuenta - :valor_retiro WHERE id_cuenta like :idcta", nativeQuery=true)
    public void retiro(@Param("idcta") String idcta,@Param("valor_retiro") Double valor_retiro); 
}
