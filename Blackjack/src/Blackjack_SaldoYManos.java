import ar.edu.unlu.blackjack.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blackjack_SaldoYManos {
    private final Mazo mazo;
    private final List<Jugador> jugadores;
    private final Crupier crupier;
    private final Scanner scanner;
    private int cantidadJugadores;
    private List<Mano> manos;

    public Blackjack_SaldoYManos() {
        mazo = new Mazo();
        crupier = new Crupier();
        manos = new ArrayList<Mano>();
        jugadores = new ArrayList<Jugador>();
        scanner = new Scanner(System.in);
    }

    public void iniciarJuego(){
        System.out.println("Bienvenido al Blackjack!");

        configurarJugadores();

        realizarApuesta();

        repartirCartasIniciales();
        // crupier.recibirCarta(mazo.repartirCarta());
        // crupier.recibirCarta(mazo.repartirCarta());
        crupier.pedirCarta();
        crupier.pedirCarta();

        System.out.println("Cartas restantes: " + mazo.cartasRestantes());

        crupier.mostrarPrimeraCarta();

        for (Jugador jugador : jugadores){
            turnoJugador(jugador);
        }
        // checkJugadoresValidos();
        turnoCrupier();

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
            jugador.setApuesta(monto);
            jugador.mostrarSaldo();
            jugador.iniciarMano();
            System.out.printf("[LOG] Mano iniciada correctamente para el usuario %s.\n", jugador.getNombre());
        }
    }

