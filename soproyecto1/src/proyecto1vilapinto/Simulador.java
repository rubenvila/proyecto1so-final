package proyecto1vilapinto;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import proyecto1vilapinto.Interfaz;
import java.awt.Color;
/**
 *
 * @author Andrea
 */
public class Simulador {
    public ColaDeProcesos colaListos;
    private ColaDeProcesos colaBloqueados;
    private ColaDeProcesos colaFinalizados;
    private ColaDeProcesos colaGeneral;
    private Procesador[] procesadores;
    private int cicloGlobal;
    private PoliticaDePlanificacion politicaDePlanificacion;
    private int numProcesadores; 
    private Interfaz Interfaz;
    private static Simulador instancia;
    private int duracionCiclo;

    public Simulador(int numProcesadores, Interfaz Interfaz) {
        this.numProcesadores = numProcesadores;  
        this.duracionCiclo = duracionCiclo;
        this.colaListos = new ColaDeProcesos(100);
        this.colaBloqueados = new ColaDeProcesos(100);
        this.colaFinalizados = new ColaDeProcesos(100);
        this.colaGeneral = new ColaDeProcesos(100);
        this.procesadores = new Procesador[numProcesadores];
        for (int i = 0; i < numProcesadores; i++) {
            procesadores[i] = new Procesador(i);
        }
        this.cicloGlobal = 0;
        this.Interfaz = Interfaz;
        this.instancia = instancia;
    }
    
    public static Simulador obtenerInstancia(int numProcesadores, Interfaz Interfaz) {
        if (instancia == null) {
            instancia = new Simulador(numProcesadores, Interfaz); // Pasar vistaPrincipal al constructor
        }
        return instancia;
    }
    
    // Método para cambiar el número de procesadores
    public void setNumProcesadores(int numProcesadores) {
        this.numProcesadores = numProcesadores; 
    }
    
    public int getNumProcesadores() {
        return numProcesadores; 
    }

    public void agregarProceso(Proceso proceso) {
        colaGeneral.agregar(proceso);
    }
    
    public void reiniciar() {
        this.colaListos.limpiar(); 
        this.colaBloqueados.limpiar();
        this.colaFinalizados.limpiar();
        this.colaGeneral.limpiar();
        for (int i = 0; i < numProcesadores; i++) {
            procesadores[i].reiniciar(); 
        }
        this.cicloGlobal = 0;
        this.politicaDePlanificacion = null; 
    }
    
   public void clasificarProcesos() {

    for (int i = 0; i < colaGeneral.tamaño(); i++) {
        Proceso proceso = colaGeneral.obtener(i);

        switch (proceso.getEstado()) {
            case"LISTO":
                if (!colaListos.contiene(proceso)) {
                    colaListos.agregar(proceso); // Agregar a la cola de listos si no está ya
                    colaBloqueados.eliminar(proceso);
                }
                break;
            case "BLOQUEADO":
                if (!colaBloqueados.contiene(proceso)) {
                    colaBloqueados.agregar(proceso); // Agregar a la cola de bloqueados si no está ya
                    colaListos.eliminar(proceso);
                }
                break;
            case "FINALIZADO":
                if (!colaFinalizados.contiene(proceso)) {
                    colaFinalizados.agregar(proceso); 
                    colaBloqueados.eliminar(proceso);
                    colaListos.eliminar(proceso);
                }
                break;
            case "EJECUTANDO":

                colaListos.eliminar(proceso);
                break;
            default:
                System.out.println("Estado desconocido: " + proceso.getEstado());
                break;
        }
    }
}
    

