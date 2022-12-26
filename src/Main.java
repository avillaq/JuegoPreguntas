import javax.swing.JFrame;

public class Main extends JFrame implements Comandos{
	public Main () {
		setTitle("Java Quiz Game");
		setSize(ANCHO_VENTANA, ALTO_VENTANA);
		setLocationRelativeTo(null);
	    setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	    

		add(new PanelTodo());
		
		//Cuidado con el volumen
		//Para quitar el sonido, solo borre esta linea
		Sound.reproducir();
		
		setVisible(true);
	}


	public static void main(String[] args) {
		new Main();
	}

}
