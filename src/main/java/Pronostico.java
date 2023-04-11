public class Pronostico {

    private final Equipo equipo;
    private final ResultadoEnum resultado;

    public Pronostico(Equipo equipo, ResultadoEnum resultado) {

        this.equipo = equipo;
        this.resultado = resultado;
    }


    public ResultadoEnum getResultado() {
        return resultado;
    }


}
