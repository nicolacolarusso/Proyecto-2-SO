/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Extra;

import Classes.Administrador;
import Classes.IA;
import proyecto2so.mainApp;

/**
 *
 * @author nicolagabrielecolarusso
 */
public class ExtraFunctions {
     public final static String[] priority = {"Yellow", "Green", "Red"};

    public static void loadParams() {
        mainApp app = mainApp.getInstance();
        FileFunction fileFunctions = new FileFunction();
        fileFunctions.read(mainApp.getSelectedFile());

        app.setIa(new IA());

        Administrador admin = new Administrador(
                app.getIa(),
                app.getMutex(),
                fileFunctions.getYellowStarWars(), fileFunctions.getGreenStarWars(), fileFunctions.getRedStarWars(),
                fileFunctions.getYellowStarTrek(), fileFunctions.getGreenStarTrek(), fileFunctions.getRedStarTrek());

        app.setAdmin(admin);
        app.getAdmin().getIa().setAdministrator(admin);
        app.getAdmin().startSimulation();

    }
}
