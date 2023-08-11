import React from "react";
import logo from "./../../imagenes/Screenshot 2023-07-31 023349.png";


const Footer= () => {
    return(
        
        <div className="container ms-0 me-0 ps-5 pt-3 pb-3 ">
            
            <div className="row">
                <hr/>
                <div className="col-9">
                    <img className="img-fluid logo" src={logo} alt="Logo banco central"></img>
                </div>
                <div className="col-3 pt-3">
                    <p>Contactenos: (+57) 456227814</p>
                </div>
            </div>
        </div>
    )    
}

export default Footer