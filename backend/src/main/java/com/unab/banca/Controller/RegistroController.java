package com.unab.banca.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.unab.banca.Service.ClienteService;
import com.unab.banca.Dao.ClienteDao;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Security.Hash;

/*La anotacion indica que la clase "AdministradorController" es un controlador de Spring que manejara solicitudes HTTP y
 *devolvera los datos en formato JSON o XML y no en forma de lista*/
@RestController

//Permite que el controlador recibe solicitudes HTTP desde diferentes dominios
@CrossOrigin("*")

/*Anotacion que mapea una URL base para las solicitudes realizadas en este controlador, todas las solicitudes que comiencen
 *con "/cliente" seran manejadas por este controlador*/
@RequestMapping("/registrarse")
public class RegistroController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Cliente> registrarse(@RequestBody Cliente cliente) {
        if (cliente.getClave_cliente() != null && !cliente.getClave_cliente().isEmpty() &&
            cliente.getNombre_cliente() != null && !cliente.getNombre_cliente().isEmpty()) {
            cliente.setClave_cliente(Hash.sha1(cliente.getClave_cliente()));
            String idAleatorio = UUID.randomUUID().toString();
            cliente.setId_cliente(idAleatorio.substring(0, 3));
            Cliente clienteGuardado = clienteService.save(cliente);
            return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
