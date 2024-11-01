/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import Extra.ExtraFunctions;
import EstructuraDatos.Cola;
import EstructuraDatos.Lista;
import EstructuraDatos.Nodo;

/**
 *
 * @author nicolagabrielecolarusso
 */
public class Saga {
    
    private Lista yellowCards;
    private Lista greenCards;
    private Lista redCards;

    private Cola queue1 = new Cola();
    private Cola queue2 = new Cola();
    private Cola queue3 = new Cola();
    private Cola queue4 = new Cola();
    private String packageImg;
    private String logoUrl;
    private String tvShow;

    public Saga(String tvShow, String packageImg, Lista yellowCards, Lista greenCards, Lista redCards) {
        this.yellowCards = yellowCards;
        this.greenCards = greenCards;
        this.redCards = redCards;

        this.packageImg = packageImg;
        this.logoUrl = this.packageImg + "/logo.png";

    }

    private void createAndEnqueueCharacter(Nodo characterNode) {
        characterNode.setIdNode(characterNode.getIdNode() + 1);

        Personaje character = characterNode.getTInfo();

        int priorityLevel = character.getPriorityLevel();

        String characterId = character.getNameCharacter() + "-"
                + HelpersFunctions.priority[priorityLevel - 1] + "-"
                + characterNode.getIdNode();

        String nameCharacter = character.getNameCharacter();
        int hitPoints = character.getHitPoints();
        int speedVelocity = character.getSpeedVelocity();
        int agility = character.getAgility();
        String hability = character.getHability();
        String urlSource = character.getUrlSource();

        Personaje newCharacter = new Personaje(
                characterId,
                nameCharacter,
                hitPoints,
                speedVelocity,
                agility,
                hability,
                urlSource
        );

        newCharacter.setPriorityLevel(priorityLevel);

        if (priorityLevel == 1) {
            this.queue1.enqueue(newCharacter);
        } else if (priorityLevel == 2) {
            this.queue2.enqueue(newCharacter);
        } else {
            this.queue3.enqueue(newCharacter);
        }
    }

    public void createCharacter() {
        int quality = 0;
        double randomValueHP = Math.random();
        double randomValueSpeed = Math.random();
        double randomValueHability = Math.random();
        double randomValueAgility = Math.random();

        quality = (randomValueHability <= 0.6) ? quality + 1 : quality;
        quality = (randomValueHP <= 0.7) ? quality + 1 : quality;
        quality = (randomValueSpeed <= 0.5) ? quality + 1 : quality;
        quality = (randomValueAgility <= 0.4) ? quality + 1 : quality;

        Nodo node = new Nodo();

        if (quality == 4) {
            node = yellowCards.getRandomNode();
        } else if (quality == 3 || quality == 2) {
            node = greenCards.getRandomNode();
        } else {
            node = redCards.getRandomNode();
        }

        createAndEnqueueCharacter(node);
    }

    public void updateQueue1() {
        if (!(this.queue2.isEmpty())) {
            Personaje aux = this.queue2.dequeue();
            aux.setCounter(0);
            this.queue1.enqueue(aux);
        } else {
            Personaje aux = this.queue3.dequeue();
            aux.setCounter(0);
            this.queue1.enqueue(aux);
        }
    }

    /**
     * @return the yellowCards
     */
    public Lista getYellowCards() {
        return yellowCards;
    }

    /**
     * @param yellowCards the yellowCards to set
     */
    public void setYellowCards(Lista yellowCards) {
        this.yellowCards = yellowCards;
    }

    /**
     * @return the greenCards
     */
    public Lista getGreenCards() {
        return greenCards;
    }

    /**
     * @param greenCards the greenCards to set
     */
    public void setGreenCards(Lista greenCards) {
        this.greenCards = greenCards;
    }

    /**
     * @return the redCards
     */
    public Lista getRedCards() {
        return redCards;
    }

    /**
     * @param redCards the redCards to set
     */
    public void setRedCards(Lista redCards) {
        this.redCards = redCards;
    }

    /**
     * @return the queue1
     */
    public Cola getQueue1() {
        return queue1;
    }

    /**
     * @param queue1 the queue1 to set
     */
    public void setQueue1(Cola queue1) {
        this.queue1 = queue1;
    }

    /**
     * @return the queue2
     */
    public Cola getQueue2() {
        return queue2;
    }

    /**
     * @param queue2 the queue2 to set
     */
    public void setQueue2(Cola queue2) {
        this.queue2 = queue2;
    }

    /**
     * @return the queue3
     */
    public Cola getQueue3() {
        return queue3;
    }

    /**
     * @param queue3 the queue3 to set
     */
    public void setQueue3(Cola queue3) {
        this.queue3 = queue3;
    }

    /**
     * @return the queue4
     */
    public Cola getQueue4() {
        return queue4;
    }

    /**
     * @param queue4 the queue4 to set
     */
    public void setQueue4(Cola queue4) {
        this.queue4 = queue4;
    }

    /**
     * @return the packageImg
     */
    public String getPackageImg() {
        return packageImg;
    }

    /**
     * @param packageImg the packageImg to set
     */
    public void setPackageImg(String packageImg) {
        this.packageImg = packageImg;
    }

    /**
     * @return the logoUrl
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * @param logoUrl the logoUrl to set
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        String ListInfo = "\n\n\nListas:\n"
                + "-YellowCards:" + this.getYellowCards().toString() + "\n\n"
                + "-GreenCards:" + this.getGreenCards().toString() + "\n\n"
                + "-RedCards:" + this.getRedCards().toString() + "\n\n";

        String queueInfo = "\n\nColas:\n"
                + "Prioridad 1: " + this.getQueue1().toString() + "\n\n"
                + "Prioridad 2: " + this.getQueue2().toString() + "\n\n"
                + "Prioridad 3: " + this.getQueue3().toString() + "\n\n"
                + "Prioridad 4: " + this.getQueue4().toString() + "\n\n";

        return ListInfo + queueInfo;
    }

}
