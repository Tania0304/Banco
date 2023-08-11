package com.unab.banca.Service;
import com.unab.banca.Models.Administrador;
import com.unab.banca.Dao.AdministradorDao;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

//Anotacion que indica que la clase es un componente de servicio de Spring
@Service
public class AdministradorService {
    //Indica que inyecta una depedencia de "AdministracionDao" o sea que esta clase podra utilizar los metodos de "AdministracioDao"
    @Autowired
    private AdministradorDao administradorDao;

    @Transactional(readOnly=false)
    //Guardara un objeto "administrador" en la base de datos usando el metodo "save()" dado por "AdministradorDao"
    public Administrador save(Administrador administrador) {
        //Se retorna el objeto "administrador" guardado
        return administradorDao.save(administrador);
    }

    @Transactional(readOnly=false)
    //Eliminara un objeto "administrador" en la base de datos usando el metodo "delete()" dado por "AdministradorDao"
    public void delete(String id) {
        administradorDao.deleteById(id);
    }

    @Transactional(readOnly=true)
    //Se buscara un objeto "administrador" de la base de datos por medio del "id" usando el metodo "finById()" dado por "AdministradorDao"
    public Administrador findById(String id) {
        //Se retorna el objeto "id" 
        return administradorDao.findById(id).orElse(null);
    }

    @Transactional(readOnly=true)
    public List<Administrador> findAll() {
        //Se retornara una lista de informacion de objetos "administrador"
        return (List<Administrador>) administradorDao.findAll();
    }

    @Transactional(readOnly=true)
    //Se validaran los valores de "usuario" y "clave" con el metodo "login()"
    public Administrador login(String usuario, String clave) {
        //Se retornara el objeto "usuario" y "clave" validado
        return administradorDao.login(usuario, clave);
    }

}
