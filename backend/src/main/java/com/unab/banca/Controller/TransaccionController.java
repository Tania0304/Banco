package com.unab.banca.Controller;
import com.unab.banca.Models.Transaccion;
import com.unab.banca.Security.Hash;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Dao.TransaccionDao;
import com.unab.banca.Dao.ClienteDao;
import com.unab.banca.Service.TransaccionService;
import java.util.List;
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

// Anotación que indica que esta clase es un controlador en Spring
@RestController

// Anotación que permite las solicitudes desde cualquier origen, indicado por "*".
@CrossOrigin("*")

// Anotación que establece la ruta base para las solicitudes en este controlador.
@RequestMapping("/transaccion")
public class TransaccionController {

    // Inyección de dependencia del objeto TransaccionDao.
    @Autowired
    private TransaccionDao transaccionDao;
    
    // Inyección de dependencia del objeto ClienteDao.
    @Autowired
    private ClienteDao clienteDao;
    
    // Inyección de dependencia del objeto TransaccionService.
    @Autowired
    private TransaccionService transaccionService;
    
    // Anotación que indica que este método maneja solicitudes HTTP POST
    @PostMapping(value="/")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<Transaccion> agregar(@RequestBody Transaccion transaccion){   
        //Se invoca el metodo "save()" para guardar la transaccion en la BBDD
        Transaccion obj = transaccionService.save(transaccion);
        //Se devuelve una respuesta con los datos del objeto "Transaccion" y un informe de estado OK
        return new ResponseEntity<>(obj, HttpStatus.OK);     
    }

    // Anotación que indica que este método manejará peticiones HTTP PUT en la URL "/crear_transaccion"
    @PostMapping(value="/crear_transaccion") 
    public void crear_transaccion(@RequestParam ("idcta") String idcta,@RequestParam ("valor_transaccion") Double valor_transaccion,@RequestParam ("tipo") String tipo,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Cliente cliente1=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        // El método "login" parece verificar las credenciales del cliente
        cliente1=clienteDao.login(usuario, Hash.sha1(clave));
        // Se verifica que el cliente se autentique exitosamente
        if (cliente1!=null) {
            // Se registrará una nueva transacción con los valores proporcionados
           transaccionService.cear_transaccion(idcta, valor_transaccion, tipo);
        }
          
    }

    //Anotacion que mapea la solicitud HTTP DELETE a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/{id}"
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Transaccion> eliminar(@PathVariable Integer id){ 
        //Por medio del metodo "findId()" se buscara y una transaccion especifica y se guardara en el objeto "obj"
        Transaccion obj = transaccionService.findById(id); 
        // Si se encuentra una transaccion con el ID especificado
        if(obj!=null) 
            //Se eliminara la transaccion correspondiente al "id" indicado
            transaccionService.delete(id);
        else 
            //Si "obj" es null (no se encontro el id de la transaccion), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR 
            return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
        //Se mostrara los datos del prestamo borrado y un mensaje de estado OK 
        return new ResponseEntity<>(obj, HttpStatus.OK); 
    }

    // Anotación que mapea la solicitud HTTP PUT a un método controlador, cada vez que se invoque una solicitud a la ruta "/"
    @PutMapping(value="/") 
    public ResponseEntity<Transaccion> editar(@RequestBody Transaccion transaccion){ 
        //Se busca una transaccion especifica por su id
        Transaccion obj = transaccionService.findById(transaccion.getId_transaccion()); 
        // Si se encuentra una transacción con el ID especificado
        if(obj!=null) {
            //Se actualiza el valor, fecha y cuenta de la transaccion y se guarda por medio del metodo save()
           obj.setValor_transaccion(transaccion.getValor_transaccion());
           obj.setFecha_transaccion(transaccion.getFecha_transaccion());
           obj.setCuenta(transaccion.getCuenta());
           transaccionService.save(obj);
        } 
        else
            // Si "obj" es null (no se encontró la transacción), se devuelve una respuesta HTTP con un mensaje de estado INTERNAL_SERVER_ERROR 
            return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
        // Se devuelve la transacción actualizada en el cuerpo de la respuesta con un estado HTTP OK (200)    
        return new ResponseEntity<>(obj, HttpStatus.OK); 
    }

    // Método que maneja una petición HTTP GET en la ruta "/list"
    @GetMapping("/list") 
    public List<Transaccion> consultarTodo(){
        //Se mostratan todas las transacciones guardadas en la base de datos
        return transaccionService.findByAll(); 
    }

    // Método que maneja una petición HTTP GET en la ruta "/list/{id}"
    @GetMapping("/list/{id}") 
    public Transaccion consultaPorId(@PathVariable Integer id){ 
        //Se mostrara los datos de la transaccion espeficicada por su "Id"
        return transaccionService.findById(id); 
    }

    // Método que maneja una petición HTTP GET en la ruta "/consulta_transaccion"
    @GetMapping("/consulta_transaccion")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<List<Transaccion>> consulta_transaccion(@RequestParam ("idcta") String idcta,@RequestHeader ("usuario") String usuario,@RequestHeader ("clave") String clave) { 
        Cliente cliente=new Cliente();
        //Se realiza la autenticacion del cliente por medio del "usuario" y "clave"
        // El método "login" parece verificar las credenciales del cliente
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        // Se verifica que el cliente se autentique exitosamente
        if (cliente!=null) {
            // Se mostrarán los datos de la transacción indicada por el cliente por medio de id y se devuelve una respuesta con un estado HTTP OK (200)
            return new ResponseEntity<>(transaccionService.consulta_transaccion(idcta),HttpStatus.OK);
        } else {
            // Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
 
    }
}
