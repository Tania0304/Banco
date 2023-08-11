package com.unab.banca.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
//Se generaran los metodos Getter y Setter de forma automarica
@Getter
@Setter
//La clase cliente se mapeara como una tabla de nombre cuenta dentro de una BBDD
@Entity

//Indica el nombre de la tabla en la BBDD en la cual se mapeara la clase "cuenta"
@Table(name="prestamo")
public class Prestamo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_prestamo")
    private int id_prestamo;
        //@NotEmpty(message = "El campo fecha de apertura no debe ser vacío")
    
    @Column(name="fecha_solicitud")
    private LocalDate fecha_solicitud;
        //@NotEmpty(message = "El campo saldo de la cuenta no debe ser vacío")
    
    @Column(name="saldo_solicitado")
    @Min(value = 500000, message = "El campo saldo solicitado debe ser mayor o igual a 500000")
    private double saldo_solicitado;
    
    
    @Column(name="n_cuotas")
    @Min(value = 1, message = "El campo numero de cuotas debe ser mayor o igual a 1")
    private int n_cuotas;
    

    @Column(name ="valor_cuota")
    private double valor_cuota;
    public double getValor_cuota(){
        return valor_cuota = this.saldo_solicitado/this.n_cuotas;
    } 

    @Column(name="saldo_pendiente")
    private double saldo_pendiente;
    public double getSaldo_pendiente(Double saldo_solicitado){
        return saldo_pendiente = this.saldo_solicitado;
    } 

    /**
     *
     */
    @OneToOne
    @JoinColumn(name ="id_cuenta")
    private Cuenta cuenta;


    @ManyToOne
    // //Mapea la columna "id_cliente" en la tabla de la BBDD y la usa como una llave foranea haciendo referencia a la tabla "Clientes"
    @JoinColumn(name="id_cliente")
    private Cliente cliente;
    
    //"toString" es un metodo que devolvera todos campos de la clase "Cuenta" como una cadena de texto //
    @Override
    public String toString() {
        return "Prestamo [id_prestamo=" + id_prestamo + ", fecha_solicitud=" + fecha_solicitud +", saldo_solicitado="+saldo_solicitado +", n_cuotas=" + n_cuotas + ", valor_cuota=" + valor_cuota +", id_cliente="+ cliente+ ", cuenta="+ cuenta +"]";
    }
    // +", saldo_pendiente="+ saldo_pendiente+

    public double setValor_cuota(double d) {
        return 0;
    }
    

}
