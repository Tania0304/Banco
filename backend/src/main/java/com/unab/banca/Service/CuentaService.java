package com.unab.banca.Service;
import com.unab.banca.Models.Cuenta;
import com.unab.banca.Dao.CuentaDao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

//Anotacion que indica que la clase es un componente de servicio de Spring
@Service
public class CuentaService {
    
    //Indica que inyecta una depedencia de "CuentaDao" o sea que esta clase podra utilizar los metodos de "CuentaDao"
    @Autowired
    private CuentaDao cuentaDao;
    
    @Transactional(readOnly=false)
    //Guardara un objeto "cuenta" en la base de datos usando el metodo "save()" dado por "cuentaDao"
    public Cuenta save(Cuenta cuenta) {
        //Se retorna el objeto "cuenta" guardado
        return cuentaDao.save(cuenta);
    }
    @Transactional(readOnly=false)
    //Eliminara un objeto "cuenta" en la base de datos usando el metodo "delete()" dado por "cuentaDao"
    public void delete(String id) {
        cuentaDao.deleteById(id);;
    }
    @Transactional(readOnly=true)
    //Se buscara un objeto "cuenta" de la base de datos por medio del "id" usando el metodo "finById()" dado por "AdministradorDao"
    public Cuenta findById(String id) {
       return cuentaDao.findById(id).orElse(null);
    }
    @Transactional(readOnly=true)
    //Buscara y realizara una lista del objeto cuenta
    public List<Cuenta> findByAll() {
        //Se retornara una lista de informacion de objetos "cuenta"
        return (List<Cuenta>) cuentaDao.findAll();
    }
    @Transactional(readOnly=true)
    //Buscara y realizara una lista del objeto cuenta iddentificados por medio del id de cuenta
    public List<Cuenta> consulta_cuenta(String idc) {
        //Se retornara una lista de informacion de objetos "cuenta" especificados por el id de cuenta
        return (List<Cuenta>) cuentaDao.consulta_cuenta(idc);
    }

    @Transactional(readOnly=false)
    //Actualizara los datos de cuenta a partir del valor_deposito filtrados por medio del id de la tabla "cuenta"
    public void deposito(String idcta,Double valor_deposito) {
        cuentaDao.deposito(idcta, valor_deposito);
    }

    @Transactional(readOnly=false)
    //Actualizara los datos de cuenta a partir del valor_retiro filtrados por medio del id de la tabla "cuenta"
    public void retiro(String idcta,Double valor_retiro) {
        cuentaDao.retiro(idcta, valor_retiro);
    }
}
