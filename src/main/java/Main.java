import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {


        List<Persona> personas = new ArrayList<>();
        List<Partido> partidos = new ArrayList<>();
        List<Ronda> rondas = new ArrayList<>();


        leerResultados(rondas, partidos);
        leerPronosticos(personas);
        asignarPuntos(personas, rondas, partidos);
        mostrarPuntos(personas);


    }

    public static void leerResultados(List<Ronda> rondas, List<Partido> partidos) {
        Ronda ronda = new Ronda();

        Statement stmt = null;
        ResultSet rs = null;

        try {
            cargarClase();

            Connection c = DriverManager.getConnection("jdbc:mysql://db4free.net/db_integrador", "manupe", "manupe2003");

            stmt = c.createStatement();

            rs = stmt.executeQuery("SELECT * FROM partido");

            while (rs.next()) {
                int numeroRonda = rs.getInt("ronda");
                String eq1 = rs.getString("equipo1");
                int cant_goles1 = rs.getInt("cant_goles1");
                int cant_goles2 = rs.getInt("cant_goles2");
                String eq2 = rs.getString("equipo2");

                if (numeroRonda != ronda.getNro()) {
                    if (ronda.getNro() != 0) {
                        rondas.add(ronda);
                    }
                    ronda = new Ronda();
                    ronda.setNro(numeroRonda);
                }

                Equipo equipo1 = new Equipo(eq1);
                Equipo equipo2 = new Equipo(eq2);

                Partido partido = new Partido(equipo1, equipo2, cant_goles1, cant_goles2);

                partidos.add(partido);
                ronda.setPartidos(partido);
            }
            rondas.add(ronda);


        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void leerPronosticos(List<Persona> personas) {
        Persona persona = new Persona();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            cargarClase();

            Connection c = DriverManager.getConnection("jdbc:mysql://db4free.net/db_integrador", "manupe", "manupe2003");

            stmt = c.createStatement();

            rs = stmt.executeQuery("SELECT * FROM pronostico");

            while (rs.next()) {
                String eq = rs.getString("equipo1");
                String nombre = rs.getString("participante");
                int gana1 = rs.getInt("gana1");
                int empata = rs.getInt("empata");
                int gana2 = rs.getInt("gana2");

                Equipo equipo = new Equipo(eq);

                ResultadoEnum resultado = null;

                if (gana1 == 1) {
                    resultado = ResultadoEnum.GANAEQUIPO1;
                } else if (empata == 1) {
                    resultado = ResultadoEnum.EMPATE;
                } else if (gana2 == 1) {
                    resultado = ResultadoEnum.GANAEQUIPO2;
                }

                Pronostico pronostico = new Pronostico(equipo, resultado);

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

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    public static void asignarPuntos(List<Persona> personas, List<Ronda> rondas, List<Partido> partidos) {
        final int puntosPorAcertar = 3;
        final int puntosRondaCompleta = 1;
        int puntos;
        int aciertos;
        int cantPersonas = personas.size();
        int cantPartidos = partidos.size();
        int cantRondas = rondas.size();
        cantPartidos = cantPartidos / cantRondas;

        for (int i = 0; i < cantPersonas; i++) {
            puntos = 0;
            aciertos = 0;
            for (int z = 0; z < cantRondas; z++) {
                for (int j = 0; j < cantPartidos; j++) {
                    puntos = puntos + (personas.get(i).puntos(rondas.get(z).getPartidos(j).resultado(),
                            personas.get(i).getPronosticos(j + z * cantPartidos).getResultado(), personas.get(i).getPuntos())) * puntosPorAcertar;
                    aciertos += (personas.get(i).puntos(rondas.get(z).getPartidos(j).resultado(),
                            personas.get(i).getPronosticos(j + z * cantPartidos).getResultado(), personas.get(i).getPuntos()));
                }
                if (aciertos == cantPartidos) {
                    puntos = puntos + puntosRondaCompleta;
                }
                aciertos = 0;
            }
            personas.get(i).setPuntos(puntos);
        }
    }

    public static void mostrarPuntos(List<Persona> personas) {
        for (Persona p : personas) {
            System.out.println(p.getNombre() + " tiene: " + p.getPuntos() + " puntos");
        }
    }

    public static void cargarClase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
    }
}
