package Laboral;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase CalculaNominasv2 gestiona la lectura y escritura de información de empleados
 * en una base de datos, así como el cálculo y almacenamiento de sus sueldos.
 */
public class CalculaNominasv2 {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/laboral"; // URL de la base de datos
    private static final String DB_USER = "root";  // Usuario de la base de datos
    private static final String DB_PASSWORD = "123456";  // Contraseña de la base de datos
    private static final String RUTA_EMPLEADOS_NUEVOS = "C:\\Users\\gonza.DESKTOP-HQ81E30\\Desktop\\nominas-main\\nomina\\src\\main\\java\\Laboral\\empleadosNuevos.txt"; // Ruta del archivo

    /**
     * Método principal que ejecuta el programa.
     * Lee empleados desde la base de datos, imprime la información,
     * actualiza datos, y permite dar de alta nuevos empleados.
     * 
     * @param args Argumentos de la línea de comandos.
     * @throws SQLException Si ocurre un error en las operaciones de base de datos.
     * @throws DatosNoCorrectosException Si los datos de los empleados son incorrectos.
     */
    public static void main(String[] args) throws SQLException, DatosNoCorrectosException {
        List<Empleado> empleados = new ArrayList<>();

        try {
            // Leer empleados desde la base de datos
            empleados = leerEmpleadosDesdeDB();

            // Imprimir la información antes de los cambios
            escribe(empleados);
            System.out.println("FIN DE LA LECTURA!");

            // Realizar los cambios
            empleados.get(1).incrAnyo(); // Incrementar años trabajados de Ada Lovelace
            empleados.get(0).setCategoria(6); // Cambiar la categoría de James Cosling a 9
            empleados.get(4).setCategoria(1); // Cambiar la categoria de Nacho Valero a 8

            // Imprimir después de los cambios
            escribe(empleados);

            // Actualizar la base de datos con los cambios
            actualizarEmpleadosEnDB(empleados);

            // Actualizar la base de datos con los sueldos
            escribirSueldosEnDB(empleados);

            // Ejemplo de alta individual
            // Empleado nuevoEmpleado = new Empleado("Nacho Valero", "32000036B", 'M', 3, 5);
            // altaEmpleado(nuevoEmpleado);
            

            // Ejemplo de alta por lotes desde archivo
            // altaEmpleadoDesdeArchivo(RUTA_EMPLEADOS_NUEVOS);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee la lista de empleados desde la base de datos.
     * 
     * @return Una lista de objetos Empleado.
     * @throws SQLException Si ocurre un error en las operaciones de base de datos.
     * @throws DatosNoCorrectosException Si los datos de los empleados son incorrectos.
     */
    private static List<Empleado> leerEmpleadosDesdeDB() throws SQLException, DatosNoCorrectosException {
        List<Empleado> empleados = new ArrayList<>();
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String query = "SELECT nombre, dni, sexo, categoria, anyos FROM Empleados";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            String dni = rs.getString("dni");
            char sexo = rs.getString("sexo").charAt(0);
            int categoria = rs.getInt("categoria");
            int anyos = rs.getInt("anyos");

            Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
            empleados.add(empleado);
        }

        rs.close();
        stmt.close();
        conn.close();

        return empleados;
    }

    /**
     * Actualiza la información de los empleados en la base de datos
     * después de realizar cambios en sus datos.
     * 
     * @param empleados Lista de empleados a actualizar.
     * @throws SQLException Si ocurre un error en las operaciones de base de datos.
     */
    private static void actualizarEmpleadosEnDB(List<Empleado> empleados) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        for (Empleado empleado : empleados) {
            String query = "UPDATE Empleados SET categoria = ?, anyos = ? WHERE dni = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, empleado.getCategoria());
            pstmt.setInt(2, empleado.anyos);
            pstmt.setString(3, empleado.getDni());
            pstmt.executeUpdate();
            pstmt.close();
        }

        conn.close();
    }