    public void ejecutarCiclo() {
        cicloGlobal++;
        

        for (int i = 0; i < procesadores.length; i++) {
            if (procesadores[i].estaOcioso() && !colaListos.estaVacia()) {
                Proceso proceso = colaListos.eliminar(); 
                procesadores[i].asignarProceso(proceso); 
            }
        }
        
        for (int i = 0; i < procesadores.length; i++) {
            if (!procesadores[i].estaOcioso()) {
                Proceso proceso = procesadores[i].getProcesoActual();
 
                if (proceso.haTerminado()) {
                    procesadores[i].liberarProcesador(); // Liberar el procesador
                    proceso.setEstado("FINALIZADO");  
                    break;
                }
                else if (proceso.estaBloqueado() && procesadores[i].ciclosBloqueado != proceso.getCiclosDeSatisfacción()&&!("LISTO").equals(proceso.getEstado())){ 
                    proceso.setEstado("BLOQUEADO");
                    procesadores[i].ciclosBloqueado++;
                    Interfaz.ExecutionModeLabel.setForeground(Color.RED);
                    Interfaz.ExecutionModeLabel.setText("Kernel");
                }else if(proceso.estaBloqueado() && procesadores[i].ciclosBloqueado == proceso.getCiclosDeSatisfacción()){
                    procesadores[i].ciclosBloqueado =0;
                    proceso.setEstado("LISTO");
                    procesadores[i].liberarProcesador();
                    break;
                }
                else if("LISTO".equals(proceso.getEstado())||"EJECUTANDO".equals(proceso.getEstado())){
                    proceso.setEstado("EJECUTANDO");
                    proceso.incrementarContadorDePrograma();
                    proceso.incrementarMAR();
                }                      
            }
        }
    }
    
    private void asignarProcesos() {
        for (Procesador procesador : procesadores) {
            if (procesador.estaOcioso() && !colaListos.estaVacia()) {
                Proceso proceso = colaListos.eliminar();
                procesador.asignarProceso(proceso); 
            }
        }
    }
      

    public DefaultTableModel actualizarTablaDeProcesos() {
        DefaultTableModel model = new DefaultTableModel();
        for (int i = 0; i < numProcesadores; i++) {
            model.addColumn("Procesador " + (i + 1));
        }
        Object[] rowData = new Object[numProcesadores];
        for (int i = 0; i < numProcesadores; i++) {
            Procesador procesador = procesadores[i]; // 
            Proceso procesoActual = procesador.getProcesoActual();
            String nombreProceso = procesoActual != null ? procesoActual.getNombre() : "Ninguno";
            rowData[i] = nombreProceso;
        }
        model.addRow(rowData);
        return model;
    }

    public ColaDeProcesos reordenarPorSPN() {
        int tamañoCola = colaListos.tamaño(); 
        ColaDeProcesos colaOrdenada = new ColaDeProcesos(tamañoCola); 
        Proceso[] procesos = new Proceso[tamañoCola];
        for (int i = 0; i < tamañoCola; i++) {
            procesos[i] = colaListos.obtener(i); 
        }
        for (int i = 0; i < tamañoCola - 1; i++) {
            for (int j = 0; j < tamañoCola - 1 - i; j++) {
                if (procesos[j].getInstrucciones() > procesos[j + 1].getInstrucciones()) {
                    Proceso tmp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = tmp;
                }
            }
        }
        for (Proceso p : procesos) {
            colaOrdenada.agregar(p); 
        }
        return colaOrdenada; 
    }
    
    public ColaDeProcesos reordenarPorSRT() {
        int tamañoCola = colaListos.tamaño();
        ColaDeProcesos colaOrdenada = new ColaDeProcesos(tamañoCola);
        Proceso[] procesos = new Proceso[tamañoCola];
        for (int i = 0; i < tamañoCola; i++) {
            procesos[i] = colaListos.obtener(i);
        }
        for (int i = 0; i < tamañoCola - 1; i++) {
            for (int j = 0; j < tamañoCola - 1 - i; j++) {
                if (procesos[j].getInstruccionesRestantes() > procesos[j + 1].getInstruccionesRestantes()) {
                    Proceso tmp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = tmp;
                }
            }
        }
        for (Proceso p : procesos) {
            colaOrdenada.agregar(p);
        }

        return colaOrdenada; 
    }
    

