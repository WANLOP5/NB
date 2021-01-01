/** Clase repositorio
 *  Esta clase contiene metodos para listar,crear,iniciar,registrar carpetas en el repositorio del sistema
 *  con el id de los clientes y las sesiones.
 * 
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.repositorio;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import nube.comun.IConsola;
import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioClOperadorInterface;
import nube.comun.ServicioSrOperadorInterface;
import nube.comun.Utilidades;

public class Repositorio {
	// Lista de carpetas que guarda el repositorio
	public static List<String> carpetasRepositorio;
	
	// Puertos para los distintos servicios rmi.
	private static int puertoRepositorio, puertoServidor;
	
	// URLs para los servicios rmi.
	private static String URLAutenticador, URLClienteOperador, URLServidorOperador;
	
	// Objetos para iniciar los servicios del repositorio
	private static ServicioClOperadorImpl clienteOperador;
	private static ServicioSrOperadorImpl servidorOperador;
	
	// Objetos para localizar los servicios del servidor
	private static ServicioAutenticacionInterface srautenticador;
	
	// ID del repositorio autenticado
	private static int idRepositorio;
	
	// Inserta una nueva carpeta a las carpetas del cliente en el repositorio
	public static void insertarCarpetaCliente(int idCliente) {
		carpetasRepositorio.add("" + idCliente);
	}
	
	// Localiza el servicio autenticador en el registro e inicializa el objeto remoto
	private static void localizarAutenticador() {
		URLAutenticador = "rmi://localhost:" + puertoServidor + "/autenticador";
		try {
			srautenticador = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticador);
			System.out.println("[+] SERVICIO AUTENTICADOR LOCALIZADO EN EL SERVIDOR");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL AUTENTICADOR");
			System.exit(1);
		}
		
	}
	
	// Pone a correr el servicio cliente operador y lo ingresa al registro rmi
	private static void iniciarClienteOperador() {
		Utilidades.cambiarCodeBase(ServicioClOperadorInterface.class);
		URLClienteOperador = "rmi://localhost:" + puertoRepositorio + "/clienteOperador/" + idRepositorio;
		
		try {
			clienteOperador = new ServicioClOperadorImpl();
			Naming.rebind(URLClienteOperador, clienteOperador);
			System.out.println("[+] SERVICIO CLIENTE OPERADOR CORRIENDO");
		} catch(RemoteException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO CLIENTE OPERADOR");
		}
	}
	
	// Pone a correr el servicio servidor operador y lo ingresa al registro rmi
	private static void iniciarServidorOperador() {
		Utilidades.cambiarCodeBase(ServicioSrOperadorInterface.class);
		URLServidorOperador = "rmi://localhost:" + puertoRepositorio + "/servidorOperador/" + idRepositorio;
		
		try {
			servidorOperador = new ServicioSrOperadorImpl();
			Naming.rebind(URLServidorOperador, servidorOperador);
			System.out.println("[+] SERVICIO SERVIDOR OPERADOR CORRIENDO");
		} catch(RemoteException | MalformedURLException | NotBoundException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO SERVIDOR OPERADOR");
			System.exit(1);
		}
	}
	
	// Tumba el servicio cliente operador y lo saca del registro rmi
	private static void tumbarClienteOperador() {
		try {
			Naming.unbind(URLClienteOperador);
			System.out.println("[+] CLIENTE OPERADOR TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO CLIENTE OPERADOR");
		}
	}
	
	// Tumba el servicio servidor operador y lo saca del registro rmi
	private static void tumbarServidorOperador() {
		try {
			Naming.unbind(URLServidorOperador);
			System.out.println("[+] SERVIDOR OPERADOR TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO SERVIDOR OPERADOR");
		}
	}
	
	// Funcion que contiene bucle que alojara el menu de registro del programa 
	// hasta que se de por finalizado
	private static boolean bucleMenuRegistro() {
		boolean finalizado = false, autenticado = false;
		
		do {
			String opciones[] = {"Registrar un nuevo usuario", "Autenticarse en el sistema"};
			int opcion = IConsola.desplegarMenu("Repositorio", opciones);
			
			switch(opcion) {
			case 1:
				try {
					String nombre = IConsola.pedirDato("NOMBRE");
					
					if(srautenticador.registrarRepositorio(nombre) != -1)
						System.out.println("[+] "+nombre+" SE HA REGISTRADO EN EL SISTEMA");
					else
						System.err.println("(ERROR) YA EXISTE UN REPOSITORIO CON NOMBRE "+nombre);
						IConsola.pausar();
						IConsola.limpiarConsola();
				} catch (RemoteException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					System.exit(1);
				}
				
				break;
			case 2:
				try {	
					String nombre = IConsola.pedirDato("NOMBRE");
					idRepositorio = srautenticador.autenticarRepositorio(nombre);
					
					switch(idRepositorio) {
					case -2 : 
						System.err.println("(ERROR) EL REPOSITORIO "+ nombre + " YA ESTA AUTENTICADO"); 
						break;
					case -1 : 
						System.err.println("(ERROR) EL REPOSITORIO "+ nombre +" NO ESTA REGISTRADO"); 
						break;
					default : 
						System.out.println("[+] "+nombre+" SE HA AUTENTICADO EN EL SISTEMA"); 
						autenticado = true;
						finalizado = true;
						IConsola.pausar();
						IConsola.limpiarConsola();
						break;
					}
				} catch (RemoteException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					System.exit(1);
				}				
			}
		
		} while(finalizado == false);
		return autenticado;
	}
	
	// Funcion que contiene el bucleque alojara el menu principal del programa hasta que 
	// se de por finalizado
	private static void bucleMenuPrincipal() {
		boolean finalizado = false;
		
		do {
			String[] opciones = {"Listar clientes","Listar ficheros de los clientes", "Salir"};
			int opcion = IConsola.desplegarMenu("Repositorio", opciones);
			
			switch(opcion) {
			case 1 : 
				System.out.println(carpetasRepositorio);
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 2 : 
				System.out.println(carpetasRepositorio);
				String carpeta = IConsola.pedirDato("NOMBRE DE CARPETA");
				
				File carpetaDisco = new File(carpeta);
				if(carpetaDisco.exists()) {
					String[] ficheros = carpetaDisco.list();
					for(String s : ficheros) System.out.println(s);
				}else {
					System.err.println("(ERROR) LA CARPETA NO EXISTE");
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 3:
				try { 
					srautenticador.desconectarRepositorio(idRepositorio);
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO AUTENTICADOR");
				}
				finalizado = true; 
				break;
			} 
		} while(!finalizado);		
	}
	
	public static void main(String [] args) {
		// Inicializando la lista de carpetas
		carpetasRepositorio = new ArrayList<String>();
		
		// Inicializando los puertos de los distintos servicios.
		puertoServidor = 9091;
		puertoRepositorio = 9092;
		
		localizarAutenticador();
		boolean autenticado = bucleMenuRegistro();
		
		Registry registroRMI = Utilidades.iniciarRegistro(puertoRepositorio);
		
		iniciarClienteOperador();
		iniciarServidorOperador();
		
		if(autenticado) bucleMenuPrincipal();
		
		tumbarClienteOperador();
		tumbarServidorOperador();
		
		Utilidades.tumbarRegistro(registroRMI);
		System.exit(0);
	}
}
