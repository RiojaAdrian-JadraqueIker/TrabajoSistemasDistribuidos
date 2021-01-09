package principal;


import cliente.Cliente;
import interfazGrafica.Interfaz_Grafica;

public class Principal 
{
	

	public static void main(String[] args) {
		
		Cliente c =  new Cliente("localhost", 5555); //Iniciamos el cliente
		Interfaz_Grafica grafica = new Interfaz_Grafica(c); //Llamamos a la interfaz gráfica
	}

}
