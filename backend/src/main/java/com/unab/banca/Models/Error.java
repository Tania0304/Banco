package com.unab.banca.Models;

import lombok.Data;
//la anotacion genera los metodos Getters y Setters, "toString()", "equals()", "hashCode()"
@Data

public class Error {
    //Variable que representa el nombre del campo que ha causado el error 
    private String field;

    //variable que representa el mensaje de error asociado al campo
    private String message;
}
