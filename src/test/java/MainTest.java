import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

Persona p = new Persona("Juan");

Ronda r = new Ronda();
Equipo a = new Equipo("Boca");
Equipo b = new Equipo("River");
Partido partido = new Partido(a,b,3,0);
Pronostico pronostico = new Pronostico(a, ResultadoEnum.GANAEQUIPO1);
    MainTest() {
        p.setPronosticos(pronostico);
        r.setPartidos(partido);
    }


    @Test
    void testPuntos(){
    assertEquals(1, p.puntos(ResultadoEnum.GANAEQUIPO1,ResultadoEnum.GANAEQUIPO1,p.getPuntos()));
    assertEquals(1, p.puntos(r.getPartidos(0).resultado(), p.getPronosticos(0).getResultado(), p.getPuntos()));
    }
}