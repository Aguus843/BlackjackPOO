package ar.edu.unlu.blackjack.Vista;

import ar.edu.unlu.blackjack.Modelo.Jugador;
import ar.edu.unlu.blackjack.Controlador.controlador;

import java.util.Scanner;

public class VistaConsola {
    private final controlador controlador;
    Scanner scanner;

    public VistaConsola() {
        this.controlador = new controlador();
        scanner = new Scanner(System.in);
    }

    public void mostrarMensajeConSaltoLinea(String mensaje) {
        System.out.println(mensaje);
    }
    public void mostrarMensaje(String mensaje){
        System.out.printf("%s", mensaje);
    }

    public String ingresarTexto() {
        return scanner.nextLine();
    }

    public String mostrarCartas() {
        return controlador.cartasRestantes();
    }

    public void iniciarJuego() {
        boolean seCargoJugadorYApuestas = false;
        mostrarMensajeConSaltoLinea("Bienvenido al Blackjack!");
        controlador.configurarJugadores();
        // while (){
        while (controlador.getIndiceJugadores() != controlador.getCantidadJugadoresTotal()) {
            while (!seCargoJugadorYApuestas){
                mostrarMensajeConSaltoLinea(controlador.obtenerJugadorActual().getNombre() + " tu saldo actual es de " + controlador.obtenerJugadorActual().getSaldo() + " pesos.");
                while (controlador.getIndiceJugadores() != controlador.getCantidadJugadoresTotal()) {
                    mostrarMensaje(controlador.obtenerJugadorActual().getNombre() + ": ingrese el monto a apostar: ");
                    controlador.cargarApuestaJugador(controlador.obtenerJugadorActual());
                    controlador.cambiarTurnoJugador();
                }
                if (controlador.getIndiceJugadores() == controlador.getCantidadJugadoresTotal()) {
                    controlador.cambiarTurnoJugador(); // Pongo el indice en cero para mostrar las cartas de todos los jugadores
                }
                while (controlador.getIndiceJugadores() != controlador.getCantidadJugadoresTotal()) {
                    controlador.repartirCartasIniciales(controlador.obtenerJugadorActual());
                    controlador.cambiarTurnoJugador();
                }
                if (controlador.getIndiceJugadores() == controlador.getCantidadJugadoresTotal()) {
                    controlador.cambiarTurnoJugador(); // Pongo el indice en cero nuevamente para que los jugadores puedan jugar
                }
                seCargoJugadorYApuestas = true;
            }
            mostrarMensajeConSaltoLinea("Es el turno de: " + controlador.obtenerJugadorActual().getNombre());
            mostrarMensajeConSaltoLinea("El saldo del jugador es de " + controlador.obtenerJugadorActual().getSaldo());
            if (controlador.crupierTieneCarta() == false){
                controlador.crupierPideCarta();
                controlador.crupierPideCarta();
            }
            mostrarMensajeConSaltoLinea("Cartas restantes: " + mostrarCartas());
            mostrarMensajeConSaltoLinea("El crupier tiene " + controlador.crupierMuestraPrimerCarta());
            controlador.turnoJugador(controlador.obtenerJugadorActual());
            // controlador.cambiarTurnoJugador();
            // Cambia el turno al siguiente para que cargue la apuesta correspondiente
            // El juego no puede comenzar si todos los jugadores no han apostado
        }
            controlador.esTurnoCrupier();
            controlador.evaluandoGanadores();
        // }
    }
    public static void main(String[] args) {
        VistaConsola vistaConsola = new VistaConsola();
        vistaConsola.iniciarJuego();
    }
}
