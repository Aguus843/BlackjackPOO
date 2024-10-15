import ar.edu.unlu.blackjack.Crupier;
import ar.edu.unlu.blackjack.Jugador;
import ar.edu.unlu.blackjack.Mazo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Character.toLowerCase;

public class Blackjack_Saldo {
    private final Mazo mazo;
    private final List<Jugador> jugadores;
    private final Crupier crupier;
    private final Scanner scanner;
    private int cantidadJugadores;

    public Blackjack_Saldo() {
        mazo = new Mazo();
        crupier = new Crupier();
        jugadores = new ArrayList<Jugador>();
        scanner = new Scanner(System.in);
    }

    public void iniciarJuego(){
        System.out.println("Bienvenido al Blackjack!");

        configurarJugadores();

        realizarApuesta();

        repartirCartasIniciales();
        crupier.recibirCarta(mazo.repartirCarta());
        crupier.recibirCarta(mazo.repartirCarta());

        System.out.println("Cartas restantes: " + mazo.cartasRestantes());

        crupier.mostrarPrimeraCarta();

        for (Jugador jugador : jugadores){
            turnoJugador(jugador);
        }
        checkJugadoresValidos();

        evaluarGanadores();

        // imprimirPuntajesGlobal();


    }
    private void realizarApuesta(){
        for (Jugador jugador : jugadores){
            System.out.printf("%s, tu saldo actual es de %d pesos\n", jugador.getNombre(), jugador.getSaldo());
            System.out.print("Ingrese el monto a apostar: ");
            int monto = scanner.nextInt();
            while (monto > jugador.getSaldo() || monto <= 0){
                scanner.nextLine();
                if (monto > jugador.getSaldo()){
                    System.out.println("[!] El monto ingresado excede al saldo actual disponible.");
                }
                if (monto <= 0){
                    System.out.println("[!] El monto debe ser mayor que cero.");
                }
                System.out.print("Ingrese el monto a apostar: ");
                monto = scanner.nextInt();
            }
            jugador.ajustarSaldo(-monto); // Monto es negativo dado que es una apuesta y hay que retirar el dinero
            scanner.nextLine(); // Limpio el buffer
            jugador.apuesta = monto;
            jugador.mostrarSaldo();
        }
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
        System.out.println("Es el turno de: " + jugador.getNombre() + (" (") + jugador.getPuntaje() + (")"));
        String ingreso;
        boolean insurance = false;
        boolean flagDoblo = false;
        while ((!jugador.sePaso21() && jugador.getPuntaje() != 21) && !flagDoblo){
            if (jugador.tieneBlackjack()){
                // jugador.mostrarMano();
                System.out.println("Felicitaciones " + jugador.getNombre() + ", conseguiste un BJ!");
                break;
            }
            while (!insurance){
                if(crupier.getMano().getFirst().equals("A")){
                    int ingresoSeguro = jugador.seguroBlackjack(jugador);
                    if (ingresoSeguro == 1){
                        System.out.println(jugador.getNombre() + " decidió pagar el seguro.");
                        jugador.ajustarSaldo(-(jugador.apuesta/2));
                    }
                    else System.out.printf("%s: decidió no pagar el seguro.\n", jugador.getNombre());
                }
                insurance = true;
            }

            System.out.printf("%s: ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ", jugador.getNombre());
            ingreso = scanner.nextLine().toLowerCase();
            if (ingreso.equals("c")){
                jugador.recibirCarta(mazo.repartirCarta());
                jugador.mostrarMano();

                if (jugador.sePaso21()){
                    System.out.println("Se paso de los 21. Perdió el juego :(");
                    break;
                }
            }else if (ingreso.equals("p")){
                System.out.println(jugador.getNombre() + " se plantó.");
                break;
            }else if(ingreso.equals("s")){
                if (jugador.getSaldo() >= jugador.apuesta){
                    System.out.printf("%s decidió dividir.\n", jugador.getNombre());
                    jugador.dividirMano(jugador);
                }else{
                    System.out.printf("[!] No podes dividir dado que no tenés saldo suficiente (Saldo = %d).\n", jugador.getSaldo());
                }
            }else if (ingreso.equals("d")){
                if (jugador.getSaldo() >= jugador.apuesta){
                    System.out.printf("%s dobló.\n", jugador.getNombre());
                    jugador.recibirCarta(mazo.repartirCarta());
                    jugador.doblarMano(jugador);
                    flagDoblo = true;
                    break;
                }else {
                    System.out.printf("[!] No podes doblar dado que no tenés saldo suficiente. (Saldo = %d).\n", jugador.getSaldo());
                }
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
            System.out.println();
            // jugadores.add(new Jugador(nombre));
            System.out.print("Ingrese el saldo a iniciar con el jugador " + i + ": ");
            int saldo = scanner.nextInt();
            jugadores.add(new Jugador(nombre, saldo));
            scanner.nextLine();
            System.out.println();
        }
    }

    private void repartirCartasIniciales(){
        for (Jugador jugador : jugadores){
            jugador.recibirCarta(mazo.repartirCarta());
            jugador.recibirCarta(mazo.repartirCarta());
            jugador.mostrarMano();
        }

    }

    // Metodo donde controlo al crupier
    private void turnoCrupier() {
        System.out.println("Turno del crupier.");
        crupier.mostrarMano();

        // Obtiene una carta hasta obtener 17 o más.
        while (crupier.debePedirCarta()){
            System.out.println("El crupier obtiene una carta.");
            crupier.recibirCarta(mazo.repartirCarta());
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
        if (crupier.tieneBlackjack()) evaluarGanadoresBlackjack();
        else evaluarGanadoresNOBlackjack();
    }

    public void evaluarGanadoresBlackjack(){ // Entra si el crupier tiene Blackjack
        System.out.println("El crupier obtuvo blackjack.");
        for (Jugador jugador : jugadores){
            if (jugador.tieneBlackjack()){
                System.out.println(jugador.getNombre() + ": el crupier también obtuvo Blackjack por lo que es un empate!");
                System.out.println(jugador.getNombre() + " EMPATÓ!");
                devolverApuesta(jugador, jugador.apuesta);
            }else{
                if (jugador.pagoSeguro){
                    System.out.println(jugador.getNombre() + ": pagó el seguro por lo que se le devuelve el monto apostado.");
                    devolverApuesta(jugador, jugador.apuesta); // Se le devuelve el monto de la apuesta -> sería el seguro * 2
                }
                System.out.println(jugador.getNombre() + " PERDIÓ!");
            }
        }
    }

    private void devolverApuesta(Jugador jugador, int apuesta) {
        System.out.printf("%s: debido al empate se te devolvió el monto apostado (%d)\n", jugador.getNombre(), apuesta);
        jugador.ajustarSaldo(apuesta);
    }

    private void adjudicarGanancia(Jugador jugador, int apuesta){
        System.out.printf("%s: felicitaciones! Ganaste la apuesta.\n", jugador.getNombre());
        jugador.ajustarSaldo(2*apuesta);
    }

    public void evaluarGanadoresNOBlackjack(){ // Entra si el crupier NO tiene Blackjack
        int puntajeJugador;
        int puntajeCrupier = crupier.getPuntaje();
        System.out.println();
        System.out.println("========================================================");
        for(Jugador jugador : jugadores){
            puntajeJugador = jugador.getPuntaje();
            System.out.println();
            System.out.printf("El puntaje final de %s es: %d --> ", jugador.getNombre(), jugador.getPuntaje());
            // jugador.mostrarMano();

            if (jugador.sePaso21()){
                System.out.println(jugador.getNombre() + " se paso de los 21. Perdiste!");
            }else if (crupier.sePaso21()){
                System.out.println(jugador.getNombre() + " gana dado que el crupier se paso de 21.");
                adjudicarGanancia(jugador, jugador.apuesta);

            }else if (puntajeJugador > puntajeCrupier){
                System.out.println(jugador.getNombre() + " GANA!");
                adjudicarGanancia(jugador, jugador.apuesta);
            }else if (puntajeJugador < puntajeCrupier){
                System.out.println(jugador.getNombre() + " PERDIÓ!");
            }else{
                System.out.println(jugador.getNombre() + " empató con el crupier!");
                devolverApuesta(jugador, jugador.apuesta);
            }
        }
        System.out.println("El puntaje final del crupier es: " + puntajeCrupier);
    }

    public static void main(String[] args) {
        Blackjack_Saldo juego = new Blackjack_Saldo();
        juego.iniciarJuego();
    }
}
