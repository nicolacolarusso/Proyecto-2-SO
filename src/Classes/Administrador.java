/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import EstructuraDatos.Cola;
import EstructuraDatos.Lista;
import Interfaz.Controlador;
import proyecto2so.mainApp;

/**
 *
 * @author nicolagabrielecolarusso
 */
public class Administrador extends Thread {
    
    private IA ia;
    private final Semaphore mutex;
    private final Saga starWars;
    private final Saga starTrek;
    private int numRound = 0;

    public Administrador(IA ia, Semaphore mutex, Lista yellowCards1, Lista greenCards1, Lista redCards1,
            Lista yellowCards2, Lista greenCards2, Lista redCards2) {

        this.ia = ia;
        this.mutex = mutex;
        this.starWars = new Saga("StarWars", "/GUI/Assets/StarWars",
                yellowCards1, greenCards1, redCards1);
        this.starTrek = new Saga("StarTrek", "/GUI/Assets/StarTrek",
                yellowCards2, greenCards2, redCards2);
    }

    public void startSimulation() {
        Controlador.getHome().setVisible(true);

        for (int i = 0; i < 20; i++) {
            getStarWars().createCharacter();
            getStarTrek().createCharacter();
        }

        Controlador.getHome().getmoviePanelStarWars().updateUICola(getStarWars().getQueue1(),
                getStarWars().getQueue2(),
                getStarWars().getQueue3(),
                getStarWars().getQueue4()
        );

        Controlador.getHome().getmoviePanelStarTrek().updateUICola(getStarTrek().getQueue1(),
                getStarTrek().getQueue2(),
                getStarTrek().getQueue3(),
                getStarTrek().getQueue4()
        );

        Controlador.getHome().setVisible(true);

        try {
            mutex.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.start();
        this.getIa().start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                int battleDuration = Controlador.getHome().getBattleDuration().getValue();
                ia.setTime(battleDuration);
                synchronizeReinforcementQueues();
                /*
                updateReinforcementQueue(this.starWars);
                updateReinforcementQueue(this.starTrek);
*/
                if (numRound == 2) {
                    tryCreateCharacters();
                    numRound = 0;
                }

                Personaje starWarsFighter = chooseFighters(this.getStarWars());
                Personaje starTrekFighter = chooseFighters(this.getStarTrek());

                //------------------
                //TODO: Pasarle los fighters a la IA
                // Aca 0j0
                //------------------
                this.getIa().setStarWarsFighter(starWarsFighter);
                this.getIa().setStarTrekFighter(starTrekFighter);

                updateUIcola();
                mutex.release();
                Thread.sleep(100);
                mutex.acquire();

                this.numRound += 1;
                
                risePriorities(this.getStarWars());
                risePriorities(this.getStarTrek());

                updateUIcola();

            } catch (InterruptedException ex) {
                Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void risePriorities(Saga tvShow) {
        riseQueue(tvShow.getQueue2(), tvShow.getQueue1());
        riseQueue(tvShow.getQueue3(), tvShow.getQueue2());
    }

    private void riseQueue(Cola currentLevel, Cola nextLevel) {
        int len = currentLevel.getLength();

        for (int i = 0; i < len; i++) {
            Personaje character = currentLevel.dequeue();
            character.setCounter(character.getCounter() + 1);

            if (character.getCounter() >= 8) {
                character.setCounter(0);
                nextLevel.enqueue(character);
            } else {
                currentLevel.enqueue(character);
            }
        }
    }

    private Personaje chooseFighters(Saga tvShow) {
        if (tvShow.getQueue1().isEmpty()) {
            tvShow.updateQueue1();
            this.updateUIcola();
        }
        Personaje fighter = tvShow.getQueue1().dequeue();
        fighter.setCounter(0);
        return fighter;
    }

    public void updateUIcola() {
        Controlador.updateUICola("starwars",
                this.getStarWars().getQueue1(),
                this.getStarWars().getQueue2(),
                this.getStarWars().getQueue3(),
                this.getStarWars().getQueue4());
        Controlador.updateUICola("startrek",
                this.getStarTrek().getQueue1(),
                this.getStarTrek().getQueue2(),
                this.getStarTrek().getQueue3(),
                this.getStarTrek().getQueue4());
    }
    private void synchronizeReinforcementQueues() {
    // Verifica que ambas colas de refuerzo tengan personajes
    if (!this.starWars.getQueue4().isEmpty() && !this.starTrek.getQueue4().isEmpty()) {
        
        // Genera números aleatorios para determinar la probabilidad de mover a ambos personajes
        double randomNumStarWars = Math.random();
        double randomNumStarTrek = Math.random();
        
        // Desencola temporalmente el primer personaje de cada cola de refuerzos
        Personaje starWarsCharacter = this.starWars.getQueue4().dequeue();
        Personaje starTrekCharacter = this.starTrek.getQueue4().dequeue();

        // Verifica si ambos cumplen con el 40% de probabilidad al mismo tiempo
        if (randomNumStarWars <= 0.4 && randomNumStarTrek <= 0.4) {
            // Si ambos cumplen la probabilidad, los mueve a Queue1
            starWarsCharacter.setCounter(0);
            starTrekCharacter.setCounter(0);
            this.starWars.getQueue1().enqueue(starWarsCharacter);
            this.starTrek.getQueue1().enqueue(starTrekCharacter);
        } else {
            // Si alguno no cumple, ambos regresan al final de sus respectivas colas de refuerzo
            this.starWars.getQueue4().enqueue(starWarsCharacter);
            this.starTrek.getQueue4().enqueue(starTrekCharacter);
        }
    }
    }

/*    private void updateReinforcementQueue(Saga tvShow) {
        if (!(tvShow.getQueue4().isEmpty())) {
            double randomNum = Math.random();

            if (randomNum <= 0.4) {
                Personaje character = tvShow.getQueue4().dequeue();
                character.setCounter(0);
                tvShow.getQueue1().enqueue(character);
            }else {
            Personaje character = tvShow.getQueue4().dequeue();
            tvShow.getQueue4().enqueue(character);
            }
        }
    }
*/
    private void tryCreateCharacters() {
        double randomNum = Math.random();

        if (randomNum <= 0.8) {
            getStarWars().createCharacter();
            getStarTrek().createCharacter();
        }
    }

    public Saga getStarWars() {
        return starWars;
    }

    public Saga getStarTrek() {
        return starTrek;
    }



    /**
     * @return the ia
     */
    public IA getIa() {
        return ia;
    }

    /**
     * @param ia the ia to set
     */
    public void setIa(IA ia) {
        this.ia = ia;
    }

}
