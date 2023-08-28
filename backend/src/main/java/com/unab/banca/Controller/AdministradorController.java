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
    utiliza los parametros "clave" y "usuario" y un objeto "administrador" para realizar la solicitud
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
    /*El metodo "eliminar" utiliza los parametros "clave" y "usuario" para realizar la solicitud*/
    public ResponseEntity<Administrador> eliminar(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        //Se crea una instancia de la clase "Administrador" llamada objadm
        Administrador objadm=new Administrador();
        //Se invoca el metodo login de administradorDao y se verifica la autenticacion del adminitrador y se asigna al objeto "objadm"
        objadm=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (objadm!=null) {
            //Se invoca el metodo "findById()" de "administradorServices" para buscar el administrador por su id, si se encuentra se asigna a "obj"
            Administrador obj = administradorService.findById(id); 
            //Si obj no es null se procedera a borrar el administrador con el "id" proprocionado, si es null, se mostrara un mensaje de ERROR
            if(obj!=null)
                //Si el id del administrador es encontrado se eliminara por medio del metodo "delete()" 
                administradorService.delete(id);
            else 
                //Si "obj" es null (no se encontro el id del administrador), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR 
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
    
    //Indica que el valor de retorno del metodo debe ser devuelto como la respuesta HTTP
    @ResponseBody
    //El metodo "editar" maneja la solicitud PUT para editar los datos de un administrador, este utiliza los parametros "clave" y "usuario" y un objeto "administrador" para realizar la solicitud
    public ResponseEntity<Administrador> editar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario,@Valid @RequestBody Administrador administrador){ 
        //Se crea una instancia de la clase "Administrador" llamada admon1
        Administrador admon1=new Administrador();
        
        //Se autentica el adminitrador usando los valores "usuario" y "clave"
        admon1=administradorDao.login(usuario, Hash.sha1(clave));
        
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon1!=null) {
            
            //Por medio de "Hash.sha1" se encripta "clave_administrador" del objeto "administrador"
            administrador.setClave_administrador(Hash.sha1(administrador.getClave_administrador()));
            
            //Se invoca el metodo "findById" para encontrar un administrador con el "id" proporcionado
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
                //Si "obj" es null (no se encontro el id del administrador), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
            //SI la edición se realiza correctamente se devolvera una respuesta HTTP y el estado OK 
            return new ResponseEntity<>(obj, HttpStatus.OK); 
        } else {
            //Si al autenticacion del administrador falla se mostrara un mensaje de estado UNAUTHORIZED
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }
   
    //Anotacion que mapea la solicitud HTTP GET a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/list"
    @GetMapping("/list") 
    //Indica que el valor de retorno del metodo debe ser devuelto como la respuesta HTTP
    @ResponseBody
    //Se recibe la información de autenticación (clave y usuario) en los encabezados de la solicitud HTTP mediante las anotaciones @RequestHeader
    public ResponseEntity<List<Administrador>> consultarTodo(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        //Se crea una instancia de la clase Administrador para almacenar los resultados de la autenticación.
        Administrador administrador=new Administrador();
        //Se autentica el adminitrador usando los valores "usuario" y "clave"
        administrador=administradorDao.login(usuario, Hash.sha1(clave));
        // Se verifica si la autenticación fue exitosa
        if (administrador!=null) {
            //Se busca todos los administradores, se volvera la informacion de estos y una respuesta de estado OK
            return new ResponseEntity<>(administradorService.findAll(),HttpStatus.OK);
        } else {
            // Si la autenticación falla, se retorna una respuesta de estado UNAUTHORIZED (no autorizado)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }  
          
    }
  
    //Se utiliza la anotación @GetMapping para mapear una solicitud HTTP GET a este método. El segmento {id} en la anotación de mapeo indica que se espera un valor de id en la URL.
    @GetMapping("/list/{id}")
    //Esta anotación indica que el resultado del método se utilizará directamente como el cuerpo de la respuesta HTTP. 
    @ResponseBody
    public ResponseEntity<Administrador> consultaPorId(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        // Se crea una instancia de Administrador para almacenar el resultado de la autenticación
        Administrador administrador=new Administrador();
        //Se autentica el adminitrador usando los valores "usuario" y "clave"
        administrador=administradorDao.login(usuario, Hash.sha1(clave));
        // Se verifica si la autenticación fue exitosa
        if (administrador!=null) {
        // Si el administrador está autenticado, se busca un administrador por su ID y se retorna su información con una respuesta de estado OK
            return new ResponseEntity<>(administradorService.findById(id),HttpStatus.OK);
        } else {
            // Si la autenticación falla, se retorna una respuesta de estado UNAUTHORIZED (no autorizado)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }   
    }
    
    //Esta anotación indica que este método responderá a las solicitudes HTTP GET en la ruta "/login".
    @GetMapping("/login")
    //Esta anotación indica que el resultado del método se utilizará directamente como el cuerpo de la respuesta HTTP.
    @ResponseBody
    //Se obtendran los valores de los parametros "usuario" y "clave" y se asignaran a las variable correspondientes
    public Administrador ingresar(@RequestParam ("usuario") String usuario,@RequestParam ("clave") String clave) {
        //Aquí se aplica una función de hash SHA-1 a la clave proporcionada. La clave se encripta antes de ser utilizada en la autenticación.
        clave=Hash.sha1(clave);
        //Finalmente, se llama al método "login" del servicio "administradorService" y se pasan los valores de "usuario" y "clave" como argumentos. Este método verificará si la autenticación es exitosa y devolverá un objeto de tipo "Administrador".
        return administradorService.login(usuario, clave);
    }
}
