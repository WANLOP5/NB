package nube.cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import nube.comun.Fichero;
import nube.comun.IConsola;
import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioClOperadorInterface;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioDiscoClienteInterface;
import nube.comun.ServicioGestorInterface;
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
	
	// Objetos remotos para localizar los servicios rmi del servidor
	private static ServicioAutenticacionInterface srautenticador;
	private static ServicioGestorInterface srgestor;
	
	// Objetos remotos para localizar lo servicios rmi del repositorio
	private static ServicioClOperadorInterface clienteOperador;
	
	// Objetos remotos para iniciar los servicios del cliente
	private static ServicioDiscoClienteImpl discoCliente;
	
	
	// Datos de este cliente
	private static String nombreCliente;
	private static int idCliente, idRepositorioCliente;
		
	// Localiza el servicio autenticador en el registro e inicializa el objeto remoto
	private static void localizarAutenticador() {
		Utilidades.cambiarCodeBase(ServicioAutenticacionInterface.class);
		
		URLAutenticador = "rmi://localhost:" + puertoServidor + "/autenticador";
		try {
			srautenticador = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticador);
			
			System.out.println("[+] SERVICIO AUTENTICADOR LOCALIZADO EN EL SERVIDOR");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL AUTENTICADOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio gestor en el registro e inicializa el objeto remoto
	private static void localizarGestor() {
		Utilidades.cambiarCodeBase(ServicioGestorInterface.class);
		
		URLGestor = "rmi://localhost:" + puertoServidor + "/gestor";
		try {
			srgestor= (ServicioGestorInterface) Naming.lookup(URLGestor);
			System.out.println("[+] SERVICIO GESTOR LOCALIZADO EN EL SERVIDOR");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVICIO GESTOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio cliente operador en el registro e inicializa el objeto remoto
	private static void localizarClienteOperador() {
		Utilidades.cambiarCodeBase(ServicioClOperadorInterface.class);
		URLClienteOperador = "rmi://localhost:" + puertoRepositorio + "/clienteOperador/" + idRepositorioCliente;
		
		try {
			clienteOperador = (ServicioClOperadorInterface) Naming.lookup(URLClienteOperador);
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
		URLDiscoCliente = "rmi://localhost:" + puertoCliente + "/discoCliente/"+idCliente;
		
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
	private static boolean bucleMenuRegistro() {
		boolean finalizado = false, autenticado = false;
		
		do {
			String opciones[] = {"Registrar un nuevo usuario", "Autenticarse en el sistema", 
								"Salir"};
			int opcion = IConsola.desplegarMenu("Cliente", opciones);
			
			switch(opcion) {
			case 1: 
				try {
					String nombre = IConsola.pedirDato("NOMBRE");
					
					switch(srautenticador.registrarCliente(nombre)) {
					case -2 : 
						System.err.println("(ERROR) NO HAY REPOSITORIOS EN LINEA PARA "+nombre); 
						break;
					case -1: 
						System.err.println("\n(ERROR) YA EXISTE UN CLIENTE CON NOMBRE "+nombre); 
						break;
					default: 
						System.out.println("\n[+] "+nombre+" SE HA REGISTRADO EN EL SISTEMA"); 
						break;
					}

						
				} catch (RemoteException | MalformedURLException | NotBoundException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					e.printStackTrace();
					System.exit(1);
				}
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 2: 
				
				try {	
					String nombre = IConsola.pedirDato("NOMBRE");					
					idCliente = srautenticador.autenticarCliente(nombre);
					
					// imprimir mensajes de error o exito segun haya devuelto el servicio autenticador
					switch(idCliente) {
					case -3 : 
						System.err.println("\n(ERROR) EL REPOSITORIO DEL CLIENTE NO ESTA EN LINEA"); 
						break;
					case -2 : 
						System.err.println("\n(ERROR) EL CLIENTE "+ nombre + " YA ESTA AUTENTICADO"); 
						break;
					case -1 : 
						System.err.println("\n(ERROR) EL CLIENTE "+ nombre + " NO ESTA REGISTRADO"); 
						break;
					default: 
						System.out.println("\n[+] "+nombre + " SE HA AUTENTICADO EN EL SISTEMA");
						
						// Inicializar los datos del cliente con el id retornado
						nombreCliente = srgestor.buscarNombreCliente(idCliente);
						idRepositorioCliente = srgestor.buscarRepositorio(idCliente);
						
						// Ruta absoluta donde se crean las carpetas
						String rutaClientes = System.getProperty("user.dir");
						System.out.println("\n(AVISO) LAS CARPETAS DE LOS CLIENTES SE CREAN EN "+rutaClientes);
						
						autenticado = true;
						finalizado = true;
						break;
					}	
				} catch (RemoteException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR AUTENTICANDO EL USUARIO");
					System.exit(1);
				}
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 3: finalizado = true; break;
			}
			
			
		} while(!finalizado);
		return autenticado;
	}
	
	private static boolean comprobarFichero(String URIFichero) {		
		File ficheroDisco = new File(URIFichero);
				
		if(ficheroDisco.isDirectory() && !ficheroDisco.exists()) {
			System.err.println("(ERROR) EL FICHERO NO EXISTE O SE INTENTO SUBIR UNA CARPETA");
			return false;
		}
		
		return true;
	}
	
	// Funcion que contiene bucle que alojara el menu principal del programa hasta 
	// que se de por finalizado
	private static void bucleMenuPrincipal() {
		boolean finalizado = false;
		
		do {
			String opciones[] = {"Subir fichero", "Bajar fichero", "Borrar fichero",
								"Listar ficheros", "Listar clientes", "Salir"};
			int opcion = IConsola.desplegarMenu("Cliente", opciones);
			
			switch(opcion) {
			case 1: 
				String URIFichero = IConsola.pedirDato("NOMBRE DEL FICHERO");
				
				if(!comprobarFichero(URIFichero)) break;
					
				try {
					Fichero nuevoFichero = new Fichero(URIFichero, ""+idCliente);
					if(srgestor.subirFichero(idCliente, URIFichero) == -1) 
						System.err.println("\n(ERROR) EXISTE UN FICHERO CON EL MISMO NOMBRE");
					
					if(!clienteOperador.subirFichero(nuevoFichero)) 
						System.err.println("\n(ERROR) NO SE PUDO SUBIR EL FICHERO");
					
					
				} catch(RemoteException e) {
					System.err.println("(ERROR) INESPERADO FUNCIONAMIENTO DE LOS SERVICIOS");
					System.exit(1);
				}
				
				System.out.println("\n[+] FICHERO SUBIDO AL REPOSITORIO CON EXITO");
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 2: 
				try {
					System.out.println(srgestor.listarFicheros(idCliente));
					int idFichero = Integer.parseInt(IConsola.pedirDato("ELIJA ID DE FICHERO"));
					
					String nombreFichero = srgestor.bajarFichero(idFichero, idCliente, URLDiscoCliente);
					
					if(nombreFichero == null) { 
						System.err.println("(ERROR) NO SE PUDO BAJAR EL FICHERO");
						break;
					}
					
					System.out.println("[+] FICHERO "+nombreFichero+" BAJADO CON EXITO");
					break;
					
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 3: 
				try {
					System.out.println(srgestor.listarFicheros(idCliente));
					int idFichero = Integer.parseInt(IConsola.pedirDato("ELIJA ID DE FICHERO"));
					String nombreFichero = srgestor.buscarMetadatos(idFichero).getNombre();
					String carpetaFichero = "" + idCliente;
					
					if(srgestor.borrarFichero(idFichero, idCliente) == -1) {
						System.err.println("(ERROR) EL ARCHIVO NO LE PERTENECE AL CLIENTE");
						break;
					}
					
					if(!clienteOperador.borrarFichero(nombreFichero, carpetaFichero)) break;
					
					System.out.println("[+] FICHERO "+nombreFichero+ ", BORRADO CON EXITO DE "
							+ "LA CARPETA DEL CLIENTE "+ idCliente);
					break;
					
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				
				break;
			case 4: 
				try {
					System.out.println(srgestor.listarFicheros(idCliente));
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 5: 
				try {
					System.out.println(srgestor.listarClientes());
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				} 
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 6:
				try { 
					srautenticador.desconectarCliente(idCliente);
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO AUTENTICADOR");
				}
				
				finalizado = true; 
				break;
			}
			
			
		} while(!finalizado);
	}
	
	public static void main(String[] args) {
		// Inicializando los puertos de los distintos servicios.
		puertoServidor = 9091;
		puertoRepositorio = 9092;
		puertoCliente = 9093;
		
		localizarAutenticador();
		localizarGestor();
		
		boolean autenticado = bucleMenuRegistro();
		
		Registry registroRMI = Utilidades.iniciarRegistro(puertoCliente);
			
		iniciarDiscoCliente();
		localizarClienteOperador();
		
		if(autenticado) bucleMenuPrincipal();
		
		tumbarDiscoCliente();
		
		Utilidades.tumbarRegistro(registroRMI);
		System.exit(0);
	}
}
