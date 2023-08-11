package com.unab.banca.Models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

//Estas anotaciones indican que se crearan los metodos Getters y Setter de los atributos sin tener escribirlos manualmente
@Getter
@Setter

/*Indica que la clase administrador funcionara como una entidad persistente, lo que quiere decir 
que se mapeara como una tabla en una base de datos relacional*/
@Entity

//Indica el nombre de la tabla en la BBDD en la cual se mapeara la clase "Administrador"
@Table(name="administrador")

//"Serializable" permite que los objetos de la clase "Administrador" se conviertan en bytes y se almacenen o trasmitan
public class Administrador implements Serializable {

    //Indica que el campo "id_administrador" sera la clave primaria de la tabla administrador en la BBDD
    @Id

    /*"Column" indica que se mapeara un campo en una columna en la BBDD, por lo que se mapera el campo
      "id_administrador", "nombre_administrador" y "clave_administrador" a las columnas "id_administrador", 
      "nombre_administrador" y "clave_administrador" dentro de la tabla "administrador"*/
    @Column(name="id_administrador")
    private String id_administrador;
    @Column(name="nombre_administrador")
    private String nombre_administrador;
    @Column(name="clave_administrador")
    private String clave_administrador;

    
    @Override
    /*"toString" es un metodo que devolvera todos campos de la clase "Administrador" (id_administrador, nombre_administrador ...) 
    como una cadena de texto */
    public String toString() {
        return "Administrador [id_administrador=" + id_administrador + ", nombre_administrador=" + nombre_administrador
                + ", clave_administrador=" + clave_administrador + "]";
    }

    
}