    /**
     * Escribe los sueldos de los empleados en la base de datos.
     * 
     * @param empleados Lista de empleados cuyos sueldos se van a almacenar.
     * @throws SQLException Si ocurre un error en las operaciones de base de datos.
     */
    private static void escribirSueldosEnDB(List<Empleado> empleados) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        // Insertar los nuevos sueldos
        for (Empleado empleado : empleados) {
            String query = "INSERT INTO Nominas (empleado_id, sueldo) VALUES ((SELECT id FROM Empleados WHERE dni = ?), ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, empleado.getDni());
            pstmt.setInt(2, Nomina.sueldo(empleado));
            pstmt.executeUpdate();
            pstmt.close();
        }
        
        // Codigo si en vez de insertar nuevoos registros solo queremos actualizarlos
        
        /*
        for (Empleado empleado : empleados) {
            String query = "UPDATE Nominas SET sueldo = ? WHERE empleado_id = (SELECT id FROM Empleados WHERE dni = ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Nomina.sueldo(empleado)); 
            pstmt.setString(2, empleado.getDni()); 
            pstmt.executeUpdate(); 
            pstmt.close(); 
        }
        */
        
        conn.close();
    }


    /**
     * Da de alta un empleado individualmente en la base de datos.
     * 
     * @param empleado El empleado a dar de alta.
     * @throws SQLException Si ocurre un error en las operaciones de base de datos.
     */
    public static void altaEmpleado(Empleado empleado) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String query = "INSERT INTO Empleados (nombre, dni, sexo, categoria, anyos) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, empleado.nombre);
        pstmt.setString(2, empleado.getDni());
        pstmt.setString(3, String.valueOf(empleado.sexo));
        pstmt.setInt(4, empleado.getCategoria());
        pstmt.setInt(5, empleado.anyos);

        pstmt.executeUpdate(); // Insertar empleado

        // Calcular y almacenar sueldo
        int sueldo = Nomina.sueldo(empleado);
        almacenarSueldo(empleado.getDni(), sueldo, conn);

        pstmt.close();
        conn.close();
    }

    /**
     * Da de alta empleados desde un archivo de texto.
     * 
     * @param archivo Ruta del archivo que contiene los datos de nuevos empleados.
     */
    public static void altaEmpleadoDesdeArchivo(String archivo) {
        List<Empleado> nuevosEmpleados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0].trim();
                String dni = datos[1].trim();
                char sexo = datos[2].trim().charAt(0);
                int categoria = Integer.parseInt(datos[3].trim());
                int anyos = Integer.parseInt(datos[4].trim());

                Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
                nuevosEmpleados.add(empleado);
                altaEmpleado(empleado); // Llamar al método individual para cada empleado
            }
        } catch (IOException | DatosNoCorrectosException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al dar de alta el empleado: " + e.getMessage());
        }
    }

    /**
     * Almacena el sueldo de un empleado en la base de datos.
     * 
     * @param dni El DNI del empleado.
     * @param sueldo El sueldo a almacenar.
     * @param conn Conexión a la base de datos.
     * @throws SQLException Si ocurre un error en las operaciones de base de datos.
     */
    private static void almacenarSueldo(String dni, int sueldo, Connection conn) throws SQLException {
        String query = "INSERT INTO Nominas (empleado_id, sueldo) VALUES ((SELECT id FROM Empleados WHERE dni = ?), ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, dni);
        pstmt.setInt(2, sueldo);
        pstmt.executeUpdate(); // Insertar sueldo
        pstmt.close();
    }

    /**
     * Imprime la información de los empleados en la consola.
     * 
     * @param empleados Lista de empleados a imprimir.
     */
    private static void escribe(List<Empleado> empleados) {
        for (Empleado e : empleados) {
            e.imprime();
            System.out.println("Sueldo: " + Nomina.sueldo(e));
            System.out.println("--------------------------------");
        }
    }
}
