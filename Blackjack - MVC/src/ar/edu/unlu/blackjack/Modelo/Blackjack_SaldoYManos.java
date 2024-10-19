package ar.edu.unlu.blackjack.Modelo;

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
    private int indiceJugador;

    public Blackjack_SaldoYManos() {
        mazo = new Mazo();
        crupier = new Crupier();
        manos = new ArrayList<Mano>();
        jugadores = new ArrayList<Jugador>();
        this.indiceJugador = 0;
        scanner = new Scanner(System.in);
    }
    public void crupierPideCarta(){
        crupier.pedirCarta();
    }
    public String crupierMuestraPrimerCarta(){
        return crupier.mostrarPrimeraCarta();
    }
    public Jugador getJugadorActualTurno(){
        return jugadores.get(indiceJugador);
    }
    public void cambiarTurno(){
        if (indiceJugador == jugadores.size()){
            indiceJugador = 0;
        }else{
            indiceJugador++;
        }
    }
    public String ControladorCartasRestantes(){
        return String.valueOf(mazo.cartasRestantes());
    }
    public boolean alMenosUnoSigueJugando(String ingreso){
        return ingreso.equals("1");
    }
    public String ingresarPorTeclado(){
        return scanner.nextLine();
    }
    public int getIndice(){
        return this.indiceJugador;
    }
    public int getCantidadJugadores(){
        return this.cantidadJugadores;
    }
    public boolean realizarApuesta(Jugador jugador){
        if (this.indiceJugador == jugadores.size()-1) return true;
        int monto = Integer.parseInt(ingresarPorTeclado());
        while (monto > getJugadorActualTurno().getSaldo() || monto <= 0){
            ingresarPorTeclado();
            if (monto > getJugadorActualTurno().getSaldo()){
                return false;
            }
        }
        getJugadorActualTurno().ajustarSaldo(-monto);
        ingresarPorTeclado();
        getJugadorActualTurno().setApuesta(monto);
        getJugadorActualTurno().mostrarSaldo();
        getJugadorActualTurno().iniciarMano();
        return true;
    }

    // Metodo donde controlo al jugador
    public void turnoJugador(Jugador jugador) {
        String ingreso;
        boolean insurance = false;
        boolean flagDoblo = false;
        boolean yaPidio = false;
        boolean yaPregunto = false;
        boolean yaRevisoBlackjack = false;
        int nroMano = 0;
        Mano mano = jugador.getManoActual();
        if (mano != null && jugador.puedeDividir()) {
            mano.mostrarMano(jugador);
            crupier.mostrarPrimeraCarta();
            while ((!mano.sePaso21() && mano.getPuntaje() != 21) && !flagDoblo) {

                System.out.printf("Es el turno de: %s --> (%d)\n", jugador.getNombre(), mano.getPuntaje());
                if (jugador.tieneBlackjack() && !yaRevisoBlackjack) {
                    // jugador.mostrarMano();
                    mano.mostrarMano(jugador);
                    System.out.println("Felicitaciones " + jugador.getNombre() + ", conseguiste un BJ!");
                    break;
                }else yaRevisoBlackjack = true;
                if (crupier.tieneAsPrimera() && !yaPregunto) {
                    while (!insurance && jugador.getSaldo() >= jugador.getApuesta()/2) {
                        int ingresoSeguro = mano.seguroBlackjack(jugador);
                        if (ingresoSeguro == 1) {
                            System.out.println(jugador.getNombre() + " decidió pagar el seguro.");
                            jugador.ajustarSaldo(-(jugador.getApuesta() / 2));
                            insurance = true;
                            yaPregunto = true;
                            break;
                        } else {
                            System.out.printf("%s: decidió no pagar el seguro.\n", jugador.getNombre());
                            yaPregunto = true;
                            break;
                        }
                    }
                    if (jugador.getSaldo() < jugador.getApuesta()/2) System.out.println("[!] No podes pagar el seguro dado que no tenes saldo suficiente.");
                }
                System.out.printf("%s: ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ", jugador.getNombre());
                ingreso = scanner.nextLine().toLowerCase();
                if (ingreso.equals("c")) {
                    mano.recibirCarta(mazo.repartirCarta());
                    mano.mostrarMano(jugador);
                    jugador.mostrarManosDivididas();
                    if (mano.sePaso21()) {
                        System.out.println("Se paso de los 21. Perdió el juego :(");
                        break;
                    }
                    yaPidio = true;
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
                        boolean termino = false;
                        // jugador.repartirATodasLasManos(mazo.repartirCarta());
                        jugador.repartirCartaAMano(0, mazo.repartirCarta());
                        jugador.repartirCartaAMano(1, mazo.repartirCarta());
                        for (int i = 0; i < manos.size(); i++) {
                            termino = false;
                            while (!termino){
                                if (manos.get(i).tieneBlackjack()) {
                                    System.out.printf("Felicitaciones %s! conseguiste BJ en la mano %d.\n", jugador.getNombre(), i + 1);
                                    termino = true;
                                    break;
                                }
                                while ((!manos.get(i).sePaso21() && manos.get(i).getPuntaje() != 21) && !flagDoblo) {
                                    String ingresoDividir;
                                    // manos.get(i).recibirCarta(mazo.repartirCarta());
                                    jugador.mostrarManosDivididas();
                                    System.out.printf("Es el turno de: %s con la mano %d --> (%d)\n", jugador.getNombre(), i + 1, manos.get(i).getPuntaje());
                                    System.out.printf("%s: ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ", jugador.getNombre());
                                    ingresoDividir = scanner.nextLine().toLowerCase();
                                    if (ingresoDividir.equals("c")){
                                        // Pedir carta
                                        manos.get(i).recibirCarta(mazo.repartirCarta());
                                        // System.out.printf("----= MANO %d =----\n", i + 1);
                                        // manos.get(i).mostrarMano(jugador);
                                        jugador.mostrarManosDivididas();
                                        if (manos.get(i).sePaso21()) {
                                            System.out.println("Se paso de los 21. Perdió el juego :(");
                                            termino = true;
                                            break;
                                        }
                                        yaPidio = true;
                                        break;
                                    }else if (ingresoDividir.equals("p")) {
                                        System.out.println(jugador.getNombre() + " se plantó con la mano " + (i + 1) + ".");
                                        termino = true;
                                        break;
                                    }else if (ingresoDividir.equals("d")) {
                                        if (jugador.getSaldo() >= jugador.getApuesta()) {
                                            System.out.printf("%s dobló con la mano %d.\n", jugador.getNombre(), i + 1);
                                            // jugador.pedirCartaMano1();
                                            manos.get(i).recibirCarta(mazo.repartirCarta());
                                            manos.get(i).doblarMano(jugador);
                                            flagDoblo = true;
                                            termino = true;
                                            break;
                                        }else if (yaPidio){
                                            System.out.printf("[!] No podes doblar dado que ya pediste una carta en la mano %d!", i + 1);
                                        }else System.out.printf("[!] No podés doblar dado que no tenés el saldo suficiente! (Saldo = %d)\n", jugador.getSaldo());
                                    }else{
                                        System.out.println("Lo que se ingresó no es válido.");
                                    }
                                }
                            }
                            if (i == 1) terminoMano2 = true;
                            if (terminoMano2){
                                System.out.println("------ Resumen ambas manos ------");
                                jugador.mostrarManosDivididas();
                                break;
                            }
                        }
                    } else {
                        System.out.print("[!] No podés dividir dado que no tenés dos cartas iguales!");
                    }
                    break;
                } else if (ingreso.equals("d")) {
                    System.out.printf("[DEBUG] El saldo del jugador %s es de %d.\n", jugador.getNombre(), jugador.getSaldo());
                    if (jugador.getSaldo() >= jugador.getApuesta()) {
                        System.out.printf("%s dobló.\n", jugador.getNombre());
                        // jugador.pedirCartaMano1();
                        mano.recibirCarta(mazo.repartirCarta());
                        mano.doblarMano(jugador);
                        flagDoblo = true;
                        break;
                    } else if (yaPidio){
                        System.out.println("[!] No podes doblar dado que ya pediste una carta!");
                    }else System.out.printf("[!] No podes doblar dado que no tenés saldo suficiente. (Saldo = %d).\n", jugador.getSaldo());
                } else {
                    System.out.println("Lo que se ingresó no es válido.");
                }
            }
        }else if (mano != null && !jugador.puedeDividir()) {
            mano.mostrarMano(jugador);
            crupier.mostrarPrimeraCarta();
            while ((!mano.sePaso21() && mano.getPuntaje() != 21) && !flagDoblo) {
                if (manos.size() == 2) {
                    System.out.printf("Es el turno de: %s con la mano 1 (derecha) --> (%d)\n", jugador.getNombre(), mano.getPuntaje());
                } else System.out.printf("Es el turno de: %s --> (%d)\n", jugador.getNombre(), mano.getPuntaje());
                if (jugador.tieneBlackjack() && !yaRevisoBlackjack) {
                    System.out.println("Felicitaciones " + jugador.getNombre() + ", conseguiste un BJ!");
                    break;
                }else yaRevisoBlackjack = true;
                if (mano.tieneBlackjack()){
                    System.out.println("Felicitaciones " + jugador.getNombre() + ", conseguiste un BJ!");
                    break;
                }
                if (crupier.tieneAsPrimera() && !yaPregunto) {
                    while (!insurance && jugador.getSaldo() >= jugador.getApuesta()/2) {
                        int ingresoSeguro = mano.seguroBlackjack(jugador);
                        if (ingresoSeguro == 1) {
                            System.out.println(jugador.getNombre() + " decidió pagar el seguro.");
                            jugador.ajustarSaldo(-(jugador.getApuesta() / 2));
                            insurance = true;
                            yaPregunto = true;
                        } else {
                            System.out.printf("%s: decidió no pagar el seguro.\n", jugador.getNombre());
                            yaPregunto = true;
                            break;
                        }
                    }
                    if (jugador.getSaldo() < jugador.getApuesta()/2) System.out.println("[!] No podes pagar el seguro porque no tenes saldo suficiente.");
                }
                System.out.printf("%s: ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ", jugador.getNombre());
                ingreso = scanner.nextLine().toLowerCase();
                if (ingreso.equals("c")) {
                    mano.recibirCarta(mazo.repartirCarta());
                    mano.mostrarMano(jugador);
                    // jugador.mostrarManos();
                    if (mano.sePaso21()) {
                        System.out.println("Se paso de los 21. Perdió el juego :(");
                        break;
                    }
                    yaPidio = true;
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
                    if (jugador.getSaldo() >= jugador.getApuesta() && !yaPidio) {
                        System.out.printf("%s dobló.\n", jugador.getNombre());
                        // jugador.pedirCartaMano1();
                        mano.recibirCarta(mazo.repartirCarta());
                        mano.doblarMano(jugador);
                        flagDoblo = true;
                        break;
                    }else if (yaPidio){
                        System.out.println("[!] No podes doblar dado que ya pediste una carta!");
                    }else System.out.printf("[!] No podes doblar dado que no tenés saldo suficiente! (Saldo = %d)\n", jugador.getSaldo());
                } else {
                    System.out.println("Lo que se ingresó no es válido.");
                }
            }
        }
        cambiarTurno();
    }

    public void configurarJugadores(){
        System.out.print("Ingrese cantidad de jugadores: ");
        this.cantidadJugadores = scanner.nextInt();
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
        Mano mano;
        for (Jugador jugador : jugadores){
            for (int i = 0; i < 2; i++){
                for (int j = 0; j < jugador.getManos().size(); j++){
                    // jugador.getManos().size() me devuelve la cantidad de manos activas del jugador
                    jugador.repartirCartaAMano(j, mazo.repartirCarta());
                }
            }
            mano = jugador.getManoActual() ;
            mano.mostrarMano(jugador);
            System.out.println("Presiona Enter para continuar...");
            scanner.nextLine();
        }
    }

    // Metodo donde controlo al crupier
    public void turnoCrupier() {
        System.out.println("Turno del crupier.");
        crupier.mostrarMano();

        // Obtiene una carta hasta obtener 17 o más.
        while (crupier.debePedirCarta()){
            System.out.println("El crupier está obteniendo una carta...");
            try {
                Thread.sleep(1500);
            }catch (InterruptedException e){
                e.printStackTrace();
                System.out.println("Interrumpido");
            }
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

    public void evaluarGanadores(){
        if (crupier.tieneBlackjack()) evaluarGanadoresBlackjack();
        else evaluarGanadoresNOBlackjack();
    }

    public void evaluarGanadoresBlackjack(){ // Entra si el crupier tiene Blackjack
        System.out.println("El crupier obtuvo blackjack.");
        for (Jugador jugador : jugadores){
            List<Mano> manos = jugador.getManos();
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

    public void devolverApuesta(Jugador jugador, int apuesta) {
        System.out.printf("%s: debido al empate se te devolvió el monto apostado (%d)\n", jugador.getNombre(), apuesta);
        jugador.ajustarSaldo(apuesta);
    }

    public void adjudicarGanancia(Jugador jugador, int apuesta){
        System.out.printf("%s: felicitaciones! Ganaste la apuesta.\n", jugador.getNombre());
        jugador.ajustarSaldo(2*apuesta);
    }

    public void evaluarGanadoresNOBlackjack(){ // Entra si el crupier NO tiene Blackjack
        int puntajeMano;
        int puntajeCrupier = crupier.getPuntaje();
        List<Mano> manos;
        System.out.println();
        for(Jugador jugador : jugadores){
            manos = jugador.getManos();
            if (jugador.multiplesManos()){
                for (int i = 0; i < 2; i++){
                    System.out.printf("==============================\n\t\tMANO %d\n==============================\n", i + 1);
                    // puntajeMano = jugador.getManos().get(i).getPuntaje();
                    puntajeMano = manos.get(i).getPuntaje();
                    System.out.printf("El puntaje final de la mano %d de %s es: %d --> ", i+1, jugador.getNombre(), jugador.getManos().get(i).getPuntaje());

                    if (manos.get(i).sePaso21()){
                        // System.out.println(jugador.getNombre() + " se paso de los 21. Perdiste!");
                        System.out.printf("PERDISTE!\n");
                    }else if (crupier.getPuntaje() > 21){
                        // System.out.println(jugador.getNombre() + " gana dado que el crupier se paso de 21.");
                        System.out.printf("GANASTE!\n");
                        adjudicarGanancia(jugador, jugador.getApuesta());

                    }else if (puntajeMano > puntajeCrupier){
                        // System.out.println(jugador.getNombre() + " GANA!");
                        System.out.printf("%s: tu mano es ganadora!", jugador.getNombre());
                        adjudicarGanancia(jugador, jugador.getApuesta());
                    }else if (puntajeMano < puntajeCrupier){
                        // System.out.println(jugador.getNombre() + " PERDIÓ!");
                        // System.out.printf("%s: el crupier obtuvo mas puntaje que vos. Perdiste la mano!", jugador.getNombre());
                        System.out.printf("PERDISTE!\n");
                    }else{
                        System.out.printf("EMPATASTE!\n");
                        devolverApuesta(jugador, jugador.getApuesta());
                    }
                    System.out.println();
                }
                System.out.println("El puntaje final del crupier es: " + puntajeCrupier);
            }else{
                System.out.println("========================================================");
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
}
