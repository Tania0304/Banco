import axios from "axios";
import { useState,useEffect }  from "react";
import { useNavigate, useParams} from "react-router-dom";
const URI = "http://localhost:8080/cuenta/"
const URI1 = "http://localhost:8080/transaccion/"

let headers = {
    "usuario" : sessionStorage.getItem("usuario"),
    "clave"   : sessionStorage.getItem("clave")
  };
const Deposito = () => {
    const [id_cuenta, setId_cuenta] = useState("");
    const [fecha_apertura, setFecha_apertura] = useState("");
    const [saldo_cuenta, setSaldo_cuenta] = useState("");
    const [valor_deposito, setValor_deposito] = useState("");
    const navigate = useNavigate();

    const {id} = useParams()

    const editar = async (e) => {
        e.preventDefault();
        const UpdateCuenta= await axios({
            method: "PUT",
            url: URI + "deposito?idcta="+id_cuenta+"&valor_deposito="+valor_deposito,
            headers: headers 
          });
        
        const InsertTransaccion= await axios({
            method: "POST",
            url: URI1+"crear_transaccion?idcta="+id_cuenta+"&valor_transaccion="+valor_deposito+"&tipo=D",
            headers: headers 
          });
          
        navigate("/cliente");
    };

      useEffect( ()=>{
        getCuentaById()
    },[])

    const getCuentaById = async () => {

        const res =  await axios({
            method: "GET",
            url: URI+"list/"+id,
            headers: headers 
          });
        //alert(URI+"list/"+id)
        setId_cuenta(res.data.id_cuenta)
        setFecha_apertura(res.data.fecha_apertura)
        setSaldo_cuenta(res.data.saldo_cuenta)
    }

    return(
        <div>
        <h3>Depósito</h3>
        <div className="container col-2">
        <form onSubmit={editar}>
            <div className="mb-3">
            <label className="form-label">ID</label>
            <textarea
                value={id_cuenta}
                onChange={(e) => setId_cuenta(e.target.value)}
                type="text"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">Fecha Apertura</label>
            <textarea
                value={fecha_apertura.substring(0,10)}
                onChange={(e) => setFecha_apertura(e.target.value)}
                type="text"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">Saldo Cuenta</label>
            <textarea
                value={saldo_cuenta}
                onChange={(e) => setSaldo_cuenta(e.target.value)}
                type="number"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">Valor Depósito</label>
            <textarea
                value={valor_deposito}
                onChange={(e) => setValor_deposito(e.target.value)}
                type="number"
                required
                onInvalid={e => e.target.setCustomValidity('El campo Valor Depósito es obligatorio')}
                onInput={e => e.target.setCustomValidity('')}
                className="form-control"
            />
            </div>
            <button type="submit" className="btn btn-primary">
            Guardar
            </button>
        </form>
    </div>
    </div>
    );
};

export default Deposito;