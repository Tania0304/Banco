import React from "react";
import logo from "./../../imagenes/Screenshot 2023-07-31 023349.png";


const Menu = () => {
    return (
      
        <div className="contanier">
          <div className="col logos">
            <div className="image-container d-flex justify-content-center pt-2">
              <img className="img-fluid logo" src={logo} alt="Logo banco central"></img>
            </div>
          </div>
        
          <div className="col d-flex justify-content-center"> 
            <nav className="navbar navbar-expand-lg navbar_p  ms-2 ">  
              <div className="container-fluid">
                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                  <li className="nav-item">
                    <a className="nav-link active links" aria-current="page" href="/">
                      Inicio
                    </a>
                  </li>
                  <li className="nav-item">
                    <a className="nav-link active" aria-current="page" href="/cliente">
                    Operaciones
                    </a>
                  </li>
                  <li className="nav-item">
                    <a className="nav-link" href="/">
                      <p>|</p>
                    </a>
                  </li>
                  <li className="nav-item">
                    <a className="nav-link" href="/login">
                      Iniciar sesión
                    </a>
                  </li>
                  <li className="nav-item">
                    <a className="nav-link" href="/logout">
                      Cerrar sesión
                    </a>
                  </li>
                  <li className="nav-item active">
                    <a className="nav-link" href="/registrarse">
                      Crear Usuario
                    </a>
                  </li>
                </ul>
              </div>
            </nav>
          </div>
        </div>
    );
  };
  
  export default Menu;