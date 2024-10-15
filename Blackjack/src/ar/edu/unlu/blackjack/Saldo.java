package ar.edu.unlu.blackjack;

public class Saldo {
    private int cantidad;

    public Saldo(int cantidadInicial) {
        this.cantidad = cantidadInicial;
    }

    // Getter para obtener el saldo
    public int getSaldo(){
        return this.cantidad;
    }

    public void mostrarSaldo(){
        System.out.println("Saldo disponible: " + this.cantidad);
    }

    public void agregarSaldo(int monto){
        if (monto > 0){
            this.cantidad += monto; // Valido que el monto pasado por parámetro sea mayor que cero
        }
    }

    public boolean retirarSaldo(int monto){ // Lo hago boolean para que el jugador pueda apostar con este saldo
        if (monto > 0 && this.cantidad > 0){
            this.cantidad -= monto;
            // System.out.println("Se le retiró " + monto + (this.cantidad == 0 ? "" : " de " + this.cantidad + monto));
            return true;
        }else{
            System.out.println("Saldo insuficiente.");
            return false;
        }
    }
}
