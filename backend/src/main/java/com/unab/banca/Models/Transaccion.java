package com.unab.banca.Models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

//Se generaran los metodos Getter y Setter de forma automarica
@Getter
@Setter

//La clase cliente se mapeara como una tabla de nombre cuenta dentro de una BBDD
@Entity

//Indica el nombre de la tabla en la BBDD en la cual se mapeara la clase "transaccion"
@Table(name="transaccion")
public class Transaccion implements Serializable {

    //El campo "id_transaccion" se establece como la llave primaria de la tabla transaccion y se establece como una columna llamada "id_transaccion" 
    @Id

    //La anotacion generara automaticamente el valor de la llave primaria de la entidad "transaccion"
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_transaccion")
    private int id_transaccion;
            //@NotEmpty(message = "EL campo Fecha transacción no debe ser vacío")
    @Column(name="fecha_transaccion")
    private Date fecha_transaccion;
            //@NotEmpty(message = "EL campo valor transacción no debe ser vacío")
    @Column(name="valor_transaccion")
    private double valor_transaccion;
            //@NotEmpty(message = "EL campo Tipo transacción no debe ser vacío")
    @Column(name="tipo_transaccion")
    private String tipo_transaccion;
            //@NotEmpty(message = "EL campo identificador cuenta que realizó la transacción no debe ser vacío")

        
    //Establece una relacion <Muchos a uno> entre la entidad "Transaccion" y entidad "Cuenta", cada cuenta esta asociada a una cuenta especifica
    @ManyToOne
    //Mapea la columna "id_cuenta" en la tabla de la BBDD y la usa como una llave foranea haciendo referencia a la tabla "Cuenta"
    @JoinColumn(name="id_cuenta")
    private Cuenta cuenta;

    //"toString" es un metodo que devolvera todos campos de la clase "Cuenta" como una cadena de texto //
    @Override
    public String toString() {
        return "Transaccion [id_transaccion=" + id_transaccion + ", fecha_transaccion=" + fecha_transaccion
                + ", valor_transaccion=" + valor_transaccion + ", tipo_transaccion=" + tipo_transaccion + ", cuenta="
                + cuenta + "]";
    }
}
