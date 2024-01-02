package cliente;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Tablero extends JPanel {
	Boton[] boton = new Boton[105];
	JLabel[] letra = new JLabel[11];
	JLabel[] numero = new JLabel[10];
	JPanel botones = new JPanel();
	JPanel titulo = new JPanel();
	ObjectOutputStream salida;
	boolean jugador;
	boolean disponibilidadDisparo=true;
	
	JButton rotar;

	JLabel titulo_visible;

	Tablero(boolean jugador, String tituloTablero, ObjectOutputStream salida) {
		this.jugador = jugador;
		this.salida = salida;
		BoxLayout layoutTablero = new BoxLayout(this,BoxLayout.Y_AXIS);
		setLayout(layoutTablero);
		
		GridLayout layoutBotones = new GridLayout(12, 11);
		botones.setLayout(layoutBotones);

		setBorder(new EmptyBorder(15, 5, 0, 25));

		crearTablero(tituloTablero);
	}

	public void crearTablero(String tituloTablero) {
		titulo.add(new JLabel(tituloTablero));
		add(titulo);
		// -----CREAR LAS LETRAS

		for (int x = 0; x < 11; x++) {
			switch (x) {
			case 0:
				letra[x] = new JLabel("", SwingConstants.CENTER);
				break;
			case 1:
				letra[x] = new JLabel("A", SwingConstants.CENTER);
				break;
			case 2:
				letra[x] = new JLabel("B", SwingConstants.CENTER);
				break;
			case 3:
				letra[x] = new JLabel("C", SwingConstants.CENTER);
				break;
			case 4:
				letra[x] = new JLabel("D", SwingConstants.CENTER);
				break;
			case 5:
				letra[x] = new JLabel("E", SwingConstants.CENTER);
				break;
			case 6:
				letra[x] = new JLabel("F", SwingConstants.CENTER);
				break;
			case 7:
				letra[x] = new JLabel("G", SwingConstants.CENTER);
				break;
			case 8:
				letra[x] = new JLabel("H", SwingConstants.CENTER);
				break;
			case 9:
				letra[x] = new JLabel("I", SwingConstants.CENTER);
				break;
			case 10:
				letra[x] = new JLabel("J", SwingConstants.CENTER);
				break;
			}

			botones.add(letra[x]);
		}

		// ------CREAR LOS NUMEROS Y BOTONES
		int contador = 0;

		for (int x = 0; x < 100; x++) {

			if (x % 10 == 0 && x != 99) {
				numero[contador] = new JLabel("" + (contador + 1), SwingConstants.CENTER);
				botones.add(numero[contador]);
				contador++;
			}

			boton[x] = new Boton("",x);
			if(this.jugador==false) {
				Boton b = boton[x];
				boton[x].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							if(disponibilidadDisparo==true) {
								salida.writeObject(String.valueOf(b.id));
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
				});
			}
			
			boton[x].setSize(25, 25);

			botones.add(boton[x]);
		}

		JPanel panelrotar = new JPanel();
		botones.add(panelrotar);
		panelrotar.setLayout(new FlowLayout());
		rotar = new JButton("Rotar");
		if(this.jugador==false) {
			rotar.setEnabled(false);
		}
		rotar.setBackground(Color.cyan);

		panelrotar.add(rotar);
		
		add(botones);
	}
}
