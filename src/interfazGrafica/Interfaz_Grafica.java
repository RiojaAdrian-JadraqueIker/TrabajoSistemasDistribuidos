package interfazGrafica;



import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


import cliente.Cliente;

import javax.swing.JButton;
import javax.swing.JFileChooser;


import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;


import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;

public class Interfaz_Grafica extends JFrame {

	private JPanel contentPane;
	private Cliente cliente;
	private JTextField tbNombreCancionActual;




	public Interfaz_Grafica(Cliente c)
	{
		setResizable(false);


		File f = new File("cancion.mp3");
		if(f.exists())
		{
			f.delete();
		}

		setTitle("Reproductor musica");
		this.cliente=c;

		boolean b = this.cliente.conectar();
		if(!b)//caso servidor no este activo
		{
			this.dispose();
			JOptionPane.showInternalMessageDialog(null, "ERROR DE CONEXION CON EL SERVIDOR");
		}
		else
		{
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 762, 382);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			JButton btPLay = new JButton("Play");
			btPLay.setToolTipText("Hace sonar la cancion actual");
			btPLay.setBounds(307, 303, 89, 23);
			contentPane.add(btPLay);


			JButton btPause = new JButton("Pausa");
			btPause.setToolTipText("Hace parar la cancion que esta sonando");
			btPause.setBounds(406, 303, 89, 23);
			contentPane.add(btPause);

			tbNombreCancionActual = new JTextField();
			tbNombreCancionActual.setEditable(false);
			tbNombreCancionActual.setHorizontalAlignment(SwingConstants.CENTER);
			tbNombreCancionActual.setToolTipText("Nombre de la cancion actual");
			tbNombreCancionActual.setBounds(279, 272, 308, 20);
			contentPane.add(tbNombreCancionActual);
			tbNombreCancionActual.setColumns(10);

			JPanel panelAcciones = new JPanel();
			panelAcciones.setBounds(10, 56, 228, 220);
			contentPane.add(panelAcciones);
			panelAcciones.setLayout(null);

			JLabel lbAcciones = new JLabel("ACCIONES");
			lbAcciones.setFont(new Font("Arial Black", Font.PLAIN, 16));
			lbAcciones.setBounds(64, 11, 102, 23);
			panelAcciones.add(lbAcciones);

			JButton btMostrarCanciones = new JButton("Mostrar canciones");
			btMostrarCanciones.setBounds(38, 45, 156, 23);
			panelAcciones.add(btMostrarCanciones);

			JButton btSubirCancion = new JButton("Subir cancion");
			btSubirCancion.setBounds(38, 79, 156, 23);
			panelAcciones.add(btSubirCancion);
			

			JButton btSalir = new JButton("Salir");
			btSalir.setBackground(new Color(250, 128, 114));
			btSalir.setFont(new Font("Tahoma", Font.BOLD, 14));
			btSalir.setForeground(Color.WHITE);
			btSalir.setBounds(38, 181, 156, 23);
			panelAcciones.add(btSalir);
			
			JTextField tbBuscarCancion = new JTextField();
			tbBuscarCancion.setBounds(38, 113, 156, 23);
			panelAcciones.add(tbBuscarCancion);
			
			JButton btBuscarCancion = new JButton("Buscar cancion");
			btBuscarCancion.setBounds(38, 147, 156, 23);
			panelAcciones.add(btBuscarCancion);

			JRadioButton btAleatorio = new JRadioButton("Aleatorio");
			btAleatorio.setToolTipText("Se reproduce de forma aleatoria");
			btAleatorio.setBounds(600, 303, 78, 23);
			contentPane.add(btAleatorio);

			JSlider volumen = new JSlider();
			volumen.setPaintTicks(true);
			volumen.setPaintLabels(true);
			volumen.setSnapToTicks(true);
			volumen.setOrientation(SwingConstants.VERTICAL);
			volumen.setToolTipText("Sirve para regular el volumen");
			volumen.setBounds(653, 46, 51, 195);
			contentPane.add(volumen);

			JButton btAnterior = new JButton("Anterior");
			btAnterior.setToolTipText("Se pasa a la cancion anterior");
			btAnterior.setBounds(208, 303, 89, 23);
			contentPane.add(btAnterior);

			JButton btSiguiente = new JButton("Siguiente");
			btSiguiente.setToolTipText("Pasa a la siguiente cancion");
			btSiguiente.setBounds(505, 303, 89, 23);
			contentPane.add(btSiguiente);

			java.awt.List lista = new java.awt.List();
			lista.setBounds(279, 11, 308, 230);
			contentPane.add(lista);

			JLabel lblNewLabel = new JLabel("Volumen");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblNewLabel.setBounds(638, 244, 78, 14);
			contentPane.add(lblNewLabel);




			//-----------------------------------
			//---------------EVENTOS-------------	

			btBuscarCancion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					buscarCancion(tbBuscarCancion, lista);
				}
			});
			
			btSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					cliente.desconetar();
					cliente.stopCancion();
					dispose();
				}
			});

			btPLay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					clickPlay();
				}
			});


			btPause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					clickPause();
				}
			});

			btMostrarCanciones.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					try {
						tbBuscarCancion.setText("");
						clickMostrarCanciones(lista,volumen);
					} catch (NumberFormatException e1) {

						e1.printStackTrace();
					}
				}
			});


			btSubirCancion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					File fichero = seleccionarArchivo();
					if(fichero!=null)
					{
						
						System.out.println("preparando para comprobar si existe la cancion");
						if (cliente.yaExiste(fichero)) {
							JOptionPane.showInternalMessageDialog(contentPane, "LA CANCION YA EXISTE EN EL SERVIDOR");
						}
						else 
						{
							System.out.println("preparando para subir cancion");
							cliente.subirCancion(fichero);
							System.out.println("cancion subida");

							JOptionPane.showInternalMessageDialog(contentPane, "CANCION SUBIDA EXITOSAMENTE");

						}
						
					}
					else
					{
						JOptionPane.showInternalMessageDialog(contentPane, "NO SE HA SELECCIONADO NINGUNA CANCION");
					}

				}
			});

			btSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{	
					if(btAleatorio.isSelected()) {
						//Reproducimos otra cancion aleatoria:
						siguienteCancionAleatoria(lista);
					} else {
						//Reproducimos la siguiente cancion:
						siguienteCancion(lista);
					}

					//Asignamos a la nueva cancion el volumen que tenia la anterior:
					volumen(volumen);
				}
			});

			btAnterior.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					if(btAleatorio.isSelected()) {
						//Reproducimos otra cancion aleatoria:
						siguienteCancionAleatoria(lista);
					} else {
						//Reproducimos la cancion anterior
						anteriorCancion(lista);
					}

					//Asignamos a la nueva cancion el volumen que tenia la anterior:
					volumen(volumen);
				}
			});

			volumen.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) 
				{
					volumen(volumen);
				}
			});

			//--------------//

			this.mostrarInterfaz();
		}





	}


	private void buscarCancion(JTextField textbox, java.awt.List lista)
	{
		lista.clear();
		List<String> l = this.cliente.listaCanciones(textbox.getText());
		for(String s: l)
		{
			lista.add(s);
		}
	}
	
	private void volumen(JSlider volumen) 
	{
		File f = new File("cancion.mp3");

		if(f.exists()) 
		{
			double valorActual = volumen.getValue()/100.0;
			this.cliente.regularVolumen(valorActual);
		}
	}

	private void siguienteCancion(java.awt.List lista)
	{
		if(lista.getItemCount()!=0)
		{
			if(lista.getSelectedIndex()<lista.getItemCount()-1)
			{
				lista.select(lista.getSelectedIndex()+1);
			}
			else
			{
				lista.select(0);
			}			

			//Paramos la cancion actual y reproducimos la siguiente
			String s =lista.getSelectedItem();
			cliente.pausarCancion();
			cliente.descargarCancion(s);
			mostrarNombreCancionActualSonando(s);
			cliente.reproducirCancion();

		}	
	}

	private void siguienteCancionAleatoria(java.awt.List lista)
	{
		if(lista.getItemCount()!=0)
		{
			Random r = new Random();

			int indiceAleatorio = lista.getSelectedIndex();

			while(indiceAleatorio == lista.getSelectedIndex()) {
				indiceAleatorio = r.nextInt( lista.getItemCount());
			}

			lista.select(indiceAleatorio);	

			//Paramos la cancion actual y reproducimos la siguiente
			String s =lista.getSelectedItem();
			cliente.pausarCancion();
			cliente.descargarCancion(s);
			mostrarNombreCancionActualSonando(s);
			cliente.reproducirCancion();

		}	
	}

	private void anteriorCancion(java.awt.List lista)
	{
		if(lista.getItemCount()!=0)
		{
			if(lista.getSelectedIndex()>0)
			{
				lista.select(lista.getSelectedIndex()-1);
			}
			else
			{
				lista.select(lista.getItemCount()-1);
			}
			String s =lista.getSelectedItem();
			cliente.pausarCancion();
			cliente.descargarCancion(s);
			mostrarNombreCancionActualSonando(s);
			cliente.reproducirCancion();
		}
	}

	private void mostrarInterfaz() 
	{
		this.setVisible(true);

	}
	private File seleccionarArchivo()
	{
		File fichero = null;
		JFileChooser seleccionador = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Musica mp3", "mp3");
		seleccionador.setFileFilter(filter);
		int n = seleccionador.showOpenDialog(seleccionador);
		if (n == JFileChooser.APPROVE_OPTION) 
		{
			fichero = new File(seleccionador.getSelectedFile().getAbsolutePath());
		}

		return fichero;

	}


	private void clickPlay()
	{
		File f = new File("cancion.mp3");

		if(f.exists()) 
		{
			this.cliente.reanudarCancion();
		}

	}


	private void clickPause()
	{
		this.cliente.pausarCancion();
	}


	private void clickMostrarCanciones(java.awt.List lista,JSlider volumen )
	{
		List<String> listaCanciones = this.cliente.listaCanciones();

		lista.clear(); // limpìamos la lista por si habia algo ya para asi actualizar
		for(String s: listaCanciones)
		{
			lista.add(s);
		}
		if(lista.getActionListeners().length==0)
		{
			lista.addActionListener(new ActionListener() {

				//Cuando hacemos click en una cancion esta se descarga y se reproduce 
				public void actionPerformed(ActionEvent e) 
				{
					String s =lista.getSelectedItem();
					cliente.descargarCancion(s);
					mostrarNombreCancionActualSonando(s);
					cliente.reproducirCancion();
					volumen(volumen);
				}
			});
		}


	}

	private void mostrarNombreCancionActualSonando(String t)
	{
		this.tbNombreCancionActual.setText(t);
	}
}
