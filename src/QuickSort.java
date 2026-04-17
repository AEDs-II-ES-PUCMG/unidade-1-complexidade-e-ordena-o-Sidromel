import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Quicksort com partição de Lomuto (pivô na última posição do intervalo), alinhado ao material usual de AED2.
 */
public class QuickSort<T extends Comparable<T>> implements IOrdenador<T> {

    private long comparacoes;
    private long movimentacoes;
    private LocalDateTime inicio;
    private LocalDateTime termino;
    private Comparator<T> comparador;

    public QuickSort() {
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
        inicio = LocalDateTime.now();
        if (dadosOrdenados.length > 0) {
            ordenarRecursivo(dadosOrdenados, 0, dadosOrdenados.length - 1);
        }
        termino = LocalDateTime.now();
        return dadosOrdenados;
    }

    private void ordenarRecursivo(T[] vetor, int inicioVet, int fimVet) {
        if (inicioVet < fimVet) {
            int pivo = particionar(vetor, inicioVet, fimVet);
            ordenarRecursivo(vetor, inicioVet, pivo - 1);
            ordenarRecursivo(vetor, pivo + 1, fimVet);
        }
    }

    private int particionar(T[] vetor, int inicioVet, int fimVet) {
        T pivo = vetor[fimVet];
        int i = inicioVet - 1;
        for (int j = inicioVet; j < fimVet; j++) {
            comparacoes++;
            if (comparador.compare(vetor[j], pivo) <= 0) {
                i++;
                if (i != j) {
                    trocar(vetor, i, j);
                }
            }
        }
        trocar(vetor, i + 1, fimVet);
        return i + 1;
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
