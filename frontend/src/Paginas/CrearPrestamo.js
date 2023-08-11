// import axios from "axios";
// import React, { useState, useEffect } from "react";
// import { useNavigate, useParams } from "react-router-dom";
// const URI = "http://localhost:8080/prestamo/"
// const URI1 = "http://localhost:8080/cuenta/"
// const URI2 = "http://localhost:8080/cliente/"

// let headers = {
//   "usuario" : sessionStorage.getItem("usuario"),
//   "clave"   : sessionStorage.getItem("clave")
// };

// const CrearPrestamo = () => {  
//   const [saldo_solicitado, setSaldo_solicitado] = useState(0);
//   const [cuotas, setCuotas] = useState(0);
//   const [id_cuenta, setId_cuenta] = useState("");

//   const [fecha_solicitud, setFecha_solicitud] = useState("");
//   const []
//   const navigate = useNavigate();

//   const {id} = useParams()
  
//   const handleSubmit = async (e) => {
//     // e.preventDefault();
//     // const InsertPrestamo = await axios({
//     //   method: "POST",
//     //   url: URI+"crear_prestamo?saldo_solicitado="+saldo_solicitado+"&cuotas="+cuotas+"&idcta="id_cuenta,
//     //   headers: headers
//     // });
//     // navigate("/cliente");
//       e.preventDefault();
//       const data = {
//         saldo_solicitado: saldo_solicitado,
//         cuotas: cuotas,
//         idcta: id_cuenta,
//       };
      
//       try {
//         const InsertPrestamo = await axios.post(URI + "crear_prestamo", data, { headers });
//         navigate("/cliente");
//       } catch (error) {
//         // Manejar el error según tus necesidades
//       }
//   };
//   useEffect(() => {
//     const fetchCuenta = async () => {
//       const res = await axios({
//         method: "GET",
//         url: URI1 + "list/" + id,
//         headers: headers,
//       });
//       setId_cuenta(res.data.id_cuenta);
//     };
  
//     fetchCuenta();
//   }, []);

//   const getCuentaById = async () => {

//     const res =  await axios({
//         method: "GET",
//         url: URI1+"list/"+id,
//         headers: headers 
//       });
//     //alert(URI+"list/"+id)
//     setId_cuenta(res.data.id_cuenta)
//   }

//   const getClienteById = async () => {

//     const res1 =  await axios({
//         method: "GET",
//         url: URI2+"list/"+id,
//         headers: headers 
//       });
//     //alert(URI+"list/"+id)
//     setId_cliente(res1.data.id_cliente)
//   }

//   return (
//     <div className='container'>
//       <h2>Crear Préstamo</h2>
//       <form onSubmit={handleSubmit}>
//       <div className="mb-3">
//           <label className="form-label">Monto</label>
//           <textarea
//             type="number"
//             placeholder="Monto Solicitado"
//             className="form-control"
//             value={saldo_solicitado}
//             onChange={(e) => setSaldo_solicitado(e.target.value)
//             }
//           />
//       </div>
//       <div className="mb-3">
//       <label className="form-label">Número de cuotas</label>
//         <textarea
//           type="number"
//           className="form-control"
//           placeholder="Cuotas"
//           value={cuotas}
//           onChange={(e) => setCuotas(e.target.value)}
//         />
//       </div>
//       <div className="mb-3">
//       <label className="form-label">ID cuenta</label>
//         <textarea
//           type="text"
//           className="form-control"
//           placeholder="ID de Cuenta"
//           value={id_cuenta}
//           onChange={(e) => setId_cuenta(e.target.value)}
//         />
//       </div>
//         <button type="submit" className="btn btn-primary">Crear Préstamo</button>
//       </form>
//     </div>
//   );
// };

// export default CrearPrestamo;