//    private void imprimirPuntajesGlobal() {
//        System.out.println("=============================================");
//        for (Jugador jugador : jugadores){
//            if (jugador.getPuntaje() <= 21 && crupier.getPuntaje() >= 21){
//                System.out.printf("El puntaje de %s es de: %d. GANÓ!\n", jugador.getNombre(), jugador.getPuntaje());
//            }else if (jugador.getPuntaje() >= 21 && crupier.getPuntaje() <= 21){
//                System.out.printf("El puntaje de %s es de: %d. PERDIÓ!\n", jugador.getNombre(), jugador.getPuntaje());
//            }else if (crupier.getPuntaje() <= 21 && crupier.getPuntaje() == jugador.getPuntaje()){
//                System.out.printf("El puntaje de %s es de: %d. EMPATÓ!\n", jugador.getNombre(), jugador.getPuntaje());
//            }
//
//        }
//        System.out.println("=============================================");
//    }

    private void checkJugadoresValidos(){
        boolean esValido = false;
        for (Jugador jugador : jugadores){
            for (Mano mano : manos){
                if (mano != null){
                    if (!mano.sePaso21()){
                        esValido = true;
                        break;
                    }
                }
            }
        }
        if (esValido){
            turnoCrupier();
        }
    }

    // Metodo donde controlo al jugador
    private void turnoJugador(Jugador jugador) {
        // System.out.println("Es el turno de: " + jugador.getNombre() + (" (") + jugador.getPuntaje() + (")"));
        String ingreso;
        boolean insurance = false;
        boolean flagDoblo = false;
        int nroMano = 0;
        Mano mano = jugador.getManoActual();
        if (mano != null && jugador.puedeDividir()) {
            while ((!mano.sePaso21() && mano.getPuntaje() != 21) && !flagDoblo) {
                if (jugador.getManos().size() == 2) {
                    // System.out.println("Es el turno de: " + jugador.getNombre() + (" (") + jugador.getPuntaje() + (")"));
                    System.out.printf("Es el turno de: %s con la mano %d --> (%d)\n", jugador.getNombre(), nroMano + 1, mano.getPuntaje());
                } else System.out.printf("Es el turno de: %s --> (%d)\n", jugador.getNombre(), mano.getPuntaje());
                if (jugador.getManoActual().tieneBlackjack()) {
                    // jugador.mostrarMano();
                    System.out.println("Felicitaciones " + jugador.getNombre() + ", conseguiste un BJ!");
                    break;
                }
                if (crupier.getMano().getFirst().equals("A")) {
                    while (!insurance) {
                        int ingresoSeguro = mano.seguroBlackjack(jugador);
                        if (ingresoSeguro == 1) {
                            System.out.println(jugador.getNombre() + " decidió pagar el seguro.");
                            jugador.ajustarSaldo(-(jugador.getApuesta() / 2));
                            insurance = true;
                        } else {
                            System.out.printf("%s: decidió no pagar el seguro.\n", jugador.getNombre());
                            break;
                        }
                    }
                }
                System.out.printf("%s: ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ", jugador.getNombre());
                ingreso = scanner.nextLine().toLowerCase();
                if (ingreso.equals("c")) {
                    mano.recibirCarta(mazo.repartirCarta());
                    mano.mostrarMano(jugador);
                    if (mano.sePaso21()) {
                        System.out.println("Se paso de los 21. Perdió el juego :(");
                        break;
                    }
                } else if (ingreso.equals("p")) {
                    System.out.println(jugador.getNombre() + " se plantó.");
                    break;
                } else if (ingreso.equals("s")) {
                    if ((jugador.getSaldo() >= jugador.getApuesta()) && mano.getMano().getFirst().getValor().equals(mano.getMano().get(1).getValor())) {
                        System.out.printf("%s decidió dividir.\n", jugador.getNombre());
                        // jugador.dividirMano(jugador); Terminar luego
                        jugador.getManoActual().dividirMano(jugador);
                        // Arranco un ciclo while para cargar ambas manos
                        List<Mano> manos = jugador.getManos();
                        boolean terminoMano2 = false;
                        for (int i = 0; i < manos.size(); i++) {
                            while ((!manos.get(i).sePaso21() && manos.get(i).getPuntaje() < 21) && !flagDoblo) {
                                System.out.printf("Es el turno de: %s con la mano %d --> (%d)\n", jugador.getNombre(), i + 1, manos.get(i).getPuntaje());

                                if (manos.get(i).tieneBlackjack()) { // PINCHA ACA
                                    System.out.printf("Felicitaciones %s! conseguiste BJ en la mano %d.\n", jugador.getNombre(), i + 1);
                                    break;
                                }

                                System.out.printf("%s: ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ", jugador.getNombre());
                                ingreso = scanner.nextLine().toLowerCase();
                                switch (ingreso) {
                                    case "c":
                                        // Pedir carta
                                        manos.get(i).recibirCarta(mazo.repartirCarta());
                                        System.out.printf("----= MANO %d =----\n", i + 1);
                                        manos.get(i).mostrarMano(jugador);
                                        if (manos.get(i).sePaso21()) {
                                            System.out.println("Se paso de los 21. Perdió el juego :(");
                                            break;
                                        }
                                        break;
                                    case "p":
                                        System.out.println(jugador.getNombre() + " se plantó con la mano " + (i + 1) + ".");
                                        break;
                                    case "d":
                                        System.out.printf("[DEBUG] El saldo del jugador %s es de %d.\n", jugador.getNombre(), jugador.getSaldo());
                                        if (jugador.getSaldo() >= jugador.getApuesta()) {
                                            System.out.printf("%s dobló con la mano %d.\n", jugador.getNombre(), i + 1);
                                            // jugador.pedirCartaMano1();
                                            manos.get(i).recibirCarta(mazo.repartirCarta());
                                            manos.get(i).doblarMano(jugador);
                                            flagDoblo = true;
                                            break;
                                        } else {
                                            System.out.printf("[!] No podés doblar dado que no tenés el saldo suficiente! (Saldo = %d)\n", jugador.getSaldo());
                                        }
                                        break;
                                    default:
                                        System.out.println("Lo que se ingresó no es válido.");
                                        break;
                                }
                            }
                            if (i == 2) terminoMano2 = true;
                            if (terminoMano2){
                                System.out.println("------ Resumen ambas manos ------");
                                jugador.mostrarManos();
                            }
                        }


                    } else {
                        System.out.printf("[!] No podes dividir dado que no tenés saldo suficiente (Saldo = %d).\n", jugador.getSaldo());
                    }
                } else if (ingreso.equals("d")) {
                    System.out.printf("[DEBUG] El saldo del jugador %s es de %d.\n", jugador.getNombre(), jugador.getSaldo());
                    if (jugador.getSaldo() >= jugador.getApuesta()) {
                        System.out.printf("%s dobló.\n", jugador.getNombre());
                        // jugador.pedirCartaMano1();
                        mano.recibirCarta(mazo.repartirCarta());
                        mano.doblarMano(jugador);
                        flagDoblo = true;
                        break;
                    } else {
                        System.out.printf("[!] No podes doblar dado que no tenés saldo suficiente. (Saldo = %d).\n", jugador.getSaldo());
                    }
                } else {
                    System.out.println("Lo que se ingresó no es válido.");
                }
            }
        } else if (mano != null && !jugador.puedeDividir()) {
            while ((!mano.sePaso21() && mano.getPuntaje() != 21) && !flagDoblo) {
                if (manos.size() == 2) {
                    // System.out.println("Es el turno de: " + jugador.getNombre() + (" (") + jugador.getPuntaje() + (")"));
                    System.out.printf("Es el turno de: %s con la mano 1 (derecha) --> (%d)\n", jugador.getNombre(), mano.getPuntaje());
                } else System.out.printf("Es el turno de: %s --> (%d)\n", jugador.getNombre(), mano.getPuntaje());
                if (jugador.tieneBlackjack()) {
                    // jugador.mostrarMano();
                    System.out.println("Felicitaciones " + jugador.getNombre() + ", conseguiste un BJ!");
                    break;
                }
                if (crupier.getMano().getFirst().equals("A")) {
                    while (!insurance) {
                        int ingresoSeguro = jugador.seguroBlackjack(jugador);
                        if (ingresoSeguro == 1) {
                            System.out.println(jugador.getNombre() + " decidió pagar el seguro.");
                            jugador.ajustarSaldo(-(jugador.getApuesta() / 2));
                            insurance = true;
                        } else {
                            System.out.printf("%s: decidió no pagar el seguro.\n", jugador.getNombre());
                            break;
                        }
                    }
                }
                System.out.printf("%s: ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ", jugador.getNombre());
                ingreso = scanner.nextLine().toLowerCase();
                if (ingreso.equals("c")) {
                    mano.recibirCarta(mazo.repartirCarta());
                    mano.mostrarMano(jugador);
                    if (mano.sePaso21()) {
                        System.out.println("Se paso de los 21. Perdió el juego :(");
                        break;
                    }
                } else if (ingreso.equals("p")) {
                    System.out.println(jugador.getNombre() + " se plantó.");
                    break;
                } else if (ingreso.equals("s")) {
                    if ((jugador.getSaldo() >= jugador.getApuesta()) && mano.getMano().getFirst().getValor().equals(mano.getMano().get(1).getValor())) {
                        System.out.printf("%s decidió dividir.\n", jugador.getNombre());
                        // jugador.dividirMano(jugador); Terminar luego
                    } else {
                        System.out.printf("[!] No podes dividir dado que no tenés saldo suficiente (Saldo = %d).\n", jugador.getSaldo());
                    }
                } else if (ingreso.equals("d")) {
                    // System.out.printf("[DEBUG] El saldo del jugador %s es de %d.\n", jugador.getNombre(), jugador.getSaldo());
                    if (jugador.getSaldo() >= jugador.getApuesta()) {
                        System.out.printf("%s dobló.\n", jugador.getNombre());
                        // jugador.pedirCartaMano1();
                        mano.recibirCarta(mazo.repartirCarta());
                        mano.doblarMano(jugador);
                        flagDoblo = true;
                        break;
                    } else {
                        System.out.print("[!] No podes doblar dado que no tenés las cartas iguales");
                    }
                } else {
                    System.out.println("Lo que se ingresó no es válido.");
                }
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

    public void repartirCartasIniciales(){
        for (Jugador jugador : jugadores){
            for (int i = 0; i < 2; i++){
                for (int j = 0; j < jugador.getManos().size(); j++){
                    // jugador.getManos().size() me devuelve la cantidad de manos activas del jugador
                    jugador.repartirCartaAMano(j, mazo.repartirCarta());
                }
            }
            jugador.mostrarManos();
        }
    }

    // Metodo donde controlo al crupier
    private void turnoCrupier() {
        System.out.println("Turno del crupier.");
        crupier.mostrarMano();

        // Obtiene una carta hasta obtener 17 o más.
        while (crupier.debePedirCarta()){
            System.out.println("El crupier obtiene una carta.");
            crupier.pedirCarta();
            crupier.mostrarMano();
        }

        // Verifico si se pasó de 21
        if (crupier.getPuntaje() > 21){
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
                devolverApuesta(jugador, jugador.getApuesta());
            }else{
                if (jugador.getPagoSeguro()){
                    System.out.println(jugador.getNombre() + ": pagó el seguro por lo que se le devuelve el monto apostado.");
                    devolverApuesta(jugador, jugador.getApuesta()); // Se le devuelve el monto de la apuesta -> sería el seguro * 2
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
        int puntajeMano;
        int puntajeCrupier = crupier.getPuntaje();
        System.out.println();
        System.out.println("========================================================");
        for(Jugador jugador : jugadores){
            if (jugador.multiplesManos()){
                for (int i = 0; i < 2; i++){
                    System.out.printf("=================================================\n\t\tMANO %d\n=================================================\n", i + 1);
                    puntajeMano = jugador.getManos().get(i).getPuntaje();
                    System.out.println();
                    System.out.printf("El puntaje final de la mano %d de %s es: %d --> ",i+1, jugador.getNombre(), jugador.getManos().get(i).getPuntaje());

                    if (manos.get(i).sePaso21()){
                        // System.out.println(jugador.getNombre() + " se paso de los 21. Perdiste!");
                        System.out.printf("%s: tu mano se pasó de los 21. Perdiste!", jugador.getNombre());
                    }else if (crupier.getPuntaje() > 21){
                        // System.out.println(jugador.getNombre() + " gana dado que el crupier se paso de 21.");
                        System.out.printf("%s: tu mano es ganadora dado que el crupier se pasó de 21!", jugador.getNombre());
                        adjudicarGanancia(jugador, jugador.getApuesta());

                    }else if (puntajeMano > puntajeCrupier){
                        // System.out.println(jugador.getNombre() + " GANA!");
                        System.out.printf("%s: tu mano es ganadora!", jugador.getNombre());
                        adjudicarGanancia(jugador, jugador.getApuesta());
                    }else if (puntajeMano < puntajeCrupier){
                        // System.out.println(jugador.getNombre() + " PERDIÓ!");
                        System.out.printf("%s: el crupier obtuvo mas puntaje que vos. Perdiste la mano!", jugador.getNombre());
                    }else{
                        // System.out.println(jugador.getNombre() + " empató con el crupier!");
                        System.out.printf("%s: tu mano se empató con el crupier!", jugador.getNombre());
                        devolverApuesta(jugador, jugador.getApuesta());
                    }
                }
                System.out.println("El puntaje final del crupier es: " + puntajeCrupier);
            }else{
                int puntajeManoUnica = jugador.getManoActual().getPuntaje();
                System.out.println();
                System.out.printf("El puntaje final de %s es: %d --> ", jugador.getNombre(), jugador.getManoActual().getPuntaje());
                if (jugador.getManoActual().sePaso21()){
                    System.out.println(jugador.getNombre() + " se paso de los 21. Perdiste!");
                }else if (crupier.getPuntaje() > 21){
                    System.out.println(jugador.getNombre() + " gana dado que el crupier se paso de 21.");
                    adjudicarGanancia(jugador, jugador.getApuesta());
                }else if (puntajeManoUnica > puntajeCrupier){
                    System.out.println(jugador.getNombre() + " GANA!");
                    adjudicarGanancia(jugador, jugador.getApuesta());
                }else if (puntajeManoUnica < puntajeCrupier){
                    System.out.println(jugador.getNombre() + " PERDIÓ!");
                }else{
                    System.out.println(jugador.getNombre() + " empató con el crupier!");
                    devolverApuesta(jugador, jugador.getApuesta());
                }
                System.out.println("El puntaje final del crupier es: " + puntajeCrupier);
            }
        }

    }

    public static void main(String[] args) {
        Blackjack_SaldoYManos juego = new Blackjack_SaldoYManos();
        juego.iniciarJuego();
    }
}
