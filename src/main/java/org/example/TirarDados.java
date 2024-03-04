package org.example;

import java.util.concurrent.Semaphore;;

public class TirarDados extends Thread {

    private static String DADO1 = "Dado 1";
    private static String DADO2 = "Dado 2";

    public static void main(String[] args) {

        // Objeto Buzon para la comunicación entre hilos
        Buzon l_Buzon = new Buzon();

        // Creación de los objetos Dado.
        Dado l_ObjRunnable1 = new Dado(l_Buzon, DADO1);
        Dado l_ObjRunnable2 = new Dado(l_Buzon, DADO2);

        // Creación de los hilos.
        Thread l_HiloDado1 = new Thread(l_ObjRunnable1);
        Thread l_HiloDado2 = new Thread(l_ObjRunnable2);

        // Inicio de la ejecución de los hilos.
        l_HiloDado1.start();
        l_HiloDado2.start();
    }
}

// Clase Dado para generar las tiradas y números.
class Dado implements Runnable {
    private static final String DADO1 = "Dado 1";
    private static final String DADO2 = "Dado 2";
    private Buzon a_Buzon;
    private String a_Nombre;

    public Dado(Buzon a_Buzon, String a_Nombre) {
        this.a_Buzon = a_Buzon;
        this.a_Nombre = a_Nombre;
    }

    @Override
    public void run() {

        // Bucle para repetir la ejecución de los dados hasta que no coincidan los números.
        while (!a_Buzon.a_Coincidencia) {

            // El semáforo adquiere el token.
            try {
                a_Buzon.l_Semaforo.acquire(1);

            } catch (InterruptedException e) {
                System.err.println("\n>>> Error al adquirir el toker del semáforo: " + e.getMessage());     // Informar del error producido.
            }

            int l_NumGenerado = (int) (Math.random() * 6) + 1;      // Generar número aleatorio del dado.

            // Comprobar qué dado está tirando.
            if (a_Nombre.equals(DADO1)) {
                a_Buzon.a_NumDado1 = l_NumGenerado;     // Almacenar el número en el buzón.

            } else if (a_Nombre.equals(DADO2)) {

                a_Buzon.a_NumDado2 = l_NumGenerado;     // Almacenar el número en el buzón.

                // Imprimir los números de ambos dados.
                System.out.println("A: " + a_Buzon.a_NumDado1);
                System.out.println("B: " + a_Buzon.a_NumDado2);

                a_Buzon.a_NumRondas++;                                      // Aumentar el número de rondas tiradas.
                System.out.println("R: " + a_Buzon.a_NumRondas + "\n");     // Imprimir el número de ronda.

                // Mostrar el mensaje si se produce una coincidencia.
                if (a_Buzon.a_NumDado1 == a_Buzon.a_NumDado2) {
                    System.out.println(">>> Los números coinciden en la ronda: " + a_Buzon.a_NumRondas);
                    a_Buzon.a_Coincidencia = true;
                }
            }

            a_Buzon.l_Semaforo.release();       // Soltar el token del semáforo.
        }
    }
}


// Clase Buzón para almacenar las variables.
class Buzon {
    public int a_NumDado1;
    public int a_NumDado2;
    public int a_NumRondas = 0;
    public boolean a_Coincidencia = false;
    public Semaphore l_Semaforo = new Semaphore(1);
}