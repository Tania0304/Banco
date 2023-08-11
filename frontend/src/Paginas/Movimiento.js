import swal from "sweetalert"
import axios from "axios";
import { useState,useEffect }  from "react";
import { useNavigate, useParams} from "react-router-dom";
const URI = "http://localhost:8080/transaccion/"


//sessionStorage.setItem("usuario",
//sessionStorage.setItem("clave",123456);
let headers = {
    "usuario" : sessionStorage.getItem("usuario"),
    "clave"   : sessionStorage.getItem("clave")
  };

const MostrarTransaccion = () => {
    const navigate = useNavigate();
    const [transacciones, setTransacciones] = useState([])
    useEffect(() =>{
        getTransacciones()
    })
    const {id} = useParams()
    const getTransacciones = async () =>{
        try {
            
            const res = await axios({
                method : "GET",
                url : URI + "consulta_transaccion?idcta="+id,
                headers: headers 
               
            });
            
            setTransacciones(res.data)
            
            //console.log(clientes)
        }
        catch (error) {
            alert("No tiene Acceso a esta Opción");
            navigate("/");
        }
    }

     
return(
        <div className='container'>
            <div className='row'>
                <div className='col'>
                    <table className='table'>
                        <thead className='table-primary'>
                            <tr>
                                <th>Id</th>
                                <th>Fecha</th>
                                <th>Valor</th>
                                <th>Tipo</th>
                            </tr>
                        </thead>
                        <tbody>
                            { transacciones.map ( (transaccion) => (
                                <tr key={ transaccion.id_transaccion}>
                                    <td> { transaccion.id_transaccion } </td>
                                    <td> { transaccion.fecha_transaccion } </td>
                                    <td> { transaccion.valor_transaccion } </td>
                                    <td> { transaccion.tipo_transaccion } </td>
                                    
                                </tr>
                            )) }
                        </tbody>
                    </table>
                </div>    
            </div>
        </div>
    );
};

export default MostrarTransaccion;