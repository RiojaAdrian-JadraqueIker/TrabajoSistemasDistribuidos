package principal;

import java.io.IOException;
import java.net.UnknownHostException;

import cliente.Cliente;
import interfazGrafica.Interfaz_Grafica;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Principal {

	public static void main(String[] args) throws UnknownHostException, IOException, BasicPlayerException {
		
		Cliente c =  new Cliente("localhost", 5555);
		Interfaz_Grafica grafica = new Interfaz_Grafica(c);
		grafica.mostrarInterfaz();
	}

}
