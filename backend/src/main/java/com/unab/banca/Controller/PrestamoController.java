package com.unab.banca.Controller;
import com.unab.banca.Models.Prestamo;
import com.unab.banca.Security.Hash;
import com.unab.banca.Dao.PrestamoDao;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Models.Cuenta;
import com.unab.banca.Dao.ClienteDao;
import com.unab.banca.Models.Administrador;
import com.unab.banca.Dao.AdministradorDao;
import com.unab.banca.Service.ClienteService;
import com.unab.banca.Service.CuentaService;
import com.unab.banca.Service.PrestamoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/prestamo")
public class PrestamoController {
    // Inyección de dependencia del objeto PrestamoDao.
    @Autowired
    private PrestamoDao prestamoDao;

    // Inyección de dependencia del objeto AdministradorDao.
    @Autowired
    private AdministradorDao administradorDao;

    // Inyección de dependencia del objeto ClienteDao.
    @Autowired
    private ClienteDao clienteDao;

    // Inyección de dependencia del objeto PrestamoService.
    @Autowired
    private PrestamoService prestamoService;

    // Inyección de dependencia del objeto CuentaService.
    @Autowired
    private CuentaService cuentaService;
    
    
    // Anotación que indica que este método maneja solicitudes HTTP POST.
    @PostMapping(value="/")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<Prestamo> agregar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario, @Valid @RequestBody Prestamo prestamo){   
        Administrador admon=new Administrador();
 
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon!=null) {
            //Se mostrara los datos del pretamo guardada y un mensaje de estado OK
            return new ResponseEntity<>(prestamoService.save(prestamo), HttpStatus.OK); 
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401),
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
            
    }

    //Anotacion que mapea la solicitud HTTP DELETE a un metodo controlador, cada vez que se invoque una solicitud a la ruta "/{id}"
    @DeleteMapping(value="/{id}") 
    public ResponseEntity<Prestamo> eliminar(@PathVariable int id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
       if (admon!=null) {
            //Se buscara el prestamo por medio de su "id"
            Prestamo obj = prestamoService.findById(id); 
            // Si se encuentra un préstamo con el ID especificado se eliminará el préstamo
            if(obj!=null) 
                //Al ser encontrada, se eliminara la cuenta referenciada con dicho "id"
                prestamoService.delete(id);
            else 
                //Si "obj" es null (no se encontro el id del prestamo), se devolvera una respuesta HTTP y mostrara un mensaje de estado INTERNAL_SERVER_ERROR 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
            //Se mostrara los datos del prestamo eliminado y un mensaje de estado OK 
            return new ResponseEntity<>(obj, HttpStatus.OK); 
      
       } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401),
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }
       
        
    }


    @PutMapping(value="/") 
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<Prestamo> editar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario,@Valid @RequestBody Prestamo prestamo){ 
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el admninistrador se autentica exitosamente 
        if (admon!=null) {
            // Se buscará un préstamo por medio de su "id"
            Prestamo obj = prestamoService.findById(prestamo.getId_prestamo());
            //Si se encuentra un préstamo con el ID especificado se actualiza el valor de "n_cuotas" del préstamo 
            if(obj!=null) { 
                // Al ser encontrado, se actualizarán los valores de "n_cuotas" del préstamo y se guardará 
                obj.setN_cuotas(prestamo.getN_cuotas());
                prestamoService.save(prestamo); 
            } 
            else 
                // Si "obj" es null (no se encontró el id del préstamo), se devolverá una respuesta HTTP y mostrará un mensaje de estado INTERNAL_SERVER_ERROR 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
            // Se mostrarán los datos del préstamo actualizado y un mensaje de estado OK 
            return new ResponseEntity<>(obj, HttpStatus.OK); 
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401),
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    // Anotación que indica que este método manejará peticiones HTTP PUT en la URL "/deposito_cuota"
    @PutMapping(value="/deposito_cuota") 
    public void deposito_cuota_c(@RequestParam ("idp") int idp ,@RequestParam ("valor_deposito_c") Double valor_deposito_c, @RequestHeader("clave") String clave, 
    @RequestHeader("usuario") String usuario){ 
        Cliente cliente1=new Cliente();
        Prestamo prestamo1 = new Prestamo();

        //Se valida que el "usuario" y la "clave" sean correctos
        cliente1=clienteDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el cliente se autentica exitosamente 
        if (cliente1!=null) {
            // Se busca el préstamo correspondiente al "idp"
            prestamo1 = prestamoService.findById(idp);
            // Se obtiene el valor de la cuota del préstamo
            double valorCuota = prestamo1.getValor_cuota(); 
            
            // Si el valor del depósito es mayor o igual al valor de la cuota
                if(valor_deposito_c >= valorCuota){
                    // Se realiza un depósito de la cuota para el préstamo
                    prestamoService.deposito_cuota_c(idp, valor_deposito_c);
                }
            }
          
    }

    // Anotación que indica que este método manejará peticiones HTTP GET en la URL "/list"
    @GetMapping("/list") 
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<List<Prestamo>> consultarTodo(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        // El método "login" parece verificar las credenciales del administrador
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        
        // Se verifica que el administrador se autentique exitosamente
        if (admon!=null) {
            // Se buscarán los préstamos existentes en la BBDD y se devolverá la información de estos con una respuesta de estado OK
            return new ResponseEntity<>(prestamoService.findByAll(),HttpStatus.OK);
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401),
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }  
          
    }

    // Anotación que indica que este método manejará peticiones HTTP GET en la URL "/list/{id}"
    @GetMapping("/list/{id}") 
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<Prestamo> consultaPorId(@PathVariable int id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
         Cliente cliente=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        // El método "login" parece verificar las credenciales del cliente
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el cliente se autentica exitosamente 
        if (cliente!=null) {
            // Se busca un préstamo por el ID específico y se devuelve la información del préstamo con una respuesta de estado OK
            return new ResponseEntity<>(prestamoService.findById(id),HttpStatus.OK);
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401),
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }   
    }

    // Anotación que indica que este método manejará peticiones HTTP GET en la URL "/consulta_prestamo"
    @GetMapping("/consulta_prestamo")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<List<Prestamo>> consulta_prestamo(@RequestParam ("idc") String idc, @RequestHeader ("usuario") String usuario,@RequestHeader ("clave") String clave) { 
        Cliente cliente=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        // El método "login" parece verificar las credenciales del cliente
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        //Se verifica que el cliente se autentica exitosamente 
        if (cliente!=null) {
            // Se busca una cuenta por el ID de un cliente específico y se devuelve la información de las cuentas del cliente con una respuesta de estado OK
            return new ResponseEntity<>(prestamoService.consulta_prestamo(idc),HttpStatus.OK);
        } else {
            //Si la autenticación falla, se devuelve una respuesta con un estado HTTP UNAUTHORIZED (401),
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    // Anotación que indica que este método manejará peticiones HTTP POST en la URL "/crear_prestamo"
    @PostMapping("/crear_prestamo")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    public ResponseEntity<Prestamo> crearPrestamo(
    @RequestHeader("clave") String clave,
    @RequestHeader("usuario") String usuario,
    @RequestParam("saldo_solicitado") double saldoSolicitado,
    @RequestParam("cuotas") int cuotas,
    @RequestParam("idcta") String idcta){
        //Se valida que el "usuario" y la "clave" sean correctos
        // El método "login" parece verificar las credenciales del cliente
        Cliente cliente = clienteDao.login(usuario, Hash.sha1(clave));
        
        //Se verifica que el cliente se autentica exitosamente 
        if (cliente != null) {
            // Calcular la tasa de interés (puedes ajustar esto según tu lógica)
            double tasaInteres = 0.1;  // Ejemplo: tasa de interés del 10%
            
            // Calcular el valor de la cuota a pagar (incluyendo la tasa de interés)
            double valorCuota = saldoSolicitado * (tasaInteres + 1) / cuotas;
            
            // Crear una nueva instancia de la clase Prestamo y establecer sus propiedades
            Prestamo prestamo = new Prestamo();
            prestamo.setFecha_solicitud(LocalDate.now());
            prestamo.setSaldo_solicitado(saldoSolicitado);
            prestamo.setN_cuotas(cuotas);
            prestamo.setValor_cuota(valorCuota);
            prestamo.setSaldo_pendiente(saldoSolicitado);
            prestamo.setCliente(cliente);
            
            // Buscar la cuenta asociada al ID proporcionado
            Cuenta cuenta = cuentaService.findById(idcta);
            prestamo.setCuenta(cuenta);
            
            // Guardar el nuevo préstamo en la base de datos
            prestamoService.save(prestamo);
            
            // Devolver el préstamo creado en el cuerpo de la respuesta con un estado HTTP OK (200)
            return new ResponseEntity<>(prestamo, HttpStatus.OK);
        } else {
            // Si la autenticación falla, se devuelve una respuesta con un estado HTTP BAD REQUEST (400)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }    

}
