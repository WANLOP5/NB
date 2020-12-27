/** Servidor es el programa principal que contiene el main del Servidor.
 *  Hace uso de sus servicios ya implementados y proporciona la interfaz grafica 
 *  para el usuario.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es		
 */

package nube.servidor;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioGestorInterface;
import nube.comun.Utilidades;
import nube.comun.IConsola;

public class Servidor {
	// Puerto para los servicios del servidor
	private static int puertoServidor;
	// Registro rmi del servidor
	private static Registry registroRMI;

	// URLs para los servicios del servidor
	private static String URLBaseDatos, URLAutenticador, URLGestor;
	// Objetos para los servicios del servidor.
	private static ServicioDatosImpl baseDatos;
	private static ServicioAutenticacionImpl autenticador;
	private static ServicioGestorImpl gestor;	
	// Objetos remotos para los servicios del servidor.
	private static ServicioDatosInterface baseDatosRemoto;
	private static ServicioAutenticacionInterface autenticadorRemoto;
	private static ServicioGestorInterface gestorRemoto;	
	
	public static void iniciarBaseDatos() {
		Utilidades.cambiarCodeBase(ServicioDatosInterface.class);
		URLBaseDatos = "rmi://localhost:" + puertoServidor + "/baseDatos";
		
		try {			
			baseDatos = new ServicioDatosImpl();
			Naming.rebind(URLBaseDatos, baseDatos);
			System.out.println("[+] SERVICIO DE BASE DE DATOS CORRIENDO");
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO LA BASE DE DATOS");
			System.exit(1);
		
		}
	}
	
	public static void iniciarAutenticador() {
		Utilidades.cambiarCodeBase(ServicioAutenticacionInterface.class);
		URLAutenticador = "rmi://localhost:" + puertoServidor + "/autenticador";
		
		try {
			autenticador = new ServicioAutenticacionImpl();
			Naming.rebind(URLAutenticador, autenticador);
			System.out.println("[+] SERVICIO AUTENTICADOR CORRIENDO");
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO AUTENTICADOR");
			System.exit(1);
		
		}
		
	}
	public static void iniciarGestor() {
		Utilidades.cambiarCodeBase(ServicioGestorInterface.class);
		URLGestor = "rmi://localhost:" + puertoServidor + "/gestor";
		
		try {
			gestor = new ServicioGestorImpl();
			Naming.rebind(URLGestor, gestor);
			System.out.println("[+] SERVICIO GESTOR CORRIENDO");
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO GESTOR");
			e.printStackTrace();
			System.exit(1);
		
		}
	
	}
	
	public static void tumbarBaseDatos() {
		try {
			Naming.unbind(URLBaseDatos);
			System.out.println("[+] BASE DE DATOS TUMBADA CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO LA BASE DE DATOS");
		} 
	}
	
	public static void tumbarAutenticador() {
		try {
			Naming.unbind(URLAutenticador);
			System.out.println("[+] SERVICIO AUTENTICADOR TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO AUTENTICADOR");
		} 
	}

	public static void tumbarGestor() {
		try {
			Naming.unbind(URLGestor);
			System.out.println("[+] SERVICIO GESTOR TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO GESTOR");
			System.exit(1);
		}  
	}

	
	public static void bucleMenuPrincipal() {
		boolean finalizado = false;
		
		do {
			String[] opciones = {"Listar clientes", "Listar repositorios", "Listar parejas cliente-repositorio"};
			int opcion = IConsola.desplegarMenu("Servidor", opciones);
			
			switch(opcion) {
			case 1: baseDatos.listarClientes(); break;
			case 2: baseDatos.listarRepositorios(); break;
			case 3: baseDatos.listarClientesRepositorios(); break;
			case 4: finalizado = true; break;
			}
			
			
		} while(!finalizado);
	}
	
	public static void main(String[] args) {
		puertoServidor = 9091;
		registroRMI = Utilidades.iniciarRegistro(puertoServidor);
		
		iniciarBaseDatos();
		iniciarAutenticador();
		iniciarGestor();
	
		bucleMenuPrincipal();
		
		tumbarBaseDatos();
		tumbarAutenticador();
		tumbarGestor();
		
		Utilidades.tumbarRegistro(puertoServidor);
		System.exit(0);
	}
}
