package reproductor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.State;
import java.util.LinkedList;
import java.util.List;


import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;



public class Reproductor
{
	//Atributos
	private BasicPlayer reproductor;
	
	
	//Metodos
	
	public Reproductor() 
	{
		this.reproductor = new BasicPlayer();
	}
	
	public void abrirCancion(File f) throws FileNotFoundException, BasicPlayerException 
	{
		this.reproductor.open(f);
	}
	public void abrirCancion(InputStream in) throws FileNotFoundException, BasicPlayerException 
	{
		this.reproductor.open(in);
	}
	
	public void play() throws BasicPlayerException //o hacer try catch
	{
		this.reproductor.play(); 
		
		/*this.reproductor.play();
		Thread tarray[] = new Thread[Thread.activeCount()-1];
		int anterior = Thread.activeCount();
		Thread.enumerate(tarray);
		
		Thread hilo = tarray[3];
		String s= hilo.getState().toString();
		while(!s.equals("TERMINATED"))//ESPERA A QUE TERMINE
		{
			s =tarray[3].getState().toString();
		}
		
		System.out.println("---------------");
		return true;//devuelve true cuando termina;*/
		
	}
	public void pause() throws BasicPlayerException //o hacer try catch
	{
		this.reproductor.pause();
	}
	public void continuar() throws BasicPlayerException //o hacer try catch
	{
		this.reproductor.resume();
	}
	
	public void regularVolumen(double n) throws BasicPlayerException
	{
		this.reproductor.setGain(n);
	}
	
	

	
	
	

}