    public ColaDeProcesos reordenarPorHRRN() {
        int tamañoCola = colaListos.tamaño();
        ColaDeProcesos colaOrdenada = new ColaDeProcesos(tamañoCola);
        Proceso[] procesos = new Proceso[tamañoCola];
        for (int i = 0; i < tamañoCola; i++) {
            procesos[i] = colaListos.obtener(i);
        }
        double[] razonesDeRespuesta = new double[tamañoCola];
        int tiempoActual = cicloGlobal;
        for (int i = 0; i < tamañoCola; i++) {
            Proceso p = procesos[i];
            razonesDeRespuesta[i] = (tiempoActual - p.getOrdenDeLlegada() + p.getInstruccionesRestantes()) / (double) p.getInstruccionesRestantes();
        }
        for (int i = 0; i < tamañoCola - 1; i++) {
            for (int j = 0; j < tamañoCola - 1 - i; j++) {
                if (razonesDeRespuesta[j] < razonesDeRespuesta[j + 1]) {
                    // Intercambiar procesos
                    Proceso tmp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = tmp;
                    double tempRatio = razonesDeRespuesta[j];
                    razonesDeRespuesta[j] = razonesDeRespuesta[j + 1];
                    razonesDeRespuesta[j + 1] = tempRatio;
                }
            }
        }
        for (Proceso p : procesos) {
            colaOrdenada.agregar(p);
        }
        return colaOrdenada;
    }
    

    public ColaDeProcesos reordenarPorFCFS() {
        int tamañoCola = colaListos.tamaño();
        ColaDeProcesos colaOrdenada = new ColaDeProcesos(tamañoCola);
        //colaOrdenada.limpiar(); 
        Proceso[] procesos = new Proceso[tamañoCola];
        for (int i = 0; i < tamañoCola; i++) {
            procesos[i] = colaListos.obtener(i);
        }
        for (int i = 0; i < tamañoCola - 1; i++) {
            for (int j = 0; j < tamañoCola - 1 - i; j++) {
                if (procesos[j].getOrdenDeLlegada() > procesos[j + 1].getOrdenDeLlegada()) {
                    Proceso tmp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = tmp;
                }
            }
        }
        for (Proceso p : procesos) {
            colaOrdenada.agregar(p);
        }
        return colaOrdenada; 
    }
    

    public ColaDeProcesos reordenarPorRoundRobin() {
        int tamañoCola = colaListos.tamaño();
        ColaDeProcesos colaOrdenada = new ColaDeProcesos(tamañoCola);
        Proceso[] procesos = new Proceso[tamañoCola];
        
        for (int i = 0; i < tamañoCola; i++) {
            procesos[i] = colaListos.obtener(i);
        }
        for (int i = 0; i < tamañoCola - 1; i++) {
            for (int j = 0; j < tamañoCola - 1 - i; j++) {
                if (procesos[j].getOrdenDeLlegada() > procesos[j + 1].getOrdenDeLlegada()) {
                    Proceso tmp = procesos[j];
                    procesos[j] = procesos[j + 1];
                    procesos[j + 1] = tmp;
                }
            }
        }
        for (Proceso p : procesos) {
            colaOrdenada.agregar(p);
        }
        return colaOrdenada; 
    }

    public int getCicloGlobal() { return cicloGlobal; }
    public void setCicloGlobal(int nuevoCiclo) {
        this.cicloGlobal = nuevoCiclo; 
    }
    
    public void setPoliticaDePlanificacion(PoliticaDePlanificacion nuevaPolitica) {
        politicaDePlanificacion = nuevaPolitica; 
    }
    
     public void setColaListos(ColaDeProcesos nuevaCola) {
        colaListos = nuevaCola; 
    }

    public ColaDeProcesos getColaListos() {
        return colaListos; 
    }

    public void reordenarYEstablecerColaListos() {
        ColaDeProcesos colaOrdenada = politicaDePlanificacion.reordenar(this); 
        setColaListos(colaOrdenada); 
    }
    
    public ColaDeProcesos getColaBloqueados() { return colaBloqueados; }
    public ColaDeProcesos getColaFinalizados() { return colaFinalizados; }
    public ColaDeProcesos getColaGeneral() { return colaGeneral; }
    
    public int getDuracionCiclo (){
        return duracionCiclo;
    }
    
    public void setDuracionCiclo (int duracionCiclo){
        this.duracionCiclo = duracionCiclo;
    }

    void PoliticaDePlanificacion(PoliticaDePlanificacion policy) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}