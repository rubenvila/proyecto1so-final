package proyecto1vilapinto;
import java.util.concurrent.Semaphore;
/**
 *
 * @author Andrea
 */
public class Procesador {
    private int id;
    private Proceso procesoActual;
    public boolean estaOcioso;
    private Semaphore semaforo;
    public int ciclosBloqueado;

    public Procesador(int id) {
        this.id = id;
        this.procesoActual = null;
        this.semaforo = new Semaphore(1);
        this.estaOcioso = true;
        this.ciclosBloqueado = 0;
    }

    public int getId() { return id; }
    public Proceso getProcesoActual() { return procesoActual; }
    public boolean estaOcioso() { return estaOcioso; }

    public void asignarProceso(Proceso proceso) {
        try {
            semaforo.acquire(); // Adquirir el semáforo
            this.estaOcioso = false;
            this.procesoActual = proceso;
            
            // Iniciar un hilo para ejecutar el proceso
            Thread hiloProceso = new Thread(proceso);
            hiloProceso.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void liberarProcesador() {
        this.procesoActual = null;
        semaforo.release();
        this.estaOcioso = true;
    }
    
    public void reiniciar() {
        this.procesoActual = null;
        this.estaOcioso = true; 
        this.semaforo = new Semaphore(1); 
    }
}
