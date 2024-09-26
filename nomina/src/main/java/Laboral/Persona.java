package Laboral;

/**
 * La clase Persona representa una persona con nombre, DNI y sexo.
 * Proporciona métodos para obtener y modificar el DNI y para imprimir la información de la persona.
 */
public class Persona {

    /** El nombre de la persona */
    public String nombre;

    /** El DNI de la persona */
    public String dni;

    /** El sexo de la persona ('M' para masculino, 'F' para femenino) */
    public char sexo;

    /**
     * Constructor que inicializa una persona con un nombre, DNI y sexo.
     * 
     * @param nombre El nombre de la persona.
     * @param dni El DNI de la persona.
     * @param sexo El sexo de la persona.
     */
    public Persona(String nombre, String dni, char sexo) {
        super();
        this.nombre = nombre;
        this.dni = dni;
        this.sexo = sexo;
    }

    /**
     * Constructor que inicializa una persona con un nombre y sexo.
     * 
     * @param nombre El nombre de la persona.
     * @param sexo El sexo de la persona.
     */
    public Persona(String nombre, char sexo) {
        super();
        this.nombre = nombre;
        this.sexo = sexo;
    }

    /**
     * Obtiene el DNI de la persona.
     * 
     * @return El DNI de la persona.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI de la persona.
     * 
     * @param dni El nuevo DNI de la persona.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Imprime la información de la persona (nombre y DNI) en la consola.
     */
    public void imprime() {
        System.out.println("Nombre: " + nombre + ", DNI: " + dni);
    }

}
