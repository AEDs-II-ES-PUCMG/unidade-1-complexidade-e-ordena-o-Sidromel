import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Ordenação por heap (flutuação / construção do heap), no mesmo estilo dos demais ordenadores do laboratório.
 */
public class HeapSort<T extends Comparable<T>> implements IOrdenador<T> {

    private long comparacoes;
    private long movimentacoes;
    private LocalDateTime inicio;
    private LocalDateTime termino;
    private Comparator<T> comparador;

    public HeapSort() {
        comparacoes = 0;
        movimentacoes = 0;
    }

    @Override
    public T[] ordenar(T[] dados) {
        return ordenar(dados, T::compareTo);
    }

    @Override
    public T[] ordenar(T[] dados, Comparator<T> comparador) {
        this.comparador = comparador;
        comparacoes = 0;
        movimentacoes = 0;
        T[] dadosOrdenados = Arrays.copyOf(dados, dados.length);
        int n = dadosOrdenados.length;
        inicio = LocalDateTime.now();

        for (int i = n / 2 - 1; i >= 0; i--) {
            afundar(dadosOrdenados, n, i);
        }
        for (int fim = n - 1; fim > 0; fim--) {
            trocar(dadosOrdenados, 0, fim);
            afundar(dadosOrdenados, fim, 0);
        }

        termino = LocalDateTime.now();
        return dadosOrdenados;
    }

    private void afundar(T[] vetor, int tamanhoHeap, int indice) {
        int maior = indice;
        int esq = 2 * indice + 1;
        int dir = 2 * indice + 2;

        if (esq < tamanhoHeap) {
            comparacoes++;
            if (comparador.compare(vetor[esq], vetor[maior]) > 0) {
                maior = esq;
            }
        }
        if (dir < tamanhoHeap) {
            comparacoes++;
            if (comparador.compare(vetor[dir], vetor[maior]) > 0) {
                maior = dir;
            }
        }
        if (maior != indice) {
            trocar(vetor, indice, maior);
            afundar(vetor, tamanhoHeap, maior);
        }
    }

    private void trocar(T[] vetor, int i, int j) {
        movimentacoes++;
        T tmp = vetor[i];
        vetor[i] = vetor[j];
        vetor[j] = tmp;
    }

    @Override
    public long getComparacoes() {
        return comparacoes;
    }

    @Override
    public long getMovimentacoes() {
        return movimentacoes;
    }

    @Override
    public double getTempoOrdenacao() {
        if (inicio == null || termino == null) {
            return 0;
        }
        return Duration.between(inicio, termino).toNanos() / 1_000_000.0;
    }
}
