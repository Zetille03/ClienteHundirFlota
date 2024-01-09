package cliente;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import info.Barco;
import info.Boton;


public class TableroReplay extends JPanel {
	Boton[] boton = new Boton[105];
	JLabel[] letra = new JLabel[11];
	JLabel[] numero = new JLabel[10];
	JPanel botones = new JPanel();
	JPanel titulo = new JPanel();
	ObjectOutputStream salida;
	boolean jugador;
	ArrayList<Barco> arrayBarcos;
	JButton rotar;
	ReplayFrame partida;
	JLabel titulo_visible;
	TableroReplay yo = this;

	public TableroReplay(boolean jugador, String tituloTablero, ObjectOutputStream salida, ArrayList<Barco> posicionBarcos, ReplayFrame partida) {
		this.jugador = jugador;
		this.salida = salida;
		this.arrayBarcos = posicionBarcos;
		this.partida = partida;
		BoxLayout layoutTablero = new BoxLayout(this,BoxLayout.Y_AXIS);
		setLayout(layoutTablero);
		
		GridLayout layoutBotones = new GridLayout(12, 11);
		botones.setLayout(layoutBotones);

		setBorder(new EmptyBorder(15, 5, 0, 25));

		crearTablero(tituloTablero);
		
		rellenarPosicionesBarco(this.arrayBarcos);
	}

	public void crearTablero(String tituloTablero) {
		
		titulo.add(new JLabel("Tablero de "+tituloTablero));
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

			boton[x] = new Boton("");
			boton[x].setPosicionTablero(x);
			boton[x].setAgua(true);
			
			boton[x].setSize(25, 25);

			botones.add(boton[x]);
		}

		JPanel panelrotar = new JPanel();
		botones.add(panelrotar);
		panelrotar.setLayout(new FlowLayout());
		rotar = new JButton("Rotar");
		rotar.setEnabled(false);
		rotar.setBackground(Color.cyan);

		panelrotar.add(rotar);
		
		add(botones);
	}
	
	public void rellenarPosicionesBarco(ArrayList<Barco> arrayBarcos) {
		if(arrayBarcos==null) {
			System.out.println("esta vacio");
		}else {
			for(Barco barco: arrayBarcos) {
				for(Boton b : barco.getBotonesBarco()) {
					this.boton[b.getPosicionTablero()].setAgua(false);
				}
			}
		}
		
	}
	
	public boolean ganador(ArrayList<Barco> arrayBarcos) {
		for(Barco barco: arrayBarcos) {
			if(barco.isHundido()==false) {
				return false;
			}
		}
		return true;
	}
}
