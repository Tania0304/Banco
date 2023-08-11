package com.unab.banca.Models;

import java.io.Serializable;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

//Se generaran los metodos Getter y Setter de forma automarica
@Getter
@Setter

//La clase cliente se mapeara como una tabla de nombre cliente dentro de una BBDD
@Entity

//Indica el nombre de la tabla en la BBDD en la cual se mapeara la clase "cliente"
@Table(name="cliente")
public class Cliente  implements Serializable{

    //El campo "id_cliente" se establece como la llave primaria de la tabla cliente y se establece como una columna llamada "id_cliente" 
    @Id

    //Anotacion que indica que el campo "id_cliente" no debe esta vacio y si lo esta se generara un mensaje de error con el mensaje suministrado
    @NotEmpty(message = "El campo identificador del  cliente no debe ser vacío")
    @Column(name="id_cliente", unique = true)


    private String id_cliente;

    /*La anotacion "NotEmpty" y "Size" indican que las restricciones de los campos "id_cliente" y "nombre_cliente", indicando
     *que estos no deben estar vacios y que tienen un tamaño minimo y maximo especificado, si estas restricciones no se cumplen
     *se generar un mensaje de error correspondiente.*/
    @NotEmpty(message = "El campo nombre cliente no debe ser vacío")
    @Size(min = 5, max = 80,message = "El campo nombre Cliente debe tener mínimo 5 caracteres y máximo 80")
    @Column(name="nombre_cliente")
    private String nombre_cliente;
    @NotEmpty(message = "El campo clave cliente no debe ser vacío")
    @Size(min = 5, max = 80,message = "El campo nombre Cliente debe tener mínimo 5 caracteres y máximo 50")
    
    @Column(name="clave_cliente")
    private String clave_cliente;

    @Override
     /*"toString" es un metodo que devolvera todos campos de la clase "Cliente" como una cadena de texto */
    public String toString() {
        return "Cliente [id_cliente=" + id_cliente + ", nombre_cliente=" + nombre_cliente + ", clave_cliente="
                + clave_cliente + "]";
    }


}
