/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import proyecto2so.mainApp;
import Extra.RenderImage;
import Interfaz.Controlador;

/**
 *
 * @author nicolagabrielecolarusso
 */
public class IA extends Thread{
    
    private Administrador administrator;
    private Personaje starWarsFighter;
    private Personaje starTrekFighter;
    private int victoriesStarWars = 0;
    private int victoriesStarTrek = 0;

    private final Semaphore mutex;

    private long time;
    private int round;

    public IA() {
        this.administrator = mainApp.getApp().getAdmin();
        this.mutex = mainApp.getApp().getMutex();
        this.time = mainApp.getApp().getBattleDuration();
        this.round = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.mutex.acquire();

                this.round += 1;

                Controlador.getHome().getWinnerLabelID().setText("");
                Controlador.getHome().getIaStatusLabel().setText("Determinando el resultado del combate...");
                updateCardsUI(getStarWarsFighter(), getStarTrekFighter());

                Controlador.getHome().getRoundLabel().setText("Round: " + String.valueOf(round));

                Thread.sleep((long) (this.getTime() * 1000 * 0.7));
                
                
                double aux = Math.random();
                //audioooo
                //AudioManager audioManager = new AudioManager(); 

                if (aux <= 0.4) {
                    Controlador.getHome().getIaStatusLabel().setText("¡Hay un ganador!");
                    Personaje winner = getWinnerCharacter(this.starWarsFighter, this.starTrekFighter);
                    Controlador.getHome().getWinnerLabelID().setText(winner.getCharacterId());
                    //audioManager.playSoundEffect("/GUI/Assets/victory.wav", 2.0f);
                    Thread.sleep((long) ((getTime() * 1000 * 0.3) * 0.6));

                } else if (aux > 0.40 && aux <= 0.67) {
                    Controlador.getHome().getIaStatusLabel().setText("¡El combate termina en empate!");
                    //audioManager.playSoundEffect("/GUI/Assets/tie.wav", 2.0f);
                    Thread.sleep((long) ((getTime() * 1000 * 0.3) * 0.6));
                    

                    this.getAdministrator().getStarWars().getQueue1().enqueue(this.starWarsFighter);
                    this.getAdministrator().getStarTrek().getQueue1().enqueue(this.starTrekFighter);
                } else {
                    Controlador.getHome().getIaStatusLabel().setText("El combate no se llevará a cabo.");
                    //audioManager.playSoundEffect("/GUI/Assets/fail.wav", 2.0f);
                    Thread.sleep((long) ((getTime() * 1000 * 0.3) * 0.6));

                    this.getAdministrator().getStarWars().getQueue4().enqueue(this.starWarsFighter);
                    this.getAdministrator().getStarTrek().getQueue4().enqueue(this.starTrekFighter);
                }

                clearFightersUI();

                Thread.sleep((long) ((getTime() * 1000 * 0.3) * 0.4));
                this.mutex.release();
                Thread.sleep(100);

            } catch (InterruptedException ex) {
                Logger.getLogger(IA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void clearFightersUI() {
        Controlador.getHome().getIaStatusLabel().setText("Esperando nuevos personajes...");
        Controlador.getHome().getWinnerLabelID().setText("");
        Controlador.getHome().getStarWarsFighter().clearFightersLabels();
        Controlador.getHome().getStarTrekFighter().clearFightersLabels();
    }

    private Personaje getWinnerCharacter(Personaje starWarsFighter, Personaje starTrekFighter) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + getTime() * 1000; // Convierte tiempo de combate a milisegundos
        boolean combatEnd = false;

        // Determina quién ataca primero basado en la velocidad inicialmente
        boolean isStarWarsTurn = starWarsFighter.getSpeedVelocity() >= starTrekFighter.getSpeedVelocity();

        while (System.currentTimeMillis() < endTime && !combatEnd) {
            int damage;
            if (isStarWarsTurn) {
                // Star Wars ataca
                Controlador.getHome().getStarWarsFighter().getStatusLabel().setText("Enviando daño");
                Controlador.getHome().getStarTrekFighter().getStatusLabel().setText("Recibiendo daño");
                damage = calculateDamage(starWarsFighter, starTrekFighter);
                starTrekFighter.takeDamage(damage);
                Controlador.getHome().getStarTrekFighter().getHPLabel().setText(String.valueOf(starTrekFighter.getHitPoints()));
                if (starTrekFighter.getHitPoints() <= 0) combatEnd = true;
            } else {
                // Star Trek ataca
                Controlador.getHome().getStarTrekFighter().getStatusLabel().setText("Enviando daño");
                Controlador.getHome().getStarWarsFighter().getStatusLabel().setText("Recibiendo daño");
                damage = calculateDamage(starTrekFighter, starWarsFighter);
                starWarsFighter.takeDamage(damage);
                Controlador.getHome().getStarWarsFighter().getHPLabel().setText(String.valueOf(starWarsFighter.getHitPoints()));
                if (starWarsFighter.getHitPoints() <= 0) combatEnd = true;
            }

            // Alterna el turno para el próximo ciclo
            isStarWarsTurn = !isStarWarsTurn;
            Controlador.getHome().getStarTrekFighter().getHPLabel().setText(String.valueOf(starTrekFighter.getHitPoints()));
            Controlador.getHome().getStarWarsFighter().getHPLabel().setText(String.valueOf(starWarsFighter.getHitPoints()));

            // Simula una pausa por ronda
            try {
                Thread.sleep(1000); // Ajusta según necesidad para permitir actualización de UI
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (combatEnd) break; // Salir del bucle si el combate terminó.
        }
        
        if (!combatEnd) {
        // Aquí se decide el ganador basado en quién tiene más HP.
            if (starWarsFighter.getHitPoints() > starTrekFighter.getHitPoints()) {
                this.victoriesStarWars++;
                Controlador.getHome().getmoviePanelStarWars().getVictoriesLabel().setText(String.valueOf(this.victoriesStarWars));
                return starWarsFighter;
            } else if (starWarsFighter.getHitPoints() < starTrekFighter.getHitPoints()) {
                this.victoriesStarTrek++;
                Controlador.getHome().getmoviePanelStarTrek().getVictoriesLabel().setText(String.valueOf(this.victoriesStarTrek));
                return starTrekFighter;
            } else {
                // En caso de empate por HP
                return starTrekFighter;
            }
        }

        // Determinar ganador basado en HP restante.
        if (starWarsFighter.getHitPoints() > 0) {
            this.victoriesStarWars++;
            Controlador.getHome().getmoviePanelStarWars().getVictoriesLabel().setText(String.valueOf(this.victoriesStarWars));
            return starWarsFighter;
        } else if (starTrekFighter.getHitPoints() > 0) {
            this.victoriesStarTrek++;
            Controlador.getHome().getmoviePanelStarTrek().getVictoriesLabel().setText(String.valueOf(this.victoriesStarTrek));
            return starTrekFighter;
        } else {
            return null; // Manejo de empate
        }
    }


    private int calculateDamage(Personaje attacker, Personaje defender) {
        // Daño base con la lógica que el ataque no puede ser completo porque sino lo matariamos de one.
         int baseDamage = (attacker.getSpeedVelocity() + (attacker.getAgility() / 2)) / 2;

         // Inicializamos el daño
         int damage = baseDamage;

         switch (attacker.getHability()) {
             case "Ataque Crítico":
                 //INCREMENTE EL DAÑO BASE DE X1.5
                 damage *= 1.5;
                 break;
             case "Curación":
                 // RECUPERA EN VIDA LA MITAD DE LO QUE LO ATACARÍA 
                 int healAmount = baseDamage / 2;
                 attacker.heal(healAmount);
                 damage = (attacker.getSpeedVelocity() + (attacker.getAgility() / 2)) / 4;
                 break;
             case "Parálisis":
                 // Se le disminuye la agilidad al enemigo en un 50%
                 defender.setAgility(defender.getAgility() / 2);
                 break;
             case "Congelamiento":
                 // Se le disminuye la velocidad al enemigo en un 50%
                 defender.setSpeedVelocity(defender.getSpeedVelocity() / 2);
                 break;
             default:
                 // No special ability
                 break;
         }

         return damage;
     }

    private void updateCardsUI(Personaje starWarsCharacter, Personaje starTrekCharacter) {
        RenderImage imageUtils = new RenderImage();

        ImageIcon starWarsCardIA = imageUtils.loadScaledImage(starWarsCharacter.getUrlSource(), 150, 200
        );

        ImageIcon starTrekCardIA = imageUtils.loadScaledImage(starTrekCharacter.getUrlSource(), 150, 200
        );

        Controlador.getHome().getStarWarsFighter().getFighterCard().setIcon(starWarsCardIA);
        Controlador.getHome().getStarWarsFighter().getCharacterIDLabel().setText(starWarsCharacter.getCharacterId());
        Controlador.getHome().getStarWarsFighter().getHPLabel().setText(String.valueOf(starWarsCharacter.getHitPoints()));

        Controlador.getHome().getStarTrekFighter().getFighterCard().setIcon(starTrekCardIA);
        Controlador.getHome().getStarTrekFighter().getCharacterIDLabel().setText(starTrekCharacter.getCharacterId());
        Controlador.getHome().getStarTrekFighter().getHPLabel().setText(String.valueOf(starTrekCharacter.getHitPoints()));
    }

    public Personaje getStarWarsFighter() {
        return starWarsFighter;
    }

    public void setStarWarsFighter(Personaje starWarsFighter) {
        this.starWarsFighter = starWarsFighter;
    }

    public Personaje getStarTrekFighter() {
        return starTrekFighter;
    }

    public void setStarTrekFighter(Personaje starTrekFighter) {
        this.starTrekFighter = starTrekFighter;
    }

    /**
     * @return the administrator
     */
    public Administrador getAdministrator() {
        return administrator;
    }

    /**
     * @param administrator the administrator to set
     */
    public void setAdministrator(Administrador administrator) {
        this.administrator = administrator;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }

}
