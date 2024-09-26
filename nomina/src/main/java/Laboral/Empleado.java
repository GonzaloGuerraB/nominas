package Laboral;

/**
 * La clase Empleado representa a un empleado que extiende las funcionalidades de la clase Persona.
 * 
 * @author Gonzalo
 * @version 1.0
 */
public class Empleado extends Persona {

    /**
     * Categoría del empleado (entre 1 y 10).
     */
    private Integer categoria;

    /**
     * Años trabajados por el empleado siempre positivo.
     */
    public Integer anyos;

    /**
     * Constructor de la clase Empleado con todos los parámetros.
     * 
     * @param nombre   Nombre del empleado.
     * @param dni      DNI del empleado.
     * @param sexo     Sexo del empleado ('M' o 'F').
     * @param categoria Categoría laboral del empleado, debe estar entre 1 y 10.
     * @param anyos    Años trabajados por el empleado, debe ser mayor o igual a 0.
     * @throws DatosNoCorrectosException Si la categoría no está entre 1 y 10, o los años trabajados son negativos.
     */
    public Empleado(String nombre, String dni, char sexo, Integer categoria, Integer anyos)
            throws DatosNoCorrectosException {
        super(nombre, dni, sexo);
        if (anyos >= 0) {
            this.anyos = anyos;
        } else {
            throw new DatosNoCorrectosException("Los años introducidos no son validos");
        }
        if (categoria > 0 && categoria <= 10) {
            this.categoria = categoria;
        } else {
            throw new DatosNoCorrectosException("La categoría introducida es incorrecta");
        }
    }

    /**
     * Constructor de la clase Empleado con parámetros mínimos.
     * Por defecto, la categoría será 1 y los años trabajados serán 0.
     * 
     * @param nombre Nombre del empleado.
     * @param dni    DNI del empleado.
     * @param sexo   Sexo del empleado ('M' o 'F').
     */
    public Empleado(String nombre, String dni, char sexo) {
        super(nombre, dni, sexo);
        this.categoria = 1;
        this.anyos = 0;
    }

    /**
     * Obtiene la categoría del empleado.
     * 
     * @return La categoría laboral del empleado.
     */
    public Integer getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del empleado.
     * 
     * @param categoria Nueva categoría laboral del empleado.
     */
    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    /**
     * Incrementa en uno los años trabajados por el empleado.
     */
    public void incrAnyo() {
        this.anyos++;
    }

    /**
     * Imprime por consola los detalles del empleado.
     */
    public void imprime() {
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Sexo: " + sexo);
        System.out.println("Categoria: " + categoria);
        System.out.println("Años trabajados: " + anyos);
    }
}
