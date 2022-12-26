import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class PanelTodo extends JPanel implements Comandos {
	
	//Paneles 
	PanelMenu panelMenu = new PanelMenu();
	PanelJuego panelJuego = new PanelJuego();
	PanelScore panelScore = new PanelScore();

	// Botones y label para el Menu ------------------------------------
	BotonesGeneral BotonPlay, BotonScore, BotonExit;
	JLabel title;

	// ArrayList y label para el Juego ------------------------------------
	ArrayList<JLabel> labels = new ArrayList<JLabel>();//Almacena las casillas
	//Posiciones para las casillas
	int[][] posXYLabel = {{5,5},
					 {105,5},
					 {205,5},
					 {305,5},
					 {305,105},
					 {305,205},
					 {305,305},
					 {205,305},
					 {105,305},
					 {105,405},
					 {105,505}};
	JLabel playerTurno;
	JLabel pregunta;
	JTextField campo;
	BotonesGeneral BotonOK, BotonMenu, BotonDado;
	JLabel respuesta;
	
	//Representaciones de los jugadores
	/*String player1 = "player1";
	String player2 = "player2";*/
	String player1 = "(^ー^)";
	String player2 = "(○’ω’○)";
	
	//ArrayList que almacenaran las preguntas y respuesta
	ArrayList<String> preg = new ArrayList<String>();
	ArrayList<String> answers  = new ArrayList<String>();
	
	//Posicion actual del jugador en el tablero
	int posicionPlayer1 = 0;
	int posicionPlayer2 = 0;
	
	//Posicion en la que estuvo el jugador
	int posicionAnterior1 = 0;
	int posicionAnterior2 = 0;
	
	//Lo usamos para controlar el turno de cada jugador
	boolean turno = true;
	 
	//Controlamos los puntajes
	int score1 = 0, score2 = 0;
	
	//Etiquetas con los puntajes que se mostrara en la pantalla
	JLabel p1Score, p2Score;

	// Label, Button y ArrayList para los scores  ------------------------------------
	JLabel filaTitle, tabla;
	BotonesGeneral BotonAtras;
	
	//ArrayList para almacenar los datos que extraeremos del achivo Scores.txt que contiene
	//los puntajes de los ganadores del juego
	ArrayList<String> puntos;
	
	//Variable para hacer uso del archivo Scores.txt
		PrintWriter archivoScore;

	public PanelTodo() {
		setSize(ANCHO_VENTANA, ALTO_VENTANA);
		setLayout(null);
		
		initMenu();
		initJuego();
		initScore();
	}

	private void initMenu() {
		// Titulo del Juego
		title = new JLabel("Java quiz game");
		title.setForeground(Color.white);
		title.setFont(new Font("CHOCO DONUT", 0, 80));
		title.setBounds(X_TITLE, 20, ANCHO_TITLE, ALTO_TITLE);

		// Botones de menu : play, scores y exit
		BotonPlay = new BotonesGeneral("Play");
		BotonPlay.setBounds(X_BUTTON, Y_BUTTON, ANCHO_BUTTON, ALTO_BUTTON);

		BotonScore = new BotonesGeneral("Scores");
		BotonScore.setBounds(X_BUTTON, Y_BUTTON * 2, ANCHO_BUTTON, ALTO_BUTTON);

		BotonExit = new BotonesGeneral("Exit");
		BotonExit.setBounds(X_BUTTON, Y_BUTTON * 3, ANCHO_BUTTON, ALTO_BUTTON);

		// Añadimos eventos de escucha
		BotonPlay.addActionListener(evento);
		BotonScore.addActionListener(evento);
		BotonExit.addActionListener(evento);

		// Elementos añadidos a JPanel de menu
		panelMenu.add(title);
		panelMenu.add(BotonPlay);
		panelMenu.add(BotonScore);
		panelMenu.add(BotonExit);

		// Añadimos el JPanel del menu al panel que contendra todo
		add(panelMenu);

	}

	private void initJuego() {
		
		//Cargamos las preguntas y respuestas en los ArrayList preg y answers respectivamente
		cargarPreguntas();
		cargarRespuestas();
		
		//Creamos y posicionamos las etiquetas para formar un "camino" del juego y los añadimos al
		//ArrayList de labels
		for (int i = 0; i < posXYLabel.length; i++) {
			JLabel la = new JLabel();
			la.setForeground(Color.white);
			la.setBackground(new Color(0, 49, 83));
			la.setOpaque(true);
			la.setBorder(new LineBorder(Color.WHITE, 1));
			la.setBounds(posXYLabel[i][0], posXYLabel[i][1], ANCHO_LABEL, ALTO_LABEL);
			labels.add(la);
		}
		//Añadadimos las etiquetas a JPanel de juego
		for (JLabel i : labels) 
			panelJuego.add(i);
		
		//Creamos etiquetas para controlar los puntajes de cada jugador
		p1Score = new JLabel("Player 1 : 0");
		p1Score.setForeground(Color.white);
		p1Score.setFont(new Font("CHOCO DONUT", 0, 50));
		p1Score.setBounds(5, 130, 260, 55);
		
		p2Score = new JLabel("Player 2 : 0");
		p2Score.setForeground(Color.white);
		p2Score.setFont(new Font("CHOCO DONUT", 0, 50));
		p2Score.setBounds(5, 220, 260, 55);
		
		//Etiqueta que ayuda a saber de que jugador es el turno
		playerTurno = new JLabel("Player 1");
		playerTurno.setForeground(Color.white);
		playerTurno.setFont(new Font("CHOCO DONUT", 0, 50));
		playerTurno.setBounds(425, 5, 390, 53);
		
		//Las preguntas almacenadas en el ArrayList preg se mostraran en esta etiqueta
		pregunta = new JLabel();
		pregunta.setForeground(Color.white);
		pregunta.setBounds(425, 55, 450, 400);
		
		//Campo de texto para escribir las respuestas a las preguntas
		campo = new JTextField();
		campo.setFont(new Font("Arial", 0, 20));
		campo.setBounds(425, 475, 150, 40);
		
		//Boton que nos ayudara a confirmar la respueta escrita por el jugador
		BotonOK = new BotonesGeneral("OK");
		BotonOK.setEnabled(false);
		BotonOK.setBounds(600, 475, 80, 40);
		
		//Esta etiqueta nos señalara si la respuesta es correcta o incorrecta
		respuesta = new JLabel();
		respuesta.setForeground(Color.white);
		respuesta.setFont(new Font("CHOCO DONUT", 0, 70));
		respuesta.setBounds(425, 525, 390, 70);
			
		//Sirve para mover a los jugadores por las casillas (labels)
		BotonDado = new BotonesGeneral("DADO");
		BotonDado.setBounds(230, 455, 150, 100);
		
		//Vuelve al menu principal ocultando el panel de juego
		BotonMenu = new BotonesGeneral("MENU");
		BotonMenu.setBounds(700, 475, 120, 40);
		
		//Añadimos eventos
		BotonOK.addActionListener(evento);
		BotonMenu.addActionListener(evento);
		BotonDado.addActionListener(evento);	
		
		
		// Elementos añadidos a JPanel de juego
		panelJuego.add(p1Score);
		panelJuego.add(p2Score);
		panelJuego.add(playerTurno);
		panelJuego.add(pregunta);
		panelJuego.add(campo);
		panelJuego.add(BotonOK);
		panelJuego.add(BotonMenu);
		panelJuego.add(respuesta);
		panelJuego.add(BotonDado);

		add(panelJuego);

	}
	private void initScore() {
		
		//Leaderboard
		//Etiqueta con los campos: Rank - Name - Score  de la tabla de puntuaciones
		filaTitle = new JLabel("<html>Rank"+esp(7)+"Name"+esp(7)+"Score</html>");
		filaTitle.setForeground(Color.WHITE);
		filaTitle.setFont(new Font("CHOCO DONUT", 0, 50));
		filaTitle.setBounds(X_FILA, 50, ANCHO_FILA, ALTO_FILA);
		
		//En esta etiqueta solo se encontraran los datos de los primeros 9 mejores puntajes
		tabla = new JLabel();
		tabla.setForeground(Color.WHITE);
		tabla.setFont(new Font("CHOCO DONUT", 0, 40));
		tabla.setBounds(X_FILA+30, 120, ANCHO_FILA-30, 380);
				
		//Nos devuelve al menu prinipal ocultando el panel de score
		BotonAtras = new BotonesGeneral("Atras");
		BotonAtras.setBounds(10, 10, 110, 50);
		
		//Añadimos un evento
		BotonAtras.addActionListener(evento);
		
		// Elementos añadadidos a JPanel de score
		panelScore.add(BotonAtras);
		panelScore.add(filaTitle);
		panelScore.add(tabla);

		add(panelScore);
	}
	
	//Hacemos uso de la interface ActionListener para controlar los eventos
	ActionListener evento = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == BotonPlay) {//Oculta el menu principal y hace visible el panel de juego
				panelMenu.setVisible(false);
				panelJuego.setVisible(true);

			} else if (e.getSource() == BotonScore) {//Oculta el menu principal y hace visible el panel de score
				// Abrimos el archivo Scores.txt y añadimos cada linea al ArrayList puntos
				try {
					Scanner archivoScore = new Scanner(new FileReader("./Scores.txt"));
					puntos = new ArrayList<String>();
					while (archivoScore.hasNextLine())
						puntos.add(archivoScore.nextLine());
					
					archivoScore.close();

					// Ordenamos los puntos de forma descendente
					ordenarPuntos(puntos);
					
				} catch (Exception error) {
					System.out.println(error);
				}
				
				
				// El formato de los puntos es Ejemplo: 140p1 .
				// (140) La parte numerica es el puntaje que obtuvo el jugador .
				// (p1 o p2) Que jugador obtuvo ese puntaje

				// Primero analizaremos a que jugador pertenece el puntaje y luego obtendremos su puntaje.
				// Ambos datos seran obtenidos usando en metodo substring()
				String acumulado = "";
				for (int i = 0; i < puntos.size() && i < 9; i++) {
					if (puntos.get(i).substring(puntos.get(i).length() - 2, puntos.get(i).length()).compareTo("p1") == 0) {
						acumulado = acumulado + (i + 1) + esp(12) + "Player 1" + esp(10) + puntos.get(i).substring(0, puntos.get(i).length() - 2) + "<br>";
					} 
					else
						acumulado = acumulado + (i + 1) + esp(12) + "Player 2" + esp(10) + puntos.get(i).substring(0, puntos.get(i).length() - 2) + "<br>";
				}
				// Todos los datos seran añadidos a la tabla de puntuaciones
				tabla.setText("<html>" + acumulado + "<html>");
				
				panelMenu.setVisible(false);
				panelScore.setVisible(true);
				
			} else if (e.getSource() == BotonAtras) {//Oculta el panel score y retorna al menu principal
				panelScore.setVisible(false);
				panelMenu.setVisible(true);
				
			} else if (e.getSource() == BotonExit){//Cierra el programa
				System.exit(0);
				
			}else if(e.getSource() == BotonDado){
				int n = (int) (Math.random()*4+1);//Obtenemos de forma aleatoria el numero de casilla a avanzar
				
				//Limpiamos el campo de texto y el mensaje de correcto o incorrecto
				respuesta.setText("");
				campo.setText("");
				
				//Empezamos con el jugador 1 de acuerdo a la variable turno 
				///////////////////////////////// Player1 /////////////////////////////////
				if (turno) {
										
					if(posicionPlayer1 +n <= 11) {//Comprobamos que el jugador no este fuera de los limites de las casillas
						
						//Actualizamos la posicion actual del jugador
						posicionPlayer1 = posicionPlayer1+ n;
						
						//Borrar player1 la casilla anterior
						posicionAnterior1 = posicionPlayer1-n;
						if (posicionAnterior1 != 0) {
							if(labels.get(posicionAnterior1-1).getText().compareTo(player1+"_"+player2)==0) {
								labels.get(posicionAnterior1-1).setText(player2);
							}
							else
								labels.get(posicionAnterior1-1).setText("");
						}
						
						//Mover a player1 a una casilla posterior 
						if (labels.get(posicionPlayer1-1).getText().compareTo(player2)==0) {
							labels.get(posicionPlayer1-1).setText(player1+"_"+player2);	
						}
						else
							labels.get(posicionPlayer1-1).setText(player1);	
						
						//Combrobar el ganador. Nos mostrara un mensaje con el nombre del ganador y deshabilira los botones ok y dado
						if(posicionPlayer1 == 11) {
							JOptionPane.showMessageDialog(null, "Player 1 Win", "Win", 0);
							desactivarOk();
							desactivarDado();
							
							//Capturamos el puntaje del ganador y imprimimos en el archivo Scores.txt
							try {
								archivoScore = new PrintWriter(new FileWriter("./Scores.txt", true));
								archivoScore.println(String.valueOf(score1)+"p1");	//Formato: (puntaje obtenido)(abreviatura del ganador)
								archivoScore.close();
							} catch (IOException e1) {
								System.out.println("Archivo no encontrado");
							}
							
							return;
						}
						
						//Mostramos la pregunta en el juego y desactivamos el dado para evitar que lo lanze de nuevo
						//habilitamos el boton ok para comprobar su respuesta
						pregunta.setText("<html><font face=\"Courier New\" size=\"5\">"+preg.get(posicionPlayer1-1)+"</font></html>");
						activarOk();
						desactivarDado();
					}		
				}
				
				///////////////////////////////// Player 2 /////////////////////////////////
				else if(posicionPlayer2 + n <= 11){
					
					//Actualizamos la posicion actual del jugador
					posicionPlayer2 = posicionPlayer2+ n;
					
					//Borrar player2 la casilla anterior
					posicionAnterior2 = posicionPlayer2-n;
					if (posicionAnterior2 != 0) {
						if(labels.get(posicionAnterior2-1).getText().compareTo(player1+"_"+player2)==0) {
							labels.get(posicionAnterior2-1).setText(player1);
						}
						else
							labels.get(posicionAnterior2-1).setText("");
					}
					
					
					//Mover a player2 a una casilla posterior
					if (labels.get(posicionPlayer2-1).getText().compareTo(player1)==0) {
						labels.get(posicionPlayer2-1).setText(player1+"_"+player2);	
					}
					else
						labels.get(posicionPlayer2-1).setText(player2);	
					
					
					//Combrobar el ganador
					if(posicionPlayer2 == 11) {
						JOptionPane.showMessageDialog(null, "Player 2 Win", "Win", 0);
						BotonOK.setEnabled(false);
						
						//Capturamos el puntaje del ganador
						try {
							archivoScore = new PrintWriter(new FileWriter("./Scores.txt", true));
							archivoScore.println(String.valueOf(score2)+"p2");
							archivoScore.close();
						} catch (IOException e1) {
							System.out.println("Archivo no encontrado");
						}
						return;
					}
					
					pregunta.setText("<html><font face=\"Courier New\" size=\"5\">"+preg.get(posicionPlayer2-1)+"</font></html>");
					activarOk();
					desactivarDado();

				}
				
				
			}else if(e.getSource() == BotonOK) {//Boton para comprobar las respuestas
				
				if(turno) {
					//Comparamos la respuesta del jugador con las que tenemos almacenadas en el ArrayList answers
					if(campo.getText().compareToIgnoreCase(answers.get(posicionPlayer1-1))==0) {
						
						//Borramos la pregunta y su respuesta para que ya no se vuelva a repetir
						preg.remove(posicionPlayer1-1);
						answers.remove(posicionPlayer1-1);
						
						//Mostramos un  mensaje señalando que la respuesta es correcta
						respuesta.setText("Correct!!");
						
						//Actualizamos la etiqueta de puntajes
						score1+=100;
						p1Score.setText("Player 1 : "+String.valueOf(score1));
					}
					else {//Si la respuesta no coincide mostraremos el mensaje de incorrecto
						respuesta.setText("Incorrect :(");
					}
					
					//Cambiaremos la etiqueta playerTurno para indicar que es turno del otro jugador
					playerTurno.setText("Player 2");
						
				}
				else{
					if(campo.getText().compareToIgnoreCase(answers.get(posicionPlayer2-1))==0) {
						preg.remove(posicionPlayer2-1);
						answers.remove(posicionPlayer2-1);
						
						respuesta.setText("Correct!!");
						
						score2+=100;
						p2Score.setText("Player 2 : "+String.valueOf(score2));
					}
					else {
						respuesta.setText("Incorrect :(");
					}
					
					//Cambiaremos la etiqueta playerTurno para indicar que es turno del otro jugador
					playerTurno.setText("Player 1");
				}
				
				//Cambiamos de turno
				cambiarTurno();
				desactivarOk();
				activarDado();
				
			}else {//Regresa al menu
				panelJuego.setVisible(false);
				panelMenu.setVisible(true);

			}
				
		}

	};
	
	//Usaremos este metodo para cambiar de turno despues de presionar el boton ok
	public void cambiarTurno() {
		 turno= !turno;
	}
	//Escribimos todas las preguntas que apareceran en el juego y las añadimos al ArrayList preg
	public void cargarPreguntas() {
		
		preg.add("El concepto de tener múltiples métodos con el "
			+ "mismo nombre,  pero diferentes parámetros"
			+ "se llama:<br>"
			+ "a) overriding<br>"
			+ "b) overloading");
		preg.add("Cual es la salida de este codigo?<br>"
				+ "public class Prog{<br>"
				+ " public static void main (String[] args){<br>"
				+ "  String a = \"10\";<br>"
				+ "  String b = \"20\";<br>"
				+ "  a.concat(b);<br>"
				+ "  System.out.print(a);<br>"
				+ " }<br>"
				+ "}<br>"
				+ "a) 1020<br>"
				+ "b) 2010<br>"
				+ "c) 10<br>"
				+ "d) 20");
		
		preg.add("Cual es la salida de este codigo?<br>"
				+"System.out.println(\"\\\\a\\\\b\");");

		preg.add("Cual es la salida de este codigo?<br>Import.java.util.HashMap;<br>"
				+"public class MyClass {<br>public static void main(String[] args) {<br>"
				+ " HashMap<String, Integer> points = new HashMap<String, Integer>();<br>"
				+ " points.put(\"Amy\", 154);<br>"
				+ " points.put(\"Dave\", 42);<br>"
				+ " points.put(\"Rob\", 733);<br>"
				+ " points.put(\"Rob\", 403);<br>"
				+ " System.out.print(points.get(\"ROB\");<br>"
				+ "a) null<br>"
				+ "b) error<br>"
				+ "c) 403<br>"
				+ "d) 733");

		preg.add("Cual es la salida de este codigo?<br>"
				+ "int a=2;<br>"
				+ "int x=0;<br>"
				+ "switch(a) {<br>"
				+ "case 1: ++x;<br>"
				+ "case 2: ++x;<br>"
				+ "case 3: ++x;<br>"
				+ "default: ++x;<br>"
				+ "}<br>"
				+ "System.out.print(x);");
		
		preg.add("Si se crea una referencia a un objeto pero "
				+ "no se inicializa...<br>"
				+ "a) Provocará un error en el acceso<br>"
				+ "b) Se inicializará como null<br>"
				+ "c) Se inicializará utilizando el constructor "
				+ "del objeto.");
		
		preg.add("Es posible sobrescribir los métodos "
				+ "estáticos?<br>"
				+ "a) True<br>"
				+ "b) False");
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "int x = 4;<br>"
				+ "int y = 0;<br>"
				+ "switch(--x) {<br>"
				+ "case 3:<br>"
				+ "y+=x;<br>"
				+ "case 4:<br>"
				+ "y+=x;<br>"
				+ "default:<br>"
				+ "y++;<br>"
				+ "break;<br>"
				+ "}<br>"
				+ "System.out.print(y);");
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "try {<br>"
				+ "int a=5;<br>"
				+ "int b=0;<br>"
				+ "int c[]={a, b};<br>"
				+ "c[a]=a/b;<br>"
				+ "System.out.print(\"C\");<br>"
				+ "}<br>"
				+ "catch(IndexOutOfBoundsException e){<br>"
				+ "System.out.print(\"A\");<br>"
				+ "}<br>"
				+ "catch(ArithmeticException e){<br>"
				+ "System.out.print(\"B\");<br>"
				+ "}<br>"
				+ "a) B<br>"
				+ "b) C<br>"
				+ "c) A");
		
		preg.add("El siguiente codigo es valido?<br>"
				+ "public class MyClass {<br>"
				+ "int i=7;<br>"
				+ "public static void main(String [] args) {<br>"
				+ "i++;<br>"
				+ "}<br>"
				+ "}<br>"
				+ "a) False<br>"
				+ "b) True");
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "int[] arr = new int[] {0, 1, 2, 3};<br>"
				+ "System.out.print(arr[1]++);");
		
		preg.add("Cual es el valor mas alto que podria imprimirse?<br>"
				+ "Random rand = new Random();<br>"
				+ "int x = rand.nextlnt(5)%10;<br>"
				+ "System.out.print(x);<br>"
				+ "a) 4<br>"
				+ "b) 9<br>"
				+ "c) 10<br>"
				+ "d) 5");
		
		preg.add("Es posible terminar la iteración actual de un bucle e iniciar la siguiente iteración:<br>"
				+ "a) utilizando continue.<br>"
				+ "b) de ninguna manera.<br>"
				+ "c) utilizando break.");
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "int x = 007;<br>"
				+ "int y = 008;<br>"
				+ "System.out.println(++x + y--);<br>"
				+ "a) 0015<br>"
				+ "b) Error<br>"
				+ "c) 014<br>"
				+ "d) 14");
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "int x = 100;<br>"
				+ "int y = 200;<br>"
				+ "int z;<br>"
				+ "while(++x<--y) {z=x*y;}<br>"
				+ "System.out.print(x);");//150
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "String strl = \"party\";<br>"
				+ "String str2 = \"PARTY\".toLowerCase();<br>"
				+ "if(str1 == str2)<br>"
				+ "System.out.print(\"Party!\");<br>"
				+ "else<br>"
				+ "System.out.print(\"No Party!\");<br>"
				+ "a) El código no compila<br>"
				+ "b) Party!<br>"
				+ "c) Se lanza una excepción<br>"
				+ "d) No Party!");
		
		preg.add("Cual es el valor de retorno de la expresion siguiente?<br>"
				+ "Math.round(5.4);");//5.0
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "int x = 1;<br>"
				+ "int y = 2;<br>"
				+ "y += --x+y-x<br>"
				+ "System.out.print(y);");
		
		preg.add("Cual es el tipo de retorno del método equals() de la clase String?<br>"
				+ "a) boolean<br>"
				+ "b) double<br>"
				+ "c) void<br>"
				+ "d) int");
		
		preg.add("Cual es la salida de este codigo?<br>"
				+ "String b = new String(\"java\");<br>"
				+ "String a = new String(\"java\");<br>"
				+ "a = b;<br>"
				+ "System.out.print(a == b);");
		
		preg.add("Los metodos de una interfaz que no declarados como default o static son implicitamente:<br>"
				+ "a) default<br>"
				+ "b) static<br>"
				+ "c) abstract<br>"
				+ "d) final");
	}
	
	//Escribimos todas las respuestas en el mismo orden que las preguntas y las añadimos al ArrayList answers
	public void cargarRespuestas() {
		answers.add("b");
		answers.add("c");
		answers.add("\\a\\b");
		answers.add("a");
		answers.add("3");
		answers.add("b");
		answers.add("b");
		answers.add("7");
		answers.add("a");
		answers.add("a");
		answers.add("1");
		answers.add("a");
		answers.add("a");
		answers.add("b");
		answers.add("150");
		answers.add("d");
		answers.add("5.0");
		answers.add("4");
		answers.add("a");
		answers.add("true");
		answers.add("c");
	}
	
	//Metodo para ordenar el ArrayList puntos de forma descendente usando el metodo substring()  
	public void ordenarPuntos(ArrayList<String> array) {
		String aux;
		for (int i = 0; i < array.size(); i++) {
			for (int j = 0; j < array.size()-1; j++) {
				if(Integer.parseInt(array.get(j).substring(0, array.get(j).length()-2))<
						Integer.parseInt(array.get(j+1).substring(0, array.get(j+1).length()-2))) {
					aux = array.remove(j);
					array.add(j+1, aux);
				}
			}
		}
	}
	
	//Metodo que devuelve n espacios vacios
	public String esp(int n) {
		String espacios = "";
		for (int i = 0; i < n; i++) {
			espacios+="&nbsp;";
		}
		return espacios;
	}
	
	//Metodos para habilitar y deshabilitar el boton ok y boton dado
	public void activarOk() {
		BotonOK.setEnabled(true);
	}
	public void desactivarOk() {
		BotonOK.setEnabled(false);
	}
	public void activarDado() {
		BotonDado.setEnabled(true);
	}
	public void desactivarDado() {
		BotonDado.setEnabled(false);
	}
}
