/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Extra;

import Classes.Personaje;
import EstructuraDatos.Lista;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author nicolagabrielecolarusso
 */
public class FileFunction {

    // Definimos arrays para cada categoría
    private Lista yellowStarWars = new Lista();
    private Lista greenStarWars = new Lista();
    private Lista redStarWars = new Lista();

    private Lista yellowStarTrek = new Lista();
    private Lista greenStarTrek = new Lista();
    private Lista redStarTrek = new Lista();

    public void read(File file) {
        String line;

        // Rellenar los arrays con las instancias de Character
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentSaga = null;

            while ((line = br.readLine()) != null) {
                if (!(line.isEmpty())) {
                    if (line.startsWith("[")) {
                        currentSaga = line.substring(1, line.length() - 1);

                    } else {
                        String[] parts = line.split(",");

                        Personaje character = new Personaje(
                                "",
                                parts[0],
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3]),
                                parts[4],
                                parts[5].split(";")[0]
                        );
 
                        // Clasificar el personaje en la linkedList correspondiente
                        if (line.contains("yellow.png") && "Star Wars".equals(currentSaga)) {
                            character.setPriorityLevel(1);
                            this.getYellowStarWars().addEnd(character);
                        } else if (line.contains("green.png") && "Star Wars".equals(currentSaga)) {
                            character.setPriorityLevel(2);
                            this.getGreenStarWars().addEnd(character);
                        } else if (line.contains("red.png") && "Star Wars".equals(currentSaga)) {
                            character.setPriorityLevel(3);
                            this.getRedStarWars().addEnd(character);
                        } else if (line.contains("yellow.png") && "Star Trek".equals(currentSaga)) {
                            character.setPriorityLevel(1);
                            this.getYellowStarTrek().addEnd(character);
                        } else if (line.contains("green.png") && "Star Trek".equals(currentSaga)) {
                            character.setPriorityLevel(2);
                            this.getGreenStarTrek().addEnd(character);
                        } else if (line.contains("red.png") && "Star Trek".equals(currentSaga)) {
                            character.setPriorityLevel(3);
                            this.getRedStarTrek().addEnd(character);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Lista getYellowStarWars() {
        return yellowStarWars;
    }

    public void setYellowStarWars(Lista yellowStarWars) {
        this.yellowStarWars = yellowStarWars;
    }

    public Lista getGreenStarWars() {
        return greenStarWars;
    }

    public void setGreenStarWars(Lista greenStarWars) {
        this.greenStarWars = greenStarWars;
    }

    public Lista getRedStarWars() {
        return redStarWars;
    }

    public void setRedStarWars(Lista redStarWars) {
        this.redStarWars = redStarWars;
    }

    public Lista getYellowStarTrek() {
        return yellowStarTrek;
    }

    public void setYellowStarTrek(Lista yellowStarTrek) {
        this.yellowStarTrek = yellowStarTrek;
    }

    public Lista getGreenStarTrek() {
        return greenStarTrek;
    }

    public void setGreenStarTrek(Lista greenStarTrek) {
        this.greenStarTrek = greenStarTrek;
    }

    public Lista getRedStarTrek() {
        return redStarTrek;
    }

    public void setRedStarTrek(Lista redStarTrek) {
        this.redStarTrek = redStarTrek;
    }

    
}

