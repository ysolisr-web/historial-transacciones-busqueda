package logica;

import modelo.Transaccion;

public class GestorTransacciones {

    public static class ResultadoBusqueda {
        private final int indice;
        private final int comparaciones;
        private final boolean ordenado;

        public ResultadoBusqueda(int indice, int comparaciones, boolean ordenado) {
            this.indice = indice;
            this.comparaciones = comparaciones;
            this.ordenado = ordenado;
        }

        public int getIndice() {
            return indice;
        }

        public int getComparaciones() {
            return comparaciones;
        }

        public boolean isOrdenado() {
            return ordenado;
        }

        public boolean fueEncontrado() {
            return indice >= 0;
        }

        public int getPuntoInsercion() {
            return (indice >= 0) ? indice : -(indice + 1);
        }
    }

    /**
     * Búsqueda lineal por número de transacción (funciona con datos desordenados).
     */
    public static ResultadoBusqueda busquedaLinealPorNumero(Transaccion[] historial, int numeroBuscado) {
        if (historial == null || historial.length == 0) {
            return new ResultadoBusqueda(-1, 0, false);
        }
        int comparaciones = 0;
        for (int i = 0; i < historial.length; i++) {
            comparaciones++;
            if (historial[i].getNumero() == numeroBuscado) {
                return new ResultadoBusqueda(i, comparaciones, false);
            }
        }
        return new ResultadoBusqueda(-1, comparaciones, false);
    }

    /**
     * Búsqueda secuencial de todas las transacciones pertenecientes a un mismo tipo.
     */
    public static Transaccion[] buscarPorTipo(Transaccion[] historial, String tipoBuscado) {
        if (historial == null || historial.length == 0 || tipoBuscado == null) {
            return new Transaccion[0];
        }
        int coincidencias = 0;
        for (Transaccion t : historial) {
            if (t.getTipo().equalsIgnoreCase(tipoBuscado.trim())) {
                coincidencias++;
            }
        }

        Transaccion[] resultado = new Transaccion[coincidencias];
        int k = 0;
        for (Transaccion t : historial) {
            if (t.getTipo().equalsIgnoreCase(tipoBuscado.trim())) {
                resultado[k++] = t;
            }
        }
        return resultado;
    }

    /**
     * Ordenamiento manual ascendente por número de transacción (Insertion Sort).
     */
    public static void ordenarPorNumeroManual(Transaccion[] historial) {
        if (historial == null || historial.length < 2) {
            return;
        }
        for (int i = 1; i < historial.length; i++) {
            Transaccion actual = historial[i];
            int j = i - 1;
            while (j >= 0 && historial[j].getNumero() > actual.getNumero()) {
                historial[j + 1] = historial[j];
                j--;
            }
            historial[j + 1] = actual;
        }
    }

    /**
     * Verifica si el arreglo está ordenado ascendentemente por número de transacción.
     */
    public static boolean estaOrdenadoPorNumero(Transaccion[] historial) {
        if (historial == null || historial.length < 2) {
            return true;
        }
        for (int i = 1; i < historial.length; i++) {
            if (historial[i - 1].getNumero() > historial[i].getNumero()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Búsqueda binaria por número sobre un arreglo previamente ordenado.
     * Si no existe, retorna -(puntoInsercion) - 1.
     */
    public static ResultadoBusqueda busquedaBinariaPorNumero(Transaccion[] historial, int numeroBuscado) {
        if (historial == null || historial.length == 0) {
            return new ResultadoBusqueda(-1, 0, true);
        }
        int lo = 0;
        int hi = historial.length - 1;
        int comparaciones = 0;

        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1);
            comparaciones++;
            int numMedio = historial[mid].getNumero();

            if (numMedio == numeroBuscado) {
                return new ResultadoBusqueda(mid, comparaciones, true);
            } else if (numMedio < numeroBuscado) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        // No encontrado: lo representa la posición exacta de inserción
        return new ResultadoBusqueda(-(lo) - 1, comparaciones, true);
    }

    /**
     * Inserta una nueva transacción en el punto de inserción manteniendo el orden.
     */
    public static Transaccion[] insertarEnPosicion(Transaccion[] historial, Transaccion nueva, int posicion) {
        Transaccion[] nuevoArreglo = new Transaccion[historial.length + 1];
        System.arraycopy(historial, 0, nuevoArreglo, 0, posicion);
        nuevoArreglo[posicion] = nueva;
        System.arraycopy(historial, posicion, nuevoArreglo, posicion + 1, historial.length - posicion);
        return nuevoArreglo;
    }

    /**
     * Genera un conjunto inicial de transacciones desordenadas para pruebas.
     */
    public static Transaccion[] generarHistorialInicial() {
        return new Transaccion[]{
            new Transaccion(1045, "2026-11-01", "Depósito", 1500000.00),
            new Transaccion(1012, "2026-11-02", "Retiro", 320000.00),
            new Transaccion(1089, "2026-11-03", "Transferencia", 850000.00),
            new Transaccion(1024, "2026-11-04", "Pago Servicio", 145000.00),
            new Transaccion(1060, "2026-11-05", "Depósito", 2400000.00),
            new Transaccion(1005, "2026-11-06", "Transferencia", 610000.00),
            new Transaccion(1073, "2026-11-07", "Retiro", 200000.00),
            new Transaccion(1038, "2026-11-08", "Depósito", 980000.00),
            new Transaccion(1099, "2026-11-09", "Pago Servicio", 310000.00),
            new Transaccion(1051, "2026-11-10", "Transferencia", 1250000.00)
        };
    }
}
