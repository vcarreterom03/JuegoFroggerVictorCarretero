package pantallas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;

import base.PanelJuego;
import base.Pantalla;

public class PantallaDerrotaDeprimente implements Pantalla {
	PanelJuego panelJuego;

	BufferedImage imagenOriginalInicial;
	Image imagenReescaladaInicial;

	Clip sonido;
	
	public PantallaDerrotaDeprimente(PanelJuego panelJuego) {
		this.panelJuego = panelJuego;
		try {
			imagenOriginalInicial = ImageIO.read(new File("src/Imagenes/fondoDerrotaDeprimente.jpg"));
			if(panelJuego.getWidth() != 0) {
				imagenReescaladaInicial = imagenOriginalInicial.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(), Image.SCALE_SMOOTH);
			}
			sonido = AudioSystem.getClip();
			sonido.open(AudioSystem.getAudioInputStream(new File("src/Musicas/musicaVictoria.wav")));
			sonido.start();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void inicializarPantalla() {

	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.drawImage(imagenReescaladaInicial, 0, 0, null);

	}

	@Override
	public void ejecutarFrame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moverRaton(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pulsarRaton(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			sonido.close();
			PantallaInicialFrogger pantallaJuego = new PantallaInicialFrogger(panelJuego);
			pantallaJuego.inicializarPantalla();
			panelJuego.setPantallaActual(pantallaJuego);
		}
		if (SwingUtilities.isRightMouseButton(e)) {
			sonido.close();
			PantallaJuegoFrogger pantallaJuego = new PantallaJuegoFrogger(panelJuego);
			pantallaJuego.inicializarPantalla();
			panelJuego.setPantallaActual(pantallaJuego);
		}

	}

	@Override
	public void redimensionarPantalla(ComponentEvent e) {
		imagenReescaladaInicial = imagenOriginalInicial.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				Image.SCALE_SMOOTH);
	}

}