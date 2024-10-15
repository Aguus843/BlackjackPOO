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
            for (Carta carta : getManoActual().getMano()) {
                System.out.printf("%s de %s\n", carta.getValor(), carta.getPalo());
                sumatoriaPuntaje += carta.getValorNumerico();
                manos.getFirst().setPuntaje(sumatoriaPuntaje);
            }
            if (manos.getFirst().tieneAs() && sumatoriaPuntaje <= 20){
                System.out.printf("El puntaje actual es de: %d/%d\n", manos.getFirst().getPuntaje()-10, manos.getFirst().getPuntaje());
            }else System.out.println("El puntaje actual es de: " + manos.getFirst().getPuntaje());
            System.out.println("===========================================");

        }else{
            int sumatoriaPuntaje1 = 0;
            int sumatoriaPuntaje2 = 0;
            System.out.println(getNombre() + " tiene las siguientes cartas en ambas manos:");
            Carta cartasMano1;
            Carta cartasMano2;
            int debugManos = getManoActual().getMano().size();
            System.out.println(debugManos);
            for (int i = 0; i < getManoActual().getMano().size(); i++) {
                Carta cartaMano = getManoActual().getMano().get(i);
                System.out.printf("%s de %s\t\t\t\t", cartaMano.getValor(), cartaMano.getPalo());
                sumatoriaPuntaje1 += cartaMano.getValorNumerico();
                manos.getFirst().setPuntaje(sumatoriaPuntaje1);
                for (int j = 0; j < getMano2().getMano().size(); j++) {
                    Carta cartaMano2 = getMano2().getMano().get(j);
                    System.out.printf("%s de %s\n", cartaMano2.getValor(), cartaMano2.getPalo());
                    sumatoriaPuntaje2 += cartaMano2.getValorNumerico();
                    manos.get(1).setPuntaje(sumatoriaPuntaje2);
                }
            }
//            for (Carta carta : getManoActual().getMano()) {
//                System.out.printf("%s de %s\t\t\t\t", carta.getValor(), carta.getPalo());
//                sumatoriaPuntaje1 += carta.getValorNumerico();
//                for (Carta cartaMano2 : getMano2().getMano()) {
//                    System.out.printf("%s de %s\n", cartaMano2.getValor(), cartaMano2.getPalo());
//                    sumatoriaPuntaje2 += cartaMano2.getValorNumerico();
//                }
//            }
            for (int i = 0; i < 2; i++){
                if (manos.get(i).tieneAs() && sumatoriaPuntaje1 < 21){
                    // System.out.printf("El puntaje actual de la mano %d es de: %d/%d\n", i+1, this.puntaje-10, this.puntaje);
                    System.out.printf("El puntaje actual de la mano %d es de: %d/%d\n", i+1, manos.get(i).getPuntaje()-10, manos.get(i).getPuntaje());
                }else System.out.printf("El puntaje actual de la mano %d es de: %d\n", i+1, manos.get(i).getPuntaje());
            }
                System.out.println("===========================================");
        }
    }

    public int seguroBlackjack(Jugador jugador){
        int ingreso = -1;
        System.out.println("El crupier tiene un As de primer carta.");
        System.out.printf("Ingrese '1' para pagar el seguro o '0' para no pagar el seguro ($%d): ", jugador.apuesta/2);
        ingreso = scanner.nextInt();
        while (ingreso != 1 || ingreso != 0){
            if (ingreso != 1 || ingreso != 0){
                System.out.println("[!] El numero ingresado no corresponde ni a '1' ni '0'.");
            }
            System.out.println("Ingrese '1' para pagar el seguro o '0' para no pagar el seguro: ");
            ingreso = scanner.nextInt();
        }
        if (ingreso == 1) pagoSeguro = true; // pagoSeguro queda guardado para el jugador
        return ingreso;
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
