/** Clase repositorio
 *  Esta clase contiene metodos para listar,crear,iniciar,registrar carpetas en el repositorio del sistema
 *  con el id de los clientes y las sesiones.
 * 
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.repositorio;

import java.io.File;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import nube.comun.IConsola;
import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioSrOperadorInterface;
import nube.comun.Utilidades;

public class Repositorio {
	
	// lista de carpetas que se crearan en el repositorio
	public static List<String> listaDeCarpetas;
	
	
	//atributos para buscar en el servicio de autenticacion del servidor
	private static int sesion = 1;
	private static int puerto = 9091;
	private static ServicioAutenticacionInterface servidor;
	private static String direccion ="localhost";
	
	//para guardar las URL RMI
	private static String autenticador;
	private static String servidorOperador;
	private static String clienteOperador;
	
	//atributos para correr los servicios servidor-operador y cliente-operador
	private static int puertoDeServicio = 9092;
	private static Registry registryServicio;
	private static String direccionServicio = "localhost";
	
	//metodo main del repositorio
	public static void main(String[]args) throws Exception{
		
		autenticador = "rmi://" + direccion + ":" + puerto + "/autenticador";
		servidorOperador = "rmi://" + direccionServicio + ":" + puertoDeServicio + "/servidorOperador/";
		clienteOperador = "rmi://" + direccionServicio + ":" + puertoDeServicio + "/clienteOperador/";
	
		new Repositorio().iniciar();
		System.exit(0); // finaliza el programa
	}

	// inicializado la tabla de la estructura logica de las carpetas
	public Repositorio () {
		super();
		listaDeCarpetas = new ArrayList<String>();
	}
	
	/* pone en funcionamiento todos los repositorios.
	 * intentando acceder para registrar o autenticar
	*/
	private void iniciar() throws Exception{
		// si el servidor no esta disponible, se cierra dejando informacion sobre ello
		try {
			
			String URLRegistro = autenticador;
			servidor = (ServicioAutenticacionInterface)Naming.lookup(URLRegistro);
			int opcion;
			
			do {
				String [] opciones = {"Registrar repositorio","Autenticar repositorio"};
				opcion = IConsola.desplegarMenu("Repositorio", opciones);
				switch(opcion) {
				case 1: 
					registrar(); 
					IConsola.pausar();
					IConsola.limpiarConsola();
					break;
				case 2: 
					autenticar(); 
					IConsola.pausar();
					IConsola.limpiarConsola();
					break;
				}
			}while(opcion != 3);
			
		} catch (ConnectException e) {
			System.err.println("(ERROR) NO SE PUDO CONECTAR AL SERVIDOR O NO ESTA DISPONBILE");
			IConsola.pausar();
		}
		
	}
	
	// inicio de autenticacion de un repositorio en el sistema
	private void autenticar() throws Exception{
		String r = IConsola.pedirDato("NOMBRE DE REPOSITORIO");
		sesion = servidor.autenticarRepositorio(r);
		switch(sesion) {
		case -2 : System.err.println("(ERROR) EL REPOSITORIO "+ r + " YA ESTA AUTENTICADO"); break;
		case -1 : System.err.println("(ERROR) EL REPOSITORIO "+ r +" NO ESTA REGISTRADO"); break;
		default : 
			System.out.println("[+] "+r+" SE HA AUTENTICADO EN EL SISTEMA"); 
			IConsola.pausar();
			IConsola.limpiarConsola();
			iniciarServicio(); // corre los servicios servidor-operador y cliente-operador
			break;
		}
	}

	// corre los servicios del repositorio con su identificador de sesion
	private void iniciarServicio() throws Exception{
		
		String URLRegistro;
		//metodo iniciarRegistro de la clase utilidades
		Utilidades.iniciarRegistro(puertoDeServicio);
		Utilidades.cambiarCodeBase(ServicioSrOperadorInterface.class);
		
		// correr servidorOperador en servidorOperador
		ServicioSrOperadorImpl ObjSerOperador = new ServicioSrOperadorImpl();
		URLRegistro = servidorOperador + sesion; // RMI
		Naming.rebind(URLRegistro, ObjSerOperador);
		System.out.println("[+] SERVICIO SERVIDOR OPERADOR CORRIENDO");
		
		//correr clienteOperador en clienteOPerador
		ServicioClOperadorImpl ObjSerClienteOperador = new ServicioClOperadorImpl();
		URLRegistro = clienteOperador + sesion;//RMI
		Naming.rebind(URLRegistro, ObjSerClienteOperador);
		System.out.println("[+] SERVIDOR CLIENTE OPERADOR CORRIENDO");
		
		//menu una vez autenticado el servicio
		int opcion = 0;
		do {
			opcion = IConsola.desplegarMenu("Repositorio", new String[] 
					{"Lista clientes","Listar ficheros de los clientes"});
			switch(opcion) {
			case 1 : 
				listarClientes();
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 2 : 
				listarFicherosClientes();
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			}
		}while(opcion !=3);
		
		try{
			// eliminar servidor-Operador
			URLRegistro = servidorOperador + sesion; // RMI
			Naming.unbind(URLRegistro);
			System.out.println("[+] SERVICIO SERVIDOR OPERADOR TUMBADO");
			
			// eliminar cliente-operador
			URLRegistro = clienteOperador + sesion; // RMI
			Naming.unbind(URLRegistro);
			System.out.println("[+] SERVICIO CLIENTE OPERADOR TUMBADO");
			
			// cerrar RMIRegistry del objeto registry unico
			if(RegistryEstaVacio(clienteOperador)) { //RMI
				UnicastRemoteObject.unexportObject(registryServicio, true); // true aunque hayan cosas pendientes, false solo si no hay pendientes. 
				System.out.println("[+] REGISTRO RMI CERRADO");
			}else 
				System.out.println("La operacion registry todavia esta abierta aun quedan repositorios conectados");
			
		}catch(NoSuchObjectException e) {
			System.err.println("(ERROR) NO SE PUDO CERRAR EL REGISTRO RMI");
		}
		
		sesion =0;
		
	}
	
	// Registra un repositorio en el sistema
	public void registrar() throws RemoteException{
		String re = IConsola.pedirDato("NOMBRE DE REPOSITORIO");

		if(servidor.registrarRepositorio(re) != -1)
			System.out.println("[+] "+re+" SE HA REGISTRADO EN EL SISTEMA");
		else
			System.err.println("(ERROR) YA EXISTE UN REPOSITORIO CON NOMBRE "+re);
	}

	
	// metodo que imprime la lista de ficheros de los cliente
	private void listarFicherosClientes() {
		if(listaDeCarpetas.isEmpty()) {
			System.out.println("[+] NO HAY CARPETAS");
		}else {
			listarClientes();
			String carpeta = IConsola.pedirDato("NOMBRE DE CARPETA");
			File dir = new File(carpeta);
			if(dir.exists()) {
				String[] ficheros = dir.list();
				for(String s : ficheros) System.out.println(s);
			}else {
				System.out.println("(ERROR) LA CARPETA NO EXISTE");
			}
		}
	}
	
	// metodo que imprime la lista de carpetas, los identificadores de los clientes que mantiene en la estructura logica listaDeCarpetas
	private void listarClientes() {
		System.out.println(listaDeCarpetas);
	}
		
	// metodo para comprobar si esta vacia la lista de los servicios de la URL
	private static boolean RegistryEstaVacio(String URL) throws RemoteException, MalformedURLException{
		String[] names = Naming.list(URL);
		if(names.length == 0) 
			return true;
		else
			return false;
			
	}
}
