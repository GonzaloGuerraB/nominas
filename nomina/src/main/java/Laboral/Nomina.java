package Laboral;

/**
 * La clase Nomina se encarga de calcular el sueldo de los empleados 
 * según su categoría y años de experiencia.
 * 
 * @author Gonzalo
 * @version 1.0
 */
public class Nomina {
	
    /**
     * Tabla de sueldos base según la categoría del empleado. 
     * Cada índice de este array representa el sueldo base de la categoría correspondiente.
     */
	private static final int SUELDO_BASE[] = 
		{50000, 70000, 90000, 110000, 130000, 
		150000, 170000, 190000, 210000, 230000};

    /**
     * Calcula el sueldo del empleado en función de su categoría y años trabajados.
     * 
     * @param empleado El empleado para el que se va a calcular el sueldo.
     * @return El sueldo total calculado, que es la suma del sueldo base 
     *         correspondiente a la categoría del empleado y un bono de 5000 
     *         por cada año trabajado.
     */
	public static int sueldo(Empleado empleado) {
		return SUELDO_BASE[empleado.getCategoria() - 1] + 5000 * empleado.anyos;
	}
	
}
