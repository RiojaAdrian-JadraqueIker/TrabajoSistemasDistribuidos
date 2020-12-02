package principal;

import java.io.IOException;
import java.net.UnknownHostException;

import cliente.Cliente;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Principal {

	public static void main(String[] args) throws UnknownHostException, IOException, BasicPlayerException {
		
		Cliente c =  new Cliente("localhost", 5555);
		c.conectar();
	}

}
