package cliente;



import java.io.*;
import java.net.*;
import java.util.*;
import reproductor.Reproductor;



//
public class Cliente
{

	private String host;
	private int puerto;
	private Reproductor reproductor;
	private DataInputStream mensajesEntrada;
	private PrintStream mensajesSalida;


	public Cliente(String host, int puerto) {
		
		this.host = host;
		this.puerto = puerto;
		this.reproductor= new Reproductor();

	}
	
	public boolean conectar()
	{
		boolean b = false;
		try 
		{
			Socket cliente = new Socket(this.host, this.puerto);
			this.mensajesEntrada = new DataInputStream(cliente.getInputStream());
			this.mensajesSalida = new PrintStream(cliente.getOutputStream());
			b= true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return b;
		
	}
	
	public void desconetar() 
	{
		this.mensajesSalida.println(3);
		this.mensajesSalida.flush();
		
		if(this.mensajesEntrada!=null)
		{
			try
			{
				this.mensajesEntrada.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		if(this.mensajesSalida!=null)
		{
			try
			{
				this.mensajesSalida.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public List<String> listaCanciones(String busqueda) {
		List<String> lista = new ArrayList<String> ();
		
		try {
			//es el caso 4 del servidor luego mandamos la opcion 4
			this.mensajesSalida.println(4);
			this.mensajesSalida.flush();
			
			this.mensajesSalida.println(busqueda);
			this.mensajesSalida.flush();
			
			
			int tam = Integer.parseInt(mensajesEntrada.readLine());
			
			for(int i =0; i<tam;i++)
			{
				String s = mensajesEntrada.readLine();
				lista.add(s);
			} 	
			
		} 
		catch (NumberFormatException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IOException e2) 
		{
			e2.printStackTrace();
		} 
		
		return lista;	
	}

	public List<String> listaCanciones() 
	//Devuelve una lista de canciones enviada por el servidor
	{
		List<String> lista = new ArrayList<String> ();
		
		try {
			//es el caso 0 del servidor luego mandamos la opcion 0
			this.mensajesSalida.println(0);
			this.mensajesSalida.flush();
			
			int tam = Integer.parseInt(mensajesEntrada.readLine());
			
			for(int i =0; i<tam;i++)
			{
				String s = mensajesEntrada.readLine();
				lista.add(s);
			} 	
			
		} 
		catch (NumberFormatException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IOException e2) 
		{
			e2.printStackTrace();
		} 
		
		return lista;	
	}
	
	
	public boolean yaExiste(File f) {
		try(FileInputStream fichero = new FileInputStream(f);)
		{
			
			this.mensajesSalida.println(5);
			this.mensajesSalida.println(f.getName());
			this.mensajesSalida.println(f.length());
			this.mensajesSalida.flush(); //mandamos el nombre y el tamaño de lo que queremos subir
			
			if(mensajesEntrada.read()==1) {
				long tamañoAComparar = Long.parseLong( this.mensajesEntrada.readLine() ); //cogemos el amaño minimo de los dos
				long cont = 0;
				//----------------------------------------------------------------
				byte buff[] = new byte[65536];
				int leidos = fichero.read(buff);
				cont += leidos;
				boolean iguales = true;
				
				while(leidos!= -1 && iguales && (cont <= tamañoAComparar)) 
				{
					mensajesSalida.write(buff,0,leidos);
					mensajesSalida.flush();
					
					String s = this.mensajesEntrada.readLine(); //leemos si siguen siendo iguales o no
					if(!s.equals("true"))
					{
						iguales= false;
					}
					
					
					leidos = fichero.read(buff);
					cont += leidos;
				}
				
				
				if (mensajesEntrada.read()==0) {
					return false;
				} else return true;
			}
			else {
				return false;	
			}
			
			
		} 
		catch (FileNotFoundException e1 )
		{
			e1.printStackTrace();
		} 
		catch (IOException e2) 
		{
			e2.printStackTrace();
		}
		return true;
	}
	
	public void subirCancion(File f,String nombre) 
	//Envia al servidor el fichero y este lo almacena en su "BD"
	{
		try(FileInputStream fichero = new FileInputStream(f);)
		{
			this.mensajesSalida.println(2);
			this.mensajesSalida.println(nombre);
			this.mensajesSalida.println(f.length());
			this.mensajesSalida.flush();

			byte buff[] = new byte[300000];
			int leidos = fichero.read(buff);
			while(leidos!=-1)
			{
				this.mensajesSalida.write(buff, 0, leidos);
				this.mensajesSalida.flush();
				leidos = fichero.read(buff);
			}
			
		} 
		catch (FileNotFoundException e1 )
		{
			e1.printStackTrace();
		} 
		catch (IOException e2) 
		{
			e2.printStackTrace();
		}
		
	}
	
	public void descargarCancion(String s) 
	//Pide al servidor la cancion dada por parametro y la descarga en el archivo mp3 del reproductor
	{
		File cancion = new File("cancion.mp3");
		
		try(FileOutputStream f = new FileOutputStream(cancion, false))
		{
			this.mensajesSalida.println(1);
			this.mensajesSalida.flush();
			
			//Le decimos la cancion que queremos:
			this.mensajesSalida.println(s);
			this.mensajesSalida.flush();
			
			
			//Descarga la cancion solicitada y la almacena en el fichero
			long tamFich = Long.parseLong(mensajesEntrada.readLine());
			byte buff[] = new byte[300000];
			int leidos = mensajesEntrada.read(buff);
			long suma =0;
			suma += leidos;
			while(suma<tamFich)
			{
				f.write(buff,0,leidos);
				f.flush();
				leidos = mensajesEntrada.read(buff);
				suma += leidos;
			}
			System.out.println();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reproducirCancion() 
	//Reproduce la cancion que tenemos disponible en el momento
	{
	
		this.reproductor.abrirCancion(new File("cancion.mp3"));
		this.reproductor.play();
	}
	
	public void pausarCancion() 
	//Pausa la cancion que esta sonando 
	{	
		this.reproductor.pause();
	}
	public void stopCancion()
	{
		this.reproductor.finalizarCancion();
	}
	
	public void reanudarCancion() 
	//Continua reproduciendo una canción previamente pausada
	{
		this.reproductor.continuar();
	}
	
	public void regularVolumen(double n) 
	//Asigna a la cancion que se esta reproduciendo el volumen n
	{
		this.reproductor.regularVolumen(n);
	}
	
	public double getVolumen() 
	//Obtiene el valor del volumen de la canción que se está reproduciendo
	{
		return this.reproductor.getVolumen();
	}
}
