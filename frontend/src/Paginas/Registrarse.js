import swal from "sweetalert";
import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";

const URI = "http://localhost:8080/registrarse"

const Registrarse = () => {
    const navigate = useNavigate();

    const [usuario, setUsuario] = useState({
        nombre_cliente: "",
        clave_cliente: ""
    });

    const [id_cliente, setIdCliente] = useState(null);
    const [inputId, setInputId] = useState(""); // Estado para almacenar el ID ingresado por el usuario
    const [isButtonDisabled, setButtonDisabled] = useState(true); // Estado para habilitar/deshabilitar el botón "Aceptar"

    const handleChange = (event) => {
        const { name, value } = event.target;
        setUsuario({ ...usuario, [name]: value });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        if (usuario.clave_cliente && usuario.nombre_cliente) {
            axios.post(`${URI}`, usuario)
                .then((response) => {
                    const { data } = response;
                    setIdCliente(data.id_cliente);
                    swal({
                        title: "¡Registro exitoso!",
                        text: `El usuario ha sido registrado correctamente. Su idenficiación de usuario (ID) será ${data.id_cliente}, asegurece de guardarlo`,
                        icon: "success",
                        content: {
                            element: "input", // Agregar un campo de entrada para que el usuario escriba el ID
                            attributes: {
                                placeholder: "Escribe el ID aquí",
                            },
                        },
                        buttons: {
                            confirm: {
                                text: "Aceptar",
                                value: true,
                            },
                        },
                        closeOnClickOutside: false, // Evitar que la alerta se cierre al hacer clic fuera de ella
                        closeOnEsc: false, // Evitar que la alerta se cierre al presionar la tecla "Esc"
                    }).then((value) => {
                        // Verificar el ID ingresado por el usuario antes de cerrar la alerta
                        if (value === data.id_cliente) {
                            navigate("../home");
                        } else {
                            swal("¡ID incorrecto!", "Por favor, ingresa el ID correcto.", "error");
                        }
                    });
                })
                .catch((error) => {
                    swal("¡Error!", "Ha ocurrido un error al registrar el usuario.", "error");
                });
        } else {
            swal("¡Campos incompletos!", "Por favor, completa todos los campos.", "warning");
        }
    };

    // Función para habilitar/deshabilitar el botón "Aceptar" según el estado del formulario
    const handleInputIdChange = (event) => {
        setInputId(event.target.value);
        setButtonDisabled(event.target.value !== id_cliente); // Habilitar el botón solo si el ID ingresado coincide con el generado
    };

    return (
        <div>
            <div className="container  col-2 pt-3">
                <h3 className="d-flex justify-content-center">Registro de Usuario</h3>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Nombre:</label>
                        <input className="form-control" type="text" name="nombre_cliente" onChange={handleChange} />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Clave:</label>
                        <input className="form-control" type="password" name="clave_cliente" onChange={handleChange} />
                    </div>
                    <div className="d-flex justify-content-center">
                        <button className="btn btn-primary" type="submit">Registrar</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Registrarse;
