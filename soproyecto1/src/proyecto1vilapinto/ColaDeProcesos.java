package proyecto1vilapinto;


import java.io.BufferedWriter;
import java.io.IOException;
import javax.swing.DefaultListModel;

/**
 *
 * @author Luisitus
 */
public class ColaDeProcesos {
    private Proceso[] procesos; // Arreglo de procesos
    private int frente; // Índice del frente de la cola
    private int finalCola; // Índice del final de la cola
    private int capacidad; // Capacidad de la cola

    // Constructor
    public ColaDeProcesos(int capacidad) {
        this.capacidad = capacidad;
        this.procesos = new Proceso[capacidad];
        this.frente = 0;
        this.finalCola = -1;
    }

    public void agregar(Proceso proceso) {
        if (finalCola < capacidad - 1) {
            finalCola++;
            procesos[finalCola] = proceso;
        } else {
            // System.out.println("Cola llena, no se pueden agregar más procesos.");
        }
    }

    public Proceso eliminar() {
        if (frente > finalCola) {
            System.out.println("Cola vacía, no hay procesos para eliminar.");
            return null;
        } else {
            Proceso proceso = procesos[frente];
            frente++;
            return proceso;
        }
    }
    
    public boolean eliminar(Proceso procesoAEliminar) {
        for (int i = frente; i <= finalCola; i++) {
            if (procesos[i].equals(procesoAEliminar)) {
                // Mover los elementos restantes una posición hacia la izquierda
                for (int j = i; j < finalCola; j++) {
                    procesos[j] = procesos[j + 1];
                }
                procesos[finalCola] = null; // Eliminar la referencia al último elemento
                finalCola--; // Reducir el tamaño de la cola
                return true; // Proceso eliminado
            }
        }
        return false; // Proceso no encontrado
    }
    
    public boolean contieneNombre(String nombre) {
        for (int i = frente; i <= finalCola; i++) {
            if (procesos[i].getNombre().equals(nombre)) { 
                return true; 
            }
        }
        return false; 
    }

    public boolean estaVacia() {
        return frente > finalCola;
    }

    public int tamaño() {
        return finalCola - frente + 1;
    }
    
    public void limpiar() {
        frente = 0;
        finalCola = -1;
        // System.out.println("Cola limpiada."); // Eliminar o mover este mensaje
    }

    public Proceso obtener(int indice) {
        if (indice >= 0 && indice < tamaño()) {
            return procesos[frente + indice]; 
        } else {
            return null; 
        }
    }
    
    public boolean contiene(Proceso procesoABuscar) {
        for (int i = frente; i <= finalCola; i++) {
            if (procesos[i].equals(procesoABuscar)) {
                return true; // El proceso está en la cola
            }
        }
        return false; // El proceso no está en la cola
    }
    
    public DefaultListModel<String> obtenerNombresDeProcesos() {
        DefaultListModel<String> modeloNombresProcesos = new DefaultListModel<>();
        for (int i = 0; i < procesos.length; i++) {
            Proceso proceso = procesos[i];
            if (proceso != null) {
                modeloNombresProcesos.addElement(proceso.getNombre());
            }
        }
        return modeloNombresProcesos;
    }

    public void iterarProcesos(BufferedWriter writer) throws IOException {
        if (estaVacia()) {
            writer.write("No hay procesos en la cola.");
            writer.newLine();
            return;
        }
        
        int i = frente;
        
        while (true) {
            Proceso proceso = procesos[i];
            if (proceso != null) {
                writer.write(proceso.getId() + ", " +              
                             proceso.getNombre() + ", " +            
                             proceso.getInstrucciones() + ", " +    
                             (proceso.esCpuLimitado() ? "true" : "false") + ", " + 
                             proceso.getCiclosDeExcepción() + ", " +  
                             proceso.getCiclosDeSatisfacción() + ", " + 
                             proceso.getContadorDePrograma() + ", " +   
                             proceso.getMAR() + ", " + 
                             proceso.getEstado());             
                writer.newLine();
            }
            
            if (i == finalCola) break; 
            i = (i + 1) % capacidad; 
        }
    }

    public void imprimirCola() {
        if (finalCola == -1) {
            System.out.println("La cola está vacía.");
            return;
        }
        
        System.out.print("Contenido de la cola: ");
        
        for (int i = frente; i <= finalCola; i++) {
            System.out.print(procesos[i].getNombre() + ", ");
        }
        
        System.out.println();
    }
}
