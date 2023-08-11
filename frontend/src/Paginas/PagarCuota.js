import swal from "sweetalert";
import axios from "axios";
import { useState,useEffect }  from "react";
import { useNavigate, useParams} from "react-router-dom";
const URI = "http://localhost:8080/prestamo/"
const URI1 = "http://localhost:8080/cuenta/"


//sessionStorage.setItem("usuario",
//sessionStorage.setItem("clave",123456);
let headers = {
    "usuario" : sessionStorage.getItem("usuario"),
    "clave"   : sessionStorage.getItem("clave")
  };

const Deposito_prestamo = () => {
   
    const [id_prestamo, setId_prestamo] = useState("");
    const [saldo_solicitado, setSaldo_solicitado] = useState("");
    const [n_cuotas, setN_cuotas] = useState("");
    const [valor_cuota, setValor_cuota] = useState("");
    const [valor_deposito_c, setValor_deposito_c] = useState("");
    const [id_cuenta, setId_cuenta] = useState("");
    const navigate = useNavigate();

    
    const {id} = useParams()

    const editar = async (e) => {
        e.preventDefault();

        // Verificar si el valor del depósito es menor que el valor de la cuota
        if (valor_deposito_c < valor_cuota) {
            swal("Error", "El valor del depósito debe ser igual o mayor al valor de la cuota", "error");
            return; // Evitar continuar con la actualización
          }
          
        const UpdatePrestamo= await axios({
            method: "PUT",
            url: URI + "deposito_cuota?idp="+id_prestamo+"&valor_deposito_c="+valor_deposito_c,
            headers: headers 
           
          });
          
        navigate("/cliente");
      };

      useEffect( ()=>{
        getPrestamos()
        getCuentaById()
    },[])

    const getPrestamos = async () =>{
        
        const res = await axios({
            method : "GET",
            url : URI + "list/"+id,
            headers: headers
           
        });
        
        setId_prestamo(res.data.id_prestamo)
        setSaldo_solicitado(res.data.saldo_solicitado)
        setN_cuotas(res.data.n_cuotas)
        setValor_cuota(res.data.valor_cuota)    
            
        
    }

    const getCuentaById = async () => {
        const res1 =  await axios({
            method: "GET",
            url: URI1+"list/"+id,
            headers: headers 
          });
        //alert(URI+"list/"+id)
        setId_cuenta(res1.data.id_cuenta)
    }
     
  


    return(
        <div>
        <h3>Depósito cuota</h3>
        <div className="container col-2">
        <form onSubmit={editar}>
            <div className="mb-3">
            <label className="form-label">ID</label>
            <textarea
                value={id_prestamo}
                onChange={(e) => setId_prestamo(e.target.value)}
                type="number"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">ID cuenta</label>
            <textarea
                value={id_cuenta}
                onChange={(e) => setId_cuenta(e.target.value)}
                type="text"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">Saldo solicitado</label>
            <textarea
                value={saldo_solicitado}
                onChange={(e) => setSaldo_solicitado(e.target.value)}
                type="number"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">Numero de cuotas</label>
            <textarea
                value={n_cuotas}
                onChange={(e) => setN_cuotas(e.target.value)}
                type="number"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">Valor de cuota</label>
            <textarea
                value={valor_cuota}
                onChange={(e) => setValor_cuota(e.target.value)}
                type="number"
                disabled="false"
                className="form-control"
            />
            </div>
            <div className="mb-3">
            <label className="form-label">Valor Depósito Cuota</label>
            <textarea
                value={valor_deposito_c}
                onChange={(e) => setValor_deposito_c(e.target.value)}
                type="number"
                required
                onInvalid={e => e.target.setCustomValidity('El campo Valor Depósito cuota es obligatorio')}
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

export default Deposito_prestamo;