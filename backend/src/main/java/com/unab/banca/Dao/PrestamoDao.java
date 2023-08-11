package com.unab.banca.Dao;
import com.unab.banca.Models.Prestamo;
import com.unab.banca.Models.Transaccion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrestamoDao extends CrudRepository<Prestamo, Integer> {
    //Operación para seleccionar transacciones de una cuenta en particular usando el identificador "Id_cuenta" y "idcta" (SELECT)
    @Transactional(readOnly=true)//No afecta integridad base de datos, indica que solo se realizara una lectura de la BBDD
    @Query(value="SELECT * FROM prestamo WHERE id_cliente= :idc", nativeQuery=true)
    public List<Prestamo> consulta_prestamo(@Param("idc") String idc); 
    
    @Transactional(readOnly=true)//No afecta integridad base de datos, indica que solo se realizara una lectura de la BBDD
    @Query(value="SELECT valor_cuota FROM prestamo WHERE id_prestamo= :idp", nativeQuery=true)
    public double consulta_cuota(@Param("idp") double idp); 
    
    //Operación Crear transacción por depósito o retiro
    @Transactional(readOnly=false)//El valor de "readOnly" es igual a "falso", indicando que es posible modificar en la BBDD
    @Modifying// indica el metodo actual realizará una modificacion en la BBDD 
    @Query(value="INSERT INTO prestamo(fecha_solicitud,saldo_solicitado,n_cuotas,valor_cuota, saldo_pendiente, id_cuenta, id_cliente) VALUES (current_date(), :saldo_solicitado, :cuotas, :valor_cuota, :saldo_pendiente, :idcta, :iduser)", nativeQuery=true)
    
    public void crear_prestamo(
        @Param("idcta") String id_cuenta,
        @Param("iduser") String id_cliente,
        @Param("saldo_solicitado") Double saldo_solicitado,
        @Param("cuotas") int n_cuotas,
        @Param("valor_cuota") double valor_cuota,  
        @Param("saldo_pendiente") double saldo_pendiente);


    @Transactional(readOnly=false)//El valor de "readOnly" es igual a "falso", indicando que es posible modificar en la BBDD
    @Modifying// indica el metodo actual realizará una modificacion en la BBDD 
    @Query(value="UPDATE prestamo SET saldo_pendiente=saldo_pendiente - :valor_deposito_c WHERE id_prestamo like :idp", nativeQuery=true)
    public void deposito_cuota_c(@Param("idp") int idp, @Param("valor_deposito_c") Double valor_deposito_c);

    // @Transactional(readOnly=false)
    // @Modifying// indica el metodo actual realizará una modificacion en la BBDD 
    // @Query(value="UPDATE prestamo SET saldo_pendiente=saldo_pendiente - :valor_retiro_p WHERE id_prestamo like :idp", nativeQuery=true)
    // public void retiro(@Param("idp") int idp, @Param("valor_retiro_p") Double valor_retiro_p);

    
    
}
