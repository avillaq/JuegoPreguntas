import java.awt.*;
import javax.swing.*;

public class PanelMenu extends JPanel implements Comandos{
	private Image icon;

	public PanelMenu() {
		setSize(ANCHO_VENTANA, ALTO_VENTANA);
		setLayout(null);
	}
	
	//Fuente: https://www.youtube.com/watch?v=CfKlAHInank
	@Override
	public void paint(Graphics g) {
		icon = new ImageIcon(getClass().getResource("./Images/IconMenuDark.jpg")).getImage();
		g.drawImage(icon, 0, 0, getWidth(), getHeight(), this);
		setOpaque(false);
		super.paint(g);
	}
}