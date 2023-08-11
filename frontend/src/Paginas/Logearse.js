
import swal from "sweetalert";
import axios from "axios";
import { useState }  from "react";
import { useNavigate } from "react-router-dom";
const URI = "http://localhost:8080/cliente/"

const Logearse = () => {
    const navigate = useNavigate();
    const [clientes, setClientes] = useState([])
    const [id_cliente, setId_cliente] = useState("");
    const [clave_cliente, setClave_cliente] = useState("");
   

    const guardar = async (e) => {
        e.preventDefault();
        
        try {          
            const res = await axios({
                method : "GET",
                url: URI + "login?usuario="+id_cliente+"&clave="+clave_cliente
            });

            
            setClientes(res.data)
            
            if (res.data.id_cliente==null) {
                
                swal("Cliente NO Autorizado!", "Presiona el butón!", "error");
                navigate("/");
                
            } else {
               sessionStorage.setItem("usuario",id_cliente);
               sessionStorage.setItem("clave",clave_cliente);
               swal("Bienvenido "+res.data.nombre_cliente+"!", "Presiona el butón!", "success");
               navigate("/");
            }
            //navigate("/clientes");
            
        }
        catch (error) {
            swal("Operación NO realizada")
        }

      };

    return(
        <div>
           
            <div className="container  col-2 pt-3">
            <h3 className="d-flex justify-content-center">Iniciar sesión</h3>
            <form onSubmit={guardar}>
                
                <div className="mb-3">
                    <label className="form-label">ID</label>
                    <textarea
                        value={id_cliente}
                        onChange={(e) => setId_cliente(e.target.value)}
                        type="text"
                        maxLength={15}
                        required
                        onInvalid={e => e.target.setCustomValidity('El campo Id cliente es obligatorio')}
                        onInput={e => e.target.setCustomValidity('')}
                        className="form-control"
                />
                </div>
                <div className="mb-3">
                    <label className="form-label">Clave</label>
                    <input
                        value={clave_cliente}
                        onChange={(e) => setClave_cliente(e.target.value)}
                        type="password"
                        maxLength={50}
                        required
                        onInvalid={e => e.target.setCustomValidity('El campo Contraseña  es obligatorio')}
                        onInput={e => e.target.setCustomValidity('')}
                        className="form-control"
                    />
                </div>
                <div className="d-flex justify-content-center">
                    <button type="submit" className="btn btn-primary">
                    Iniciar Sesión
                    </button>
                </div>
            </form>
        </div>
        </div> 
    );
};

export default Logearse;