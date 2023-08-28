package com.unab.banca.Controller;
import com.unab.banca.Models.Cuenta;
import com.unab.banca.Security.Hash;
import com.unab.banca.Dao.CuentaDao;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Dao.ClienteDao;
import com.unab.banca.Models.Administrador;
import com.unab.banca.Dao.AdministradorDao;
import com.unab.banca.Service.ClienteService;
import com.unab.banca.Service.CuentaService;

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
// Anotación que indica que esta clase es un controlador en Spring.
@RestController
// Anotación que permite las solicitudes desde cualquier origen, indicado por "*".
@CrossOrigin("*")
// Anotación que establece la ruta base para las solicitudes en este controlador.
@RequestMapping("/cuenta")
public class CuentaController {
    // Inyección de dependencia del objeto CuentaDao.
    @Autowired
    private CuentaDao cuentaDao;
    // Inyección de dependencia del objeto AdministradorDao.
    @Autowired
    private AdministradorDao administradorDao;
    // Inyección de dependencia del objeto ClienteDao.
    @Autowired
    private ClienteDao clienteDao;
    // Inyección de dependencia del objeto CuentaService.
    @Autowired
    private CuentaService cuentaService;
    
    // Anotación que indica que este método maneja solicitudes HTTP POST.
    @PostMapping(value="/")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    //Esto define un método público llamado "agregar" que toma tres parámetros: "clave", "usuario" y "cuenta". "clave" y "usuario" se obtendrán de los encabezados de la solicitud HTTP, y "cuenta" se obtendrá del cuerpo de la solicitud.
    public ResponseEntity<Cuenta> agregar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario, @Valid @RequestBody Cuenta cuenta){   
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon!=null) {
            //Se mostrara los datos de la cuenta guardada y un mensaje de estado OK
            return new ResponseEntity<>(cuentaService.save(cuenta), HttpStatus.OK); 
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401), 
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
            
    }
    //Anotacion que mapea la solicitud HTTP DELETE a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/{id}"
    @DeleteMapping(value="/{id}") 
    //Esto define un método público llamado "editar" que toma dos parámetros: "clave" y "usuario". "clave" y "usuario" se obtendrán de los encabezados de la solicitud HTTP.
    public ResponseEntity<Cuenta> eliminar(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
         //Se valida que el "usuario" y la "clave" sean correctos
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
       if (admon!=null) {
            //Se buscara una cuenta por medio de su "id"
            Cuenta obj = cuentaService.findById(id); 
            
            if(obj!=null) 
                //Al ser encontrada, se eliminara la cuenta referenciada con dicho "id"
                cuentaService.delete(id);
            else 
                //Si "obj" es null (no se encontro el id de la cuenta), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR  
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            //Se mostrara los datos de la cuenta guardada y un mensaje de estado OK
                return new ResponseEntity<>(obj, HttpStatus.OK); 
      
       } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401), 
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }
       
        
    }

    //Anotacion que mapea la solicitud HTTP PUT a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/"
    @PutMapping(value="/")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta. 
    @ResponseBody
    //El metodo "editar" maneja la solicitud PUT para editar los datos del administrador, este utiliza los parametros "clave" y "usuario" y un objeto "cuenta" para realizar la solicitud
    public ResponseEntity<Cuenta> editar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario,@Valid @RequestBody Cuenta cuenta){ 
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon!=null) {
            //Se buscara una cuenta por medio de su "id"
            Cuenta obj = cuentaService.findById(cuenta.getId_cuenta());
            //Se busca la cuenta por medio del ID 
            if(obj!=null) { 
                //Al ser encontrada, la cuenta actualizara los valores de "fecha_apertura","saldo_cuenta" y del "cliente" y se guardara
                obj.setFecha_apertura(cuenta.getFecha_apertura());
                obj.setSaldo_cuenta(cuenta.getSaldo_cuenta());
                obj.setCliente(cuenta.getCliente());
                cuentaService.save(cuenta); 
            } 
            else 
                //Si "obj" es null (no se encontro el id de la cuenta), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR  
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            //Se mostrara los datos de la cuenta guardada y un mensaje de estado OK
            return new ResponseEntity<>(obj, HttpStatus.OK); 
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401), 
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    // Anotación que mapea la solicitud HTTP PUT a un método controlador, cada vez que se invoque una solicitud a la ruta "/deposito"
    @PutMapping(value="/deposito")
    //Estas anotaciones se utilizan para obtener los valores de los parámetros y encabezados de la solicitud HTTP. Aquí se obtienen los valores "idcta", "valor_deposito", "clave" y "usuario" para realizar la operación de depósito 
    public ResponseEntity<String> deposito(@RequestParam ("idcta") String idcta,@RequestParam ("valor_deposito") Double valor_deposito,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Cliente cliente1=new Cliente();

        //Se valida que el "usuario" y la "clave" sean correctos
        cliente1=clienteDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el cliente se autentica exitosamente 
        if (cliente1!=null) {
            if(valor_deposito > 0){
                //Se realizara la actualizacion de la cuenta, sumando el valor del deposito al saldo de la cuenta del cliente
                cuentaService.deposito(idcta, valor_deposito);
                return new ResponseEntity<>("Retiro realizado con éxito", HttpStatus.OK); 
            }else{
                return new ResponseEntity<>("Saldo inferior a 0", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
          
    }

    // Anotación que mapea la solicitud HTTP PUT a un método controlador, cada vez que se invoque una solicitud a la ruta "/retiro"
    @PutMapping(value="/retiro") 
    public ResponseEntity<String> retiro(@RequestParam ("idcta") String idcta,@RequestParam ("valor_retiro") Double valor_retiro,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Cliente cliente1=new Cliente();
        
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente1=clienteDao.login(usuario, Hash.sha1(clave));
        Cuenta cuenta1 = cuentaService.findById(idcta);
        //Se verifica que el cliente se autentica exitosamente
        if (cliente1!=null) {
            if(cuenta1.getSaldo_cuenta() >= valor_retiro){
                //Se realizara la actualizacion de la cuenta, restando el valor del retiro al saldo de la cuenta del cliente
                cuentaService.retiro(idcta, valor_retiro); 
                return new ResponseEntity<>("Retiro realizado con éxito", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Saldo insuficiente en la cuenta", HttpStatus.BAD_REQUEST);
            }

        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    // Anotación que mapea la solicitud HTTP GET a un método controlador, cada vez que se invoque una solicitud a la ruta "/list"
    @GetMapping("/list")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta. 
    @ResponseBody
    //Estas anotaciones se utilizan para obtener los valores de los encabezados de la solicitud HTTP. Aquí se obtienen los valores "clave" y "usuario" para realizar la autenticación.
    public ResponseEntity<List<Cuenta>> consultarTodo(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        Administrador admon=new Administrador();
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se valida que el "usuario" y la "clave" sean correctos
        if (admon!=null) {
            //Se buscaran las cuentas existentes dentro de la BBDD y se devolvera la informacion de estas y una respuesta de estado OK
            return new ResponseEntity<>(cuentaService.findByAll(),HttpStatus.OK);
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401), 
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }  
          
    }

    //Se utiliza la anotación @GetMapping para mapear una solicitud HTTP GET a este método. El segmento {id} en la anotación de mapeo indica que se espera un valor de id en la URL.
    @GetMapping("/list/{id}")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta. 
    @ResponseBody
    public ResponseEntity<Cuenta> consultaPorId(@PathVariable String id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Cliente cliente=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el cliente se autentica exitosamente
        if (cliente!=null) {
            //Se busca una cuenta por el id especigico y se volvera la informacion de las cuentas y una respuesta de estado OK
            return new ResponseEntity<>(cuentaService.findById(id),HttpStatus.OK);
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401), 
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }   
    }

    // Anotación que mapea la solicitud HTTP GET a un método controlador, cada vez que se invoque una solicitud a la ruta "/consulta_cuenta"
    @GetMapping("/consulta_cuenta")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<List<Cuenta>> consulta_cuenta(@RequestParam ("idc") String idc,@RequestHeader ("usuario") String usuario,@RequestHeader ("clave") String clave) { 
        Cliente cliente=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el cliente se autentica exitosamente
        if (cliente!=null) {
            //Se busca una cuenta por el id de un cliente especigico y se volvera la informacion de las cuentas del cliente y una respuesta de estado OK
            return new ResponseEntity<>(cuentaService.consulta_cuenta(idc),HttpStatus.OK);
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401), 
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    // Anotación que mapea la solicitud HTTP POST a un método controlador, cada vez que se invoque una solicitud a la ruta "/nueva_cuenta"
    @PostMapping(value = "/nueva_cuenta")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<Cuenta> crearCuenta(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario, @Valid @RequestBody Cuenta cuenta){   
        Cliente cliente = new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente = clienteDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el cliente se autentica exitosamente
        if (cliente!=null) {
            //Se mostrara los datos de la cuenta guardada y un mensaje de estado OK
            return new ResponseEntity<>(cuentaService.save(cuenta), HttpStatus.OK); 
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401), 
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
            
    }
}
