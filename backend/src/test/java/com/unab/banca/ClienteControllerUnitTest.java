package com.unab.banca;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.unab.banca.Controller.CuentaController;
import com.unab.banca.Dao.ClienteDao;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Security.Hash;
import com.unab.banca.Service.CuentaService;

@SpringBootTest // Carga la configuración de Spring Boot
public class ClienteControllerUnitTest {

    @Autowired // Inyecta las dependencias automáticamente
    private CuentaController cuentaController;
    
    @MockBean // Crea un mock para el ClienteDao
    private ClienteDao clienteDao;

    @MockBean // Crea un mock para el CuentaService
    private CuentaService cuentaService;

    @Test
    public void testDepositoValorPositivo() {
        String idcta = "01-01";
        Double valorDeposito = 100.0;
        String clave = "123456";
        String usuario = "1";

        when(clienteDao.login(usuario, Hash.sha1(clave))).thenReturn(new Cliente());

        ResponseEntity<String> response = cuentaController.deposito(idcta, valorDeposito, clave, usuario);

        verify(cuentaService).deposito(idcta, valorDeposito);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Retiro realizado con éxito", response.getBody());
    }

    @Test
    public void testDepositoCero() {
        String idcta = "01-01";
        Double valorDeposito = 0.0;
        String clave = "123456";
        String usuario = "1";

        when(clienteDao.login(usuario, Hash.sha1(clave))).thenReturn(new Cliente());

        ResponseEntity<String> response = cuentaController.deposito(idcta, valorDeposito, clave, usuario);

        verify(cuentaService, never()).deposito(anyString(), anyDouble());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Saldo inferior a 0", response.getBody());
    }

    @Test
    public void testDepositoValorNegativo() {
        String idcta = "01-01";
        Double valorDeposito = -100.0;
        String clave = "123456";
        String usuario = "1";

        when(clienteDao.login(usuario, Hash.sha1(clave))).thenReturn(new Cliente());

        ResponseEntity<String> response = cuentaController.deposito(idcta, valorDeposito, clave, usuario);

        verify(cuentaService, never()).deposito(anyString(), anyDouble());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Saldo inferior a 0", response.getBody());
    }

}
