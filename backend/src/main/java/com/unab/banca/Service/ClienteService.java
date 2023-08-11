package com.unab.banca.Service;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Controller.ClienteController;
import com.unab.banca.Dao.ClienteDao;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

//Anotacion que indica que la clase es un componente de servicio de Spring
@Service
public class ClienteService {
    //Indica que inyecta una depedencia de "ClienteDao" o sea que esta clase podra utilizar los metodos de "ClienteDao"
    @Autowired
    private ClienteDao clienteDao;

    @Transactional(readOnly=false)
    //Guardara un objeto "cliente" en la base de datos usando el metodo "save()" dado por "ClienteDao"
    public Cliente save(Cliente cliente) {
       
        //Se retorna el objeto "clienter" guardado
        return clienteDao.save(cliente);
    }

    @Transactional(readOnly=false)
    //Eliminara un objeto "cliente" en la base de datos usando el metodo "delete()" dado por "ClienteDao"
    public void delete(String id) {
        clienteDao.deleteById(id);
    }

    @Transactional(readOnly=true)
    public Cliente findById(String id) {
        //Se retorna el objeto "id" 
        return clienteDao.findById(id).orElse(null);
    }

    @Transactional(readOnly=true)
    public List<Cliente> findAll() {
        //Se retornara una lista de informacion de objetos "Cliente"
        return (List<Cliente>) clienteDao.findAll();
    }


    @Transactional(readOnly=true)
    //Se validaran los valores de "usuario" y "clave" con el metodo "login()"
    public Cliente login(String usuario, String clave) {
        //Se retornara el objeto "usuario" y "clave" validado
        return clienteDao.login(usuario, clave);
    }

    // @Transactional(readOnly = false)
    // public void registrarse(Cliente cliente){
    //     clienteDao.registrarse(cliente.getId_cliente(), cliente.getNombre_cliente(), cliente.getClave_cliente());
    // }
}
