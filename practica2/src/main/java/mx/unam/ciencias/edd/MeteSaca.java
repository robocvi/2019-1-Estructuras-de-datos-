package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase abtracta para estructuras lineales restringidas a operaciones
 * mete/saca/mira.
 */
public abstract class MeteSaca<T> {

    /**
     * Clase interna protegida para nodos.
     */
    protected class Nodo {
        /** El elemento del nodo. */
        public T elemento;
        /** El siguiente nodo. */
        public Nodo siguiente;

        /**
         * Construye un nodo con un elemento.
         * @param elemento el elemento del nodo.
         */
        public Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }
    }

    /** La cabeza de la estructura. */
    protected Nodo cabeza;
    /** El rabo de la estructura. */
    protected Nodo rabo;

    /**
     * Agrega un elemento al extremo de la estructura.
     * @param elemento el elemento a agregar.
     */
    public abstract void mete(T elemento);

    /**
     * Elimina el elemento en un extremo de la estructura y lo regresa.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T saca() {
        // Aquí va su código.
        if(esVacia()){
          throw new NoSuchElementException();
        }
        T e = cabeza.elemento;
        cabeza = cabeza.siguiente;
        if(cabeza == null){
          rabo = null;
        }
        return e;
    }

    /**
     * Nos permite ver el elemento en un extremo de la estructura, sin sacarlo
     * de la misma.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T mira() {
        // Aquí va su código.
        if(esVacia()){
          throw new NoSuchElementException();
        }
        T e = cabeza.elemento;
        return e;
    }

    /**
     * Nos dice si la estructura está vacía.
     * @return <code>true</code> si la estructura no tiene elementos,
     *         <code>false</code> en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        if(this.cabeza == null || rabo == null){
          return true;
        }
        return false;
    }

    /**
     * Compara la estructura con un objeto.
     * @param object el objeto con el que queremos comparar la estructura.
     * @return <code>true</code> si el objeto recibido es una instancia de la
     *         misma clase que la estructura, y sus elementos son iguales en el
     *         mismo orden; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        @SuppressWarnings("unchecked") MeteSaca<T> m = (MeteSaca<T>)object;
        // Aquí va su código.
        if(this == null){
          return false;
        }
        if(this.esVacia() && m.esVacia()){
          return true;
        }
        Nodo n = this.cabeza;
        Nodo l = m.cabeza;
        int i = 0;
        int j = 0;
        while(n != null){
          n = n.siguiente;
          i++;
        }
        while(l != null){
          l = l.siguiente;
          j++;
        }
        if(i != j){
          return false;
        }
        while(n != null){
          if(n.elemento.equals(l.elemento)){
            n = n.siguiente;
            l = l.siguiente;
          } else{
            return false;
          }
        }
        return true;
    }
}
