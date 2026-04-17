import java.util.Comparator;

public class ComparadorPadrao implements Comparator<Produto> {
    @Override
    public int compare(Produto p1, Produto p2) {
        int tamanho1 = p1.toString().length();
        int tamanho2 = p2.toString().length();
        return Integer.compare(tamanho1, tamanho2);
    }
}