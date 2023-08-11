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

@RestController
@CrossOrigin("*")
@RequestMapping("/transaccion")
public class TransaccionController {

    @Autowired
    private TransaccionDao transaccionDao;
    @Autowired
    private ClienteDao clienteDao;
    @Autowired
    private TransaccionService transaccionService;
    
    @PostMapping(value="/")
    @ResponseBody
    public ResponseEntity<Transaccion> agregar(@RequestBody Transaccion transaccion){   
        //Se invoca el metodo "save()" para guardar la transaccion en la BBDD
        Transaccion obj = transaccionService.save(transaccion);
        //Se devuelve una respuesta con los datos del objeto "Transaccion" y un informe de estado OK
        return new ResponseEntity<>(obj, HttpStatus.OK);     
    }

    @PostMapping(value="/crear_transaccion") 
    public void crear_transaccion(@RequestParam ("idcta") String idcta,@RequestParam ("valor_transaccion") Double valor_transaccion,@RequestParam ("tipo") String tipo,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Cliente cliente1=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente1=clienteDao.login(usuario, Hash.sha1(clave));
        if (cliente1!=null) {
            //Se registrara un nueva transaccion con los valores idcta, valor_transaccion, tipo
           transaccionService.cear_transaccion(idcta, valor_transaccion, tipo);
        }
          
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Transaccion> eliminar(@PathVariable Integer id){ 
        //Por medio del metodo "findId()" se buscara y una transaccion especifica y se guardara en el objeto "obj"
        Transaccion obj = transaccionService.findById(id); 
        if(obj!=null) 
            //Se eliminara la transaccion correspondiente al "id" indicado
            transaccionService.delete(id);
        else 
            return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
        return new ResponseEntity<>(obj, HttpStatus.OK); 
    }

    @PutMapping(value="/") 
    public ResponseEntity<Transaccion> editar(@RequestBody Transaccion transaccion){ 
        //Se busca una transaccion especifica por su id
        Transaccion obj = transaccionService.findById(transaccion.getId_transaccion()); 
        if(obj!=null) {
            //Se actualiza el valor, fecha y cuenta de la transaccion y se guarda por medio del metodo save()
           obj.setValor_transaccion(transaccion.getValor_transaccion());
           obj.setFecha_transaccion(transaccion.getFecha_transaccion());
           obj.setCuenta(transaccion.getCuenta());
           transaccionService.save(obj);
        } 
        else 
            return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
        return new ResponseEntity<>(obj, HttpStatus.OK); 
    }
    @GetMapping("/list") 
    public List<Transaccion> consultarTodo(){
        //Se mostratan todas las transacciones guardadas en la base de datos
        return transaccionService.findByAll(); 
    }
    @GetMapping("/list/{id}") 
    public Transaccion consultaPorId(@PathVariable Integer id){ 
        //Se mostrara los datos de la transaccion espeficicada por su "Id"
        return transaccionService.findById(id); 
    }

    @GetMapping("/consulta_transaccion")
    @ResponseBody
    public ResponseEntity<List<Transaccion>> consulta_transaccion(@RequestParam ("idcta") String idcta,@RequestHeader ("usuario") String usuario,@RequestHeader ("clave") String clave) { 
        Cliente cliente=new Cliente();
        //Se realiza la autenticacion del cliente por medio del "usuario" y "clave"
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        if (cliente!=null) {
            //Se mostrara los datos de la transaccion indicada por el cliente por medio de id y un mensaje de estado OK
            return new ResponseEntity<>(transaccionService.consulta_transaccion(idcta),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
 
    }
}
