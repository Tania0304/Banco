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

@RestController
@CrossOrigin("*")
@RequestMapping("/prestamo")
public class PrestamoController {
    
    @Autowired
    private PrestamoDao prestamoDao;

    @Autowired
    private AdministradorDao administradorDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private CuentaService cuentaService;
    
    
    
    @PostMapping(value="/")
    @ResponseBody
    public ResponseEntity<Prestamo> agregar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario, @Valid @RequestBody Prestamo prestamo){   
        Administrador admon=new Administrador();
 
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon!=null) {

            return new ResponseEntity<>(prestamoService.save(prestamo), HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
            
    }

    // private String generarIdUnico() {
    //     return UUID.randomUUID().toString();
    // }

    @DeleteMapping(value="/{id}") 
    public ResponseEntity<Prestamo> eliminar(@PathVariable int id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
       if (admon!=null) {
            //Se buscara una cuenta por medio de su "id"
            Prestamo obj = prestamoService.findById(id); 
            if(obj!=null) 
                //Al ser encontrada, se eliminara la cuenta referenciada con dicho "id"
                prestamoService.delete(id);
            else 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            return new ResponseEntity<>(obj, HttpStatus.OK); 
      
       } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }
       
        
    }


    @PutMapping(value="/") 
    @ResponseBody
    public ResponseEntity<Prestamo> editar(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario,@Valid @RequestBody Prestamo prestamo){ 
        Administrador admon=new Administrador();
        //Se valida que el "usuario" y la "clave" sean correctos
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        if (admon!=null) {
            //Se buscara una cuenta por medio de su "id"
            Prestamo obj = prestamoService.findById(prestamo.getId_prestamo()); 
            if(obj!=null) { 
                //Al ser encontrada, la cuenta actualizara los valores de "fecha_apertura","saldo_cuenta" y del "cliente" y se guardara
                obj.setN_cuotas(prestamo.getN_cuotas());
                prestamoService.save(prestamo); 
            } 
            else 
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR); 
            return new ResponseEntity<>(obj, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }


    @PutMapping(value="/deposito_cuota") 
    public void deposito_cuota_c(@RequestParam ("idp") int idp ,@RequestParam ("valor_deposito_c") Double valor_deposito_c, @RequestHeader("clave") String clave, 
    @RequestHeader("usuario") String usuario){ 
        Cliente cliente1=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente1=clienteDao.login(usuario, Hash.sha1(clave));
        if (cliente1!=null) {
            Prestamo prestamo = prestamoService.findById(idp);
            double valorCuota = prestamo.getValor_cuota(); 
            // double obj = prestamoService.valor_cuota(idp); 
            if(valor_deposito_c >= valorCuota){
                prestamoService.deposito_cuota_c(idp, valor_deposito_c);
            }
        }
          
    }

    @GetMapping("/list") 
    @ResponseBody
    public ResponseEntity<List<Prestamo>> consultarTodo(@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){
        Administrador admon=new Administrador();
        admon=administradorDao.login(usuario, Hash.sha1(clave));
        //Se valida que el "usuario" y la "clave" sean correctos
        if (admon!=null) {
            //Se buscaran las cuentas existentes dentro de la BBDD y se devolvera la informacion de estas y una respuesta de estado OK
            return new ResponseEntity<>(prestamoService.findByAll(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }  
          
    }

    @GetMapping("/list/{id}") 
    @ResponseBody
    public ResponseEntity<Prestamo> consultaPorId(@PathVariable int id,@RequestHeader("clave")String clave,@RequestHeader("usuario")String usuario){ 
         Cliente cliente=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        if (cliente!=null) {
            //Se busca una cuenta por el id especigico y se volvera la informacion de las cuentas y una respuesta de estado OK
            return new ResponseEntity<>(prestamoService.findById(id),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }   
    }


    @GetMapping("/consulta_prestamo")
    @ResponseBody
    public ResponseEntity<List<Prestamo>> consulta_prestamo(@RequestParam ("idc") String idc, @RequestHeader ("usuario") String usuario,@RequestHeader ("clave") String clave) { 
        Cliente cliente=new Cliente();
        //Se valida que el "usuario" y la "clave" sean correctos
        cliente=clienteDao.login(usuario, Hash.sha1(clave));
        if (cliente!=null) {
            //Se busca una cuenta por el id de un cliente especigico y se volvera la informacion de las cuentas del cliente y una respuesta de estado OK
            return new ResponseEntity<>(prestamoService.consulta_prestamo(idc),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    @PostMapping("/crear_prestamo")
    @ResponseBody
    public ResponseEntity<Prestamo> crearPrestamo(
    @RequestHeader("clave") String clave,
    @RequestHeader("usuario") String usuario,
    @RequestParam("saldo_solicitado") double saldoSolicitado,
    @RequestParam("cuotas") int cuotas,
    @RequestParam("idcta") String idcta){
        Cliente cliente = clienteDao.login(usuario, Hash.sha1(clave));
        
        
        if (cliente != null) {
            // Calcular la tasa de interés (puedes ajustar esto según tu lógica)
            double tasaInteres = 0.1;  // Ejemplo: tasa de interés del 10%
            
            // Calcular el valor de la cuota a pagar (incluyendo la tasa de interés)
            double valorCuota = saldoSolicitado * (tasaInteres + 1) / cuotas;
            
            Prestamo prestamo = new Prestamo();
            prestamo.setFecha_solicitud(LocalDate.now());
            prestamo.setSaldo_solicitado(saldoSolicitado);
            prestamo.setN_cuotas(cuotas);
            prestamo.setValor_cuota(valorCuota);
            prestamo.setSaldo_pendiente(saldoSolicitado);
            prestamo.setCliente(cliente);
            
            Cuenta cuenta = cuentaService.findById(idcta);
            prestamo.setCuenta(cuenta);
            
            prestamoService.save(prestamo);
            
            return new ResponseEntity<>(prestamo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }    

}
