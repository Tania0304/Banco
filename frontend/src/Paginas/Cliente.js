import swal from "sweetalert"
import axios from "axios";
import { useState,useEffect }  from "react";
import { Link,useNavigate, useParams} from "react-router-dom";
const URI = "http://localhost:8080/cuenta/"
const URI1 = "http://localhost:8080/prestamo/"

let headers = {
    "usuario" : sessionStorage.getItem("usuario"),
    "clave"   : sessionStorage.getItem("clave")
  };
const Cuenta = () => {
    //la asignacion useNavegate a la constante navigate puede ayudar a redirigir a otras rutas de la aplicacion
    const navigate = useNavigate();
    //useState[] agregar el estado a componentes funcionales, se declara la variable cuentas y la funcion setCuentas para actualizar el estado 
    const [cuentas, setCuentas] = useState([])

    useEffect(() =>{
        getCuentas()
    })

    const getCuentas = async () =>{
        //e.preventDefault();
        try {
            
            const res = await axios({
                method : "GET",
                url : URI + "consulta_cuenta?idc="+sessionStorage.getItem("usuario"),
                headers: headers 
               
            });
            
                setCuentas(res.data)

            //console.log(clientes)
        }
        catch (error) {
            swal("No tiene Acceso a esta Opción!", "Presiona el butón!", "error");
            navigate("/");
        }
    }

    const [prestamos, setPrestamos] = useState([])
    useEffect(() =>{
        getPrestamos()
    })
    const {id} = useParams()
    const getPrestamos = async () =>{
        try {
            
            const res1 = await axios({
                method : "GET",
                url : URI1 + "consulta_prestamo?idc="+sessionStorage.getItem("usuario"),
                headers: headers 
               
            });
            
            setPrestamos(res1.data)
            
            //console.log(clientes)
        }
        catch (error) {
            alert("No tiene Acceso a esta Opción");
            navigate("/");
        }
    }

    return(
        <div className='container'>
            <div className="row">
                <h4>Funciones</h4>
            </div>
            <div className="row">
                <div className="col-2">
                    <button
                        className="btn btn-secondary"
                        onClick={() => navigate(`/nueva_cuenta/${sessionStorage.getItem("usuario")}`)}
                    >Crear Cuenta</button>
                </div>
                <div className="col-2">
                    <button
                        className="btn btn-primary"
                        onClick={() => navigate(`/crear_prestamo/${sessionStorage.getItem("usuario")}`)}
                    >Crear Prestamo</button>
                </div>
                
            </div>
            <hr/>
            <div className='row'>
                <h3 className="d-flex justify-content-center pb-3">Información cuentas</h3>
                <div className='col'>
                    <table className='table'>
                        <thead className='table-primary'>
                            <tr>
                                <th>Cuenta</th>
                                <th>Fecha Apertura</th>
                                <th>Saldo</th>
                                <th>Años de antiguedad</th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            { cuentas.map ( (cuenta) => (
                               
                                <tr key={ cuenta.id_cuenta}>
                                    <td> { cuenta.id_cuenta } </td>
                                    <td> { cuenta.fecha_apertura.substring(0,10) } </td>
                                    <td> { cuenta.saldo_cuenta } </td>
                                    <td> { cuenta.antiguedad} </td>
                                    <td >
                                        <Link to={`/deposito/${cuenta.id_cuenta}`} className='btn btn-info'><i className="fas fa-donate"></i>Depósito</Link>
                                        <Link to={`/retiro/${cuenta.id_cuenta}`} className='btn btn-warning'><i className="fas fa-money-bill-alt"></i>Retiro</Link>
                                        <Link to={`/movimiento/${cuenta.id_cuenta}`} className='btn btn-success'><i className="fas fa-file-invoice-dollar"></i>Movimientos</Link>
                                    </td>
                                </tr>
                            )) }
                           
                        </tbody>
                    </table>
                </div>    
            </div>
            <div className="row">
                <div className="col">
                   
                    <h3 className="d-flex justify-content-center pb-3 pt-3">Información prestamos</h3>

                    <table className='table'>
                            <thead className='table-primary'>
                                <tr>
                                    <th>Id</th>
                                    <th>Fecha solicitud</th>
                                    <th>Saldo solicitado</th>
                                    <th>N. Cuotas</th>
                                    <th>valor cuota</th>
                                    <th>Saldo Pendiente</th>
                                </tr>
                            </thead>
                            <tbody>
                                { prestamos.map ( (prestamo) => (
                                    <tr key={ prestamo.id_prestamo}>
                                        <td> { prestamo.id_prestamo } </td>
                                        <td> { prestamo.fecha_solicitud } </td>
                                        <td> { prestamo.saldo_solicitado } </td>
                                        <td> { prestamo.n_cuotas } </td>
                                        <td> { prestamo.valor_cuota } </td>
                                        <td> { prestamo.saldo_pendiente } </td>
                                        <td className="ps-5 pe-0">
                                            <Link to={`/deposito_cuota/${prestamo.id_prestamo}`} className='btn btn-info'><i className="fas fa-donate"></i>Pagar cuota</Link>
                                        </td>
                                    </tr>
                                )) }
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
    );
};

export default Cuenta;