package ar.edu.unlu.blackjack;

import java.util.List;

public class Crupier extends Jugador{
    private static final int LimiteCrupier = 17;
    private Mano mano;

    public Crupier() {
        super("Crupier", 0);
        mano = new Mano();
    }

    public List<Carta> getMano(){
        return mano.getMano();
    }
    public void pedirCarta(){
        mano.recibirCarta(getMazo().repartirCarta());
    }
    public boolean tieneAsPrimera(){
        if (getMano().getFirst().getValor().equals("A")) return true;
        return false;
    }

    // Metodo para saber si debe pedir otra carta
    public boolean debePedirCarta(){
        return mano.getPuntaje() < LimiteCrupier;
    }
    public int getPuntaje(){
        return mano.getPuntaje();
    }

    public void mostrarPrimeraCarta(){
        // Valido primero
        if (mano.getMano().isEmpty()){
            System.out.println("No se puede mostrar el carta de crupier dado que no tiene ninguna.");
        }else{
            // System.out.println("La primera carta del crupier es: ");
            System.out.printf("La primera carta del crupier es: %s de %s\n", mano.getMano().get(0).getValor(), mano.getMano().get(0).getPalo());
            // Imprime la primera carta del crupier siendo que la segunda está oculta.
        }
    }
    public void mostrarMano(){
        System.out.println();
        System.out.println("El crupier tiene:");
        for (Carta carta : mano.getMano()) {
            System.out.printf("%s de %s\n", carta.getValor(), carta.getPalo());
        }
        System.out.println("El puntaje actual del crupier es de: " + mano.getPuntaje());
    }

}
