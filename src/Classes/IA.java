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

/**
 *
 * @author nicolagabrielecolarusso
 */
public class IA extends Thread{
    
    private Administrador administrator;
    private Personaje regularShowFighter;
    private Personaje avatarFighter;
    private int victoriesRegularShow = 0;
    private int victoriesAvatar = 0;

    private final Semaphore mutex;

    private long time;
    private int round;

    public IA() {
        this.administrator = App.getApp().getAdmin();
        this.mutex = App.getApp().getMutex();
        this.time = App.getApp().getBattleDuration();
        this.round = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.mutex.acquire();

                this.round += 1;

                ControlMainUI.getHome().getWinnerLabelID().setText("");
                ControlMainUI.getHome().getIaStatusLabel().setText("Determinando el resultado del combate...");
                updateCardsUI(getRegularShowFighter(), getAvatarFighter());

                ControlMainUI.getHome().getRoundLabel().setText("Round: " + String.valueOf(round));

                Thread.sleep((long) (this.getTime() * 1000 * 0.7));

                double aux = Math.random();
                AudioManager audioManager = new AudioManager(); 

                if (aux <= 0.4) {
                    ControlMainUI.getHome().getIaStatusLabel().setText("¡Hay un ganador!");
                    CharacterTv winner = getWinnerCharacter(this.regularShowFighter, this.avatarFighter);
                    ControlMainUI.getHome().getWinnerLabelID().setText(winner.getCharacterId());
                    audioManager.playSoundEffect("/GUI/Assets/victory.wav", 2.0f);
                    Thread.sleep((long) ((getTime() * 1000 * 0.3) * 0.6));

                } else if (aux > 0.40 && aux <= 0.67) {
                    ControlMainUI.getHome().getIaStatusLabel().setText("¡El combate termina en empate!");
                    audioManager.playSoundEffect("/GUI/Assets/tie.wav", 2.0f);
                    Thread.sleep((long) ((getTime() * 1000 * 0.3) * 0.6));
                    

                    this.getAdministrator().getRegularShow().getQueue1().enqueue(this.regularShowFighter);
                    this.getAdministrator().getAvatar().getQueue1().enqueue(this.avatarFighter);
                } else {
                    ControlMainUI.getHome().getIaStatusLabel().setText("El combate no se llevará a cabo.");
                    audioManager.playSoundEffect("/GUI/Assets/fail.wav", 2.0f);
                    Thread.sleep((long) ((getTime() * 1000 * 0.3) * 0.6));

                    this.getAdministrator().getRegularShow().getQueue4().enqueue(this.regularShowFighter);
                    this.getAdministrator().getAvatar().getQueue4().enqueue(this.avatarFighter);
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
        ControlMainUI.getHome().getIaStatusLabel().setText("Esperando nuevos personajes...");
        ControlMainUI.getHome().getWinnerLabelID().setText("");
        ControlMainUI.getHome().getRegularShowFighter().clearFightersLabels();
        ControlMainUI.getHome().getAvatarFighter().clearFightersLabels();
    }

    private Personaje getWinnerCharacter(Personaje regularShowFighter, Personaje avatarFighter) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + getTime() * 1000; // Convierte tiempo de combate a milisegundos
        boolean combatEnd = false;

        // Determina quién ataca primero basado en la velocidad inicialmente
        boolean isRegularShowTurn = regularShowFighter.getSpeedVelocity() >= avatarFighter.getSpeedVelocity();

        while (System.currentTimeMillis() < endTime && !combatEnd) {
            int damage;
            if (isRegularShowTurn) {
                // Regular Show ataca
                ControlMainUI.getHome().getRegularShowFighter().getStatusLabel().setText("Enviando daño");
                ControlMainUI.getHome().getAvatarFighter().getStatusLabel().setText("Recibiendo daño");
                damage = calculateDamage(regularShowFighter, avatarFighter);
                avatarFighter.takeDamage(damage);
                ControlMainUI.getHome().getAvatarFighter().getHPLabel().setText(String.valueOf(avatarFighter.getHitPoints()));
                if (avatarFighter.getHitPoints() <= 0) combatEnd = true;
            } else {
                // Avatar ataca
                ControlMainUI.getHome().getAvatarFighter().getStatusLabel().setText("Enviando daño");
                ControlMainUI.getHome().getRegularShowFighter().getStatusLabel().setText("Recibiendo daño");
                damage = calculateDamage(avatarFighter, regularShowFighter);
                regularShowFighter.takeDamage(damage);
                ControlMainUI.getHome().getRegularShowFighter().getHPLabel().setText(String.valueOf(regularShowFighter.getHitPoints()));
                if (regularShowFighter.getHitPoints() <= 0) combatEnd = true;
            }

            // Alterna el turno para el próximo ciclo
            isRegularShowTurn = !isRegularShowTurn;
            ControlMainUI.getHome().getAvatarFighter().getHPLabel().setText(String.valueOf(avatarFighter.getHitPoints()));
            ControlMainUI.getHome().getRegularShowFighter().getHPLabel().setText(String.valueOf(regularShowFighter.getHitPoints()));

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
            if (regularShowFighter.getHitPoints() > avatarFighter.getHitPoints()) {
                this.victoriesRegularShow++;
                ControlMainUI.getHome().getTvPanelUI1().getVictoriesLabel().setText(String.valueOf(this.victoriesRegularShow));
                return regularShowFighter;
            } else if (regularShowFighter.getHitPoints() < avatarFighter.getHitPoints()) {
                this.victoriesAvatar++;
                ControlMainUI.getHome().getTvPanelUI2().getVictoriesLabel().setText(String.valueOf(this.victoriesAvatar));
                return avatarFighter;
            } else {
                // En caso de empate por HP
                return avatarFighter;
            }
        }

        // Determinar ganador basado en HP restante.
        if (regularShowFighter.getHitPoints() > 0) {
            this.victoriesRegularShow++;
            ControlMainUI.getHome().getTvPanelUI1().getVictoriesLabel().setText(String.valueOf(this.victoriesRegularShow));
            return regularShowFighter;
        } else if (avatarFighter.getHitPoints() > 0) {
            this.victoriesAvatar++;
            ControlMainUI.getHome().getTvPanelUI2().getVictoriesLabel().setText(String.valueOf(this.victoriesAvatar));
            return avatarFighter;
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

    private void updateCardsUI(Personaje regularShowCharacter, Personaje avatarCharacterTv) {
        ImageUtils imageUtils = new ImageUtils();

        ImageIcon regularShowCardIA = imageUtils.loadScaledImage(
                regularShowCharacter.getUrlSource(), 150, 200
        );

        ImageIcon avatarCardIA = imageUtils.loadScaledImage(
                avatarCharacterTv.getUrlSource(), 150, 200
        );

        ControlMainUI.getHome().getRegularShowFighter().getFighterCard().setIcon(regularShowCardIA);
        ControlMainUI.getHome().getRegularShowFighter().getCharacterIDLabel().setText(regularShowCharacter.getCharacterId());
        ControlMainUI.getHome().getRegularShowFighter().getHPLabel().setText(String.valueOf(regularShowCharacter.getHitPoints()));

        ControlMainUI.getHome().getAvatarFighter().getFighterCard().setIcon(avatarCardIA);
        ControlMainUI.getHome().getAvatarFighter().getCharacterIDLabel().setText(avatarCharacterTv.getCharacterId());
        ControlMainUI.getHome().getAvatarFighter().getHPLabel().setText(String.valueOf(avatarCharacterTv.getHitPoints()));
    }

    /**
     * @return the regularShowFighter
     */
    public Personaje getRegularShowFighter() {
        return regularShowFighter;
    }

    /**
     * @param regularShowFighter the regularShowFighter to set
     */
    public void setRegularShowFighter(Personaje regularShowFighter) {
        this.regularShowFighter = regularShowFighter;
    }

    /**
     * @return the avatarFighter
     */
    public Personaje getAvatarFighter() {
        return avatarFighter;
    }

    /**
     * @param avatarFighter the avatarFighter to set
     */
    public void setAvatarFighter(Personaje avatarFighter) {
        this.avatarFighter = avatarFighter;
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
