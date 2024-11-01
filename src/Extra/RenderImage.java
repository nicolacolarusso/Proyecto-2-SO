/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Extra;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author nicolagabrielecolarusso
 */
public class RenderImage {
    /**
     * Carga una imagen desde el sistema de archivos o el classpath, la ajusta al tamaño especificado y devuelve un ImageIcon.
     * 
     * @param path La ruta de la imagen.
     * @param width El ancho deseado para el icono.
     * @param height La altura deseada para el icono.
     * @return Un ImageIcon ajustado al tamaño especificado, o null si la imagen no se puede cargar.
     */
    public ImageIcon loadScaledImage(String path, int width, int height) {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("No se pudo encontrar el recurso: " + path);
            return null;
        }
    }
}
