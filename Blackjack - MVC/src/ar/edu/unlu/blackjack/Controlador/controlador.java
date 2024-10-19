package ar.edu.unlu.blackjack.Controlador;
import ar.edu.unlu.blackjack.Modelo.*;

import java.util.List;

public class controlador {
    public Blackjack_SaldoYManos modelo;

    public controlador(){
        this.modelo = new Blackjack_SaldoYManos();
    }

    public void configurarJugadores(){
        modelo.configurarJugadores();
    }
    public boolean realizarApuesta(Jugador obtenerJugadorActual){
        return modelo.realizarApuesta(obtenerJugadorActual);
    }
    public void crupierPideCarta(){
        modelo.crupierPideCarta();
    }
    public void repartirCartasIniciales(Jugador jugadorActual){
        modelo.repartirCartasIniciales(jugadorActual);
    }
    public String crupierMuestraPrimerCarta(){
        return modelo.crupierMuestraPrimerCarta();
    }
    public Jugador obtenerJugadorActual(){
        return modelo.getJugadorActualTurno();
    }
    public void turnoJugador(Jugador obtenerJugadorActual){
        modelo.turnoJugador(obtenerJugadorActual);
    }
    public boolean terminoRonda(String ingreso){
        return modelo.getJugadorActualTurno().consultarSeguirJugando(ingreso);
    }
    public String cartasRestantes(){
        return modelo.ControladorCartasRestantes();
    }
    public void esTurnoCrupier(){
        modelo.turnoCrupier();
    }
    public void evaluandoGanadores(){
        modelo.evaluarGanadores();
    }
    public String ingresarPorTeclado(){
        return modelo.ingresarPorTeclado();
    }
    public void cambiarTurnoJugador(){
        modelo.cambiarTurno();
    }
    public int getIndiceJugadores(){
        return modelo.getIndice();
    }
    public int getCantidadJugadoresTotal(){
        return modelo.getCantidadJugadores();
    }
    public void cargarApuestaJugador(Jugador jugador){
        modelo.cargarApuesta(jugador);
    }
    public boolean crupierTieneCarta(){
        return modelo.hasCrupierCard();
    }

}
