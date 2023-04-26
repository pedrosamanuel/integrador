public class Partido {
    private final Equipo equipo1;
    private final Equipo equipo2;
    private final int golesEquipo1;
    private final int golesEquipo2;

    public Partido(Equipo equipo1, Equipo equipo2, int golesEquipo1, int golesEquipo2) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.golesEquipo1 = golesEquipo1;
        this.golesEquipo2 = golesEquipo2;

    }


    ResultadoEnum resultado() {
        if (golesEquipo1 > golesEquipo2) {
            return ResultadoEnum.GANAEQUIPO1;
        } else if (golesEquipo2 > golesEquipo1) {
            return ResultadoEnum.GANAEQUIPO2;
        } else  {
            return ResultadoEnum.EMPATE;
        }
    }

}

