import ar.edu.unlu.blackjack.Baraja;
import ar.edu.unlu.blackjack.Crupier;
import ar.edu.unlu.blackjack.Jugador;

import java.util.Scanner;

public class BlackJack {
    private final Baraja baraja;
    private final Crupier crupier;
    private final Jugador jugador;
    private final Scanner scanner;
    private int cantidadJugadores;

    public BlackJack() {
        baraja = new Baraja();
        crupier = new Crupier();
        jugador = new Jugador("Agustin"); // CAMBIAR
        scanner = new Scanner(System.in);
    }

    public void iniciarJuego(){
        System.out.println("Bienvenido al Blackjack!");
        System.out.println("Ingrese un numero de jugadores: ");


        // Reparto las dos cartas al jugador y luego al crupier
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());

        // Muestro las cartas iniciales:
        jugador.mostrarMano();
        crupier.mostrarPrimeraCarta();

        turnoJugador();

        if (!jugador.sePaso21()){
            turnoCrupier();
        }

        evaluarGanador();

    }
    // Metodo donde controlo al jugador
    private void turnoJugador() {
        String ingreso;
        while (!jugador.sePaso21()){
            System.out.println("Ingrese 'c' para pedir o 'p' para plantarse: ");
            ingreso = scanner.nextLine();
            if (ingreso.equals("c")){
                jugador.recibirCarta(baraja.repartirCarta());
                jugador.mostrarMano();

                if (jugador.sePaso21()){
                    System.out.println("Se paso de los 21. Perdió el juego :(");
                    break;
                }
            }else if (ingreso.equals("p")){
                System.out.println("Usted se plantó.");
                break;
            }else {
                System.out.println("Lo que se ingresó no es válido.");
            }
        }
    }

    // Metodo donde controlo al crupier
    private void turnoCrupier() {
        System.out.println("Turno del crupier.");
        crupier.mostrarMano();

        // Obtiene una carta hasta obtener 17 o más.
        while (crupier.debePedirCarta()){
            System.out.println("El crupier obtiene una carta.");
            crupier.recibirCarta(baraja.repartirCarta());
            crupier.mostrarMano();
        }

        // Verifico si se pasó de 21
        if (crupier.sePaso21()){
            System.out.println("El crupier se paso de los 21.");
        }else{
            System.out.println("El crupier se planta en " + crupier.getPuntaje());
        }
    }

    private void evaluarGanador(){
        int puntajeJugador = jugador.getPuntaje();
        int puntajeCrupier = crupier.getPuntaje();
        System.out.println();
        System.out.println("========================================================");

        System.out.println("El puntaje final del jugador es: " + puntajeJugador);
        System.out.println("El puntaje final del crupier es: " + puntajeCrupier);

        System.out.println("========================================================");

        if (jugador.sePaso21()){
            System.out.println("El jugador se paso de las 21. Gana el crupier");
        }else if (crupier.sePaso21()){
            System.out.println("El crupier se paso de las 21. Gana el jugador");
        }else if (puntajeJugador > puntajeCrupier){
            System.out.println("Gana el jugador.");
        }else if (puntajeCrupier > puntajeJugador){
            System.out.println("Gana el crupier.");
        }else {
            System.out.println("Es un empate.");
        }
    }

    public static void main(String[] args) {
        BlackJack juego = new BlackJack();
        juego.iniciarJuego();
    }
}
