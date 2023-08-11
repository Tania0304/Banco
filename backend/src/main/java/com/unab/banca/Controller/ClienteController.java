package com.unab.banca.Controller;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Models.Administrador;
import com.unab.banca.Security.Hash;
import com.unab.banca.Dao.ClienteDao;
import com.unab.banca.Dao.AdministradorDao;
import com.unab.banca.Service.ClienteService;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/*La anotacion indica que la clase "AdministradorController" es un controlador de Spring que manejara solicitudes HTTP y
 *devolvera los datos en formato JSON o XML y no en forma de lista*/
@RestController

//Permite que el controlador recibe solicitudes HTTP desde diferentes dominios
@CrossOrigin("*")

/*Anotacion que mapea una URL base para las solicitudes realizadas en este controlador, todas las solicitudes que comiencen
 *con "/cliente" seran manejadas por este controlador*/
@RequestMapping("/cliente")
public class ClienteController {
    //Anotador que realizara la inyeccion de las dependencias de las clases "clienteDao", "clienteService" y "administradorDao"
    @Autowired
    private ClienteDao clienteDao; 
    @Autowired
    private AdministradorDao administradorDao;
    @Autowired
    private ClienteService clienteService;
    
    @PostMapping(value="/")
    @ResponseBody
    public ResponseEntity<Cliente> agregar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario, @Valid @RequestBody Cliente cliente){   
        Administrador admon=new Administrador();
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon!=null) {
            cliente.setClave_cliente(Hash.sha1(cliente.getClave_cliente()));

            return new ResponseEntity<>(clienteService.save(cliente), HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
            
    }
    
    //Anotacion que mapea la solicitud HTTP DELETE a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/{id}"
    @DeleteMapping(value="/{id}") 
    public ResponseEntity<Cliente> eliminar(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Administrador admon=new Administrador();
        //Se autenticara el cliente con un "usuario" y "clave" por medio de "login()" de administradorDao 
        admon=administradorDao.login(usuario, Hash.sha1(clave));
       if (admon!=null) {
            //Se buscara con "findById()" un cliente con su id y se guardara el objeto "obj" 
            Cliente obj = clienteService.findById(id); 
            if(obj!=null) 
                //Si el id del cliente es encontrado se eliminara por medio del metodo "delete()"
                clienteService.delete(id);
            else 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            return new ResponseEntity<>(obj, HttpStatus.OK); 
      
       } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }
       
        
    }
    
    //Anotacion que mapea la solicitud HTTP PUT a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/"
    @PutMapping(value="/") 
    @ResponseBody
    public ResponseEntity<Cliente> editar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario,@Valid @RequestBody Cliente cliente){ 
        Administrador admon=new Administrador();
        //Se autenticara el cliente con un "usuario" y "clave" por medio de "login()" de administradorDao
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon!=null) {
            //Se establecerea la clave del cliente encriptada
            cliente.setClave_cliente(Hash.sha1(cliente.getClave_cliente()));
            //Se buscara un cliente por medio de su id
            Cliente obj = clienteService.findById(cliente.getId_cliente()); 
            if(obj!=null) { 
                //Se actualiza los datos del cliente y se guardara por medio del metodo "save()"
                obj.setNombre_cliente(cliente.getNombre_cliente());
                obj.setClave_cliente(cliente.getClave_cliente());
                clienteService.save(cliente); 
            } 
            else 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            return new ResponseEntity<>(obj, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }
   
    //Anotacion que mapea la solicitud HTTP GET a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/list"
    @GetMapping("/list") 
    @ResponseBody
    public ResponseEntity<List<Cliente>> consultarTodo(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        Administrador admon=new Administrador();
        //Se autenticara el cliente con un "usuario" y "clave" por medio de "login()" de administradorDao
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon!=null) {
            //Se mostraran todos los datos pertenecientes a los clientes, junto a una notificacion de estado OK
                return new ResponseEntity<>(clienteService.findAll(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }  
          
    }

    // @GetMapping("/list/contador")
    // @ResponseBody
    // public ResponseEntity<Long> contador(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
    //     Administrador admon=new Administrador();
    //     //Se autenticara el cliente con un "usuario" y "clave" por medio de "login()" de administradorDao
    //     admon=administradorDao.login(usuario, Hash.sha1(clave));
    //     if (admon!=null) {
    //         return new ResponseEntity<>(clienteService.countUsers(), HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    //     }  
          
    // }
    
    //Anotacion que mapea la solicitud HTTP GET a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/list{id}"
    @GetMapping("/list/{id}") 
    @ResponseBody
    public ResponseEntity<Cliente> consultaPorId(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Administrador admon=new Administrador();
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon!=null) {
            /*Por medio del "id" tomado en la ruta URL se buscara por medio de la funcion "findById" el cliente correspondiente a dicho "Id"
            y se motrara la informacion junto a una notificacion de estado OK*/ 
            return new ResponseEntity<>(clienteService.findById(id),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }   
    }

    // @RequestHeader(value = "clave", required = false, defaultValue = "valor_predeterminado")
    
    //Anotacion que mapea la solicitud HTTP GET a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/login"
    @GetMapping("/login")
    @ResponseBody
    //Se obtendran los valores de los parametros "usuario" y "clave" y se asignaran a las variable correspondientes
    public Cliente ingresar(@RequestParam ("usuario") String usuario,@RequestParam ("clave") String clave) {
        clave=Hash.sha1(clave);
        //Se pasara el "usuario" y "clave" al metodo login el cual correspondera si la autenticacion fue exitosa
        return clienteService.login(usuario, clave); 
    }
   

}

    

