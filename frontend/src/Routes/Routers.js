import React from "react";

import Home from "../Paginas/Home";
import Logearse from '../Paginas/Logearse';
import Logout from "../Paginas/Logout";
import Registrarse from "../Paginas/Registrarse";

import Clientes from "../Paginas/Cliente";
import Movimiento from "../Paginas/Movimiento";
import Deposito from "../Paginas/Deposito";
import Retiro from "../Paginas/Retiro";

import CrearPrestamo from "../Paginas/CrearPrestamo";
import DepositoCuota from '../Paginas/PagarCuota';

import CrearCuenta from "../Paginas/CrearCuenta";

import Error from "../Paginas/Error";
import {Route, Routes} from "react-router-dom"
//Destructing, nos ayudara  a colocar todas las rutas de la pagina


const Routers = () => {
    return <Routes>
        <Route path="/" element={<Home />} /> 
        <Route path="/home" element={<Home />} /> 
        <Route path="/login" element={<Logearse />} />
        <Route path="/logout" element={<Logout />} /> 
        <Route path="/registrarse" element={<Registrarse />} /> 
        <Route path="/cliente" element={<Clientes />} />

        <Route path="/deposito/:id" element={<Deposito />} />
        <Route path="/retiro/:id" element={<Retiro />} />
        <Route path="/movimiento/:id" element={<Movimiento />} />
        
        {/* <Route path="/crear_prestamo/:id" element={<CrearPrestamo />}/> */}
        <Route path="/deposito_cuota/:id" element={<DepositoCuota />} />
        
        <Route path="/nueva_cuenta/:id" element={<CrearCuenta />} />

        <Route path="/*" element={<Error />} />
    </Routes>
}

export default Routers