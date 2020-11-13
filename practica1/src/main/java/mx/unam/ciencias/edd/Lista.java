package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            // Aquí va su código.
            this.siguiente = cabeza;
            this.anterior = null;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            if(this.siguiente == null){
              return false;
            }
            return true;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
            if(!hasNext()) {
              throw new NoSuchElementException();
            } else{
              T e = this.siguiente.elemento;
              this.anterior = siguiente;
              this.siguiente = siguiente.siguiente;
             return e;
           }
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
            if(this.anterior == null) {
              return false;
            } else{
              return true;
            }
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
            if(!hasPrevious()) {
              throw new NoSuchElementException();
            } else{
              T e = this.anterior.elemento;
              this.siguiente = this.anterior;
              this.anterior = this.anterior.anterior;

             return e;
           }
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
            this.siguiente = cabeza;
            this.anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
            this.siguiente = null;
          this.anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        int i = 0;
      Nodo n = this.cabeza;
      while(n != null) {
        i++;
        n = n.siguiente;
      }
      return i;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        int i = 0;
      Nodo n = this.cabeza;
      while(n != null) {
        i++;
        n = n.siguiente;
      }
      return i;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        if(longitud == 0){
          return true;
        }
        return false;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
          throw new IllegalArgumentException();
        }
        Nodo n = new Nodo(elemento);
        if(esVacia()) {
          cabeza = rabo = n;
        } else{
          n.anterior = rabo;
          rabo.siguiente = n;
          rabo = n;
        } longitud++;
      }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
        if(elemento == null) {
          throw new IllegalArgumentException();
        }
        Nodo n = new Nodo(elemento);
        if(esVacia()) {
          cabeza = rabo = n;
        } else{
          n.siguiente = cabeza;
          cabeza.anterior = n;
          cabeza = n;
        } longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        if(elemento == null){
        throw new IllegalArgumentException();
      }
      if(i <= 0){
        this.agregaInicio(elemento);
      } else if(this.getLongitud() <= i){
        this.agregaFinal(elemento);
      } else{
        Nodo aux = new Nodo(elemento);
        Nodo n = buscaNodo(get(i));
        n.anterior.siguiente = aux;
        aux.anterior = n.anterior;
        aux.siguiente = n;
        n.anterior = aux;
        longitud++;
      }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Nodo n = buscaNodo(elemento);
      if(n != null){
        if(n == this.cabeza){
          eliminaPrimero();
        } else if(n == this.rabo){
          eliminaUltimo();
        } else{
          n.anterior.siguiente = n.siguiente;
          n.siguiente.anterior = n.anterior;
          longitud--;
        }
      }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        if(esVacia()){
          throw new NoSuchElementException();
        }
        T e = cabeza.elemento;
        if(this.cabeza == this.rabo){
          cabeza = rabo = null;
        } else{
          cabeza = cabeza.siguiente;
          cabeza.anterior = null;
        }
        longitud--;
        return e;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        if(esVacia()){
          throw new NoSuchElementException();
        }
        T e = rabo.elemento;
        if(this.cabeza == this.rabo){
          rabo = cabeza = null;
        } else{
          rabo = rabo.anterior;
          rabo.siguiente = null;
        }
        longitud--;
        return e;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        Nodo n = buscaNodo(elemento);
        if(n == null){
          return false;
        }
        return true;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
        Lista<T> l = new Lista<T>();
        Nodo n = this.cabeza;
        while(n != null){
          l.agregaInicio(n.elemento);
          n = n.siguiente;
        }
        return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
        Lista<T> l = new Lista<T>();
        Nodo n = this.cabeza;
        while(n != null){
          l.agregaFinal(n.elemento);
          n = n.siguiente;
        }
        return l;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        Nodo n = this.cabeza;
        while(n != null){
          eliminaPrimero();
          n = n.siguiente;
        }
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        if(esVacia()){
          throw new NoSuchElementException();
        }
        Nodo n = this.cabeza;
        return n.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        if(esVacia()){
          throw new NoSuchElementException();
        }
        Nodo n = this.rabo;
        return n.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if(i < 0 || i >= getLongitud()) {
        throw new ExcepcionIndiceInvalido();
      }
      Nodo n = this.cabeza;
      while(i-- > 0) {
        n = n.siguiente;
      }
      return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        Nodo n = this.cabeza;
        int j = 0;
        while(n != null){
          if(n.elemento.equals(elemento)){
            return j;
          }
          n = n.siguiente;
          j++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
        if(esVacia()){
          return "[]";
        }
        Nodo n = this.cabeza;
        String s = "[";
        while(n != null){
          if(n != rabo){
            s += String.format("%d, ", n.elemento);
          } else{
            s += String.format("%d]", n.elemento);
          }
          n = n.siguiente;
        }
        return s;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        // Aquí va su código.
        if(this == null || lista == null){
          return false;
        }
        if(this.esVacia() && lista.esVacia()){
          return true;
        }
        if(this.longitud != lista.longitud){
          return false;
        }
        Nodo n = this.cabeza;
        Nodo m = lista.cabeza;
        while(n != null){
          if(n.elemento.equals(m.elemento)){
            n = n.siguiente;
            m = m.siguiente;
          } else{
            return false;
          }
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     *Busca un nodo y lo regresa.
     * @return el nodo deseado.
     */
     private Nodo buscaNodo(T elemento){
        Nodo n = this.cabeza;
        while(n != null){
          if(n.elemento.equals(elemento)){
            return n;
          }
          n = n.siguiente;
        }
        return null;
     }
}
