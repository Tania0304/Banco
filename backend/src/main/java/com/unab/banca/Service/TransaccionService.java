package com.unab.banca.Service;
import com.unab.banca.Models.Transaccion;
import com.unab.banca.Dao.TransaccionDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
@Service
public class TransaccionService {
    @Autowired
    private TransaccionDao transaccionDao;

    @Transactional(readOnly=false)
    public Transaccion save(Transaccion transaccion) {
        return transaccionDao.save(transaccion);
    }

    @Transactional(readOnly=false)
    public void delete(Integer id) {
        transaccionDao.deleteById(id);;
    }

    @Transactional(readOnly=true)
    public Transaccion findById(Integer id) {
        return transaccionDao.findById(id).orElse(null);
    }

    @Transactional(readOnly=true)
    public List<Transaccion> findByAll() {
        return (List<Transaccion>) transaccionDao.findAll();
    }

    @Transactional(readOnly=true)
    public List<Transaccion> consulta_transaccion(String idcta) {
        return (List<Transaccion>) transaccionDao.consulta_transaccion(idcta);
    }

    @Transactional(readOnly=false)
    public void cear_transaccion(String idcta, Double valor_transaccion, String tipo) {
        transaccionDao.crear_transaccion(idcta, valor_transaccion, tipo);
    }

}
