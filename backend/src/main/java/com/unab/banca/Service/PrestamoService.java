package com.unab.banca.Service;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Models.Prestamo;
import com.unab.banca.Dao.PrestamoDao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

//Anotacion que indica que la clase es un componente de servicio de Spring
@Service
public class PrestamoService {
    
    //Indica que inyecta una depedencia de "PrestamoDao" o sea que esta clase podra utilizar los metodos de "PrestamoDao"
    @Autowired
    private PrestamoDao prestamoDao;
    

    @Transactional(readOnly=false)
    //Guardara un objeto "prestamo" en la base de datos usando el metodo "save()" dado por "prestamoDao"
    public Prestamo save(Prestamo prestamo) {
        //Se retorna el objeto "prestamo" guardado
        return prestamoDao.save(prestamo);
    }

    @Transactional(readOnly=false)
    //Eliminara un objeto "prestamo" en la base de datos usando el metodo "delete()" dado por "prestamoDao"
    public void delete(int id) {
        prestamoDao.deleteById(id);;
    }
    @Transactional(readOnly=true)
    //Se buscara un objeto "pretsamo" de la base de datos por medio del "id" usando el metodo "finById()" dado por "PrestamoDao"
    public Prestamo findById(int id) {
       return (Prestamo) prestamoDao.findById(id).orElse(null);
    }
    @Transactional(readOnly=true)
    //Buscara y realizara una lista del objeto "prestamo"
    public List<Prestamo> findByAll() {
        //Se retornara una lista de informacion de objetos "prestamo"
        return (List<Prestamo>) prestamoDao.findAll();
    }
    @Transactional(readOnly=true)
    // Busca y devuelve una lista de objetos "prestamo" identificados por el "idc" de cuenta
    public List<Prestamo> consulta_prestamo(String idc) {
        //Se retornara una lista de informacion de objetos "cuenta" especificados por el id de cuenta
        return (List<Prestamo>) prestamoDao.consulta_prestamo(idc);
    }

    @Transactional(readOnly=false)
    // Actualiza los datos de un préstamo a partir del "valor_deposito_c" filtrados por su "idp"
    public void deposito_cuota_c(int idp, Double valor_deposito_c) {
        prestamoDao.deposito_cuota_c(idp, valor_deposito_c);
    }

    @Transactional(readOnly=false)
    // Crea un nuevo préstamo en la base de datos con los valores especificados
    public void crear_prestamo(String idcta, String iduser,Double saldo_solicitado, int cuotas, double valor_cuota, double saldo_pendiente) {
        prestamoDao.crear_prestamo(idcta, iduser, saldo_solicitado, cuotas, valor_cuota, saldo_pendiente);
    }

    // Consulta el valor de una cuota de un préstamo específico identificado por su "idp"
    @Transactional(readOnly = true)
    public int valor_cuota(double idp){
        return (int) prestamoDao.consulta_cuota(idp);
    } 


}
