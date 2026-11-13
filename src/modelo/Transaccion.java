package modelo;

public class Transaccion {

    private final int numero;
    private final String fecha;
    private final String tipo;
    private final double valor;

    public Transaccion(int numero, String fecha, String tipo, double valor) {
        this.numero = numero;
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;
    }

    public int getNumero() {
        return numero;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return String.format("#%d | %s | %-13s | $%,.2f", numero, fecha, tipo, valor);
    }
}