package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        // Aquí va su código.
        int lon = llave.length;
        int r = 0;
        int i = 0;
        while (lon >= 4) {
          r = r ^ bigEndian(llave[i], llave[i+1], llave[i+2], llave[i+3]);
          i = i + 4;
          lon = lon - 4;
        }
        int n = 0;
        switch (lon) {
          case 3: n = n | ((llave[i + 2] & 0xFF) << 8);
          case 2: n = n | ((llave[i + 1] & 0xFF) << 16);
          case 1: n = n | ((llave[i] & 0xFF) << 24);
        }
        r = r ^ n;
        return r;
    }
    private static int bigEndian(byte a, byte b, byte c, byte d){
      return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8)
                                                     | ((d & 0xFF));
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        // Aquí va su código.
        int n = llave.length;
        int l = n;
        int a = 0x9e3779b9;
        int b = 0x9e3779b9;
        int c = 0xffffffff;
        int i = 0;
        while (l >= 12){
          a = a + littleEndian(llave[i], llave[i+1], llave[i+2], llave[i+3]);
          b = b + littleEndian(llave[i+4], llave[i+5], llave[i+6], llave[i+7]);
          c = c + littleEndian(llave[i+8], llave[i+9], llave[i+10], llave[i+11]);
          a -= b; a -= c; a ^= (c >>> 13);
          b -= c; b -= a; b ^= (a <<  8);
          c -= a; c -= b; c ^= (b >>> 13);
          a -= b; a -= c; a ^= (c >>> 12);
          b -= c; b -= a; b ^= (a <<  16);
          c -= a; c -= b; c ^= (b >>> 5);
          a -= b; a -= c; a ^= (c >>> 3);
          b -= c; b -= a; b ^= (a <<  10);
          c -= a; c -= b; c ^= (b >>> 15);
          i += 12;
          l -=12;
        }
        c += n;
        switch (l) {
          case 11: c += ((llave[i+10]& 0xFF) << 24);
          case 10: c += ((llave[i+9]& 0xFF)  << 16);
          case  9: c += ((llave[i+8]& 0xFF)  << 8);
          case  8: b += ((llave[i+7]& 0xFF)  << 24);
          case  7: b += ((llave[i+6]& 0xFF)  << 16);
          case  6: b += ((llave[i+5]& 0xFF)  << 8);
          case  5: b +=  (llave[i+4]& 0xFF);
          case  4: a += ((llave[i+3]& 0xFF)  << 24);
          case  3: a += ((llave[i+2]& 0xFF)  << 16);
          case  2: a += ((llave[i+1]& 0xFF)  << 8);
          case  1: a += (llave[i]& 0xFF);
        }

        a -= b; a -= c; a ^= (c >>> 13);
        b -= c; b -= a; b ^= (a <<  8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a <<  16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a <<  10);
        c -= a; c -= b; c ^= (b >>> 15);
        return c;
    }

    private static int littleEndian(byte a, byte b, byte c, byte d){
      return  ((a & 0xFF)) | ((b & 0xFF) << 8) | ((c & 0xFF) << 16)
                                               | ((d & 0xFF) << 24);
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        // Aquí va su código.
        int h = 5381;
        for (int i = 0; i < llave.length; i++) {
          h += (h << 5) + (llave[i] & 0x000000FF);
        }
        return h;
    }
}
