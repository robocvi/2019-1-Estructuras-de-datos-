package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase para montículos de Dijkstra con arreglos.
 */
public class MonticuloArreglo<T extends ComparableIndexable<T>>
    implements MonticuloDijkstra<T> {

    /* Número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arreglo;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor para montículo de Dijkstra con un arreglo a partir de una
     * colección.
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloArreglo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Construye un nuevo para montículo de Dijkstra con arreglo a partir de un
     * iterable.
     * @param iterable el iterable a partir de la cual construir el montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloArreglo(Iterable<T> iterable, int n) {
       elementos=n;
   arreglo = nuevoArreglo(elementos);
        int i = 0;
        for (T x : iterable){
            arreglo[i] = x;
            arreglo[i].setIndice(i);
            i++;
        }
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        /*if (elementos == 0){
            throw new IllegalStateException("Montículo vacío");
        }
        int i = 0;
        boolean c = false;
        for(T e : arreglo){
          i++;
        }
        intercambia(0, elementos-1);
        elementos--;
        acomodandoAbajo(0);
        arreglo[elementos].setIndice(-1);
        return arreglo[elementos];
        */
        if (elementos == 0)
            throw new IllegalStateException();
        T m = arreglo[0];
        for (int i = 0 ; i < arreglo.length ; i++) {
            if (m == null)
                m = arreglo[i];
            if (m != null && arreglo[i] != null)
                if (m.compareTo(arreglo[i]) > 0)
                    m = arreglo[i];
        }
        arreglo[m.getIndice()] = null;
        m.setIndice(-1);
        elementos--;
        return m;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del arreglo.
     * @param i el índice del elemento que queremos.
     * @return el <i>i</i>-ésimo elemento del arreglo.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= elementos){
            throw new NoSuchElementException("Índice fuera de rango");
        }
        return arreglo[i];
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
     * Regresa el número de elementos en el montículo.
     * @return el número de elementos en el montículo.
     */
    @Override public int getElementos() {
        return elementos;
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
        T temporal = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[i].setIndice(i);
        arreglo[j] = temporal;
        arreglo[j].setIndice(j);
    }


    private int menor(int izq, int der){
        int resultado = comparador(izq,der);
        return resultado <= 0 ? izq : der;
    }


    private int comparador(int a, int b){
        return arreglo[a].compareTo(arreglo[b]);
     }
}
