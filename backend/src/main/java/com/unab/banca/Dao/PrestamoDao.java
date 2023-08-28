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

// Anotación que indica que la clase es un componente de acceso a datos (DAO)
@Repository
// Definición de la interfaz PrestamoDao
public interface PrestamoDao extends CrudRepository<Prestamo, Integer> {
    // Operación para seleccionar préstamos de un cliente en particular usando el identificador "Id_cliente" (SELECT)
    @Transactional(readOnly=true)// Indica que la operación es de solo lectura y no afectará la integridad de la base de datos
    @Query(value="SELECT * FROM prestamo WHERE id_cliente= :idc", nativeQuery=true)
    // Método para consultar un prestamo específico
    public List<Prestamo> consulta_prestamo(@Param("idc") String idc); 
    
    // Operación para consultar el valor de una cuota de un préstamo específico usando el identificador "Id_prestamo" (SELECT)
    @Transactional(readOnly=true)// Indica que la operación es de solo lectura y no afectará la integridad de la base de datos
    @Query(value="SELECT valor_cuota FROM prestamo WHERE id_prestamo= :idp", nativeQuery=true)
    // Método para consultar el valor de una cuota de un préstamo específico
    public double consulta_cuota(@Param("idp") double idp); 
    
    // Operación Crear préstamo
    @Transactional(readOnly=false)// Indica que la operación no es de solo lectura y puede afectar la base de datos
    @Modifying// Indica que la operación modificará la base de datos
    @Query(value="INSERT INTO prestamo(fecha_solicitud,saldo_solicitado,n_cuotas,valor_cuota, saldo_pendiente, id_cuenta, id_cliente) VALUES (current_date(), :saldo_solicitado, :cuotas, :valor_cuota, :saldo_pendiente, :idcta, :iduser)", nativeQuery=true)
    // Método para crear un nuevo préstamo
    public void crear_prestamo(
        @Param("idcta") String id_cuenta,
        @Param("iduser") String id_cliente,
        @Param("saldo_solicitado") Double saldo_solicitado,
        @Param("cuotas") int n_cuotas,
        @Param("valor_cuota") double valor_cuota,  
        @Param("saldo_pendiente") double saldo_pendiente);

    // Operación Depósito de cuota
    @Transactional(readOnly=false)// Indica que la operación no es de solo lectura y puede afectar la base de datos
    @Modifying// Indica que la operación modificará la base de datos
    @Query(value="UPDATE prestamo SET saldo_pendiente=saldo_pendiente - :valor_deposito_c WHERE id_prestamo like :idp", nativeQuery=true)
    // Método para realizar un depósito en una cuota de un préstamo
    public void deposito_cuota_c(@Param("idp") int idp, @Param("valor_deposito_c") Double valor_deposito_c);
    
}
