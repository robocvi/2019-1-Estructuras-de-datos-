package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
          if(indice < elementos)
            return true;
          return false;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (!hasNext()){
                throw new NoSuchElementException("No hay elemento");
            }
            T elemento = arbol[indice];
            indice++;
            return elemento;
         }

    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return(elemento.compareTo(adaptador.elemento));
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        //codigo
        this.arbol = nuevoArreglo(n);
        int i = 0;
        for(T e : iterable){
          arbol[i] = e;
          arbol[i].setIndice(i);
          i++;
        }
        this.elementos = arbol.length;
        for(int j = ((int)(Math.floor(n/2))-1) ; 0 <= j ; j--){
          heapifyDown(j);
        }
    }


    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        /*if(elementos == arbol.length){
          T[] a = nuevoArreglo((arbol.length*2));
          for(int i = 0 ; i < arbol.length ; i++){
            a[i] = arbol[i];
            a[i].setIndice(i);
          }
          arbol = a;
        }
        arbol[elementos] = elemento;
        arbol[elementos].setIndice(elementos);
        heapifyUp(elementos);
        elementos++;*/
        if(elemento == null){
            throw new IllegalArgumentException("No admite null");
        }
        if (elementos == arbol.length){
            T[] nuevo = nuevoArreglo(elementos*2);
            for(int i = 0; i<elementos; i++){
                nuevo[i] = arbol[i];
                nuevo[i].setIndice(i);
            }
            arbol = nuevo;
        }
        arbol[elementos] = elemento;
        arbol[elementos].setIndice(elementos);
        acomodandoArriba(elementos);
        elementos++;
    }

    private void heapifyUp(int i){
        int p = (i-1)/2;
        int m = i;
        if(p > 0 && arbol[p].compareTo(arbol[i]) > 0)
          m = p;

        if (m != i) {
          T aux = arbol[i];

        arbol[i] = arbol[p];
        arbol[i].setIndice(i);

        arbol[p] = aux;
        arbol[p].setIndice(p);

        heapifyUp(m);
      }
    }

    private void heapifyDown(int i){
        int izq = i * 2 + 1;
        int der = i * 2 + 2;

        if (izq >= elementos && der >= elementos)
            return;

        int menor = getMenor(izq, der);
        menor = getMenor(i, menor);

        if (menor != i) {
            T aux = arbol[i];

            arbol[i] = arbol[menor];
            arbol[i].setIndice(i);

            arbol[menor] = aux;
            arbol[menor].setIndice(menor);

            heapifyDown(menor);
          }
    }

    private int getMenor(int a, int b) {
        if (b >= elementos)
            return a;
        else if (arbol[a].compareTo(arbol[b]) < 0)
            return a;
        else
            return b;
    }


    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
      if(elementos == 0)
        throw new IllegalStateException();
      T e = arbol[0];
       arbol[0] = arbol[elementos-1];
       arbol[0].setIndice(0);
       arbol[elementos-1] = e;
       arbol[elementos-1].setIndice(-1);
       elementos--;
       heapifyDown(0);
       return e;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int indice = elemento.getIndice();
        if (indice < 0 || indice >= elementos){
            return;
        }
        intercambia(indice, elementos-1);
        elementos--;
        reordena(arbol[indice]);
        arbol[elementos].setIndice(-1);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        int indice = elemento.getIndice();
        if(indice >= elementos || indice < 0){
            return false;
        }
        return true;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
         return (elementos == 0) ? true : false;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        for (int i = 0; i < elementos; i++){
            arbol[i] = null;
        }
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        int indice = elemento.getIndice();
        int padre = (indice-1)/2;
        if (comparador(indice,padre)<0){
            acomodandoArriba(indice);
        } else{
            acomodandoAbajo(indice);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        /*return elementos;*/
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= elementos){
            throw new NoSuchElementException("Índice fuera de rango");
        }
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
         String s = "";
        for (int i = 0; i < elementos; i++) {
            s += arbol[i].toString()+", ";
        }
        return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if (elementos != monticulo.elementos){
            return false;
        }
        for(int i = 0; i < elementos; i++){
            if (!(arbol[i].compareTo(monticulo.arbol[i])==0)){
                return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> adaptadores = new Lista<Adaptador<T>>();
        for(T x : coleccion){
            Adaptador<T> i = new Adaptador<T>(x);
            adaptadores.agrega(i);
        }
        Lista<T> lista = new Lista<T>();
        MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<Adaptador<T>>(adaptadores);
        while(!monticulo.esVacia()){
            T elemento = monticulo.elimina().elemento;
            lista.agrega(elemento);
        }
        return lista;
    }





    private void acomodandoArriba(int indice){
        if (indice == 0){
            return;
        }
        int i = (indice-1) /2;
        if (comparador(indice, i) < 0){
            intercambia(i, indice);
            acomodandoArriba(i);
        }
    }


    private void acomodandoAbajo(int indice){
        int izq = indice*2+1;
        int der = indice*2+2;
        if (indice < 0 || indice >= elementos-1){
            return;
        }
        if (izq >= elementos){
            return;
        }
        if (izq == elementos-1){
            if (comparador(indice,izq)>=0){
            intercambia(indice,izq);
            }
            return;
        }
        int hijoMenor = menor(izq,der);
        if (comparador(indice, hijoMenor)>=0){
            intercambia(indice, hijoMenor);
            acomodandoAbajo(hijoMenor);
        }
    }


    private void intercambia(int i, int j){
        T temporal = arbol[i];
        arbol[i] = arbol[j];
        arbol[i].setIndice(i);
        arbol[j] = temporal;
        arbol[j].setIndice(j);
    }


    private int menor(int izq, int der){
        int resultado = comparador(izq,der);
        return resultado <= 0 ? izq : der;
    }


    private int comparador(int a, int b){
        return arbol[a].compareTo(arbol[b]);
     }

}
