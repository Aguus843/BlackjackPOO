package ar.edu.unlu.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jugador {
    private String nombre;
    private List<Mano> manos;
    private int puntaje;
    private Saldo saldo;
    private int apuesta;
    private int apuestaMano2;
    private boolean doblo;
    private boolean pagoSeguro;
    private Scanner scanner;
    private Mazo mazo;

    public Jugador(String nombre, int saldoInicial) {
        this.nombre = nombre;
        this.manos = new ArrayList<>();
        mazo = new Mazo();
        this.puntaje = 0;
        this.saldo = new Saldo(saldoInicial);
        this.apuesta = 0;
        this.apuestaMano2 = 0;
        this.doblo = false;
        this.pagoSeguro = false;
        this.scanner = new Scanner(System.in);
    }
    public Mazo getMazo(){
        return this.mazo;
    }
    public int getSaldo(){
        return saldo.getSaldo();
    }
    public void ajustarSaldo(int monto){
        if (monto > 0){
            saldo.agregarSaldo(monto);
            System.out.println("Saldo agregado: " + monto);
        }else{
            // saldo.retirarSaldo(-monto);
            if (!saldo.retirarSaldo(-monto)) throw new IllegalArgumentException("[!] Monto insuficiente para hacer la apuesta.");
        }
    }


    // Metodo que reparte a UNA mano.
    public void repartirCartaAMano(int indexMano, Carta carta){
        List<Mano> manos = getManos();
        if (indexMano  >= 0 && indexMano < manos.size()){
            manos.get(indexMano).recibirCarta(carta);
        }else System.out.println("El indice de mano no es valido.");
    }
    // Metodo que reparte a ambas manos
    public void repartirATodasLasManos(Carta carta){
        for (Mano mano : manos){
            mano.recibirCarta(carta);
        }
    }

    public void mostrarSaldo(){
        System.out.println("El jugador " + nombre + " tiene un saldo de: " + saldo.getSaldo());
    }
    public String getNombre() {
        return this.nombre;
    }
    public int getApuesta(){
        return this.apuesta;
    }
    public int getApuestaMano2(){
        return this.apuestaMano2;
    }
    public void setApuestaMano2(int monto){
        this.apuestaMano2 = monto;
    }
    public void setApuesta(int monto){
        this.apuesta = monto;
    }
    public boolean getPagoSeguro(){
        return this.pagoSeguro;
    }
    public void agregarMano(){
        manos.add(new Mano());
    }
    public void iniciarMano(){
        manos.clear();
        manos.add(new Mano());
    }
    // Getter de mano actual (Cuando NO hay division)
    public Mano getManoActual(){
        return manos.getFirst();
    }
    public Mano getMano2(){
        return manos.get(1);
    }
    // Getter de Manos
    public List<Mano> getManos(){
        return manos;
    }
    public boolean multiplesManos(){
        return manos.size() > 1;
    }

    public void mostrarManos(){
        int cantManos = getManos().size();
        if (cantManos == 0) return;
        if (cantManos == 1){
            int sumatoriaPuntaje = 0;
            System.out.println(getNombre() + " tiene las siguientes cartas:");
            Mano mano = getManoActual();
            for (Carta carta : getManoActual().getMano()) {
                System.out.printf("%s de %s\n", carta.getValor(), carta.getPalo());
                sumatoriaPuntaje += carta.getValorNumerico();
                manos.getFirst().actualizarPuntaje();
                manos.getFirst().setPuntaje(manos.getFirst().getPuntaje());
            }
            if (manos.getFirst().tieneAs() && sumatoriaPuntaje <= 20){
                System.out.printf("El puntaje actual es de: %d/%d\n", manos.getFirst().getPuntaje()-10, manos.getFirst().getPuntaje());
            }else System.out.println("El puntaje actual es de: " + manos.getFirst().getPuntaje());
            System.out.println("===========================================");

        }else mostrarManosDivididas();
    }

    public void mostrarManosDivididas(){
        int sumatoriaPuntaje1 = 0;
        int sumatoriaPuntaje2 = 0;
        System.out.println("===================================================");
        System.out.println(getNombre() + " tiene las siguientes cartas en ambas manos:");
        Carta cartasMano1;
        Carta cartasMano2;

        int cantidadCartas = Math.max(getManoActual().getMano().size(), getMano2().getMano().size());
        System.out.printf("-= MANO 1=-\t\t\t\t\t %-17s\n", "-= MANO 2 =-");
        for (int i = 0; i < cantidadCartas; i++){
            if (i < getManoActual().getMano().size()){
                cartasMano1 = getManoActual().getMano().get(i);
                System.out.printf("%s %s", cartasMano1.getValor(), cartasMano1.getPalo());
                sumatoriaPuntaje1 += cartasMano1.getValorNumerico();
                manos.getFirst().setPuntaje(sumatoriaPuntaje1);
            }else System.out.printf("%-20s\n", "");
            if (i < getMano2().getMano().size()){
                cartasMano2 = getMano2().getMano().get(i);
                System.out.printf("%-15s %s de %s\n", "", cartasMano2.getValor(), cartasMano2.getPalo());
                sumatoriaPuntaje2 += cartasMano2.getValorNumerico();
                manos.getFirst().setPuntaje(sumatoriaPuntaje2);
            }else System.out.printf("%-15s\n", "");
        }
        if (getManoActual().tieneAs() && sumatoriaPuntaje1 <= 20){
            System.out.printf("El puntaje actual de la mano %d es de: %d/%d\n", 1, getManoActual().getPuntaje()-10, getManoActual().getPuntaje());
        }else System.out.printf("El puntaje actual de la mano %d es de: %d\n", 1, getManoActual().getPuntaje());
        if (getMano2().tieneAs() && sumatoriaPuntaje2 <= 20){
            System.out.printf("El puntaje actual de la mano %d es de: %d/%d\n", 2, getMano2().getPuntaje()-10, getManoActual().getPuntaje());
        }else System.out.printf("El puntaje actual de la mano %d es de: %d\n", 2, getMano2().getPuntaje());
        System.out.println("===================================================");
    }

    public boolean puedeDividir(){
        Mano mano = getManoActual();
        if (mano != null){
            if (mano.getMano().getFirst().getValor().equals(mano.getMano().get(1).getValor())){
                return true;
            }
        }
        return false;
    }
    public void setPagoSeguro(boolean b) {
        this.pagoSeguro = b;
    }
    public boolean tieneBlackjack(){
//        for (Mano mano : manos){
//            if (mano != null){
//                if ((mano.getMano().getFirst().equals("A")) && (mano.getMano().get(1).getValor().equals("10") || mano.getMano().get(1).getValor().equals("J") || mano.getMano().get(1).getValor().equals("Q") || mano.getMano().get(1).getValor().equals("K"))){
//                    return true;
//                }else return (mano.getMano().getFirst().getValor().equals("10") || mano.getMano().getFirst().getValor().equals("J") || mano.getMano().getFirst().getValor().equals("Q") || mano.getMano().getFirst().getValor().equals("K")) && mano.getMano().get(1).getValor().equals("A");
//            }
//        }
        List<Mano> manos = getManos();
        for (int i = 0; i < manos.size(); i++){
            if ((manos.get(i).getMano().getFirst().equals("A")) && (manos.get(i).getMano().get(1).equals("10") || manos.get(i).getMano().get(1).equals("J") || manos.get(i).getMano().get(1).equals("Q") || manos.get(i).getMano().get(1).equals("K"))){
                return true;
            }else return (manos.get(i).getMano().getFirst().equals("10") || manos.get(i).getMano().getFirst().equals("J") || manos.get(i).getMano().getFirst().equals("Q") || manos.get(i).getMano().getFirst().equals("K") && manos.get(i).getMano().get(1).getValor().equals("A"));
        }
        return false;
    }
}
