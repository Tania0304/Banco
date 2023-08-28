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
    // Anotación que mapea la solicitud HTTP POST a un método controlador. Cada vez que se invoque una solicitud a la raíz ("/"). 
    @PostMapping(value="/")
    //Indica que el valor de retorno del metodo debe ser devuelto como la respuesta HTTP 
    @ResponseBody
    //Esto define un método público llamado "agregar" que toma tres parámetros: "clave", "usuario" y "cliente". "clave" y "usuario" se obtendrán de los encabezados de la solicitud HTTP, y "cliente" se obtendrá del cuerpo de la solicitud.
    public ResponseEntity<Cliente> agregar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario, @Valid @RequestBody Cliente cliente){
        //Se crea una instancia de la clase "Administrador" llamada admon   
        Administrador admon=new Administrador();
        // Autenticación del administrador usando los valores "usuario" y "clave"
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon!=null) {
            // Encripta la clave del cliente antes de guardarla
            cliente.setClave_cliente(Hash.sha1(cliente.getClave_cliente()));

            // Guarda el cliente y devuelve una respuesta con un estado OK
            return new ResponseEntity<>(clienteService.save(cliente), HttpStatus.OK); 
        } else {
            // Devuelve una respuesta con estado UNAUTHORIZED si la autenticación falla
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
            
    }
    
    //Anotacion que mapea la solicitud HTTP DELETE a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/{id}"
    @DeleteMapping(value="/{id}")
    /*El metodo "eliminar" utiliza los parametros "clave" y "usuario" para realizar la solicitud*/ 
    public ResponseEntity<Cliente> eliminar(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        //Se crea una instancia de la clase "Administrador" llamada admon
        Administrador admon=new Administrador();
        //Se invoca el metodo login de administradorDao y se verifica la autenticacion del adminitrador y se asigna al objeto "admon"
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon!=null) {
            //Se invoca el metodo "findById()" de "clienteServices" para buscar el cliente por su id, si se encuentra se asigna a "obj"
            Cliente obj = clienteService.findById(id); 
            //Si obj no es null se procedera a borrar el cliente con el "id" proprocionado, si es null, se mostrara un mensaje de ERROR
            if(obj!=null) 
                //Si el id del cliente es encontrado se eliminara por medio del metodo "delete()"
                clienteService.delete(id);
            else
                //Si "obj" es null (no se encontro el id del cliente), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR  
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            //SI la eliminacion se realiza correctamente se devolvera una respuesta HTTP y el estado OK
            return new ResponseEntity<>(obj, HttpStatus.OK); 
      
       } else {
            //Si al autenticacion del administrador falla se mostrara un mensaje de estado UNAUTHORIZED
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }
       
        
    }
    
    //Anotacion que mapea la solicitud HTTP PUT a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/"
    @PutMapping(value="/")
    //Indica que el valor de retorno del metodo debe ser devuelto como la respuesta HTTP 
    @ResponseBody
    //El metodo "editar" maneja la solicitud PUT para editar los datos de un cliente, este utiliza los parametros "clave" y "usuario" y un objeto "cliente" para realizar la solicitud
    public ResponseEntity<Cliente> editar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario,@Valid @RequestBody Cliente cliente){ 
        //Se crea una instancia de la clase "Administrador" llamada admon
        Administrador admon=new Administrador();
        //Se autenticara el cliente con un "usuario" y "clave" por medio de "login()" de administradorDao
        admon=administradorDao.login(usuario, Hash.sha1(clave));
         //Se verifica que el admninistrador se autentica exitosamente 
        if (admon!=null) {
            //Por medio de "Hash.sha1" se encripta "clave_cliente" del objeto "cliente"
            cliente.setClave_cliente(Hash.sha1(cliente.getClave_cliente()));
            //Se invoca el metodo "findById" para encontrar un cliente con el "id" proporcionado
            Cliente obj = clienteService.findById(cliente.getId_cliente()); 
            if(obj!=null) { 
                //Se actualiza el valor de "nombre_cliente"
                obj.setNombre_cliente(cliente.getNombre_cliente());
                 //Se actualiza el valor de "clave_cliente"
                obj.setClave_cliente(cliente.getClave_cliente());
                //Se guarda el objeto "cliente" actulizado en la BBDD
                clienteService.save(cliente); 
            } 
            else
            //Si "obj" es null (no se encontro el id del cliente), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR  
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
    public ResponseEntity<List<Cliente>> consultarTodo(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        Administrador admon=new Administrador();
        //Se autenticara el cliente con un "usuario" y "clave" por medio de "login()" de administradorDao
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon!=null) {
            //Se mostraran todos los datos pertenecientes a los clientes, junto a una notificacion de estado OK
                return new ResponseEntity<>(clienteService.findAll(),HttpStatus.OK);
        } else {
            // Si la autenticación falla, se retorna una respuesta de estado UNAUTHORIZED (no autorizado)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }  
          
    }
    
    //Se utiliza la anotación @GetMapping para mapear una solicitud HTTP GET a este método. El segmento {id} en la anotación de mapeo indica que se espera un valor de id en la URL.
    @GetMapping("/list/{id}")
    //Esta anotación indica que el resultado del método se utilizará directamente como el cuerpo de la respuesta HTTP. 
    @ResponseBody
    public ResponseEntity<Cliente> consultaPorId(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        // Se crea una instancia de Administrador para almacenar el resultado de la autenticación
        Administrador admon=new Administrador();
        //Se autentica el adminitrador usando los valores "usuario" y "clave"
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        // Se verifica si la autenticación fue exitosa
        if (admon!=null) {
            // Si el administrador está autenticado, se busca un cliente por su ID y se retorna su información con una respuesta de estado OK
            return new ResponseEntity<>(clienteService.findById(id),HttpStatus.OK);
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
    public Cliente ingresar(@RequestParam ("usuario") String usuario,@RequestParam ("clave") String clave) {
        //Aquí se aplica una función de hash SHA-1 a la clave proporcionada. La clave se encripta antes de ser utilizada en la autenticación.
        clave=Hash.sha1(clave);
        //Finalmente, se llama al método "login" del servicio "clienteService" y se pasan los valores de "usuario" y "clave" como argumentos. Este método verificará si la autenticación es exitosa y devolverá un objeto de tipo "Cliente".
        return clienteService.login(usuario, clave); 
    }

}

    

