import java.util.ArrayList;
import java.util.List;

public class Persona {

    private final List<Pronostico> pronosticos;

    private String nombre;

    private int puntos;


    public Persona(String nombre) {
        this.nombre = nombre;
        this.pronosticos = new ArrayList<>();
    }

    public Persona() {
        this.pronosticos = new ArrayList<>();
    }

    public void setPronosticos(Pronostico p) {
        this.pronosticos.add(p);
    }

    public Pronostico getPronosticos(int i) {
        return this.pronosticos.get(i);

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


}

