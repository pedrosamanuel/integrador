import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    Main main = new Main();
    Persona persona = new Persona("Juan");
    Ronda ronda = new Ronda();
    Equipo a = new Equipo("Boca");
    Equipo b = new Equipo("River");
    Partido partido = new Partido(a,b,3,0);
    Pronostico pronostico = new Pronostico(a, ResultadoEnum.GANAEQUIPO1);
    List<Persona> personas = new ArrayList<>();
    List<Partido> partidos = new ArrayList<>();
    List<Ronda> rondas = new ArrayList<>();

    MainTest() {
        persona.setPronosticos(pronostico);
        ronda.setPartidos(partido);
        personas.add(persona);
        partidos.add(partido);
        rondas.add(ronda);
    }





    @Test
    void testPuntos(){
        assertEquals(true, main.comparar(ResultadoEnum.GANAEQUIPO1,ResultadoEnum.GANAEQUIPO1));
        assertEquals(true, main.comparar(ronda.getPartidos(0).resultado(), persona.getPronosticos(0).getResultado()));
    }
    @Test
    void testSumarPuntos(){
        assertEquals(7, main.sumaPuntos(0,2,2));
        assertEquals(3, main.sumaPuntos(0,1,2));
        assertEquals(10, main.sumaPuntos(0,3,3));
        assertEquals(5, main.sumaPuntos(2,1,2));


    }
}