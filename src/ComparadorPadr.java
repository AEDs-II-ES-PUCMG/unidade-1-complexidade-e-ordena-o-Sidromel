import java.util.Comparator;

public class ComparadorPadr implements Comparator<Produto> {
    @Override
    public int compare(Produto p1, Produto p2) {
        // Obtém o tamanho do texto de cada produto
        int tamanho1 = p1.toString().length();
        int tamanho2 = p2.toString().length();

        // Compara os dois inteiros
        // Retorna: negativo se p1 < p2, zero se iguais, positivo se p1 > p2
        return Integer.compare(tamanho1, tamanho2);
    }
}