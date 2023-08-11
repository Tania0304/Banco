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
    //Guardara un objeto "cuenta" en la base de datos usando el metodo "save()" dado por "cuentaDao"
    public Prestamo save(Prestamo prestamo) {
        //Se retorna el objeto "cuenta" guardado
        return prestamoDao.save(prestamo);
    }

    @Transactional(readOnly=false)
    //Eliminara un objeto "cuenta" en la base de datos usando el metodo "delete()" dado por "cuentaDao"
    public void delete(int id) {
        prestamoDao.deleteById(id);;
    }
    @Transactional(readOnly=true)
    //Se buscara un objeto "cuenta" de la base de datos por medio del "id" usando el metodo "finById()" dado por "AdministradorDao"
    public Prestamo findById(int id) {
       return (Prestamo) prestamoDao.findById(id).orElse(null);
    }
    @Transactional(readOnly=true)
    //Buscara y realizara una lista del objeto cuenta
    public List<Prestamo> findByAll() {
        //Se retornara una lista de informacion de objetos "cuenta"
        return (List<Prestamo>) prestamoDao.findAll();
    }
    @Transactional(readOnly=true)
    //Buscara y realizara una lista del objeto cuenta iddentificados por medio del id de cuenta
    public List<Prestamo> consulta_prestamo(String idc) {
        //Se retornara una lista de informacion de objetos "cuenta" especificados por el id de cuenta
        return (List<Prestamo>) prestamoDao.consulta_prestamo(idc);
    }

    @Transactional(readOnly=false)
    //Actualizara los datos de cuenta a partir del valor_deposito filtrados por medio del id de la tabla "cuenta"
    public void deposito_cuota_c(int idp, Double valor_deposito_c) {
        prestamoDao.deposito_cuota_c(idp, valor_deposito_c);
    }

    @Transactional(readOnly=false)
    public void crear_prestamo(String idcta, String iduser,Double saldo_solicitado, int cuotas, double valor_cuota, double saldo_pendiente) {
        prestamoDao.crear_prestamo(idcta, iduser, saldo_solicitado, cuotas, valor_cuota, saldo_pendiente);
    }

    @Transactional(readOnly = true)
    public int valor_cuota(double idp){
        return (int) prestamoDao.consulta_cuota(idp);
    } 


}
