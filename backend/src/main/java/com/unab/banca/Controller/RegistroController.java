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

// Anotación que indica que esta clase es un controlador en Spring
@RestController

// Anotación que permite las solicitudes desde cualquier origen, indicado por "*".
@CrossOrigin("*")

// Anotación que establece la ruta base para las solicitudes en este controlador.
@RequestMapping("/registrarse")

public class RegistroController {
    // Inyección de dependencia del objeto ClienteService.
    @Autowired
    private ClienteService clienteService;

    // Anotación que indica que este método maneja solicitudes HTTP POST.
    @PostMapping("")
    // Anotación que indica que el resultado del método debe ser devuelto como cuerpo de la respuesta.
    @ResponseBody
    // Método que maneja una petición HTTP POST para el registro de un cliente
    public ResponseEntity<Cliente> registrarse(@RequestBody Cliente cliente) {
        // Verificar que la clave y el nombre del cliente no estén vacíos
        if (cliente.getClave_cliente() != null && !cliente.getClave_cliente().isEmpty() &&
            cliente.getNombre_cliente() != null && !cliente.getNombre_cliente().isEmpty()) {
            // Hashear la clave del cliente usando el algoritmo sha1
            cliente.setClave_cliente(Hash.sha1(cliente.getClave_cliente()));
            
            // Generar un ID aleatorio para el cliente (3 primeros caracteres de un UUID)
            String idAleatorio = UUID.randomUUID().toString();
            cliente.setId_cliente(idAleatorio.substring(0, 3));
            
            // Guardar el cliente en la base de datos a través del servicio correspondiente
            Cliente clienteGuardado = clienteService.save(cliente);
            
            // Devolver el cliente guardado en el cuerpo de la respuesta con un estado HTTP OK (200)
            return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
        } else {
            // Si la clave o el nombre del cliente están vacíos, devolver una respuesta con estado HTTP BAD REQUEST (400)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
