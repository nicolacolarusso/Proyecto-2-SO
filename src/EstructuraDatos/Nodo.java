/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EstructuraDatos;
import Classes.Personaje;
/**
 *
 * @author nicolagabrielecolarusso
 */
public class Nodo {
    
        /**
     * La infomacion a guardad en el nodo.
     */
    private Personaje tInfo;
    /**
     * El siguiente nodo.
     */
    private Nodo pNextNode;

    private int idNode;

    /**
     * Constructor de la clase.
     *
     * @param tInfo, la informacion a almacenar en el nodo.
     */
    public Nodo(Personaje tInfo) {
        this.tInfo = tInfo;
        this.pNextNode = null;
    }

    /**
     * Constructor de la clase.
     */
    public Nodo() {
        this.tInfo = null;
        this.pNextNode = null;
    }

    /**
     * Getter para acceder a la informacion que guarda el nodo.
     *
     * @return
     */
    public Personaje getTInfo() {
        return this.tInfo;
    }

    /**
     * Setter para modificar la informacion que guarda el nodo.
     *
     * @param tInfo
     */
    public void setTInfo(Personaje tInfo) {
        this.tInfo = tInfo;
    }

    /**
     * Getter para acceder al siguiente nodo enlazado.
     *
     * @return el nodo siguiente que esta enlazado.
     */
    public Nodo getNextNode() {
        return this.pNextNode;
    }

    /**
     * Setter para nodo siguiente que esta enlazado.
     *
     * @param pNextNode
     */
    public void setNextNode(Nodo pNextNode) {
        this.pNextNode = pNextNode;
    }

    @Override
    public String toString() {
        return "\n" + this.tInfo.toString();
    }

    /**
     * @return the idNode
     */
    public int getIdNode() {
        return idNode;
    }

    /**
     * @param idNode the idNode to set
     */
    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }
    
}
