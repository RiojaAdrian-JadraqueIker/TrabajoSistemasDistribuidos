package servidor;

//


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Servidor 
{
	public static void main(String args[]) 
	{
		
		ExecutorService pool = Executors.newCachedThreadPool();
		try(ServerSocket conexion = new ServerSocket(5555);) //atender por el puerto deseado
		{
			while(true)
			{
				try
				{
					Socket servidor = conexion.accept();
					AtenderPeticion peticion = new AtenderPeticion(servidor);
					pool.execute(peticion);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			pool.shutdown();
		}
	}
	

}
