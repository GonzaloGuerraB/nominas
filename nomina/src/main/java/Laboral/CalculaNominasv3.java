package Laboral;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * La clase CalculaNominasv2 gestiona la lectura y escritura de información de empleados
 * en una base de datos, así como el cálculo y almacenamiento de sus sueldos.
 */
public class CalculaNominasv3 {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/laboral"; // URL de la base de datos
    private static final String DB_USER = "root";  // Usuario de la base de datos
    private static final String DB_PASSWORD = "123456";  // Contraseña de la base de datos

    public static void main(String[] args) throws SQLException, DatosNoCorrectosException {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea
            switch (opcion) {
                case 1:
                    mostrarInformacionEmpleados();
                    break;
                case 2:
                    System.out.print("Ingrese el DNI del empleado: ");
                    String dni = scanner.nextLine();
                    mostrarSalarioEmpleado(dni);
                    break;
                case 3:
                    mostrarSubmenu(scanner);
                    break;
                case 4:
                    System.out.print("Ingrese el DNI del empleado para recalcular su sueldo: ");
                    dni = scanner.nextLine();
                    recalcularSueldoEmpleado(dni);
                    break;
                case 5:
                    recalcularSueldosDeTodos();
                    break;
                case 6:
                    realizarCopiaSeguridad();
                    break;
                case 7:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 7);
    }

    // Método para mostrar el menú principal
    private static void mostrarMenu() {
        System.out.println("-------- Menú de opciones --------");
        System.out.println("1. Mostrar la información de todos los empleados");
        System.out.println("2. Mostrar el salario de un empleado por DNI");
        System.out.println("3. Modificar datos de un empleado (excepto sueldo)");
        System.out.println("4. Recalcular y actualizar el sueldo de un empleado");
        System.out.println("5. Recalcular y actualizar los sueldos de todos los empleados");
        System.out.println("6. Realizar una copia de seguridad de la base de datos");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción: ");
    }

    // Método para mostrar la información de todos los empleados
    private static void mostrarInformacionEmpleados() throws SQLException, DatosNoCorrectosException {
        List<Empleado> empleados = leerEmpleadosDesdeDB();
        escribe(empleados);
    }

