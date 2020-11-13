package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
            this.pila = new Pila<>();
            if(raiz != null){
              izquierdo(raiz, pila);
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            if(this.pila.esVacia())
              return false;
            return true;
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
            Vertice v = pila.saca();
            if(v.hayDerecho()){
              izquierdo(v.derecho, pila);
            }
            return v.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null)
          throw new IllegalArgumentException();
        Vertice v = nuevoVertice(elemento);
        elementos++;
        if(this.esVacia()){
          this.raiz = v;
          ultimoAgregado = raiz;
        } else{
          agregaAux(raiz, v);
        }
    }

    private void agregaAux(Vertice a, Vertice b){
      if(a.elemento.compareTo(b.elemento) >= 0){
        if(!a.hayIzquierdo()){
          a.izquierdo = b;
          b.padre = a;
          ultimoAgregado = b;
        }else{
          agregaAux(a.izquierdo, b);
        }
      }
      if(a.elemento.compareTo(b.elemento) < 0){
        if(!a.hayDerecho()){
          a.derecho = b;
          b.padre = a;
          ultimoAgregado = b;
        }else{
          agregaAux(a.derecho, b);
        }
      }

    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeArbolBinario<T> v = busca(elemento);
        if(v == null)
          return;
        Vertice a = vertice(v);
        elementos--;
        if(a.izquierdo == null || a.derecho == null){
          eliminaVertice(a);
        }else{
          Vertice b = intercambiaEliminable(a);
          eliminaVertice(b);
        }
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
        Vertice v = this.maximoSubArbol(vertice.izquierdo);
        T e = vertice.elemento;
        vertice.elemento = v.elemento;
        v.elemento = e;
        return v;
    }

    private Vertice maximoSubArbol(Vertice v){
      if(v.derecho == null)
        return v;
      return maximoSubArbol(v.derecho);
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
        Vertice a = vertice.padre;
        if(vertice.izquierdo == null && vertice.derecho == null){
          if(a == null){
            this.raiz = null;
          }else{
            if(vertice == a.izquierdo){
              a.izquierdo = null;
            }else{
              a.derecho = null;
            }
          }
        }
        if(vertice.izquierdo != null){
          if(a == null){
            this.raiz = vertice.izquierdo;
            vertice.izquierdo.padre = null;
          }else{
            if(vertice == a.izquierdo){
              a.izquierdo = vertice.izquierdo;
            }else{
              a.derecho = vertice.izquierdo;
            }
            vertice.izquierdo.padre = a;
          }
        }
        if(vertice.derecho != null){
          if(a == null){
            this.raiz = vertice.derecho;
            vertice.derecho.padre = null;
          }else{
            if(vertice == a.izquierdo){
              a.izquierdo = vertice.derecho;
            }else{
              a.derecho = vertice.derecho;
            }
            vertice.derecho.padre = a;
          }
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        return buscaRec(raiz, elemento);
      }

      private VerticeArbolBinario<T> buscaRec(Vertice v, T elemento){
        if(v == null)
          return null;
        if(v.elemento.compareTo(elemento) == 0)
          return v;
        if(v.elemento.compareTo(elemento) > 0)
          return buscaRec(v.izquierdo, elemento);
        return buscaRec(v.derecho, elemento);
      }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        // Aquí va su código.
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        Vertice q = vertice(vertice);
        if(q.izquierdo == null){
          return;
        }
        Vertice p = q.izquierdo;
        Vertice j = q.padre;
        if(j == null){
          p.padre = null;
          q.padre = p;
          if(p.derecho != null){
            Vertice s = p.derecho;
            s.padre = q;
            q.izquierdo = s;
          }else{
            q.izquierdo = null;
          }
          p.derecho = q;
          raiz = p;
        }else{
          if(j.izquierdo == q){
            j.izquierdo = p;
          }else{
            j.derecho = p;
          }
          p.padre = j;
          q.padre = p;
          if(p.derecho != null){
            Vertice z = p.derecho;
            z.padre = q;
            q.izquierdo = z;
          }else{
            q.izquierdo = null;
          }
          p.derecho = q;
        }
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        Vertice q = vertice(vertice);
        if(q.derecho == null){
          return;
        }
        Vertice p = q.derecho;
        Vertice j = q.padre;
        if(j == null){
          p.padre = null;
          q.padre = p;
          if(p.izquierdo != null){
            Vertice s = p.izquierdo;
            s.padre = q;
            q.derecho = s;
          }else{
            q.derecho = null;
          }
          p.izquierdo = q;
          raiz = p;
        }else{
          if(j.derecho == q){
            j.derecho = p;
          }else{
            j.izquierdo = p;
          }
          p.padre = j;
          q.padre = p;
          if(p.izquierdo != null){
            Vertice z = p.izquierdo;
            z.padre = q;
            q.derecho = z;
          }else{
            q.derecho = null;
          }
          p.izquierdo = q;
        }
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPreOrder(raiz, accion);
    }

    private void dfsPreOrder(Vertice v, AccionVerticeArbolBinario<T> accion){
      if(v != null){
        accion.actua(v);
        dfsPreOrder(v.izquierdo, accion);
        dfsPreOrder(v.derecho, accion);
      }
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsInOrder(raiz, accion);
    }

    private void dfsInOrder(Vertice v, AccionVerticeArbolBinario<T> accion){
      if(v != null){
        dfsInOrder(v.izquierdo, accion);
        accion.actua(v);
        dfsInOrder(v.derecho, accion);
      }
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPostOrder(raiz, accion);
    }

    private void dfsPostOrder(Vertice v ,AccionVerticeArbolBinario<T> accion){
      if(v != null){
        dfsPostOrder(v.izquierdo, accion);
        dfsPostOrder(v.derecho, accion);
        accion.actua(v);
      }
    }

    private void izquierdo(Vertice v, Pila<Vertice> p){
      if(v == null)
        return;
      p.mete(v);
      izquierdo(v.izquierdo, p);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
