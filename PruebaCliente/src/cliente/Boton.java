package cliente;

import java.awt.Color;

import javax.swing.JButton;

public class Boton extends JButton {
	
	public static Color color = Color.green;
	
	public boolean activo = false;
	public boolean tocado = false;
	public boolean agua = false;
	public boolean hundido = false;
	public int id;
	
	Boton(String texto,int id){
		this.setText(texto);
		this.id = id;
	}
	
	public void setColorDefault()
	{
		this.setBackground(new JButton().getBackground());
		this.setBorder(new JButton().getBorder());
	}
	
	public void setColorTocado()
	{
		this.setBackground(Color.YELLOW);
	}
	
	public void setColorAgua()
	{
		this.setBackground(Color.cyan);	
	}
	
	public void setColorHundido()
	{
		this.setBackground(Color.red);	
	}
}
