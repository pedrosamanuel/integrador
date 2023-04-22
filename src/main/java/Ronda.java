import java.util.ArrayList;
import java.util.List;

public class Ronda {

    private final List<Partido> partidos;
    private int nro;


    public Ronda() {
        this.nro = nro;
        this.partidos = new ArrayList<>();
    }

    public void setPartidos(Partido p) {
        this.partidos.add(p);
    }

    public Partido getPartidos(int i) {
        return partidos.get(i);
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }


}
