package ar.edu.unlu.blackjack.Vista;

import ar.edu.unlu.blackjack.Modelo.Jugador;
import ar.edu.unlu.blackjack.Controlador.controlador;

import java.util.Scanner;

public class VistaConsola {
    private controlador controlador;
    Scanner scanner;
    public VistaConsola(){
        this.controlador = controlador;
        scanner = new Scanner(System.in);
    }

    public void mostrarMensaje(String mensaje){
        System.out.println(mensaje);
    }
    public String ingresarTexto(){
        return scanner.nextLine();
    }
    public String mostrarCartas()   {
        return controlador.cartasRestantes();
    }

    public void iniciarJuego(){
        mostrarMensaje("Bienvenido al Blackjack!");
        controlador.configurarJugadores();
        while (!controlador.terminoRonda(controlador.ingresarPorTeclado())){
            while (controlador.getIndiceJugadores() != controlador.getCantidadJugadoresTotal()-1){
                mostrarMensaje(controlador.obtenerJugadorActual().getNombre() + " tu saldo actual es de " + controlador.obtenerJugadorActual().getSaldo() + " pesos.");
                mostrarMensaje("Ingrese el monto a apostar: ");
                while (!controlador.realizarApuesta(controlador.obtenerJugadorActual())){
                    mostrarMensaje("[!] El dato ingresado no es correcto. Revisalo e ingresalo nuevamente.");
                    mostrarMensaje("Ingrese el monto a apostar: ");
                }
                mostrarMensaje("El saldo del jugador es de " + controlador.obtenerJugadorActual().getSaldo());
                mostrarMensaje("Es el turno de: " + controlador.obtenerJugadorActual().getNombre());
                controlador.turnoJugador(controlador.obtenerJugadorActual());
                controlador.cambiarTurnoJugador();
                // Cambia el turno al siguiente para que cargue la apuesta correspondiente
                // El juego no puede comenzar si todos los jugadores no han apostado
            }
            controlador.repartirCartasIniciales();
            controlador.crupierPideCarta();
            controlador.crupierPideCarta();
            mostrarMensaje("Cartas restantes: " + mostrarCartas());
            mostrarMensaje("El crupier tiene " + controlador.crupierMuestraPrimerCarta());
            controlador.esTurnoCrupier();
            controlador.evaluandoGanadores();
        }
    }
}
