/** Clase IConsola es una interfaz de consola que brinda métodos para facilitar 
 * la interacción del programa del Servidor, Cliente y Repositorio con el usuario en la consola.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.io.IOException;
import java.util.Scanner;

public class IConsola {
	// Scanner utilizado para la entrada de datos.
	private static Scanner entrada;
	
	// Funcion para desplegar un menu, dado su titulo y las opciones a desplegar
	public static int desplegarMenu(String name, String[] entradas) {
		System.out.println("==========================");
		System.out.println(" Menu " + name); // Titulo del menu
		System.out.println("==========================");
		for(int i=0; i<entradas.length; i++) { // Imprime las opciones pasadas como argumentos enumeradas.
			System.out.println((i+1) + ") " + entradas[i]);
		}
		
		int opcionElegida = 0;
		
		try { 
			do {  // Pedir opcion al usuario
				System.out.print("\nOPCION ELEGIDA > ");
				
				entrada = new Scanner(System.in);
				opcionElegida = entrada.nextInt();
				System.out.print("\n\n");
				
			} while(opcionElegida < 0 && opcionElegida>entradas.length); // Si la opcion no existe repetir 
		}
		catch(Exception e){ 
			System.out.println("\n(ERROR) ELIGIENDO LA OPCION");
		}
		
		return opcionElegida; 
	}
	
	// Metodo para limpiar la consola imprimiendo lineas vacias.
	public static void limpiarConsola()  { 
        for(int i = 0; i < 50; i++) System.out.println("\b");
	}
	
	// Metodo para pedir un dato (en formato String) desde la consola.
	public static String pedirDato(String dato) {
		String respuesta=null;
		System.out.print(dato + "> ");
		try {
			entrada = new Scanner(System.in);
			respuesta = entrada.nextLine();
		}
		catch (Exception e) {
			System.out.println("(ERROR) EN LA INTRODUCCION DE LOS DATOS");
		}
		return respuesta;
	}
	
	// Metodo para pausar el programa esperando que el usuario presione alguna tecla
	public static void pausar() {
		System.out.println("\n[*] PRESIONE ENTER PARA CONTINUAR.. ");
		try {
			System.in.read();
		} catch (IOException e) { e.printStackTrace();}
		
	}
}
