import swal from "sweetalert";
import banner from "../imagenes/7931432.jpg"
import { useNavigate } from "react-router-dom";
const Logout = () => { 
    sessionStorage.removeItem("usuario")
    sessionStorage.removeItem("clave")
    const navigate = useNavigate();
    swal("Sesión Finalizada!", "Presiona el butón!", "success");
        navigate("/");
    return(
        <div>
            <img className="banner" src={banner} alt="Banner banco central" />
        </div>
        
    )
}

export default Logout