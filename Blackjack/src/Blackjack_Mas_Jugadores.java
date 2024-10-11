import ar.edu.unlu.blackjack.Baraja;
import ar.edu.unlu.blackjack.Crupier;
import ar.edu.unlu.blackjack.Jugador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blackjack_Mas_Jugadores {
    private final Baraja baraja;
    private final List<Jugador> jugadores;
    private final Crupier crupier;
    private final Scanner scanner;
    private int cantidadJugadores;

    public Blackjack_Mas_Jugadores() {
        baraja = new Baraja();
        crupier = new Crupier();
        jugadores = new ArrayList<Jugador>();
        scanner = new Scanner(System.in);
    }

    public void iniciarJuego(){
        System.out.println("Bienvenido al Blackjack!");

        configurarJugadores();

        repartirCartasIniciales();

        System.out.println("Cartas restantes: " + baraja.cartasRestantes());

        crupier.mostrarPrimeraCarta();

        for (Jugador jugador : jugadores){
            turnoJugador(jugador);
        }
        checkJugadoresValidos();

        evaluarGanadores();

        // imprimirPuntajesGlobal();


    }

    private void imprimirPuntajesGlobal() {
        System.out.println("=============================================");
        for (Jugador jugador : jugadores){
            if (jugador.getPuntaje() <= 21 && crupier.getPuntaje() >= 21){
                System.out.printf("El puntaje de %s es de: %d. GANÓ!\n", jugador.getNombre(), jugador.getPuntaje());
            }else if (jugador.getPuntaje() >= 21 && crupier.getPuntaje() <= 21){
                System.out.printf("El puntaje de %s es de: %d. PERDIÓ!\n", jugador.getNombre(), jugador.getPuntaje());
            }else if (crupier.getPuntaje() <= 21 && crupier.getPuntaje() == jugador.getPuntaje()){
                System.out.printf("El puntaje de %s es de: %d. EMPATÓ!\n", jugador.getNombre(), jugador.getPuntaje());
            }

        }
        System.out.println("=============================================");
    }

    private void checkJugadoresValidos(){
        boolean esValido = false;
        for (Jugador jugador : jugadores){
            if (!jugador.sePaso21()){
                esValido = true;
                break;
            }
        }
        if (esValido){
            turnoCrupier();
        }
    }

    // Metodo donde controlo al jugador
    private void turnoJugador(Jugador jugador) {
        System.out.println("Es el turno de: " + jugador.getNombre());
        String ingreso;
        while (!jugador.sePaso21() || jugador.getPuntaje() == 21){
            System.out.println(jugador.getNombre() +": ingrese 'c' para pedir o 'p' para plantarse: ");
            ingreso = scanner.nextLine();
            if (ingreso.equals("c")){
                jugador.recibirCarta(baraja.repartirCarta());
                jugador.mostrarMano();

                if (jugador.sePaso21()){
                    System.out.println("Se paso de los 21. Perdió el juego :(");
                    break;
                }
            }else if (ingreso.equals("p")){
                System.out.println(jugador.getNombre() + " se plantó.");
                break;
            }else {
                System.out.println("Lo que se ingresó no es válido.");
            }
        }
    }

    private void configurarJugadores(){
        System.out.print("Ingrese cantidad de jugadores: ");
        int cantidadJugadores = scanner.nextInt();
        scanner.nextLine(); // Limpio buffer
        if (cantidadJugadores > 7 || cantidadJugadores <= 0){
            throw new IllegalArgumentException("[!] El numero ingresado es invalido. Debe estar entre 1 y 7!");
        }

        for (int i = 1; i <= cantidadJugadores; i++){
            System.out.print("Ingrese el nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            jugadores.add(new Jugador(nombre));
        }
    }

    private void repartirCartasIniciales(){
        for (Jugador jugador : jugadores){
            jugador.recibirCarta(baraja.repartirCarta());
            jugador.recibirCarta(baraja.repartirCarta());
            jugador.mostrarMano();
        }
        crupier.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());
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

    private void evaluarGanadores(){
        int puntajeJugador;
        int puntajeCrupier = crupier.getPuntaje();
        System.out.println();

        System.out.println();
        System.out.println("========================================================");

        for(Jugador jugador : jugadores){
            puntajeJugador = jugador.getPuntaje();
            System.out.println("El puntaje final de " + jugador.getNombre() + " es: " + puntajeJugador);
            // jugador.mostrarMano();
            System.out.println();
            if (jugador.sePaso21()){
                System.out.println(jugador.getNombre() + " se paso de los 21. Perdiste!");
            }else if (crupier.sePaso21()){
                System.out.println(jugador.getNombre() + " gana dado que el crupier se paso de 21.");
            }else if (puntajeJugador > puntajeCrupier){
                System.out.println(jugador.getNombre() + " GANA!");
            }else if (puntajeJugador < puntajeCrupier){
                System.out.println(jugador.getNombre() + " PERDIÓ!");
            }else{
                System.out.println(jugador.getNombre() + " empató con el crupier!");
            }
        }
        System.out.println("El puntaje final del crupier es: " + puntajeCrupier);
    }

    public static void main(String[] args) {
        Blackjack_Mas_Jugadores juego = new Blackjack_Mas_Jugadores();
        juego.iniciarJuego();
    }
}
