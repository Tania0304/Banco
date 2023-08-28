package com.unab.banca.Service;
import com.unab.banca.Models.Transaccion;
import com.unab.banca.Dao.TransaccionDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

// Anotación que indica que la clase es un componente de servicio de Spring
@Service
public class TransaccionService {

    // Inyecta una dependencia de "TransaccionDao", lo que permite que esta clase utilice los métodos de "TransaccionDao"
    @Autowired
    private TransaccionDao transaccionDao;

    // Guarda un objeto "transaccion" en la base de datos usando el método "save()" proporcionado por "transaccionDao"
    @Transactional(readOnly=false)
    public Transaccion save(Transaccion transaccion) {
        return transaccionDao.save(transaccion);
    }

    // Elimina un objeto "transaccion" en la base de datos usando el método "deleteById()" proporcionado por "transaccionDao"
    @Transactional(readOnly=false)
    public void delete(Integer id) {
        transaccionDao.deleteById(id);;
    }

    // Busca un objeto "transaccion" en la base de datos por su "id" usando el método "findById()" proporcionado por "transaccionDao"
    @Transactional(readOnly=true)
    public Transaccion findById(Integer id) {
        return transaccionDao.findById(id).orElse(null);
    }

    // Busca y devuelve una lista de objetos "transaccion"
    @Transactional(readOnly=true)
    public List<Transaccion> findByAll() {
        // Retorna una lista de información de objetos "transaccion"
        return (List<Transaccion>) transaccionDao.findAll();
    }

    // Busca y devuelve una lista de objetos "transaccion" identificados por el "idcta" de cuenta
    @Transactional(readOnly=true)
    public List<Transaccion> consulta_transaccion(String idcta) {
        // Retorna una lista de información de objetos "transaccion" especificados por el "idcta" de cuenta
        return (List<Transaccion>) transaccionDao.consulta_transaccion(idcta);
    }

    // Crea una nueva transacción en la base de datos con los valores especificados
    @Transactional(readOnly=false)
    public void cear_transaccion(String idcta, Double valor_transaccion, String tipo) {
        transaccionDao.crear_transaccion(idcta, valor_transaccion, tipo);
    }

}
