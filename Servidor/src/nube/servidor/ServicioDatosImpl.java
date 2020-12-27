/** ServicioDatosImpl contiene los metodos para gestionar los datos en el sistema.
 * 
 * Hace la funcion de una base de datos para el servidor utilizando ArrayList y HashMap
 * para relacionar Cliente-Metadatos-Fichero-Repositorio
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

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
	private HashMap<Integer, ArrayList<Integer>> TClienteFicheros;
	
	// Datos para localizar el ServicioServidorOperador
	private int puertoSrOperador;
	private String URLServidorOperador;
	private ServicioSrOperadorInterface servidorOperador;
	
	// Inicializa las tablas de los datos del sistema y 
	// localiza el ServicioSrOperador en el registro.
	public ServicioDatosImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		
		// Inicializa las tablas de los datos del sistema
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
		
		TClienteFicheros = new HashMap<Integer, ArrayList<Integer>>();
		
		// atributos para localizar el ServicioServidorOperador en el rmiregistry
		puertoSrOperador = 9091; 
		URLServidorOperador = "rmi://localhost:" + puertoSrOperador + "/servidorOperador";
		
		
	}
	
	private void localizarServidorOperador() {
		try {
			servidorOperador = (ServicioSrOperadorInterface) Naming.lookup(URLServidorOperador);
		} catch(RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
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
		
		for(int fichero : TClienteFicheros.get(idCliente)) {
			String nFichero = buscarMetadatos(fichero).getNombre();
			
			lista.concat(fichero + ") " + nFichero + "\n");
		}
		
		return lista;
	}
	
	// Busca un repositorio aleatorio disponible
	public int buscarRepositorioAleatorio() {
		if(TRepositorioSesion.size() == 0) return 0;
		
		int indiceAleatorio = new Random().nextInt(TRepositorioSesion.size() - 0 + 1) + 0;
		int repositorio = (int) TRepositorioSesion.keySet().toArray()[indiceAleatorio];
		
		return repositorio;
		
	}
	
	// Busca el repositorio del id de un cliente
	public int buscarRepositorio(int idCliente) {
		return TClienteRepositorio.get(idCliente);
	}
	
	// Busca el nombre de un cliente
	public String buscarNombreCliente(int idCliente) {
		return TIClienteNCliente.get(idCliente);
	}
	
	// Busca los metadatos de un id de fichero
	public Metadatos buscarMetadatos(int idFichero) {
		return TIFicheroMetadatos.get(idFichero);
	}
	// Busca el id del cliente con su sesionn.
	public int buscarIDCliente(int sesionCliente) {
		return TClienteSesion.get(sesionCliente);
	}
	// Busca el id del repositorio con su sesionn.
	public int buscarIDRepositorio(int sesionRepositorio) {
		return TRepositorioSesion.get(sesionRepositorio);
	}
	
	// Comprueba el cliente en el sistema para autenticarlo
	private int comprobarCliente(String nombre, String accion) {
		if(accion.toLowerCase() == "registrar") {
			// Comprueba que el cliente no este registrado en el sistema
			if(TNClienteICliente.containsKey(nombre)) return -1;
		}
		else if(accion.toLowerCase() == "autenticar") {
			// Comprueba que el cliente este registrado en el sistema
			if(!TNClienteICliente.containsKey(nombre)) return -1;
			// Comprueba que el cliente no este autenticado en el sistema
			int idCliente = TNClienteICliente.get(nombre);
			if(!TClienteSesion.containsKey(idCliente)) return -2;
			// Comprueba que el repositorio del cliente este autenticado en el sistema.
			int repositorioCliente = TClienteRepositorio.get(idCliente);
			if(!TRepositorioSesion.containsKey(repositorioCliente)) return -3;
		} 
		return 0;
		
	}
	
	// Inserta los pares cliente-sesion para autenticar un cliente
	public int insertarSesionCliente(String nombre, int sesion) {
		// Comprueba que no hayan inconvenientes para insertar.
		int errorInsertar = comprobarCliente(nombre, "autenticar");
		if(errorInsertar != 0) return errorInsertar;
		
		int idCliente = buscarIDCliente(sesion);
		TClienteSesion.put(idCliente, sesion);
		TClienteSesion.put(sesion, idCliente);
		
		return idCliente;
	}
	// Inserta un cliente nuevo para registrarlo
	public int insertarCliente(String nombre, int idCliente) throws RemoteException {
		// Comprueba que no hayan inconvenientes para insertar.
		int errorInsertar = comprobarCliente(nombre, "registrar");
		if(errorInsertar != 0) return errorInsertar;
		
		// Buscar un repositorio aleatorio de los disponibles para el cliente.
		int idRepositorio = buscarRepositorioAleatorio(); 
		// Si no hay repositorios entonces devolver -2 como error.
		if(idRepositorio == 0) return -2;
		
		localizarServidorOperador();
		
		// Crear la carpeta en el repositorio con el id del cliente.
		servidorOperador.crearCarpetaRepositorio(idCliente);
		// Ingresar al cliente en las tablas nombre-cliente
		TNClienteICliente.put(nombre, idCliente);
		TIClienteNCliente.put(idCliente, nombre);
		// Ingresar los pares cliente-repositorio para relacionarlos en las tablas.
		TClienteRepositorio.put(idCliente, idRepositorio);
		TRepositorioCliente.put(idRepositorio, idCliente);
		/* Ingresar el cliente en la tabla que lo relacionara 
		* con su nueva lista de sus ficheros. */
		TClienteFicheros.put(idCliente, new ArrayList<Integer>());
		
		return idCliente;
		
	}
	
	// Comprueba el repositorio en el sistema para autenticarlo
	private int comprobarRepositorio(String nombre, String accion) {
		if(accion.toLowerCase() == "registrar") {
			// Comprueba que el cliente no este registrado en el sistema
			if(TNRepositorioIRepositorio.containsKey(nombre)) return -1;
		}
		else if(accion.toLowerCase() == "autenticar") {
			// Comprueba que el repositorio este registrado en el sistema
			if(!TNRepositorioIRepositorio.containsKey(nombre)) return -1;
			// Comprueba que el repositorio no este autenticado en el sistema
			int idRepositorio = TNClienteICliente.get(nombre);
			if(!TRepositorioSesion.containsKey(idRepositorio)) return -2;
		}
		
		return 0;
	}
	
	
	// Inserta los pares repositorio-sesion para autenticar un cliente
	public int insertarSesionRepositorio(String nombre, int sesion) {
		// Comprueba que no hayan inconvenientes para insertar.
		int errorInsertar = comprobarRepositorio(nombre, "autenticar");
		if(errorInsertar != 0) return errorInsertar;
		
		int idRepositorio = buscarIDRepositorio(sesion);
		TClienteSesion.put(idRepositorio, sesion);
		TClienteSesion.put(sesion, idRepositorio);
		
		return idRepositorio;
	}
	
	// Inserta un repositorio nuevo para registrarlo
	public int insertarRepositorio(String nombre, int idRepositorio) {
		// Comprueba que no hayan inconvenientes para insertar.
		int errorInsertar = comprobarRepositorio(nombre, "registrar");
		if(errorInsertar != 0) return errorInsertar;
		
		// Ingresar los pares nombre-id del repositorio al sistema.
		TNRepositorioIRepositorio.put(nombre, idRepositorio);
		TIRepositorioNRepositorio.put(idRepositorio, nombre);
		
		return idRepositorio;
	}
	
	// Comprueba que el id de fichero pasado no se encuentre en la lista del cliente.
	private int comprobarFichero(int idFichero, int idCliente) {
		return TClienteFicheros.get(idCliente).contains(idFichero) ? 0 : -1;
	}
	
	// Comprueba que el cliente no disponga de un fichero con el nombre especificado.
	private int comprobarFicheroDuplicado(String nombreFichero, int idCliente) {
		for(Metadatos m : TMetadatosIFichero.keySet()) {
		    if(m.getIdCliente() == idCliente && nombreFichero == m.getNombre()) 
		    	return -1;
		}
		
		return 0;
	}
	
	// Agrega un fichero a la lista de ficheros
	public int insertarFichero(String nombreFichero, int idCliente) {		
		// Nuevos metadatos para el fichero a agregar al cliente.
		Metadatos nuevoFichero = new Metadatos(idCliente, buscarRepositorio(idCliente),nombreFichero);
		
		// Comprueba que el fichero no este duplicado antes de agregarlo
		// No pueden haber dos ficheros con el mismo nombre para un mismo cliente.
		int errorDuplicado = comprobarFicheroDuplicado(nuevoFichero.getNombre(), idCliente);
		if(errorDuplicado != 0) return errorDuplicado;
		
		// Inserta el id del fichero a la lista del cliente
		TClienteFicheros.get(idCliente).add(nuevoFichero.getIdFichero());
		// Inserta el id del fichero y los nuevos metadatos a las tablas correspondientes. 
		TIFicheroMetadatos.put(nuevoFichero.getIdFichero(), nuevoFichero);
		TMetadatosIFichero.put(nuevoFichero, nuevoFichero.getIdFichero());
		
		// Devuelve el id del nuevo fichero
		return nuevoFichero.getIdFichero();
		
	}
	// Elimina un fichero a la lista de ficheros
	public int eliminarFichero(int idFichero, int idCliente) {
		int errorEliminar = comprobarFichero(idFichero, idCliente);
		// Comprobar que el fichero pertenece al cliente
		if(errorEliminar != 0) return errorEliminar;
		
		// Elimina el id del fichero de la lista del cliente
		TClienteFicheros.get(idCliente).remove(idFichero);
		// Elimina el fichero de las tablas fichero-metadatos
		TIFicheroMetadatos.remove(idFichero);
		TMetadatosIFichero.remove(buscarMetadatos(idFichero));
		
		// Devuelve el id del fichero borrado
		return idFichero;
		
	}
	// Comprueba si el fichero pertenece al cliente y devuelve sus metadatos
	public Metadatos descargarFichero(int idFichero, int idCliente) {
		int errorPropietario = comprobarFichero(idFichero, idCliente);
		// Comprobar que el fichero pertenece al cliente
		if(errorPropietario != 0) return null;
		
		// Devuelve los metadatos del fichero
		return buscarMetadatos(idFichero);
	}
	
}
