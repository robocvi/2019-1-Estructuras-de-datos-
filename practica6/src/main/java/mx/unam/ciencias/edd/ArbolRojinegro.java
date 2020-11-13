package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            super(elemento);
            this.color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            // Aquí va su código.
            if(esRojo(this))
              return "R{" + String.valueOf(elemento) + "}";
            if(esNegro(this))
              return "N{" + String.valueOf(elemento) + "}";
            return "";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
            return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeArbolBinario<T> v = super.getUltimoVerticeAgregado();
        VerticeRojinegro vrn = (VerticeRojinegro)v;
        vrn.color = Color.ROJO;
        this.rebalanceo(vrn);
    }

    private void rebalanceo(VerticeRojinegro v){
      //CASO 1:
      if(!v.hayPadre()){
        v.color = Color.NEGRO;
        return;
      }

      //CASO 2:
      VerticeRojinegro p = (VerticeRojinegro)v.padre;
      if(esNegro(p)) {return; }

      //CASO 3:
      VerticeRojinegro a = (VerticeRojinegro)p.padre;
      VerticeRojinegro t = null;
      if(p == a.izquierdo){
        t = (VerticeRojinegro)a.derecho;
      }else{
        t = (VerticeRojinegro)a.izquierdo;
      }
      if(esRojo(t)){
        t.color = Color.NEGRO;
        p.color = Color.NEGRO;
        a.color = Color.ROJO;
        rebalanceo(a);
      }else{
        //CASO 4:
        if(a.izquierdo == p && p.derecho == v){
          super.giraIzquierda(p);
          VerticeRojinegro s = p;
          p = v;
          v = s;
        }else
        if (a.derecho == p && p.izquierdo == v){
          super.giraDerecha(p);
          VerticeRojinegro s = p;
          p = v;
          v = s;
        }

        p.color = Color.NEGRO;
        a.color = Color.ROJO;
        if(v == p.izquierdo){
          super.giraDerecha(a);
        }else{
          super.giraIzquierda(a);
        }
      }
    }

    private boolean esNegro(VerticeRojinegro v){
      return (v == null || v.color == Color.NEGRO || v == this.raiz);
    }

    private boolean esRojo(VerticeRojinegro v){
      return (v != null && v.color == Color.ROJO && v != this.raiz);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeArbolBinario<T> a = busca(elemento);
        if(a == null)
          return;
        VerticeRojinegro v = (VerticeRojinegro)a;
        if(v.izquierdo != null && v.derecho != null){
          v = (VerticeRojinegro)intercambiaEliminable(v);
        }
        VerticeRojinegro fantasma = null;
        if(v.izquierdo == null && v.derecho == null){
          fantasma = (VerticeRojinegro)nuevoVertice(null);
            fantasma.color = Color.NEGRO;
            fantasma.padre = v;
            v.izquierdo = fantasma;
        }
        VerticeRojinegro h = getHijo(v);
        eliminaVertice(v);
        if(esNegro(v) && esNegro(h)){
          h.color = Color.NEGRO;
          rebalanceoElim(h);
        }else{
          h.color = Color.NEGRO;
        }

        if(fantasma != null){
          if(fantasma == raiz){
            raiz = fantasma = null;
          }else{
            if(esHijoIzquierdo(fantasma)){
              fantasma.padre.izquierdo = null;
            }else{
              fantasma.padre.derecho = null;
            }
          }
        }
        elementos--;
    }

    private VerticeRojinegro getHijo(VerticeRojinegro v){
      if(v.hayIzquierdo())
        return (VerticeRojinegro)v.izquierdo;
      return (VerticeRojinegro)v.derecho;
    }

    private void rebalanceoElim(VerticeRojinegro v){
        //CASO 1:
        if(v.padre == null){
          v.color = Color.NEGRO;
          raiz = v;
          return;
        }

        //CASO 2:
        VerticeRojinegro p = (VerticeRojinegro)v.padre;
        VerticeRojinegro h = verticeHermano(v);
        if(esRojo(h)){
          p.color = Color.ROJO;
          h.color = Color.NEGRO;
          if(p.izquierdo == v){
            super.giraIzquierda(p);
          }else{
            super.giraDerecha(p);
          }
          p = (VerticeRojinegro)v.padre;
          h = verticeHermano(v);
        }

        //CASO 3:
        VerticeRojinegro hizq = (VerticeRojinegro)h.izquierdo;
        VerticeRojinegro hder = (VerticeRojinegro)h.derecho;
        if(esNegro(p) && esNegro(h) && esNegro(hizq) && esNegro(hder)){
          h.color = Color.ROJO;
          rebalanceoElim(p);
          return;
        }

        //CASO 4:
        if(esNegro(h) && esNegro(hizq) && esNegro(hder) && esRojo(p)){
          h.color = Color.ROJO;
          p.color = Color.NEGRO;
          return;
        }

        //CASO 5:
        if(((p.izquierdo == v) && esRojo(hizq) && esNegro(hder))
            || ((p.derecho == v) && esNegro(hizq) && esRojo(hder))) {

          h.color = Color.ROJO;
          if(esRojo(hizq)){
            hizq.color = Color.NEGRO;
          }else{
            hder.color = Color.NEGRO;
          }
          if(p.izquierdo == v){
            super.giraDerecha(h);
          }else{
            super.giraIzquierda(h);
          }
          h = verticeHermano(v);
          hizq = (VerticeRojinegro)h.izquierdo;
          hder = (VerticeRojinegro)h.derecho;
        }

        //CASO 6:
        h.color = getColor(p);
        p.color = Color.NEGRO;
        if(p.izquierdo == v){
          hder.color = Color.NEGRO;
          super.giraIzquierda(p);
        }else{
          hizq.color = Color.NEGRO;
          super.giraDerecha(p);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

    private boolean esHijoIzquierdo(VerticeRojinegro v){
      if(v.padre == null)
        return false;
      if(v.padre.izquierdo == v)
        return true;
      return false;
    }

    private boolean esHijoDerecho(VerticeRojinegro v){
      if(v.padre == null)
        return false;
      if(v.padre.derecho == v)
        return true;
      return false;
    }

    private VerticeRojinegro verticeHermano(VerticeRojinegro v){
      if(v.padre.izquierdo == v)
        return (VerticeRojinegro)v.padre.derecho;
      return (VerticeRojinegro)v.padre.izquierdo;
    }
}
