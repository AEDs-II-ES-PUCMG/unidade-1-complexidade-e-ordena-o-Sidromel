import java.util.Random;
import java.util.Scanner;

public class App {
    static final int[] tamanhosTesteGrande =  { 31_250_000, 62_500_000, 125_000_000, 250_000_000, 500_000_000 };
    static final int[] tamanhosTesteMedio =   {     12_500,     25_000,      50_000,     100_000,     200_000 };
    static final int[] tamanhosTestePequeno = {          3,          6,          12,          24,          48 };
    static Random aleatorio = new Random();
    static long operacoes;
    static double nanoToMilli = 1.0/1_000_000;
    

    /**
     * Gerador de vetores aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static int[] gerarVetor(int tamanho){
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1, tamanho/2);
        }
        return vetor;        
    }

    /**
     * Gerador de vetores de objetos do tipo Integer aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor de Objetos Integer com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static Integer[] gerarVetorObjetos(int tamanho) {
        Integer[] vetor = new Integer[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1, 10 * tamanho);
        }
        return vetor;
    }


    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int opcao = -1;

        for (int n : tamanhosTesteMedio) {
            Integer[] vetorBase = gerarVetorObjetos(n);

            while (opcao != 0) {
                System.out.println("\n=========================================");
                System.out.println("      SISTEMA DE AVALIAÇÃO DE MÉTODOS     ");
                System.out.println("=========================================");
                System.out.println(" [1] BubbleSort");
                System.out.println(" [2] InsertionSort");
                System.out.println(" [3] SelectionSort");
                System.out.println(" [4] MergeSort");
                System.out.println(" [5] Sair do Programa");
                System.out.println("-----------------------------------------");
                System.out.print("Escolha o método que deseja utilizar: ");
                
                opcao = teclado.nextInt();
                IOrdenador<Integer> ordenador = null;
                
                switch (opcao) {
                    case 1: ordenador = new BubbleSort<>(); break;
                    case 2: ordenador = new InsertionSort<>(); break;
                    case 3: ordenador = new SelectionSort<>(); break;
                    case 4: ordenador = new MergeSort<>(); break;
                    case 5: System.out.println("\nEncerrando sistema..."); continue;
                    default: System.out.println("\nOpção inválida! Tente novamente."); continue;
                }
                if (ordenador != null) {
                    Integer[] vetorParaTeste = vetorBase.clone();
                    ordenador.ordenar(vetorParaTeste);

                    System.out.println("-----------------------------------------");
                    System.out.println("\n>>> RESULTADOS DA ORDENAÇÃO <<<");
                    System.out.println("-----------------------------------------");
                    System.out.printf(" Método:         %s\n", ordenador.getClass().getSimpleName());
                    System.out.printf(" Comparações:    %,d\n", ordenador.getComparacoes());
                    System.out.printf(" Movimentações:  %,d\n", ordenador.getMovimentacoes());
                    System.out.printf(" Tempo Gasto:    %.4f ms\n", ordenador.getTempoOrdenacao());
                    System.out.println("-----------------------------------------");
                }
            }
        }
        teclado.close();
    }
}