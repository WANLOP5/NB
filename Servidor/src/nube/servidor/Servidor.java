/** Servidor es el programa principal que contiene el main del Servidor.
 *  Hace uso de sus servicios ya implementados y proporciona la interfaz grafica 
 *  para el usuario.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es		
 */

package nube.servidor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioGestorInterface;
import nube.comun.IConsola;

public class Servidor {
	private static int puertoServidor;
	private static int puertoRMI;
	private static Registry registroRMI;
	private static IConsola consola;
	
	private ServicioDatosInterface baseDatos;
	private ServicioAutenticacionInterface autenticador;
	private ServicioGestorInterface gestor;	
	
	public Servidor() {
		puertoServidor = 9090;
		puertoRMI = 1099;
	}
	
	public void iniciarRegistro() throws RemoteException {
		
		try { // Si el registro no se puede crear se ignora la accion por la excepcion.
			LocateRegistry.createRegistry(puertoRMI);
        } catch (ExportException e) {}

		/* Independientemente de si se acaba de crear o ya estaba creado
		* se localiza en el puerto a utilizar. */
        registroRMI = LocateRegistry.getRegistry(puertoRMI);
        
        
    }
		
	public void tumbarRegistro() {
		
	}
	
	public void iniciarServicios() {
		
	}
	
	public void tumbarServicios() {
		
	}
	
	public static void main(String[] args) {
		new Servidor();
	}
}
