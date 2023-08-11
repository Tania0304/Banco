package com.unab.banca.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
//Se generaran los metodos Getter y Setter de forma automarica
@Getter
@Setter

//La clase cliente se mapeara como una tabla de nombre cuenta dentro de una BBDD
@Entity

//Indica el nombre de la tabla en la BBDD en la cual se mapeara la clase "cuenta"
@Table(name="cuenta")
public class Cuenta implements Serializable {
    
    //El campo "id_cuenta" se establece como la llave primaria de la tabla cuenta y se establece como una columna llamada "id_cuenta" 
    @Id
    
    //Anotacion que indica que el campo "id_cliente" no debe esta vacio y si lo esta se generara un mensaje de error con el mensaje suministrado
    @NotEmpty(message = "El campo identificador de la cuenta no debe ser vacío")
    @Column(name="id_cuenta")
    private String id_cuenta;
        //@NotEmpty(message = "El campo fecha de apertura no debe ser vacío")
    @Column(name="fecha_apertura")
    private LocalDate fecha_apertura;
        //@NotEmpty(message = "El campo saldo de la cuenta no debe ser vacío")
    @Column(name="saldo_cuenta")
    private double saldo_cuenta;
        //@NotEmpty(message = "El campo identificador del cliente, prpietario de la cuenta no debe ser vacío")

    /*NUEVO CODIGO*/
    @Transient
    private int antiguedad;

    public int getAntiguedad(){
        return Period.between(this.fecha_apertura,LocalDate.now()).getYears();
    }
    //Establece una relacion <Mucho a uno> entre la entidad "Cuenta" y entidad "Cliente", cada cuenta esta asociada aun cliente especifico
    @ManyToOne
    //Mapea la columna "id_cliente" en la tabla de la BBDD y la usa como una llave foranea haciendo referencia a la tabla "Clientes"
    @JoinColumn(name="id_cliente")
    private Cliente cliente;
    
    //"toString" es un metodo que devolvera todos campos de la clase "Cuenta" como una cadena de texto //
    @Override
    public String toString() {
        return "Cuenta [id_cuenta=" + id_cuenta + ", fecha_apertura=" + fecha_apertura +"Years_time_antiguedad="+antiguedad +", saldo_cuenta="
                + saldo_cuenta + ", cliente=" + cliente + "]";
    }

    

}
