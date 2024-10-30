/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;
import EstrcuturaDatos.Cola;
/**
 *
 * @author nicolagabrielecolarusso
 */
public class Controlador {
    
    
    private static final Home home = new Home();

    public static Home getHome() {
        return home;
    }

    public static void updateUIQueue(String tvShow, Cola queue1, Cola queue2, Cola queue3, Cola queue4) {
        if (tvShow.equalsIgnoreCase("regularshow")) {
            home.getTvPanelUI1().updateUIQueue(queue1, queue2, queue3, queue4);
        } else {
            home.getTvPanelUI2().updateUIQueue(queue1, queue2, queue3, queue4);
        }
    }

    
}
