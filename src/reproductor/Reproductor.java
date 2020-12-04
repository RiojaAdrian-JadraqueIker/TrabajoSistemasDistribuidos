package reproductor;

import java.io.File;
import java.io.InputStream;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;



public class Reproductor
{
	//ATRIBUTOS
	private BasicPlayer reproductor;
	
	
	//METODOS:
	
	
	public Reproductor() 
	{
		this.reproductor = new BasicPlayer();
	}
	
	public void abrirCancion(File f) 
	{
		try {
			this.reproductor.open(f);
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	} 
	public void abrirCancion(InputStream in)  
	{
		try {
			this.reproductor.open(in);
		} catch (BasicPlayerException e2) {
			e2.printStackTrace();
		}
	}
	
	public void play()
	{
		try {
			this.reproductor.play();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		} 
		
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
	
	public void pause() 
	{
		try 
		{
			this.reproductor.pause();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void continuar()  
	{
		try 
		{
			this.reproductor.resume();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void regularVolumen(double n) 
	{
		try 
		{
			this.reproductor.setGain(n);
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
	}
	public double getVolumen()
	{
		return this.reproductor.getGainValue();
	}
	
	

	
	
	

}
