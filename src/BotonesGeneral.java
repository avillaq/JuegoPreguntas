import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

public class BotonesGeneral extends JButton{
	public BotonesGeneral(String a) {
		super(a);
		//Fondo del boton
		setBackground(new Color(69,86,102));
		//Color de letra
		setForeground(Color.LIGHT_GRAY);
		setFont(new Font("CHOCO DONUT", 0, 40));
	}	
}