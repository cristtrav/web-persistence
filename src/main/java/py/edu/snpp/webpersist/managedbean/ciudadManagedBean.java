package py.edu.snpp.webpersist.managedbean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import py.edu.snpp.webpersist.entidades.Ciudad;

/**
 *
 * @author traver
 */
@Named(value = "ciudadManagedBean")
@RequestScoped
public class ciudadManagedBean {

    private int accion = 1;

    @PersistenceContext(unitName = "py.edu.snpp_webpersist_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    private String nombre;
    private Ciudad ciudadSeleccionada;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Ciudad getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudad ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public ciudadManagedBean() {
    }

    public void agregar() {
        System.out.print("Agregar");
        Ciudad c = new Ciudad();
        c.setNombre(this.nombre);
        try {
            utx.begin();
            em.persist(c);
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

    public void modificar() {
        System.out.print("Modificar");
        Ciudad c = new Ciudad();
        c.setNombre(this.nombre);
        c.setIdciudad(this.id);
        try {
            utx.begin();
            em.merge(c);
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

    public List<Ciudad> getCiudades() {
        TypedQuery<Ciudad> q = this.em.createQuery("SELECT c FROM Ciudad c", Ciudad.class
        );
        return q.getResultList();
    }

    public void seleccionarCiudad() {
        if (this.ciudadSeleccionada != null) {
            this.accion = 2;
            this.id = this.ciudadSeleccionada.getIdciudad();
            this.nombre = this.ciudadSeleccionada.getNombre();
        } else {
            this.accion = 1;
            this.id = null;
            this.nombre = null;
        }
    }
    
    public void eliminarCiudad(Integer idciudad){
        
        try {
            utx.begin();
            em.remove(this.em.find(Ciudad.class, idciudad));
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}
