import java.util.Arrays;

public class MergeSort<T extends Comparable<T>> implements IOrdenador<T> {
    private int comparacoes;
    private int movimentacoes;
    private double tempoOrdenacao;
    private double inicio;

    private double nanoToMilli = 1.0/1_000_000;

    @Override
    public int getComparacoes() {
        return comparacoes;
    }

    @Override
    public int getMovimentacoes() {
        return movimentacoes;
    }

    @Override
    public double getTempoOrdenacao() {
        return tempoOrdenacao;
    }

    private void iniciar(){
        this.comparacoes = 0;
        this.movimentacoes = 0;
        this.inicio = System.nanoTime();
    }

    private void terminar(){
        this.tempoOrdenacao = (System.nanoTime() - this.inicio) * nanoToMilli;
    }

    @Override
    public T[] ordenar(T[] dados) {
        T[] dadosOrdenados = Arrays.copyOf(dados, dados.length);
        iniciar();
        mergeSort(dadosOrdenados, 0, dadosOrdenados.length - 1);
        terminar();
        return dadosOrdenados;
    }

    private void mergeSort(T[] array, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSort(array, esq, meio);
            mergeSort(array, meio + 1, dir);
            intercalar(array, esq, meio, dir);
        }
    }

    private void intercalar(T[] array, int esq, int meio, int dir) {
        int n1, n2, i, j, k;
        n1 = meio - esq + 1;
        n2 = dir - meio;

        T[] a1 = Arrays.copyOfRange(array, esq, meio + 1);
        T[] a2 = Arrays.copyOfRange(array, meio + 1, dir + 1);
        movimentacoes += (n1 + n2);

        for (i = j = 0, k = esq; (i < n1 && j < n2); k++) {
            comparacoes++;
            if (a1[i].compareTo(a2[j]) <= 0) {
                array[k] = a1[i++];
            } else {
                array[k] = a2[j++];
            }
            movimentacoes++;
        }
        if (i == n1) {
            for (; k <= dir; k++) {
                array[k] = a2[j++];
                movimentacoes++;
            }
        } else {
            for (; k <= dir; k++) {
                array[k] = a1[i++];
                movimentacoes++;
            }
        }
    }
}