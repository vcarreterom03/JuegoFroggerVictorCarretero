package pantallas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import base.PanelJuego;
import base.Pantalla;
import base.Sprite;

public class PantallaJuegoFrogger implements Pantalla {

	private static final int ANCHO_COCHE = 80;
	private static final int ALTO_COCHE = 60;

	private static final Color COLOR_PUNTUACION = Color.WHITE;

	private int contadorVida = 3;

	Clip sonido;
	
	PanelJuego panelJuego;
	
	// Creacion de los diferentes arrays que contendran los sprites
	ArrayList<Sprite> cochesBlancosPrimero;
	ArrayList<Sprite> cochesRojosPrimero;
	ArrayList<Sprite> cochesBlancosSegundo;
	ArrayList<Sprite> cochesRojosSegundo;

	ArrayList<Sprite> troncosIzquierdaPrimero;
	ArrayList<Sprite> troncosDerecha;
	ArrayList<Sprite> troncosIzquierdaSegundo;

	BufferedImage imagenOriginal;
	Image imagenReescalada;

	Sprite personaje;

	Sprite marcador1;
	Sprite marcador2;
	Sprite marcador3;
	Sprite marcador4;

	boolean hayMarcador1;
	boolean hayMarcador2;
	boolean hayMarcador3;
	boolean hayMarcador4;
	
	int contadorMarcador;

	// Variables para el contador de tiempo
	double tiempoInicial;
	double tiempoDeJuego;
	private DecimalFormat formatoDecimal; // Formatea la salida.
	Font fuenteTiempo;

