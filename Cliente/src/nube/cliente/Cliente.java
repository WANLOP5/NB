package nube.cliente;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import nube.comun.IConsola;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioDiscoClienteInterface;
import nube.comun.Utilidades;
import nube.servidor.ServicioAutenticacionImpl;
import nube.servidor.ServicioDatosImpl;
import nube.servidor.ServicioGestorImpl;
import nube.repositorio.ServicioClOperadorImpl;

public class Cliente {
	// Puertos de los servicios en rmi
	private static int puertoCliente, puertoServidor, puertoRepositorio;
	
	// URLs de los servicios rmi a usar
	private static String URLAutenticador, URLGestor, 
			URLDiscoCliente, URLClienteOperador;
	
	// Objetos remotos para localizar los servicios rmi
	private static ServicioAutenticacionImpl srautenticador;
	private static ServicioGestorImpl srgestor;
	private static ServicioDiscoClienteImpl discoCliente;
	private static ServicioClOperadorImpl clienteOperador;
	
	// Objeto de registro rmi
	private static Registry registroRMI;
	
	// Datos de este cliente
	private static String nombreCliente;
	private static int idCliente, idRepositorioCliente;
		
	// Localiza el servicio autenticador en el registro e inicializa el objeto remoto
	private static void localizarAutenticador() {
		URLAutenticador = "rmi://localhost:" + puertoServidor + "/autenticador";
		try {
			srautenticador = (ServicioAutenticacionImpl) Naming.lookup(URLAutenticador);
			System.out.println("[+] SERVICIO AUTENTICADOR LOCALIZADO EN EL SERVIDOR");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL AUTENTICADOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio gestor en el registro e inicializa el objeto remoto
	private static void localizarGestor() {
		URLGestor = "rmi://localhost:" + puertoServidor + "/gestor";
		try {
			srgestor= (ServicioGestorImpl) Naming.lookup(URLGestor);
			System.out.println("[+] SERVICIO GESTOR LOCALIZADO EN EL SERVIDOR");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVICIO GESTOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio cliente operador en el registro e inicializa el objeto remoto
	private static void localizarClienteOperador() {
		URLClienteOperador = "rmi://localhost:" + puertoRepositorio + "/clienteOperador";
		try {
			clienteOperador = (ServicioClOperadorImpl) Naming.lookup(URLClienteOperador);
			System.out.println("[+] SERVICIO CLIENTE OPERADOR LOCALIZADO EN EL REPOSITORIO");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVICIO CLIENTE OPERADOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Pone a correr el servicio del disco cliente y lo ingresa al registro rmi
	private static void iniciarDiscoCliente() {
		Utilidades.cambiarCodeBase(ServicioDiscoClienteInterface.class);
		URLDiscoCliente = "rmi://localhost:" + puertoCliente + "/discoCliente";
		
		try {			
			discoCliente = new ServicioDiscoClienteImpl();
			Naming.rebind(URLDiscoCliente, discoCliente);
			System.out.println("[+] SERVICIO DISCO CLIENTE CORRIENDO");
		} catch (RemoteException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO DISCO CLIENTE");
			e.printStackTrace();
			System.exit(1);
		
		}
		
	}

	// Tumba el servicio disco cliente y lo saca del registro rmi
	private static void tumbarDiscoCliente() {
		try {
			Naming.unbind(URLDiscoCliente);
			System.out.println("[+] DISCO CLIENTE TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO DISCO CLIENTE");
		} 
	}
	
	// Funcion que contiene bucle que alojara el menu de registro del programa 
	// hasta que se de por finalizado
	public static boolean bucleMenuRegistro() {
		boolean finalizado = false, autenticado = false;
		
		do {
			String opciones[] = {"Registrar un nuevo usuario", "Autenticarse en el sistema", 
								"Salir"};
			int opcion = IConsola.desplegarMenu("Cliente", opciones);
			
			switch(opcion) {
			case 1: 
				String nombreRegistro = IConsola.pedirDato("NOMBRE");
				try {
					srautenticador.registrarCliente(nombreRegistro);
					System.out.println("[+] USUARIO REGISTRADO");
				} catch (RemoteException | MalformedURLException | NotBoundException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					System.exit(1);
				}
				break;
			case 2: 
				String nombreAutenticacion = IConsola.pedirDato("NOMBRE");
				try {
					idCliente = srautenticador.autenticarCliente(nombreAutenticacion);
					System.out.println("[+] USUARIO AUTENTICADO");
				} catch (RemoteException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					System.exit(1);
				}
				break;
			case 3: finalizado = true; break;
			}
			
			
		} while(!finalizado);
		return autenticado;
	}
	
	// Funcion que contiene bucle que alojara el menu principal del programa hasta 
	// que se de por finalizado
	private static void bucleMenuPrincipal() {
		boolean finalizado = false;
		
		do {
//			1.- Subir fichero.
//			2.- Bajar fichero.
//			3.- Borrar fichero.
//			4.- Compartir fichero (Opcional).
//			5.- Listar ficheros.
//			6.- Listar Clientes del sistema.
//			7.- Salir.
		
			String opciones[] = {""};
			int opcion = IConsola.desplegarMenu("Cliente", opciones);
			
			switch(opcion) {
			case 1: 
				String URIFichero = IConsola.pedirDato("NOMBRE DEL FICHERO");
				idRepositorioCliente = srgestor.subirFichero(idCliente, URIFichero);
				
				File ficheroDisco = new File(URIFichero);
				if(!ficheroDisco.isDirectory() && ficheroDisco.exists()) {
					System.err.println("(ERROR) EL FICHERO INGRESADO NO EXISTE");
					break;
				}

			
				break;
			case 2: 
				srgestor.bajarFichero(idFichero, idCliente); 
				break;
			case 3: 
				idRepositorioCliente = srgestor.borrarFichero(idFichero, idCliente); 
				break;
			case 4: 
				srgestor.listarFicheros(idCliente); 
				break;
			case 5: 
				srgestor.listarClientes(); 
				break;
			case 6: finalizado = true; break;
			}
			
			
		} while(!finalizado);
	}
	
	public static void main(String[] args) {
		// Inicializando los puertos de los distintos servicios.
		puertoServidor = 9091;
		puertoRepositorio = 9092;
		puertoCliente = 9093;
		
		registroRMI = Utilidades.iniciarRegistro(puertoCliente);
			
		iniciarDiscoCliente();
		localizarAutenticador();
		localizarGestor();
		
		boolean autenticado = bucleMenuRegistro();
		if(autenticado) bucleMenuPrincipal();
		
		tumbarDiscoCliente();
		
		Utilidades.tumbarRegistro(puertoCliente);
		System.exit(0);
	}


	
	
	
}
