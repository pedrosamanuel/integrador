import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introducir la ruta del archivo \"resultados\"");
        Path dirResultados = Paths.get(sc.nextLine());
        System.out.println("Introducir la ruta del archivo \"pronósticos\"");
        Path dirPronosticos = Paths.get(sc.nextLine());


        List<Persona> personas = new ArrayList<>();
        List<Partido> partidos = new ArrayList<>();
        Persona persona = new Persona();
        List<Ronda> rondas = new ArrayList<>();
        Ronda ronda = new Ronda();


        leerResultados(dirResultados, ronda, rondas, partidos);
        leerPronosticos(dirPronosticos, persona, personas);
        asignarPuntos(personas, rondas, partidos);
        mostrarPuntos(personas);


    }

    public static void leerResultados(Path dirResultados, Ronda ronda, List<Ronda> rondas, List<Partido> partidos) throws Exception {
        String[] arrPartidos;
        for (String linea : Files.readAllLines(dirResultados)) {

            arrPartidos = linea.split(";");

            if (arrPartidos.length != 5) {
                System.out.println("Cantidad de campos incorrectas en archivo resultados.");
            }
            try {
                int num1 = Integer.parseInt(arrPartidos[2]);
                int num2 = Integer.parseInt(arrPartidos[3]);

            } catch (NumberFormatException e) {
                System.out.println("Los goles no son números enteros");
            }

            int ronda1 = Integer.parseInt(arrPartidos[0]);

            if (ronda1 != ronda.getNro()) {
                if (ronda.getNro() != 0) {
                    rondas.add(ronda);
                }
                ronda = new Ronda();
                ronda.setNro(Integer.parseInt(arrPartidos[0]));

            }
            int golesEq1 = Integer.parseInt(arrPartidos[2]);
            int golesEq2 = Integer.parseInt(arrPartidos[3]);


            Equipo equipo1 = new Equipo(arrPartidos[1]);
            Equipo equipo2 = new Equipo(arrPartidos[4]);

            Partido partido = new Partido(equipo1, equipo2, golesEq1, golesEq2);
            partidos.add(partido);
            ronda.setPartidos(partido);
        }
        rondas.add(ronda);

    }

    public static void leerPronosticos(Path dirPronosticos, Persona persona, List<Persona> personas) throws Exception {

        String[] arrPronosticos;
        for (String lin : Files.readAllLines(dirPronosticos)) {

            arrPronosticos = lin.split(";");

            Equipo equipo = new Equipo(arrPronosticos[1]);

            ResultadoEnum resultado = null;

            if (arrPronosticos[2].equals("X")) {
                resultado = ResultadoEnum.GANAEQUIPO1;
            } else if (arrPronosticos[3].equals("X")) {
                resultado = ResultadoEnum.EMPATE;
            } else if (arrPronosticos[4].equals("X")) {
                resultado = ResultadoEnum.GANAEQUIPO2;
            }

            Pronostico pronostico = new Pronostico(equipo, resultado);

            String nombre = arrPronosticos[0];

            if (!nombre.equals(persona.getNombre())) {
                if (persona.getNombre() != null) {
                    personas.add(persona);
                }
                persona = new Persona();
                persona.setNombre(nombre);
            }
            persona.setPronosticos(pronostico);
        }
        personas.add(persona);
    }

    public static void asignarPuntos(List<Persona> personas, List<Ronda> rondas, List<Partido> partidos) {
        int puntos;
        int cantPersonas = personas.size();
        int cantPartidos = partidos.size();
        int cantRondas = rondas.size();
        cantPartidos = cantPartidos / cantRondas;

        for (int i = 0; i < cantPersonas; i++) {
            puntos = 0;
            for (int z = 0; z < cantRondas; z++) {
                for (int j = 0; j < cantPartidos; j++) {
                    puntos += personas.get(i).puntos(rondas.get(z).getPartidos(j).resultado(), personas.get(i).getPronosticos(j + z * cantPartidos).getResultado(), personas.get(i).getPuntos());
                }
            }
            personas.get(i).setPuntos(puntos);
        }
    }

    public static void mostrarPuntos(List<Persona> personas) {
        for (Persona p : personas) {
            System.out.println(p.getNombre() + " tiene: " + p.getPuntos() + " puntos");
        }
    }
}
