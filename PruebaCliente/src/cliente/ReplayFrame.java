package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import info.Barco;
import info.Boton;

public class ReplayFrame extends JFrame {
	public int iterador=0;
	public int turno=0;
	public int arrayBarcos1;
	public int arrayBarcos2;
	public TableroReplay tableroJugador1;
	public TableroReplay tableroJugador2;
	public JButton volverMenuBoton;
	public JButton salirAplicacionBoton;
	public JButton siguienteTurno;
	public JLabel textoTurnos;
	MenuFrame menuFrame;
	ObjectOutputStream salida;
	ReplayFrame replayFrame = this;
	String[] disparos;
	
	
	JTextArea chatArea;
	JTextField mensajeField;
	
	public ReplayFrame(ObjectOutputStream salida, MenuFrame menuFrame) {
		this.salida = salida;
		this.menuFrame = menuFrame;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        mensajeField = new JTextField();

        JButton enviarButton = new JButton("Enviar");
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	String mensaje = mensajeField.getText();
					salida.writeObject("R@P@"+mensaje);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });
        

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(mensajeField, BorderLayout.CENTER);
        inputPanel.add(enviarButton, BorderLayout.EAST);
        
        panel.add(inputPanel, BorderLayout.SOUTH);
        
        add(panel);
	}
	
	public void iniciarReplay(int id_jugador1,int id_jugador2,String nombreJugador1, ArrayList<Barco> arrayBarcos1, String nombreJugador2, ArrayList<Barco> arrayBarcos2, int id_partida, String disparos1, String ganador) {
		 this.getContentPane().removeAll();
		 this.revalidate();
		 this.repaint();
		 this.disparos = disparos1.split(";");
		 
		 
		 this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));

		 menuFrame.setVisible(false);
			setTitle("ID_PARTIDA: "+id_partida);
			JPanel tablerosPanel = new JPanel();
			JPanel botonesPanel = new JPanel();
			botonesPanel.setLayout(new FlowLayout());
			GridLayout layout = new GridLayout(1,2);
			tablerosPanel.setLayout(layout);
			
			volverMenuBoton = new JButton("Volver al menu");
			salirAplicacionBoton = new JButton("Salir");
			siguienteTurno = new JButton("Siguiente turno");
			textoTurnos = new JLabel("");
			mostrarMensaje("Turno nº"+this.turno);
			
			volverMenuBoton.addActionListener(new ActionListener() {
				MenuFrame menu= menuFrame;
				
				@Override
				public void actionPerformed(ActionEvent e) {
					replayFrame.setVisible(false);
					menu.setVisible(true);
				}
			});
			
			salirAplicacionBoton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			siguienteTurno.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						turno++;
						mostrarMensaje("Turno nº"+turno);
						String[] disparo;
						if(iterador<disparos1.length()) {
							if(iterador==disparos1.length()-1) {
								disparo = disparos[iterador].split(",");
								if(Integer.valueOf(disparo[0])==id_jugador1) {
									System.out.println(arrayBarcos1.size());
									recibirDisparo(Integer.valueOf(disparo[1]),arrayBarcos2,tableroJugador2);
								}else {
									System.out.println(arrayBarcos2.size());
									recibirDisparo(Integer.valueOf(disparo[1]),arrayBarcos1,tableroJugador1);
								}
								iterador++;
							}else {
								int i = 0;
								while( i <2) {
									disparo = disparos[iterador].split(",");
									if(Integer.valueOf(disparo[0])==id_jugador1 && iterador!=disparos1.length()-1) {
										System.out.println(arrayBarcos1.size());
										recibirDisparo(Integer.valueOf(disparo[1]),arrayBarcos2,tableroJugador2);
									}else if(iterador!=disparos1.length()-1) {
										System.out.println(arrayBarcos2.size());
										recibirDisparo(Integer.valueOf(disparo[1]),arrayBarcos1,tableroJugador1);
									}
									iterador++;
									i++;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e1) {
						mostrarMensaje("Ultimo turno: ganador "+ ganador);
					}
				}
			});
			
			
			botonesPanel.add(volverMenuBoton);
			botonesPanel.add(salirAplicacionBoton);
			botonesPanel.add(siguienteTurno);
			botonesPanel.add(textoTurnos);
			
//			while(barcosJugador==null || barcosEnemigo==null) {
//				Thread.sleep(1);
//			}
			
			tableroJugador1 = new TableroReplay(false, nombreJugador1,salida,arrayBarcos1,this);
			tableroJugador2 = new TableroReplay(false, nombreJugador2,salida,arrayBarcos2,this);
			
			tablerosPanel.add(tableroJugador1);
			tablerosPanel.add(tableroJugador2);
			
			
			
			
			setBounds(100,100,1000,600);
			add(botonesPanel);
			add(tablerosPanel);
			setLocationRelativeTo(null);
			setVisible(true);
	}
	
	public void cargarDatos(String partidas) {
		String[] partidasAMostrar = partidas.split(";");
		for(String partida: partidasAMostrar) {
			String[] partidasFormateado = partida.split(",");
			
			SwingUtilities.invokeLater(() -> {
				chatArea.append(partidasFormateado[0]+".-"+" "+partidasFormateado[1]+" vs "+partidasFormateado[2]+"\n");
			});
		}
	}
	
	public void recibirDisparo(int casilla, ArrayList<Barco> array, TableroReplay tablero) {
		Boton b = tablero.boton[casilla];
		if(b.getAgua()==true) {
			b.setFallido(true);
			b.setColorFallido();
		}else {
			b.setColorTocado();
			b.setTocado(true);
			Barco barco = FramePartida.seleccionarBarco(b,array);
			boolean hundido = FramePartida.comprobacionBarcoHundido(barco);
			if(hundido==true) {
				hundirBarco(barco,tablero);
			}
			
		}
	}
	
	public static void hundirBarco(Barco barco,TableroReplay tablero) {
		barco.setHundido(true);
		for(Boton b: barco.getBotonesBarco()) {
			tablero.boton[b.getPosicionTablero()].setColorHundido();
			tablero.boton[b.getPosicionTablero()].setHundido(true);
		}
	}
	
	public void mostrarMensaje(String texto) {
		this.textoTurnos.setText(texto);
		this.textoTurnos.setForeground(Color.red);
	}
}
