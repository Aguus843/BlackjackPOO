package ar.edu.unlu.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private List<Carta> mano;
    private int puntaje;
    private Saldo saldo;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<Carta>();
        this.puntaje = 0;
        // this.saldo = new Saldo(saldoInicial);
    }
    public int getSaldo(){
        return saldo.getSaldo();
    }
    public void ajustarSaldo(int monto){
        if (monto > 0){
            saldo.agregarSaldo(monto);
        }else{
            saldo.retirarSaldo(-monto);
        }
    }
    public void mostrarSaldo(){
        System.out.println("El jugador " + nombre + "tiene un saldo de: " + saldo.getSaldo());
    }
    public String getNombre() {
        return this.nombre;
    }

    // Metodo que actualiza el puntaje del jugador al recibir una carta
    public void recibirCarta(Carta carta){
        this.mano.add(carta);
        actualizarPuntaje();
    }
    // Getter para saber el puntaje

    public int getPuntaje() {
        return puntaje;
    }
    // Actualizo el puntaje de la mano
    private void actualizarPuntaje() {
        puntaje = 0;
        int cantidadDeAs = 0;

        // calculo el puntaje inicial (las dos cartas recibidas)
        // Recorro el array de la mano para saber qué cartas salieron
        for (Carta carta : mano) {
            puntaje += carta.getValorNumerico();
            // Si la carta que tiene es un As aumento el contador en 1
            if (carta.getValor().equals("A")){
                cantidadDeAs++;
            }
        }

        //Si el jugador cuenta con cartas As y tiene +11, se lo cuenta como 1 en vez de 11
        while (puntaje > 21 && cantidadDeAs > 0){
            puntaje -= 10;
            cantidadDeAs--;
        }
    }
    // Metodo para saber si se paso de 21
    public boolean sePaso21(){
        return puntaje > 21;
    }

    // Metodo para nueva mano
    public void nuevaMano(){
        mano.clear();
        puntaje = 0; // Reinicio puntaje y limpio la mano
    }

    // Metodo para mostrar la mano
    public void mostrarMano(){
        System.out.println(this.nombre + " tiene las siguientes cartas:");
        for (Carta carta : mano) {
            System.out.printf("%s de %s\n", carta.getValor(), carta.getPalo());
            // System.out.println(carta.getValor());
            // System.out.println(carta.getPalo());
        }
        System.out.println("El puntaje actual es de: " + this.puntaje);
        System.out.println("===========================================");
    }

    public List<Carta> getMano(){
        return mano;
    }
}
