// package com.unab.banca;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// class BancaApplicationTests {

// 	@Test
// 	void contextLoads() {
// 	}

// }

package com.unab.banca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.unab.banca.Controller.CuentaController;
import com.unab.banca.Dao.ClienteDao;
import com.unab.banca.Models.Cliente;
import com.unab.banca.Models.Cuenta;
import com.unab.banca.Models.Prestamo;
import com.unab.banca.Models.Transaccion;
import com.unab.banca.Service.ClienteService;
import com.unab.banca.Service.CuentaService;
import com.unab.banca.Service.PrestamoService;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class BancaApplicationTests {
    @LocalServerPort
	private int port;

	private String baseUrl;

	private static RestTemplate restTemplate;

	 @Autowired
    private ClienteService clienteService;

	@Autowired
	private CuentaService cuentaService;

	@Autowired
	private PrestamoService prestamoService;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setUp() {
		baseUrl = "http://localhost:" + port ;
	}

	

	@Test
	public void testIniciarSesionCliente() {
		String usuario = "008";
		String clave = "008008";

		// Enviar una solicitud GET al servidor con los parámetros de usuario y clave
		ResponseEntity<Cliente> response = restTemplate.getForEntity(
			baseUrl + "/cliente/login?usuario=" + usuario + "&clave=" + clave, Cliente.class);

		// Verificar que la respuesta sea OK (HttpStatus.OK)
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Obtener el cliente de la respuesta
		Cliente cliente = response.getBody();

		// Verificar que el cliente no sea nulo
		assertNotNull(cliente);
		assertEquals(usuario, cliente.getId_cliente());
		assertEquals("ab2dbf0e47d1c85ea7624cba3022756f1928c3c7", cliente.getClave_cliente());
	}


	@Test
    public void testRegistrarse() {
        // Crear un nuevo objeto Cliente con los valores deseados
        Cliente cliente = new Cliente();
        cliente.setNombre_cliente("Silvia Merch");
        cliente.setClave_cliente("Strixs");

        // Configurar los encabezados (headers) para la solicitud
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Crear una entidad HttpEntity que contiene el objeto Cliente y los headers
        HttpEntity<Cliente> request = new HttpEntity<>(cliente, headers);

        // Enviar una solicitud POST al servidor y recibir la respuesta como un objeto Cliente
        ResponseEntity<Cliente> response = restTemplate.postForEntity(
                baseUrl + "/registrarse", request, Cliente.class);

        // Verificar que la respuesta sea OK (HttpStatus.OK)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Obtener el cliente de la respuesta
        Cliente clienteRegistrado = response.getBody();

        // Verificar que el cliente no sea nulo
        assertNotNull(clienteRegistrado);

        // Verificar que el nombre del cliente coincida
        assertEquals("Silvia Merch", clienteRegistrado.getNombre_cliente());
    }

	@Test
	public void testListarCuentas() {

		// Configurar los encabezados (headers) para la autenticación
		HttpHeaders headers = new HttpHeaders();
		headers.set("clave", "123456");
		headers.set("usuario", "1");

		//Configura los parámetros de la URL.
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl+"/cuenta/consulta_cuenta")
                .queryParam("idc", "1");

		// Crear una entidad HttpEntity que contiene los headers
		HttpEntity<String> request = new HttpEntity<>(headers);

        // Envía una solicitud GET y recibe la respuesta.
        ResponseEntity<List<Cuenta>> response = restTemplate.exchange(
        uriBuilder.toUriString(), HttpMethod.GET, request, new ParameterizedTypeReference<List<Cuenta>>() {});


        // Asegúrate de que el estado de la respuesta sea OK (HttpStatus.OK).
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Asegúrate de que la lista de cuentas no sea nula y no esté vacía.
        List<Cuenta> cuentas = response.getBody();
        assertNotNull(cuentas);
        assertTrue(cuentas.size() > 0);
    }

	@Test
    public void testRealizarRetiro() {
        String idCuenta = "01-01";
        Double valorRetiro = 5000.0;

        HttpHeaders headers = new HttpHeaders();
        headers.set("clave", "123456");
        headers.set("usuario", "1");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/cuenta/retiro?idcta=" + idCuenta + "&valor_retiro=" + valorRetiro,
                HttpMethod.PUT, request, Void.class);

		
        assertEquals(HttpStatus.OK, response.getStatusCode());

		//Verificar que los datos del cuenta se hayan actualizado correctamente en la base de datos
		Cuenta cuentaActualizada = cuentaService.findById(idCuenta);
		assertNotNull(cuentaActualizada);
		assertEquals(60000, cuentaActualizada.getSaldo_cuenta());


	}

	@Test
    public void testRealizarDeposito() {
        String idCuenta = "01-01";
        Double valorDeposito = 25000.0;

        HttpHeaders headers = new HttpHeaders();
        headers.set("clave", "123456");
        headers.set("usuario", "1");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/cuenta/deposito?idcta=" + idCuenta + "&valor_deposito=" + valorDeposito,
                HttpMethod.PUT, request, Void.class);
		
        assertEquals(HttpStatus.OK, response.getStatusCode());

		//Verificar que los datos del cuenta se hayan actualizado correctamente en la base de datos
		Cuenta cuentaActualizada = cuentaService.findById(idCuenta);
		assertNotNull(cuentaActualizada);
		assertEquals(75000, cuentaActualizada.getSaldo_cuenta());

	}

    @Test
    public void testConsultaTransaccion() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("clave", "123456");
        headers.set("usuario", "1");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List<Transaccion>> response = restTemplate.exchange(
                baseUrl + "/transaccion/consulta_transaccion?idcta=01-01",
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Transaccion>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Transaccion> transacciones = response.getBody();
        assertNotNull(transacciones);
        assertTrue(transacciones.size() > 0);
    }


    @Test
    public void testConsultaPrestamo() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("clave", "123456");
        headers.set("usuario", "1");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List<Prestamo>> response = restTemplate.exchange(
                baseUrl + "/prestamo/consulta_prestamo?idc=1",
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Prestamo>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Prestamo> prestamos = response.getBody();
        assertNotNull(prestamos);
        assertTrue(prestamos.size() > 0);
    }

	@Test
    public void testRealizarPagoCuota() {
        String idp = "126";
        Double valorPago = 1000000.0;

        HttpHeaders headers = new HttpHeaders();
        headers.set("clave", "123456");
        headers.set("usuario", "1");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/prestamo/deposito_cuota?idp=" + idp + "&valor_deposito_c=" + valorPago,
                HttpMethod.PUT, request, Void.class);
		
        assertEquals(HttpStatus.OK, response.getStatusCode());

		//Verificar que los datos del cuenta se hayan actualizado correctamente en la base de datos
		Prestamo prestamoActualizado = prestamoService.findById(127);
		assertNotNull(prestamoActualizado);
		
	}

}