    // Método para mostrar el salario de un empleado por DNI
    private static void mostrarSalarioEmpleado(String dni) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String query = "SELECT sueldo FROM Nominas WHERE empleado_id = (SELECT id FROM Empleados WHERE dni = ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, dni);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int sueldo = rs.getInt("sueldo");
            System.out.println("El sueldo del empleado con DNI " + dni + " es: " + sueldo);
        } else {
            System.out.println("Empleado no encontrado.");
        }
        rs.close();
        pstmt.close();
        conn.close();
    }

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
    
    // Método para mostrar el submenú de modificación de datos de empleados
    private static void mostrarSubmenu(Scanner scanner) throws SQLException, DatosNoCorrectosException {
        System.out.print("Ingrese el DNI del empleado que desea modificar: ");
        String dni = scanner.nextLine();
        Empleado empleado = obtenerEmpleadoPorDNI(dni);  // Método para obtener empleado por DNI

        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        System.out.println("Empleado encontrado: ");
        empleado.imprime();
        
        boolean continuar = true;
        while (continuar) {
            System.out.println("Seleccione qué desea modificar:");
            System.out.println("1. Modificar nombre");
            System.out.println("2. Modificar DNI");
            System.out.println("3. Modificar sexo");
            System.out.println("4. Modificar categoría");
            System.out.println("5. Modificar años trabajados");
            System.out.println("6. Salir del submenú");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Consumir salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nuevo nombre: ");
                    String nuevoNombre = scanner.nextLine();
                    empleado.setNombre(nuevoNombre);
                    break;
                case 2:
                    System.out.print("Ingrese el nuevo DNI: ");
                    String nuevoDni = scanner.nextLine();
                    empleado.setDni(nuevoDni);
                    break;
                case 3:
                    System.out.print("Ingrese el nuevo sexo (M/F): ");
                    char nuevoSexo = scanner.nextLine().toUpperCase().charAt(0);
                    if (nuevoSexo == 'M' || nuevoSexo == 'F') {
                        empleado.setSexo(nuevoSexo);
                    } else {
                        System.out.println("Sexo no válido.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese la nueva categoría: ");
                    int nuevaCategoria = scanner.nextInt();
                    scanner.nextLine();  // Consumir salto de línea
                    empleado.setCategoria(nuevaCategoria);
                    break;
                case 5:
                    System.out.print("Ingrese los nuevos años trabajados: ");
                    int nuevosAnyos = scanner.nextInt();
                    scanner.nextLine();  // Consumir salto de línea
                    empleado.setAnyos(nuevosAnyos);
                    break;
                case 6:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            if (!continuar) {
                // Actualizar el empleado en la base de datos
                actualizarEmpleadoEnDB(empleado);

                // Recalcular el sueldo automáticamente después de cada modificación
                recalcularSueldoEmpleado(empleado.getDni());

                System.out.println("Modificación exitosa.");
            }
        }
    }


    // Método para obtener un empleado por DNI desde la base de datos
    private static Empleado obtenerEmpleadoPorDNI(String dni) throws SQLException, DatosNoCorrectosException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String query = "SELECT nombre, dni, sexo, categoria, anyos FROM Empleados WHERE dni = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, dni);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String nombre = rs.getString("nombre");
            char sexo = rs.getString("sexo").charAt(0);
            int categoria = rs.getInt("categoria");
            int anyos = rs.getInt("anyos");
            Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
            rs.close();
            pstmt.close();
            conn.close();
            return empleado;
        }

        rs.close();
        pstmt.close();
        conn.close();
        return null;
    }

    // Método para recalcular el sueldo de un empleado y actualizarlo en la base de datos
    private static void recalcularSueldoEmpleado(String dni) throws SQLException, DatosNoCorrectosException {
        Empleado empleado = obtenerEmpleadoPorDNI(dni);
        if (empleado != null) {
            actualizarEmpleadoEnDB(empleado);
            escribirSueldosEnDB(List.of(empleado));  // Actualiza el sueldo
            System.out.println("Sueldo recalculado y actualizado para el empleado con DNI: " + dni);
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    // Método para recalcular los sueldos de todos los empleados
    private static void recalcularSueldosDeTodos() throws SQLException, DatosNoCorrectosException {
        List<Empleado> empleados = leerEmpleadosDesdeDB();
        escribirSueldosEnDB(empleados);
        System.out.println("Sueldos recalculados y actualizados para todos los empleados.");
    }

    // Método para realizar una copia de seguridad de la base de datos en ficheros
    /**
     * Realiza una copia de seguridad de la información de los empleados
     * (nombre, dni, sexo, categoría, años trabajados y sueldos) en un archivo de texto.
     */
    private static void realizarCopiaSeguridad() {
        List<Empleado> empleados = new ArrayList<>();
        
        // Definir la ruta del archivo de copia de seguridad
        String archivoBackup = "C:\\Users\\usuario\\Desktop\\GonzaloInstituto\\nomina\\src\\main\\java\\Laboral\\copiaDeSeguridad.txt";
        
        try {
            // Leer la lista de empleados desde la base de datos
            empleados = leerEmpleadosDesdeDB(); // Asegúrate de tener este método implementado

            // Crear el archivo de copia de seguridad
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoBackup));

            // Escribir la cabecera en el archivo
            bw.write("Nombre, DNI, Sexo, Categoria, Años Trabajados, Sueldo\n");

            // Recorrer la lista de empleados y escribir la información en el archivo
            for (Empleado empleado : empleados) {
                String linea = String.format("%s, %s, %c, %d, %d, %d",
                        empleado.nombre, empleado.getDni(), empleado.sexo, 
                        empleado.getCategoria(), empleado.anyos, Nomina.sueldo(empleado));		
                bw.write(linea); // Escribir la línea en el archivo
                bw.newLine(); // Nueva línea
            }

            // Cerrar el archivo de copia de seguridad
            bw.close();

            // Confirmación de que la copia de seguridad fue exitosa
            System.out.println("Copia de seguridad realizada exitosamente en " + archivoBackup);

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de copia de seguridad: " + e.getMessage());
        } catch (SQLException | DatosNoCorrectosException e) {
            System.err.println("Error al leer empleados de la base de datos: " + e.getMessage());
        }
    }


    // Resto de los métodos de la clase como leerEmpleadosDesdeDB, escribirSueldosEnDB, actualizarEmpleadoEnDB, etc.
    
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

        // Actualizar sueldos en la base de datos
        for (Empleado empleado : empleados) {
            String query = "UPDATE Nominas SET sueldo = ? WHERE empleado_id = (SELECT id FROM Empleados WHERE dni = ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Nomina.sueldo(empleado)); // Calcula el sueldo del empleado
            pstmt.setString(2, empleado.getDni());    // Actualiza el sueldo del empleado con el DNI correspondiente
            pstmt.executeUpdate(); // Ejecuta la actualización del sueldo
            pstmt.close(); // Cierra el PreparedStatement
        }

        conn.close(); // Cierra la conexión con la base de datos
    }
    
    // Método para actualizar un empleado individualmente en la base de datos
    private static void actualizarEmpleadoEnDB(Empleado empleado) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        
        // Consulta SQL para actualizar todos los atributos del empleado
        String query = "UPDATE Empleados SET nombre = ?, dni = ?, sexo = ?, categoria = ?, anyos = ? WHERE dni = ?";
        
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        // Configurar los valores en la consulta preparada
        pstmt.setString(1, empleado.getNombre());  // Actualizar el nombre
        pstmt.setString(2, empleado.getDni());     // Actualizar el DNI
        pstmt.setString(3, String.valueOf(empleado.getSexo())); // Actualizar el sexo
        pstmt.setInt(4, empleado.getCategoria());  // Actualizar la categoría
        pstmt.setInt(5, empleado.getAnyos());      // Actualizar los años trabajados
        
        // El DNI actual del empleado en la cláusula WHERE
        pstmt.setString(6, empleado.getDni());

        // Ejecutar la actualización
        pstmt.executeUpdate();
        
        // Cerrar la consulta y conexión
        pstmt.close();
        conn.close();
    }

    
    // Método para imprimir la información de los empleados
    private static void escribe(List<Empleado> empleados) {
        for (Empleado e : empleados) {
            e.imprime();
            System.out.println("Sueldo: " + Nomina.sueldo(e));
            System.out.println("--------------------------------");
        }
    }
}

