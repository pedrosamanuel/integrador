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

            Connection c = DriverManager.getConnection("jdbc:mysql://db4free.net/db_integrador", "manupe", "manupe2003"); //conectamos a db

            stmt = c.createStatement();

            rs = stmt.executeQuery("SELECT * FROM partido");

            while (rs.next()) {
                int numeroRonda = rs.getInt("ronda");
                String eq1 = rs.getString("equipo1");
                int cant_goles1 = rs.getInt("cant_goles1");
                int cant_goles2 = rs.getInt("cant_goles2");
                String eq2 = rs.getString("equipo2");

                if (numeroRonda != ronda.getNro()) {
                    ronda = new Ronda();
                    ronda.setNro(numeroRonda); //crea ronda cuando no es repetida
                    if (ronda.getNro() != 0) {
                        rondas.add(ronda); //añade a la lista de rondas cuando cambia a la siguiente
                    }
                }

                Equipo equipo1 = new Equipo(eq1);
                Equipo equipo2 = new Equipo(eq2);

                Partido partido = new Partido(equipo1, equipo2, cant_goles1, cant_goles2);

                partidos.add(partido); //añade el partido a la lista partidos
                ronda.setPartidos(partido); //añade el partido a la lista rondas
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            cerrarConex(rs, stmt);
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

                ResultadoEnum resultado = asignarEnum(gana1, gana2, empata);

                Pronostico pronostico = new Pronostico(equipo, resultado);

                if (!nombre.equals(persona.getNombre())) {
                    persona = new Persona();
                    persona.setNombre(nombre);//crea la persona cuando no se repite
                    if (persona.getNombre() != null) {
                        personas.add(persona); //añade la persona a la lista cuando empieza con la otra persona
                    }
                }

                persona.setPronosticos(pronostico); //asigna el pronostico a la persona
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            cerrarConex(rs, stmt);
        }
    }

    public static void asignarPuntos(List<Persona> personas, List<Ronda> rondas, List<Partido> partidos) {
        int puntos;
        int aciertos;
        int cantPartidos = partidos.size();
        int cantRondas = rondas.size();
        cantPartidos = cantPartidos / cantRondas;

        for (Persona persona : personas) {
            puntos = 0;
            aciertos = 0;
            for (int z = 0; z < cantRondas; z++) {
                for (int j = 0; j < cantPartidos; j++) {
                    if (comparar(persona.getPronosticos(j + z * cantPartidos).getResultado(), rondas.get(z).getPartidos(j).resultado())) {
                        aciertos++;
                    }
                }
                puntos = sumaPuntos(puntos,aciertos,cantPartidos);
                aciertos = 0;
            }
            persona.setPuntos(puntos);
        }
    } //recorre en orden personas, rondas, partidos
    public static boolean comparar(ResultadoEnum resultado, ResultadoEnum pronostico) {

        return resultado == pronostico;

    }
    public static int sumaPuntos(int puntos, int aciertos,  int cantPartidos){
        final int puntosPorAcertar = 3;
        final int puntosRondaCompleta = 1;
        puntos += aciertos * puntosPorAcertar; //suma los puntos que da cada acierto
        if (aciertos == cantPartidos) {
            puntos +=  puntosRondaCompleta; //suma puntos por acertar todos los partidos de la ronda
        }
        return puntos;
    }

    public static void mostrarPuntos(List<Persona> personas) {
        for (Persona p : personas) {
            System.out.println(p.getNombre() + " tiene: " + p.getPuntos() + " puntos");
        }
    }
    static ResultadoEnum asignarEnum(int gana1, int gana2, int empata){
        if (gana1 == 1) {
            return ResultadoEnum.GANAEQUIPO1;
        } else if (empata == 1) {
            return ResultadoEnum.EMPATE;
        } else if (gana2 == 1) {
            return ResultadoEnum.GANAEQUIPO2;
        }else {
            return null;
        }
    }

    public static void cargarClase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
    }

    public static void cerrarConex(ResultSet rs, Statement stmt) {
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
