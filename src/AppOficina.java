
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * MIT License
 *
 * Copyright(c) 2022-25 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class AppOficina {

    static final int MAX_PEDIDOS = 100;
    static Produto[] produtos;
    /** Cópia ordenada por identificador (Tarefa 2 — busca binária). */
    static Produto[] produtosPorCodigo;
    /** Cópia ordenada por descrição (Tarefa 2 — busca binária). */
    static Produto[] produtosPorDescricao;
    static int quantProdutos = 0;
    static String nomeArquivoDados = "produtos.txt";
    static IOrdenador<Produto> ordenador;

    // #region utilidades
    static Scanner teclado;

    static <T extends Number> T lerNumero(String mensagem, Class<T> classe) {
        System.out.print(mensagem + ": ");
        T valor;
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Tecle Enter para continuar.");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS COMÉRCIO DE COISINHAS v0.2\n================");
    }

    static int exibirMenuPrincipal() {
        cabecalho();
        System.out.println("1 - Procurar produto");
        System.out.println("2 - Filtrar produtos por preço máximo");
        System.out.println("3 - Ordenar produtos");
        System.out.println("4 - Embaralhar produtos");
        System.out.println("5 - Listar produtos");
        System.out.println("0 - Finalizar");

        Integer op = lerNumero("Digite sua opção", Integer.class);
        return op == null ? -1 : op;
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.out.println("5 - Heapsort");
        System.out.println("6 - Quicksort");
        System.out.println("0 - Voltar");

        Integer op = lerNumero("Digite sua opção", Integer.class);
        return op == null ? -1 : op;
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Padrão (descrição)");
        System.out.println("2 - Por código (identificador)");

        Integer op = lerNumero("Digite sua opção", Integer.class);
        return op == null ? -1 : op;
    }

    static int exibirMenuBuscaProduto() {
        cabecalho();
        System.out.println("1 - Por identificador (busca binária)");
        System.out.println("2 - Por descrição exata (busca binária)");

        Integer op = lerNumero("Digite sua opção", Integer.class);
        return op == null ? -1 : op;
    }

    // #endregion

    static Produto[] carregarProdutos(String nomeArquivo) {
        Scanner dados;
        Produto[] dadosCarregados;
        quantProdutos = 0;
        try {
            dados = new Scanner(new File(nomeArquivo));
            int tamanho = Integer.parseInt(dados.nextLine());

            dadosCarregados = new Produto[tamanho];
            while (dados.hasNextLine()) {
                Produto novoProduto = Produto.criarDoTexto(dados.nextLine());
                dadosCarregados[quantProdutos] = novoProduto;
                quantProdutos++;
            }
            dados.close();
        } catch (FileNotFoundException fex) {
            System.out.println("Arquivo não encontrado. Produtos não carregados");
            dadosCarregados = null;
        }
        return dadosCarregados;
    }

    static void atualizarIndicesOrdenados() {
        if (produtos == null || quantProdutos <= 0) {
            produtosPorCodigo = new Produto[0];
            produtosPorDescricao = new Produto[0];
            return;
        }
        Produto[] base = Arrays.copyOf(produtos, quantProdutos);
        IOrdenador<Produto> merge = new MergeSort<>();
        produtosPorCodigo = merge.ordenar(Arrays.copyOf(base, quantProdutos), new ComparadorPorCodigo());
        produtosPorDescricao = merge.ordenar(Arrays.copyOf(base, quantProdutos), Comparator.naturalOrder());
    }

    static Produto buscarPorIdentificador(int id) {
        if (produtosPorCodigo == null || quantProdutos == 0) {
            return null;
        }
        int lo = 0;
        int hi = quantProdutos - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = Integer.compare(id, produtosPorCodigo[mid].getCodigoIdentificador());
            if (cmp == 0) {
                return produtosPorCodigo[mid];
            }
            if (cmp < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return null;
    }

    static Produto buscarPorDescricaoExata(String descricao) {
        if (produtosPorDescricao == null || quantProdutos == 0 || descricao == null) {
            return null;
        }
        int lo = 0;
        int hi = quantProdutos - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = descricao.compareTo(produtosPorDescricao[mid].getDescricao());
            if (cmp == 0) {
                return produtosPorDescricao[mid];
            }
            if (cmp < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return null;
    }

    static Produto localizarProduto() {
        cabecalho();
        System.out.println("Localizando um produto");
        int opcaoBusca = exibirMenuBuscaProduto();
        if (opcaoBusca == 0 || opcaoBusca == -1) {
            return null;
        }
        if (opcaoBusca == 1) {
            Integer numero = lerNumero("Digite o identificador do produto", Integer.class);
            if (numero == null) {
                return null;
            }
            return buscarPorIdentificador(numero);
        }
        if (opcaoBusca == 2) {
            System.out.print("Digite a descrição exata do produto: ");
            String texto = teclado.nextLine();
            return buscarPorDescricaoExata(texto.trim());
        }
        System.out.println("Opção inválida.");
        return null;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        String mensagem = "Dados inválidos";

        if (produto != null) {
            mensagem = String.format("Dados do produto:\n%s", produto);
        }

        System.out.println(mensagem);
    }

    private static void filtrarPorPrecoMaximo() {
        cabecalho();
        if (produtos == null || quantProdutos == 0) {
            System.out.println("Não há produtos carregados.");
            return;
        }
        System.out.println("Filtrando por valor máximo:");
        Double valor = lerNumero("valor", Double.class);
        if (valor == null) {
            return;
        }
        StringBuilder relatorio = new StringBuilder();
        for (int i = 0; i < quantProdutos; i++) {
            if (produtos[i].valorDeVenda() < valor) {
                relatorio.append(produtos[i]).append("\n");
            }
        }
        System.out.println(relatorio.toString());
    }

    static void ordenarProdutos() {
        cabecalho();
        if (produtos == null || quantProdutos == 0) {
            System.out.println("Não há produtos carregados.");
            return;
        }

        int opcaoOrdenador = exibirMenuOrdenadores();
        if (opcaoOrdenador == 0 || opcaoOrdenador == -1) {
            return;
        }

        int opcaoComparador = exibirMenuComparadores();
        if (opcaoComparador != 1 && opcaoComparador != 2) {
            System.out.println("Critério inválido.");
            return;
        }

        Comparator<Produto> comparador = opcaoComparador == 1
                ? Comparator.naturalOrder()
                : new ComparadorPorCodigo();

        ordenador = switch (opcaoOrdenador) {
            case 1 -> new Bubblesort<>();
            case 2 -> new InsertSort<>();
            case 3 -> new SelectionSort<>();
            case 4 -> new MergeSort<>();
            case 5 -> new HeapSort<>();
            case 6 -> new QuickSort<>();
            default -> null;
        };

        if (ordenador == null) {
            System.out.println("Ordenador inválido.");
            return;
        }

        Produto[] copia = Arrays.copyOf(produtos, quantProdutos);
        Produto[] ordenados = ordenador.ordenar(copia, comparador);

        System.out.printf(
                "Ordenação concluída.%nComparações: %d%nMovimentações: %d%nTempo (ms): %.6f%n",
                ordenador.getComparacoes(),
                ordenador.getMovimentacoes(),
                ordenador.getTempoOrdenacao());

        verificarSubstituicao(ordenados);
    }

    static void verificarSubstituicao(Produto[] copiaOrdenada) {
        cabecalho();
        System.out.print("Deseja sobrescrever os dados originais pelos ordenados (S/N)? ");
        String resposta = teclado.nextLine().trim().toUpperCase();
        if (resposta.equals("S")) {
            System.arraycopy(copiaOrdenada, 0, produtos, 0, quantProdutos);
            atualizarIndicesOrdenados();
        }
    }

    static void embaralharProdutos() {
        if (produtos == null || quantProdutos == 0) {
            return;
        }
        Collections.shuffle(Arrays.asList(produtos).subList(0, quantProdutos));
    }

    static void listarProdutos() {
        cabecalho();
        if (produtos == null || quantProdutos == 0) {
            System.out.println("Não há produtos carregados.");
            return;
        }
        for (int i = 0; i < quantProdutos; i++) {
            System.out.println(produtos[i]);
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in);

        produtos = carregarProdutos(nomeArquivoDados);
        if (produtos != null) {
            atualizarIndicesOrdenados();
        }
        if (produtos != null && quantProdutos > 0) {
            embaralharProdutos();
        }

        int opcao = -1;

        do {
            opcao = exibirMenuPrincipal();
            switch (opcao) {
                case 1 -> mostrarProduto(localizarProduto());
                case 2 -> filtrarPorPrecoMaximo();
                case 3 -> ordenarProdutos();
                case 4 -> embaralharProdutos();
                case 5 -> listarProdutos();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
                default -> {
                    if (opcao != 0) {
                        System.out.println("Opção inválida.");
                    }
                }
            }
            pausa();
        } while (opcao != 0);
        teclado.close();
    }
}
