package com.unab.banca.Validation;

import java.util.ArrayList;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.unab.banca.Models.Error;

// Anotación que indica que esta clase manejará las validaciones a nivel de controlador
@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler {

	// Método que manejará errores de validación de argumentos de método no válidos
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// Crear una lista para almacenar los errores
		ArrayList<Error> errores = new ArrayList<>();

		// Iterar sobre todos los errores de validación
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			// Crear un objeto Error para cada error y establecer los detalles
			Error err = new Error();
			err.setField(((FieldError) error).getField()); // Campo en el que ocurrió el error
			err.setMessage(error.getDefaultMessage()); // Mensaje de error predeterminado
			errores.add(err);

		});
		// Devolver una respuesta con la lista de errores y el estado HTTP PARTIAL_CONTENT
		return new ResponseEntity<Object>(errores, HttpStatus.PARTIAL_CONTENT);
	}

}
