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

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import base.PanelJuego;
import base.Pantalla;

public class PantallaInicialFrogger implements Pantalla {

	PanelJuego panelJuego;
	
	BufferedImage imagenOriginalInicial;
	Image imagenReescaladaInicial;
	Font fuenteInicial;
	//Inicio pantalla
	Color colorLetras = Color.RED;
	int contadorColorFrames = 0;
	static final int CAMBIO_COLOR_INICIO = 5;
	
	public PantallaInicialFrogger(PanelJuego panelJuego) {
		this.panelJuego = panelJuego;
	}
	
	
	@Override
	public void inicializarPantalla() {
		try {
			imagenOriginalInicial = ImageIO.read(new File("Imagenes/fondoPantallaInicio.jpg"));
			if(panelJuego.getWidth() != 0) {
				imagenReescaladaInicial = imagenOriginalInicial.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(), Image.SCALE_SMOOTH);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		fuenteInicial = new Font("VERDANA", Font.BOLD, 50); 
	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.drawImage(imagenReescaladaInicial, 0,0, null);
		g.setColor(colorLetras);
		g.setFont(fuenteInicial);
		g.drawString("PRESS START",panelJuego.getWidth()-585,panelJuego.getHeight()-150);
	}

	@Override
	public void ejecutarFrame() {
		contadorColorFrames++;
		if(contadorColorFrames % CAMBIO_COLOR_INICIO == 0) {
			if(colorLetras.equals(Color.RED)) {
				colorLetras = Color.ORANGE;
			}else {
				colorLetras = Color.RED;
			}
		}

	}

	@Override
	public void moverRaton(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pulsarRaton(MouseEvent e) {
			PantallaJuegoFrogger pantallaJuego = new PantallaJuegoFrogger(panelJuego);
			pantallaJuego.inicializarPantalla();
			panelJuego.setPantallaActual(pantallaJuego);
		
		//TODO: COlocar esto en otro sitio (es cambiar de pantalla)
		/* jugando = true;
		reescalarImagen();
		tiempoInicial = System.nanoTime();
		*/

	}

	@Override
	public void redimensionarPantalla(ComponentEvent e) {
		imagenReescaladaInicial = imagenOriginalInicial.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(), Image.SCALE_SMOOTH);
	}

}
