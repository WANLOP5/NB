package nube.repositorio;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nube.comun.ServicioSrOperadorInterface;
import nube.comun.Fichero;
import nube.comun.ServicioDiscoClienteInterface;

public class ServicioSrOperadorImpl extends UnicastRemoteObject implements ServicioSrOperadorInterface{
	private int puertoDiscocliente;
	private String URLDiscoCliente;
	ServicioDiscoClienteInterface discoCliente;

	
	// identificador generado por eclipse al utilizar la clase UnicastRemoteObject
	private static final long serialVersionUID = 400066334857238973L;
	
	// constructor de la clase ServicioSrOperadorImpl
	public ServicioSrOperadorImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		puertoDiscocliente = 9092;
		URLDiscoCliente = "rmi://localhost:" + puertoDiscocliente + "/discoCliente";
		discoCliente = (ServicioDiscoClienteInterface) Naming.lookup(URLDiscoCliente);
	}

	@Override
	public void bajarFichero(String Fnombre, int idCliente) throws RemoteException  {
		
		Fichero fichero = new Fichero(""+idCliente,Fnombre,""+idCliente);
		
		if(discoCliente.bajarFicheroDisco(Fnombre,fichero)==false)
		{
			System.out.println("ha ocurrido un error en el envio (fallo en el checksum), debe intentarlo de nuevo");
		}
		else {
			System.out.println("Fichero bajado"+ Fnombre);
		}
	}

	@Override
	public boolean crearCarpetaRepositorio(int idCliente) throws RemoteException {
			File carpeta = new File(""+idCliente);
			boolean carpetaCreada = carpeta.mkdir();
			
			if(carpetaCreada) {
				
			}else {}
		return false;
	}
	
	
	
}
