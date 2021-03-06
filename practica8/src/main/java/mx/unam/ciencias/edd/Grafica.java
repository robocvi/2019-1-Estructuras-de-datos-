package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
            return this.vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            // Aquí va su código.
            return this.color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return this.vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            // Aquí va su código.
            return this.indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
            if (distancia > vertice.distancia)
                return 1;
            else if (distancia < vertice.distancia)
                return -1;
            return 0;
        }

    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
            return this.vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
            return this.vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            // Aquí va su código.
            return this.vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
        this.vertices = new Lista<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null || contiene(elemento))
       throw new IllegalArgumentException();
        Vertice v = new Vertice(elemento);
        vertices.agrega(v);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a,b,1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
        if(!contiene(a) || !contiene(b))
        throw new NoSuchElementException();
    if(a.equals(b) || sonVecinos(a,b) || peso <= 0)
       throw new IllegalArgumentException();
        Vertice v = busca(a), e = busca(b);
        v.vecinos.agrega(new Vecino(e, peso));
        e.vecinos.agrega(new Vecino(v, peso));
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        if (a.equals(b) || !sonVecinos(a, b))
            throw new IllegalArgumentException("a o b no están conectados.");
        Vecino ve_ab = null, ve_ba = null;
        for (Vecino ve : va.vecinos) {
            if (ve.vecino.equals(vb)) {
                ve_ab = ve;
            }
        }
        for (Vecino ve : vb.vecinos) {
            if (ve.vecino.equals(va)) {
                ve_ba = ve;
            }
        }
        va.vecinos.elimina(ve_ab);
        vb.vecinos.elimina(ve_ba);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return busca(elemento) !=null;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
         if(!contiene(elemento))
            throw new NoSuchElementException();
        Vertice v = busca(elemento);
        for(Vecino e : v.vecinos)
            desconecta(v.elemento, e.vecino.elemento);
        vertices.elimina(v);
    }


    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
        if (!this.contiene(a) || !this.contiene(b))
            throw new NoSuchElementException("a o b no son elementos de la gráfica");
        Vertice va = (Vertice)vertice(a);
        Vertice vb = (Vertice)vertice(b);
        for (Vecino ve : va.vecinos)
            if (ve.vecino.equals(vb))
                return true;
        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.
        if(!contiene(a) || !contiene(b))
       throw new NoSuchElementException();
        if(!sonVecinos(a,b))
       throw new IllegalArgumentException();
        Vertice v = busca(a);
        Vertice e = busca(b);
        return buscaVecino(v,e).peso;
    }

    private Vertice busca(T elemento){
        for(Vertice v : vertices)
            if(v.elemento.equals(elemento))
                return v;
        return null;
     }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.
        if(!contiene(a) || !contiene(b))
       throw new NoSuchElementException();
       if(!sonVecinos(a,b) || peso <= 0)
       throw new IllegalArgumentException();
       Vertice v = busca(a), e = busca(b);
       Vecino c = buscaVecino(v,e), i = buscaVecino(e,v);
       c.peso = peso;
       i.peso = peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
        if(!contiene(elemento))
       throw new NoSuchElementException();
       return busca(elemento);
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        // Aquí va su código.
        if(vertice == null || (vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class)){
          throw new IllegalArgumentException();
        }
        if(vertice.getClass() == Vertice.class){
          Vertice v = (Vertice)vertice;
          v.color = color;
        }
        if(vertice.getClass() == Vecino.class){
          Vecino v = (Vecino)vertice;
          v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
        Vertice v = vertices.getPrimero();
        Lista<Vertice> l = new Lista<>();
        bfs(v.elemento, a -> l.agrega((Vertice)a));
        return l.getLongitud() == vertices.getLongitud();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        for(Vertice v : vertices){
          accion.actua(v);
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        if(!contiene(elemento))
           throw new NoSuchElementException();
        Cola<Vertice> c = new Cola<>();
        recorre(elemento, accion, c);

    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        if(!contiene(elemento))
       throw new NoSuchElementException();
        Pila<Vertice> p = new Pila<>();
        recorre(elemento, accion, p);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        if(vertices.esVacia())
          return true;
        return false;
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        for(Vertice v : vertices)
          vertices.elimina(v);
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        String s = "{";
        for(Vertice a : this.vertices)
          s += String.valueOf(a.elemento) + ", ";
        s += "}, {";
        for(Vertice b : this.vertices){
          for(Vecino c : b.vecinos){
            if(c.vecino.color != Color.NEGRO){
              s += "("+String.valueOf(b.elemento)+", "
                      +String.valueOf(c.vecino.elemento)+"), ";
            }
          }
          b.color = Color.NEGRO;
        }
        paraCadaVertice(vertice -> setColor(vertice, Color.NINGUNO));
        s += "}";
        return s;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
        if ((vertices.getLongitud() != grafica.vertices.getLongitud()) || (aristas != grafica.aristas) || (equalVertice(vertices, grafica.vertices)))
       return false;
   for (Vertice v : vertices)
       for (Vertice e : vertices)
       if(v.elemento != e.elemento && sonVecinos(v.elemento, e.elemento) && !grafica.sonVecinos(v.elemento, e.elemento))
           return false;
        return true;
    }

    private boolean equalVertice(Lista<Vertice> x, Lista<Vertice> y){
        for (Vertice v : x)
       if(!y.contiene(v))
       return false;
      return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        if (!contiene(origen) || !contiene(destino))
       throw new NoSuchElementException();
   Lista<VerticeGrafica<T>> tM = new Lista<>();
   if (origen.equals(destino)){
       tM.agrega(busca(origen));
       return tM;
   }
   for (Vertice v : vertices)
       v.distancia = Double.MAX_VALUE;
   Cola<Vertice> c = new Cola<>();
   Vertice s = busca(origen), t = busca(destino);
   s.distancia = 0;
   c.mete(s);
   while(!c.esVacia()){
       Vertice n = c.saca();
       for (Vecino m : n.vecinos){
       Vertice l = m.vecino;
       if (l.distancia == Double.MAX_VALUE){
           l.distancia = n.distancia + 1;
           c.mete(l);
       }
       }
   }
   if (t.distancia == Double.MAX_VALUE)
       return tM;
   return reconstruir((a,b) -> (b.vecino.distancia == a.distancia-1) ? true : false, s, t);
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        Vertice s = busca(origen), t = busca(destino);
   if (!contiene(origen) || !contiene(destino))
       throw new NoSuchElementException();
   Lista<VerticeGrafica<T>> tM = new Lista<>();
   if (origen.equals(destino)){
       tM.agrega(s);
       return tM;
   }
   for (Vertice v : vertices)
       v.distancia = Double.MAX_VALUE;
   s.distancia = 0;
   MonticuloMinimo<Vertice> m = new MonticuloMinimo<Vertice>(vertices);
   while(!m.esVacia()){
       Vertice u = m.elimina();
       for (Vecino j : u.vecinos){
       Vertice v = j.vecino;
       if (v.distancia > u.distancia+j.peso){
           v.distancia = u.distancia+j.peso;
           m.reordena(v);
       }
       }
   }
   if (t.distancia == Double.MAX_VALUE)
       return tM;
   Vertice v = t;
   return reconstruir((a,b) -> (a.distancia-b.peso == b.vecino.distancia) ? true : false, s, t);

    }

    private void preparaVertices(Vertice vo) {
        for (Vertice v : vertices)
            v.distancia = Double.POSITIVE_INFINITY;
        vo.distancia = 0;
    }

    private void creaMonticuloYCalculaDistancias(boolean esDijkstra) {
        MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<>(vertices);
        while (!monticulo.esVacia()) {
            Vertice vAux = monticulo.elimina();
            //ve.peso es la aristas abajo.
            for (Vecino ve : vAux.vecinos) {
                if (ve.vecino.distancia == Double.POSITIVE_INFINITY ||
                        vAux.distancia + ve.peso < ve.vecino.distancia) {
                    ve.vecino.distancia = vAux.distancia + (esDijkstra ? ve.peso : 1);
                    monticulo.reordena(ve.vecino);
                }
            }
        }
    }

    private Lista<VerticeGrafica<T>> reversaTrayectoria(Vertice vo, Vertice vd, boolean esDijkstra) {
        Lista<VerticeGrafica<T>> l = new Lista<>();
        if (vd.distancia != Double.POSITIVE_INFINITY) {
            Vertice vAux = vd;
            while (vAux != vo) {
                for (Vecino ve : vAux.vecinos) {
                    if (vAux.distancia - (esDijkstra ? ve.peso : 1) == ve.vecino.distancia) {
                        l.agregaInicio(vAux);
                        vAux = ve.vecino;
                        break;
                    }
                }
                if (vAux == vo) {
                    l.agregaInicio(vo);
                }
            }
        }
        return l;
    }



     private Vecino buscaVecino(Vertice v, Vertice b){
        for(Vecino c : v.vecinos)
       if(c.vecino.elemento.equals(b.elemento))
       return c;
       return null;
     }

     private void recorre(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> ms) {
        Vertice v = busca(elemento), e;
        for(Vertice n : vertices)
       n.color = Color.ROJO;
        ms.mete(v);
        v.color = Color.NEGRO;
        while(!ms.esVacia()) {
            e = ms.saca();
            accion.actua(e);
            for (Vecino j: e.vecinos)
                if(j.vecino.color == Color.ROJO) {
                    ms.mete(j.vecino);
                    j.vecino.color = Color.NEGRO;
                }
        }
        for(Vertice x : vertices)
       x.color = Color.NINGUNO;
    }

    private Lista<VerticeGrafica<T>> reconstruir(BuscadorCamino bC, Vertice o, Vertice d) {
   Lista<VerticeGrafica<T>> tM = new Lista<>();
   Vertice v = d;
   tM.agrega(v);
   while (v != o)
       for (Vecino e : v.vecinos)
       if (bC.seSiguen(v,e)){
           v = e.vecino;
           tM.agrega(v);
           break;
       }
        return tM.reversa();
    }
}
