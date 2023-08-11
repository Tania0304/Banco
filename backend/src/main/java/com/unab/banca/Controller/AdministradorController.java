package com.unab.banca.Controller;
import com.unab.banca.Models.Administrador;
import com.unab.banca.Security.Hash;
import com.unab.banca.Dao.AdministradorDao;
import com.unab.banca.Service.AdministradorService;
import java.util.List;

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
 *con "/administrador" seran manejadas por este controlador*/
@RequestMapping("/administrador")
public class AdministradorController {
    //Anotador que realizara la inyeccion de las dependencias de las clases "administradorDao" y "administradorService"
    @Autowired
    private AdministradorDao administradorDao; 
    @Autowired
    private AdministradorService administradorService;
    
    //Anotacion que mapea una solicitud POST
    @PostMapping(value="/")
    
    //Indica que el valor de retorno del metodo debe ser devuelto como la respuesta HTTP
    @ResponseBody

    //El objeto "ResponseEntity<Administrador>" se serializa en formato JSON y se envia como respuesta
    /*El metodo "agregar" maneja la solicitud POST para agregar un nuevo administrador
    utiliza los parametros "clave" y "usuario" y in objeto "administrador" para realizar la solicitud
    Confirma la autenticacion del administrador por medio del metodo "login()" de administradorDao
    Si la autenticacion es exitosa se guarda el adminitrador en la base datos utilizando el servicio "AdministradorService"
    y se genera una respuesta de estado OK, si la autenticacion falla se envia un estado de ERROR
    */
    public ResponseEntity<Administrador> agregar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario, @Valid @RequestBody Administrador administrador){   
        Administrador admon1=new Administrador();
        admon1=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon1!=null) {
            return new ResponseEntity<>(administradorService.save(administrador), HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
            
    }
   
    //Anotacion que mapea la solicitud HTTP DELETE a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/{id}"
    @DeleteMapping(value="/{id}")
    //@PathVariable indica que el valor del parametro "id" se obtendra de la URL
    public ResponseEntity<Administrador> eliminar(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        
        Administrador objadm=new Administrador();
        //Se invoca el metodo login de administradorDao y se verifica la autenticacion del adminitrador y se asigna al objeto "objadm"
        objadm=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (objadm!=null) {
            //Se invoca el metodo "findById()" de "administradorServices" para buscar el administrado por su id, si se encuentra se asigna a "obj"
            Administrador obj = administradorService.findById(id); 
            //Si obj no es null se procedera a borrar el administrador con el "id" proprocionado, si no, se mostrara un mensaje de ERROR
            if(obj!=null) 
                administradorService.delete(id);
            else 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            //SI la eliminacion se realiza correctamente se devolvera una respuesta HTTP y el estado OK
            return new ResponseEntity<>(obj, HttpStatus.OK); 
      
       } else {
            //Si al autenticacion del administrador falla se mostrara un mensaje de estado UNAUTHORIZED
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }
       
        
    }
    
    //Anotacion que mapea la solicitud HTTP EDITE a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/"
    @PutMapping(value="/") 
    @ResponseBody
    public ResponseEntity<Administrador> editar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario,@Valid @RequestBody Administrador administrador){ 
        //Se crea una instancia de la clase "Administrador" llamda admon1
        Administrador admon1=new Administrador();
        
        //Se autentica el adminitrador usando los valores "usuario" y "clave"
        admon1=administradorDao.login(usuario, Hash.sha1(clave));
        
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon1!=null) {
            
            //Por medio de has se encripta la "clave_administrador" del objeto "administrador"
            administrador.setClave_administrador(Hash.sha1(administrador.getClave_administrador()));
            
            //Se invoca el metodo "findById" para enceontrar un administrador con el "id" proporcionado
            Administrador obj = administradorService.findById(administrador.getId_administrador()); 
            if(obj!=null) { 
                //Se actualiza el valor de "nombre_administrador"
                obj.setNombre_administrador(administrador.getNombre_administrador());
                //Se actualiza el valor de "clave_administrador"
                obj.setClave_administrador(administrador.getClave_administrador());
                //Se guarda el objeto "administrador" actulizado en la BBDD
                administradorService.save(administrador); 
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
    public ResponseEntity<List<Administrador>> consultarTodo(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        Administrador administrador=new Administrador();
        //Se autentica el adminitrador usando los valores "usuario" y "clave"
        administrador=administradorDao.login(usuario, Hash.sha1(clave));
        if (administrador!=null) {
            //Se busca todos los administradores, se volvera la informacion de estos y una respuesta de estado OK
            return new ResponseEntity<>(administradorService.findAll(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }  
          
    }
    
    //Anotacion que mapea la solicitud HTTP GET a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/list{id}"
    @GetMapping("/list/{id}") 
    @ResponseBody
    public ResponseEntity<Administrador> consultaPorId(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Administrador administrador=new Administrador();
        //Se autentica el adminitrador usando los valores "usuario" y "clave"
        administrador=administradorDao.login(usuario, Hash.sha1(clave));
        if (administrador!=null) {
            //Se busca un administrador por su id y se volvera la informacion de este y una respuesta de estado OK
            return new ResponseEntity<>(administradorService.findById(id),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }   
    }
    
    //Anotacion que mapea la solicitud HTTP GET a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/login"
    @GetMapping("/login")
    @ResponseBody
    //Se obtendran los valores de los parametros "usuario" y "clave" y se asignaran a las variable correspondientes
    public Administrador ingresar(@RequestParam ("usuario") String usuario,@RequestParam ("clave") String clave) {
        //La clave por medio de sha1 se encriptara
        clave=Hash.sha1(clave);
        //Se pasara el "usuario" y "clave" al metodo login el cual correspondera si la autenticacion fue exitosa
        return administradorService.login(usuario, clave);
    }
}
