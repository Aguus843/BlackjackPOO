package ar.edu.unlu.blackjack;

public class Crupier extends Jugador{
    private static final int LimiteCrupier = 17;

    public Crupier() {
        super("Crupier");
    }

    // Metodo para saber si debe pedir otra carta
    public boolean debePedirCarta(){
        return getPuntaje() < LimiteCrupier;
    }

    public void mostrarPrimeraCarta(){
        // Valido primero
        if (getMano().isEmpty()){
            System.out.println("No se puede mostrar el carta de crupier dado que no tiene ninguna.");
        }else{
            // System.out.println("La primera carta del crupier es: ");
            System.out.printf("La primera carta del crupier es: %s de %s\n", getMano().get(0).getValor(), getMano().get(0).getPalo());
            // Imprime la primera carta del crupier siendo que la segunda está oculta.
        }
    }

    // Ahora 'sobreescribo' el metodo mostrarMano() para que muestre la del crupier
    @Override
    public void mostrarMano(){
        System.out.println("El crupier tiene:");
        for (Carta carta : getMano()) {
            System.out.printf("%s de %s\n", carta.getValor(), carta.getPalo());
            // System.out.println(carta.getValor());
            // System.out.println(carta.getPalo());
        }
        System.out.println("El puntaje actual del crupier es de: " + this.getPuntaje());
    }

}