	public PantallaJuegoFrogger(PanelJuego panelJuego) {
		this.panelJuego = panelJuego;
		tiempoInicial = System.nanoTime();
		try {
			imagenOriginal = ImageIO.read(new File("src/Imagenes/FondoPantalla.PNG"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void inicializarPantalla() {
		cochesBlancosPrimero = new ArrayList<Sprite>();
		cochesRojosPrimero = new ArrayList<Sprite>();
		cochesRojosSegundo = new ArrayList<Sprite>();
		cochesBlancosSegundo = new ArrayList<>();
		troncosIzquierdaPrimero = new ArrayList<Sprite>();
		troncosDerecha = new ArrayList<Sprite>();
		troncosIzquierdaSegundo = new ArrayList<Sprite>();
		
		try {
			sonido = AudioSystem.getClip();
			sonido.open(AudioSystem.getAudioInputStream(new File("src/Musicas/musicaFondo.wav")));
			sonido.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		contadorMarcador = 0;

		crearSpritesEnemigos();

		personaje = new Sprite(50, 70, 400, panelJuego.getHeight() - 70, "src/Imagenes/SpriteRana.png");

		marcador1 = new Sprite(50, 70, 70, 0, "src/Imagenes/SpriteRana.png");
		marcador2 = new Sprite(50, 70, 280, 0, "src/Imagenes/SpriteRana.png");
		marcador3 = new Sprite(50, 70, 480, 0, "src/Imagenes/SpriteRana.png");
		marcador4 = new Sprite(50, 70, 670, 0, "src/Imagenes/SpriteRana.png");

		int posicionYMarcadores = 0 + marcador1.getAlto();

		fuenteTiempo = new Font("VERDANA", Font.BOLD, 20);

		tiempoInicial = System.nanoTime();
		tiempoDeJuego = 0;
		formatoDecimal = new DecimalFormat("#.##");
		reescalarImagen();
	}

	@Override
	public void pintarPantalla(Graphics g) {
		rellenarFondo(g);

		// Pintamos los cuadrados:

		for (Sprite cuadrado : cochesBlancosPrimero) {
			cuadrado.pintarSpriteEnMundo(g);
		}

		for (Sprite cuadrado : cochesRojosPrimero) {
			cuadrado.pintarSpriteEnMundo(g);
		}

		for (Sprite cuadrado : cochesBlancosSegundo) {
			cuadrado.pintarSpriteEnMundo(g);
		}

		for (Sprite cuadrado : cochesRojosSegundo) {
			cuadrado.pintarSpriteEnMundo(g);
		}

		for (Sprite cuadrado : troncosIzquierdaPrimero) {
			cuadrado.pintarSpriteEnMundo(g);
		}

		for (Sprite cuadrado : troncosDerecha) {
			cuadrado.pintarSpriteEnMundo(g);
		}

		for (Sprite cuadrado : troncosIzquierdaSegundo) {
			cuadrado.pintarSpriteEnMundo(g);
		}

		if (personaje != null) {
			personaje.pintarSpriteEnMundo(g);
		}

		pintarMarcadores(g);
		pintarTiempo(g);
		pintarVida(g);
		comprobarVictoria();

	}

	private void comprobarVictoria() {
		
		if(hayMarcador1 && hayMarcador2 && hayMarcador3 && hayMarcador4) {
			sonido.close();
			PantallaVictoria pantallaJuego = new PantallaVictoria(panelJuego);
			pantallaJuego.inicializarPantalla();
			panelJuego.setPantallaActual(pantallaJuego);
		}
		
		if(contadorVida == 0 && contadorMarcador >= 3) {
			sonido.close();
			PantallaDerrotaDeprimente pantallaJuego = new PantallaDerrotaDeprimente(panelJuego);
			pantallaJuego.inicializarPantalla();
			panelJuego.setPantallaActual(pantallaJuego);
		}
		
		if(contadorVida == 0) {
			sonido.close();
			PantallaDerrota pantallaJuego = new PantallaDerrota(panelJuego);
			pantallaJuego.inicializarPantalla();
			panelJuego.setPantallaActual(pantallaJuego);
		}
		
	}
	
	
	/* Este metodo pintar� la rana en cada nenufar si el jugador llega hasta alguno */
	private void pintarMarcadores(Graphics g) {

		if (personaje.getPosY() > -80 && personaje.getPosY() < 80) {
			if (personaje.getPosX() > 10 && personaje.getPosX() < 150) {
				marcador1.pintarSpriteEnMundo(g);
				hayMarcador1 = true;
				personaje.setPosX(400);
				personaje.setPosY(panelJuego.getHeight() - 70);
				contadorVida++;
			}

			if (personaje.getPosX() > 170 && personaje.getPosX() < 380) {
				marcador2.pintarSpriteEnMundo(g);
				hayMarcador2 = true;
				personaje.setPosX(400);
				personaje.setPosY(panelJuego.getHeight() - 70);
				contadorVida++;
			}

			if (personaje.getPosX() > 400 && personaje.getPosX() < 590) {
				marcador3.pintarSpriteEnMundo(g);
				hayMarcador3 = true;
				personaje.setPosX(400);
				personaje.setPosY(panelJuego.getHeight() - 70);
				contadorVida++;
			}

			if (personaje.getPosX() > 595 && personaje.getPosX() < 780) {
				marcador4.pintarSpriteEnMundo(g);
				hayMarcador4 = true;
				personaje.setPosX(400);
				personaje.setPosY(panelJuego.getHeight() - 70);
				contadorVida++;
			}
		}

		if (hayMarcador1) {
			marcador1.pintarSpriteEnMundo(g);
			contadorMarcador++;
		}

		if (hayMarcador2) {
			marcador2.pintarSpriteEnMundo(g);
			contadorMarcador++;
		}

		if (hayMarcador3) {
			marcador3.pintarSpriteEnMundo(g);
			contadorMarcador++;
		}

		if (hayMarcador4) {
			marcador4.pintarSpriteEnMundo(g);
			contadorMarcador++;
		}

	}

	private void crearSpritesEnemigos() {

		Random rd = new Random();

		int[] separacion = { 160, 320, 450, 720 };

		/* Coches blancos que salen primero por la derecha */
		for (int i = 0; i < 4; i++) {
			Sprite creador;
			creador = new Sprite(ANCHO_COCHE, ALTO_COCHE, panelJuego.getWidth()  + separacion[i],
					panelJuego.getHeight() - 130, 3, 0, "src/Imagenes/cocheBlanco.png");
			cochesBlancosPrimero.add(creador);
		}

		int[] separacion2 = { 80, 320, 560, 720 };

		/* Coches rojos que salen primero por la izquierda */
		for (int i = 0; i < 4; i++) {
			Sprite creador;
			creador = new Sprite(80, 60, 350 - separacion2[i], panelJuego.getHeight() - 210, 4, 0,
					"src/Imagenes/cocheRojo.png");
			cochesRojosPrimero.add(creador);
		}

		/* Coches blancos que salen segundo por la derecha */
		for (int i = 0; i < 4; i++) {
			Sprite creador;
			creador = new Sprite(ANCHO_COCHE, ALTO_COCHE, panelJuego.getWidth() - 240 + separacion[i],
					panelJuego.getHeight() - 290, 5, 0, "src/Imagenes/cocheBlanco.png");
			cochesBlancosSegundo.add(creador);
		}

		/* Coches rojos que salen segundo por la izquierda */
		for (int i = 0; i < 4; i++) {
			Sprite creador;
			creador = new Sprite(80, 60, 350 - separacion2[i], panelJuego.getHeight() - 370, 3, 0,
					"src/Imagenes/cocheRojo.png");
			cochesRojosPrimero.add(creador);
		}

		/* Troncos que salen primero por la derecha */
		for (int i = 0; i < 4; i++) {
			Sprite creador;
			creador = new Sprite(ANCHO_COCHE, ALTO_COCHE, panelJuego.getWidth() - 240 + separacion[i],
					panelJuego.getHeight() - 530, 3, 0, "src/Imagenes/tronco.png");
			troncosIzquierdaPrimero.add(creador);
		}

		/* Troncos que salen segundo por la izquierda */
		for (int i = 0; i < 4; i++) {
			Sprite creador;
			creador = new Sprite(ANCHO_COCHE, ALTO_COCHE, 350 - separacion[i], panelJuego.getHeight() - 610, 5, 0,
					"src/Imagenes/tronco.png");
			troncosDerecha.add(creador);
		}

		/* Troncos que salen segundo por la derecha */
		for (int i = 0; i < 4; i++) {
			Sprite creador;
			creador = new Sprite(ANCHO_COCHE, ALTO_COCHE, panelJuego.getWidth() - 240 + separacion[i],
					panelJuego.getHeight() - 690, 2, 0, "src/Imagenes/tronco.png");
			troncosIzquierdaPrimero.add(creador);
		}

	}

	private void pintarVida(Graphics g) {
		String vidas = Integer.toString(contadorVida);
		Font fuenteVidas = new Font("VERDANA", Font.BOLD, 20);
		g.setColor(Color.ORANGE);
		g.setFont(fuenteVidas);
		g.drawString("VIDAS: " + vidas, panelJuego.getWidth() - 120, 30);
	}

	private void pintarTiempo(Graphics g) {
		Font f = g.getFont();
		Color c = g.getColor();

		g.setColor(COLOR_PUNTUACION);
		g.setFont(fuenteTiempo);
		actualizarTiempo();
		g.drawString(formatoDecimal.format(tiempoDeJuego / 1000000000d), 25, 25);

		g.setColor(c);
		g.setFont(f);
	}

	/**
	 * Método que actualiza el tiempo de juego transcurrido.
	 */

	private void actualizarTiempo() {
		tiempoDeJuego = System.nanoTime() - tiempoInicial;
	}

	/**
	 * Método que se utiliza para rellenar el fondo del JPanel.
	 * 
	 * @param g Es el gráficos sobre el que vamos a pintar el fondo.
	 */

	private void rellenarFondo(Graphics g) {
		// Pintar la imagen de fondo reescalada:
		g.drawImage(imagenReescalada, 0, 0, null);
	}

	/**
	 * Método para mover todos los Sprites del juego.
	 */

	private void moverSprites() {

		for (int i = 0; i < cochesBlancosPrimero.size(); i++) {
			Sprite aux = cochesBlancosPrimero.get(i);
			aux.moverSpritesHaciaIzquierda(panelJuego.getWidth(), panelJuego.getHeight());
		}

		for (int i = 0; i < cochesRojosPrimero.size(); i++) {
			Sprite aux = cochesRojosPrimero.get(i);
			aux.moverSpritesHaciaDerecha(panelJuego.getWidth(), panelJuego.getHeight());
		}

		for (int i = 0; i < cochesBlancosSegundo.size(); i++) {
			Sprite aux = cochesBlancosSegundo.get(i);
			aux.moverSpritesHaciaIzquierda(panelJuego.getWidth(), panelJuego.getHeight());
		}

		for (int i = 0; i < cochesRojosSegundo.size(); i++) {
			Sprite aux = cochesRojosSegundo.get(i);
			aux.moverSpritesHaciaDerecha(panelJuego.getWidth(), panelJuego.getHeight());
		}

		for (int i = 0; i < troncosIzquierdaPrimero.size(); i++) {
			Sprite aux = troncosIzquierdaPrimero.get(i);
			aux.moverSpritesHaciaIzquierda(panelJuego.getWidth(), panelJuego.getHeight());
		}

		for (int i = 0; i < troncosDerecha.size(); i++) {
			Sprite aux = troncosDerecha.get(i);
			aux.moverSpritesHaciaDerecha(panelJuego.getWidth(), panelJuego.getHeight());
		}

		for (int i = 0; i < troncosIzquierdaSegundo.size(); i++) {
			Sprite aux = troncosIzquierdaSegundo.get(i);
			aux.moverSpritesHaciaIzquierda(panelJuego.getWidth(), panelJuego.getHeight());
		}

	}

	private void comprobarColisiones() {
		
		// Comprobar colisiones con el disparo
		try {
			for (int i = 0; i < cochesBlancosPrimero.size(); i++) {
				if (cochesBlancosPrimero.get(i).colisionan(personaje)) {
					contadorVida--;
					personaje.setPosX(400);
					personaje.setPosY(panelJuego.getHeight() - 70);
					if (contadorVida == 0) {
						
					}
				}
			}
			
			for (int i = 0; i < cochesRojosPrimero.size(); i++) {
				if (cochesRojosPrimero.get(i).colisionan(personaje)) {
					contadorVida--;
					personaje.setPosX(400);
					personaje.setPosY(panelJuego.getHeight() - 70);
					if (contadorVida == 0) {
						
					}
				}
			}
			
			for (int i = 0; i < cochesBlancosSegundo.size(); i++) {
				if (cochesBlancosSegundo.get(i).colisionan(personaje)) {
					contadorVida--;
					personaje.setPosX(400);
					personaje.setPosY(panelJuego.getHeight() - 70);
					if (contadorVida == 0) {
						
					}
				}
			}
			
			for (int i = 0; i < cochesBlancosSegundo.size(); i++) {
				if (cochesBlancosSegundo.get(i).colisionan(personaje)) {
					contadorVida--;
					personaje.setPosX(400);
					personaje.setPosY(panelJuego.getHeight() - 70);
					if (contadorVida == 0) {
						
					}
				}
			}
			
			for (int i = 0; i < troncosIzquierdaPrimero.size(); i++) {
				if (troncosIzquierdaPrimero.get(i).colisionan(personaje)) {
					contadorVida--;
					personaje.setPosX(400);
					personaje.setPosY(panelJuego.getHeight() - 450);
					if (contadorVida == 0) {
						
					}
				}
			}
			
			for (int i = 0; i < troncosDerecha.size(); i++) {
				if (troncosDerecha.get(i).colisionan(personaje)) {
					contadorVida--;
					personaje.setPosX(400);
					personaje.setPosY(panelJuego.getHeight() - 450);
					if (contadorVida == 0) {
						
					}
				}
			}
			
			for (int i = 0; i < troncosIzquierdaSegundo.size(); i++) {
				if (troncosIzquierdaSegundo.get(i).colisionan(personaje)) {
					contadorVida--;
					personaje.setPosX(400);
					personaje.setPosY(panelJuego.getHeight() - 450);
					if (contadorVida == 0) {
						
					}
				}
			}
		} catch (Exception e) {

		}

	}

	@Override
	public void ejecutarFrame() {

		comprobarColisiones();
		moverSprites();

		/* Para controlar que sucede cuando muere */
		// if (contadorVida == 0) {
		// panelJuego.ejecutando = false;
		// mensajeDialogo("HAS PERDIDO!\nQuieres volver a jugar?");
		// }

	}

	@Override
	public void pulsarRaton(MouseEvent e) {

		/* Controlar que no se salga por el lado izquierda de la pantalla */
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (personaje.getPosX() - 40 <= 0) {
				personaje.setPosX(0);
			}

			if (personaje.getPosX() - 40 > 0) {
				personaje.setPosX(personaje.getPosX() - 40);
			}
		}

		/* Controlar que no se salga por arriba */

		if (SwingUtilities.isMiddleMouseButton(e)) {
			if (personaje.getPosY() - 40 <= 0) {
				personaje.setPosY(0);
			}

			if (personaje.getPosY() - 40 > 0) {
				personaje.setPosY(personaje.getPosY() - 40);
			}
		}

		/* Controlar que no se salga por la derecha */

		if (SwingUtilities.isRightMouseButton(e)) {
			if ((personaje.getPosX() + personaje.getAncho()) + 40 > panelJuego.getWidth()) {
				personaje.setPosX(panelJuego.getWidth() - 40);
			}

			if ((personaje.getPosX() + personaje.getAncho()) + 40 < panelJuego.getWidth()) {
				personaje.setPosX(personaje.getPosX() + 40);
			}
		}

		/* Controlar que no se salga por abajo */

		if (SwingUtilities.isLeftMouseButton(e) && SwingUtilities.isRightMouseButton(e)) {
			if ((personaje.getPosY() + personaje.getAlto()) + 40 > panelJuego.getHeight()) {
				personaje.setPosY(panelJuego.getHeight() - personaje.getAlto());
			}
			if ((personaje.getPosY() + personaje.getAlto()) + 40 < panelJuego.getHeight()) {
				personaje.setPosY(personaje.getPosY() + 40);
			}
		}

	}

	@Override
	public void redimensionarPantalla(ComponentEvent e) {
		reescalarImagen();
	}

	private void reescalarImagen() {

		// Pensar en cada caso particular
		imagenReescalada = imagenOriginal.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				Image.SCALE_SMOOTH);
	}

	@Override
	public void moverRaton(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
