package proyecto1vilapinto;

import proyecto1vilapinto.Interfaz;
import java.awt.Color;
import java.util.Random;

public class Proceso implements Runnable {
    private int id;
    private String nombre;
    private int instrucciones; // Como ráfaga de CPU
    private int instruccionesRestantes; // Ráfaga restante, lo que queda para que termine de ejecutarse
    private boolean esCpuLimitado;
    private int ciclosDeExcepción;
    private int ciclosDeSatisfacción;
    private int contadorDePrograma;
    private int mar; // Registro de dirección de memoria
    private static int contadorDeOrden = 0; 
    private int ordenDeLlegada; 
    private String estado;
    private Simulador simulador;

    public Proceso(String nombre, int instrucciones, boolean esCpuLimitado, int ciclosDeExcepción, int ciclosDeSatisfacción, Simulador simulador) {
        this.id = generarIdAleatorio();
        this.nombre = nombre;
        this.instrucciones = instrucciones;
        this.esCpuLimitado = esCpuLimitado;
        this.ciclosDeExcepción = ciclosDeExcepción;
        this.ciclosDeSatisfacción = ciclosDeSatisfacción;
        this.contadorDePrograma = 1;
        this.mar = 0;
        this.estado = "LISTO";
        this.ordenDeLlegada = ++contadorDeOrden;
        this.simulador = simulador;
    }
    
    @Override
    public void run() {
        // Simular la ejecución de una instrucción
    }

    
    public boolean estaBloqueado() {
        if (!esCpuLimitado() && getContadorDePrograma() % getCiclosDeExcepción() == 0) {
            return true;
        } else {
            return false;
        }
    }

    
    private int generarIdAleatorio() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); 
    }
    
    private int getDuracionCiclo() {
        return simulador.getDuracionCiclo();
    }
    
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getInstrucciones() { return instrucciones; }
    public int getInstruccionesRestantes() { return instruccionesRestantes; }
    public boolean esCpuLimitado() { return esCpuLimitado; }
    public int getCiclosDeExcepción() { return ciclosDeExcepción; }
    public int getCiclosDeSatisfacción() { return ciclosDeSatisfacción; }
    public int getContadorDePrograma() { return contadorDePrograma; }
    public int getMAR() { return mar; }
    public int getOrdenDeLlegada() { return ordenDeLlegada; }
    public String getEstado() { return estado; }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void incrementarContadorDePrograma() { this.contadorDePrograma++; }
    
    public void incrementarMAR() { this.mar++; }

    public boolean haTerminado() {
        return contadorDePrograma >= instrucciones;
    }

}
