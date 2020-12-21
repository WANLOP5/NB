package nube.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import nube.comun.*;

public class ServicioDatosImpl extends UnicastRemoteObject 
implements ServicioDatosInterface {
	private static final long serialVersionUID = 1L;
	
	// Tablas que relacionan NombreCliente-IDCliente y viceversa.
	private HashMap<String, Integer> TNClienteICliente;
	private HashMap<Integer, String> TIClienteNCliente;
	
	// Tablas que relacionan NombreRepositorio-IDRepositorio y viceversa.
	private HashMap<String, Integer> TNRepositorioIRepositorio;
	private HashMap<Integer, String> TIRepositorioNRepositorio;
	
	// Tablas que relacionan IDCliente-IDRepositorio y viceversa
	private HashMap<Integer, Integer> TClienteRepositorio;
	private HashMap<Integer, Integer> TRepositorioCliente;
	
	// Tablas que relacionan IDCliente-Sesion y viceversa
	private HashMap<Integer, Integer> TClienteSesion;
	private HashMap<Integer, Integer> TSesionCliente;
	// Tablas que relacionan IDRepositorio-Sesion y viceversa
	private HashMap<Integer, Integer> TRepositorioSesion;
	private HashMap<Integer, Integer> TSesionRepositorio;
	
	// Tablas que relacionan IDFichero-Metadatos (del fichero) y viceversa.
	private HashMap<Integer, Metadatos> TIFicheroMetadatos;
	private HashMap<Metadatos, Integer> TMetadatosIFichero;
	
	// Tablas que relacionan IDCliente-Lista de IDFicheros (del cliente) y viceversa.
	private HashMap<Integer, List<Integer>> TClienteFicheros;
	private HashMap<List<Integer>, Integer> TFicherosCliente;
	
	// Inicializa las tablas de los datos del sistema.
	public ServicioDatosImpl() throws RemoteException {
		super();
		
		TNClienteICliente = new HashMap<String, Integer>();
		TIClienteNCliente = new HashMap<Integer, String>();
		
		TNRepositorioIRepositorio = new HashMap<String, Integer>();
		TIRepositorioNRepositorio = new HashMap<Integer, String>();
		TClienteRepositorio = new HashMap<Integer, Integer>();
		TRepositorioCliente = new HashMap<Integer, Integer>();
		
		TClienteSesion = new HashMap<Integer, Integer>();
		TSesionCliente = new HashMap<Integer, Integer>();
		TRepositorioSesion = new HashMap<Integer, Integer>();
		TSesionRepositorio = new HashMap<Integer, Integer>();
		
		TIFicheroMetadatos = new HashMap<Integer, Metadatos>();
		TMetadatosIFichero = new HashMap<Metadatos, Integer>();
		
		TClienteFicheros = new HashMap<Integer, List<Integer>>();
		TFicherosCliente = new HashMap<List<Integer>, Integer>();
		
	}
	
	// Devuelve todos los clientes del sistema con sus datos.
	public String listarClientes() {
		String lista = null;
		TNClienteICliente.entrySet().forEach(par->{
			lista.concat(par + "\n");   
		 });
		
		return lista;
	}
	// Devuelve todos los repositorios del sistema y sus datos.
	public String listarRepositorios() {
		String lista = null;
		TNRepositorioIRepositorio.entrySet().forEach(par->{
			lista.concat(par + "\n");   
		 });
		
		return lista;
	}
	
	// Devuelve los clientes con sus repositorios
	public String listarClientesRepositorios() {
		String lista = null;
		TClienteRepositorio.entrySet().forEach(par->{
		    lista.concat(par + "\n");  
		});
		
		return lista;
	}
	
	// Devuelve los ficheros de un cliente 
	public String listarFicherosCliente(int idCliente) {
		String lista = null;
		int indice = 1;
		
		for(int fichero : TClienteFicheros.get(idCliente)) {
			String NFichero = buscarMetadatos(fichero).getNombre();
			
			lista.concat(indice + ") " + NFichero + "\n");
			indice++;
		}
		
		return lista;
	}
	
	// Busca un repositorio aleatorio disponible
	public int buscarRepositorioAleatorio() {
		int indiceAleatorio = new Random().nextInt(TRepositorioSesion.size() - 0 + 1) + 0;
		int repositorio = (int) TRepositorioSesion.keySet().toArray()[indiceAleatorio];
		return repositorio;
		
	}
	
	// Busca el repositorio del id de un cliente
	public int buscarRepositorio(int idCliente) {
		return TClienteRepositorio.get(idCliente);
	}
	// Busca los metadatos de un id de fichero
	public Metadatos buscarMetadatos(int idFichero) {
		return TIFicheroMetadatos.get(idFichero);
	}
	// Busca el id del cliente con su sesión.
	public int buscarIDCliente(int sesionCliente) {
		return TClienteSesion.get(sesionCliente);
	}
	
	// Comprueba el cliente en el sistema para autenticarlo
	public byte comprobarCliente(String nombre, int idCliente) {
		return 0;
	}
	// Inserta un cliente nuevo para registrarlo
	public byte insertarCliente(String nombre, int idCliente) {
		return 0;
	}
	
	// Comprueba el repositorio en el sistema para autenticalo
	public byte comprobarRepositorio(String nombre, int idCliente) {
		return 0;
	}
	// Inserta un repositorio nuevo para registrarlo
	public byte insertarRepositorio(String nombre, int idCliente) {
		return 0;
	}
	
	// Agrega un fichero a la lista de ficheros
	public int insertarFichero(String nombre, int sesionCliente) {
		return 0;
	}
	// Elimina un fichero a la lista de ficheros
	public int eliminarFichero(int idFichero, int sesionCliente) {
		return 0;
	}
	// Comprueba si el fichero pertenece al cliente y devuelve sus metadatos
	public int descargarFichero(int idFichero, int sesionCliente) {
		return 0;
	}
	
}
