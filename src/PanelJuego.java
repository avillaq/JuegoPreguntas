import java.awt.*;
import javax.swing.*;

public class PanelJuego extends JPanel implements Comandos{
	private Image icon;

	public PanelJuego() {
		setSize(ANCHO_VENTANA, ALTO_VENTANA);
		setLayout(null);
		setVisible(false);
	}
	
	//Fuente: https://www.youtube.com/watch?v=CfKlAHInank
	@Override
	public void paint(Graphics g) {
		icon = new ImageIcon(getClass().getResource("./Images/IconJuego.jpg")).getImage();
		g.drawImage(icon, 0, 0, getWidth(), getHeight(), this);
		setOpaque(false);
		super.paint(g);
	}	
}