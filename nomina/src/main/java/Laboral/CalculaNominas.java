package Laboral;

import java.io.*;
import java.util.*;

public class CalculaNominas {
	
	private static final String RUTA = System.getProperty("user.dir") + "/src/main/java/Laboral/";

    public static void main(String[] args) throws IOException, DatosNoCorrectosException {
        List<Empleado> empleados = new ArrayList<>();
 
        try {
			// Lee la información de los empleados antes de realizar cambios
			empleados = leerEmpleadosDesdeArchivo(RUTA + "empleados.txt");
			
			// Escribir la información de los empleados antes de los cambios
			escribe(empleados);

			// Realizar cambios en los empleados conforme a las especificaciones
			empleados.get(1).incrAnyo(); // Incrementa años de Ada Lovelace
			empleados.get(0).setCategoria(9); // Cambia la categoría de James Cosling a 9

			// Escribir la información de los empleados después de los cambios
			escribe(empleados);
		} catch (IOException e) {
            System.out.println(e.getMessage());
		} catch (DatosNoCorrectosException e) {
            System.out.println(e.getMessage());
		}
        
        // Escribir los sueldos en el fichero binario
        try {
            escribirSueldosEnArchivo(RUTA + "salarios.dat", empleados);
        } catch (IOException e) {
            System.out.println("Error al escribir sueldos: " + e.getMessage());
        }
    }

    // Método para leer empleados desde un archivo de texto
    private static List<Empleado> leerEmpleadosDesdeArchivo(String archivo) throws IOException, DatosNoCorrectosException {
        List<Empleado> empleados = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            String nombre = datos[0].trim();
            String dni = datos[1].trim();
            char sexo = datos[2].trim().charAt(0);
            int categoria = Integer.parseInt(datos[3].trim());
            int anyos = Integer.parseInt(datos[4].trim());

            Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
            empleados.add(empleado);
        }

        br.close();
        return empleados;
    }

    // Método para escribir los sueldos en un archivo binario
    private static void escribirSueldosEnArchivo(String archivo, List<Empleado> empleados) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
        for (Empleado empleado : empleados) {
            // Escribir en formato: dni,sueldo
            bw.write(empleado.getDni() + "," + Nomina.sueldo(empleado));
            bw.newLine(); // Añadir nueva línea después de cada empleado
            bw.flush();
        }
        bw.close();
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
