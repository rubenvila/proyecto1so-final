package proyecto1vilapinto;

/**
 *
 * @author Luisitus
 */
public enum PoliticaDePlanificacion {
    FCFS("FCFS - Primero en llegar, primero en ser atendido") {
        @Override
        public ColaDeProcesos reordenar(Simulador simulador) {
            return simulador.reordenarPorFCFS();
        }
    },
    RR("RR - Round Robin") {
        @Override
        public ColaDeProcesos reordenar(Simulador simulador) {
            return simulador.reordenarPorRoundRobin();
        }
    },
    SPN("SPN - Siguiente Proceso Más Corto") {
        @Override
        public ColaDeProcesos reordenar(Simulador simulador) {
            return simulador.reordenarPorSPN();
        }
    },
    HRRN("HRRN - Mayor Relación de Respuesta Siguiente") {
        @Override
        public ColaDeProcesos reordenar(Simulador simulador) {
            return simulador.reordenarPorHRRN();
        }
    },
    SRT("SRT - Tiempo Restante Más Corto") {
        @Override
        public ColaDeProcesos reordenar(Simulador simulador) {
            return simulador.reordenarPorSRT();
        }
    };

    static PoliticaDePlanificacion fromString(String selectedItem) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private final String descripcion;

    PoliticaDePlanificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
    public static PoliticaDePlanificacion desdeString(String descripcion) {
        for (PoliticaDePlanificacion politica : PoliticaDePlanificacion.values()) {
            if (politica.toString().equalsIgnoreCase(descripcion)) {
                return politica;
            }
        }
        return null; 
    }

    public abstract ColaDeProcesos reordenar(Simulador simulador);
}
