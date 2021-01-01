/** Clase metadatos
 *  Esta clase guarda los datos sobre un fichero.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.io.Serializable;

public class Metadatos implements Serializable{
	private static final long serialVersionUID = 1L;
	private static int contadorUnico; // contador para los ids unicos de los ficheros
	private int idFichero; 	// id unico del fichero 
	private int idCliente;	// identificador del propietario de un fichero
	private int idRepositorio; // identificador para saber en que repositorio esta el fichero  
	private String nombre;   // nombre del fichero 
	private long peso;	// tamaño del fichero en bytes
	
	// constructor de la clase metadatos
	public Metadatos(int idCliente, int idRepositorio, String nombre) {
		super();
        contadorUnico++; // incrementa el contador el contador para obtener un nuevo id de fichero
        idFichero = contadorUnico;
		this.idCliente = idCliente;
		this.idRepositorio = idRepositorio;
		this.nombre = nombre;
		
	}

	public static int getContadorUnico() {
		return contadorUnico;
	}

	public static void setContadorUnico(int contadorUnico) {
		Metadatos.contadorUnico = contadorUnico;
	}

	public int getIdFichero() {
		return idFichero;
	}

	public void setIdFichero(int idFichero) {
		this.idFichero = idFichero;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdRepositorio() {
		return idRepositorio;
	}

	public void setIdRepositorio(int idRepositorio) {
		this.idRepositorio = idRepositorio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getPeso() {
		return peso;
	}

	public void setPeso(long peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "Metadatos [idFichero=" + idFichero + ", idCliente=" + idCliente + ", idRepositorio=" + idRepositorio
				+ ", nombre=" + nombre + ", peso=" + peso + "]";
	}
	
}
	

	