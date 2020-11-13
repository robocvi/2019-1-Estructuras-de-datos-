package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
            this.cola = new Cola<Vertice>();
            if(raiz != null){
              cola.mete(raiz);
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            if(this.cola.esVacia())
              return false;
            return true;

        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
            if(!hasNext()){
              this.cola.saca();
            }
            Vertice v = this.cola.saca();
            if(v.hayIzquierdo()){
              this.cola.mete(v.izquierdo);
            }
            if(v.hayDerecho()){
              this.cola.mete(v.derecho);
            }
            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null){
          throw new IllegalArgumentException();
        }
        Vertice v = nuevoVertice(elemento);
        elementos++;
        if(esVacia()){
          this.raiz = v;
        }else{
          Cola<Vertice> c = new Cola<Vertice>();
          c.mete(raiz);
          while(!c.esVacia()){
            Vertice a = c.saca();
            if(a.hayIzquierdo()){
              c.mete(a.izquierdo);
            } else{
              a.izquierdo = v;
              v.padre = a;
              return;
            }
            if(a.hayDerecho()){
              c.mete(a.derecho);
            }else{
              a.derecho = v;
              v.padre = a;
              return;
            }
          }
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeArbolBinario<T> a = busca(elemento);
        if(a == null)
          return;
        Vertice d = vertice(a);
        elementos--;
        if(elementos == 0){
          this.raiz = null;
        }else{
          Cola<Vertice> c = new Cola<>();
          c.mete(this.raiz);
          int i = 1;
          while(i <= elementos){
            Vertice b = c.saca();
            if(b.hayIzquierdo()){
              c.mete(b.izquierdo);
            }
            if(b.hayDerecho()){
              c.mete(b.derecho);
            }
            i++;
          }
          Vertice e = c.saca();
          T elem = d.elemento;
          d.elemento = e.elemento;
          e.elemento = elem;
          eliminaUlt(e);
        }
    }

    private void eliminaUlt(Vertice v){
      Vertice a = v.padre;
      if(v == a.izquierdo){
        a.izquierdo = null;
      }
      a.derecho = null;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
        if(esVacia())
          return -1;
        return (int) (Math.floor(Math.log(elementos)/Math.log(2)));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        if(this.esVacia()){
          return;
        }
        Cola<Vertice> c = new Cola<Vertice>();
        c.mete(raiz);
        while(!c.esVacia()){
          Vertice v = c.saca();
          accion.actua(v);
          if(v.hayIzquierdo()){
            c.mete(v.izquierdo);
          }
          if(v.hayDerecho()){
            c.mete(v.derecho);
          }
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
