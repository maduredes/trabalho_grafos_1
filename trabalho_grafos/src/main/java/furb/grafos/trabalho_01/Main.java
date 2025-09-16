package furb.grafos.trabalho_01;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static List<String> verticesVisitados = new ArrayList<>();
    static List<String> verticesFinalizados = new ArrayList<>();
    static int tempoInicial = 1;
    static int tempoFinal;
    static Map<String, List<String>> verticesTempos = new HashMap<>();

    public static Map<String, List<String>> matrizAdjacencia(String[] conjuntoVertice, String[][] conjuntoArestas) {
        Map<String, List<String>> lista = new HashMap<>();

        for (int i = 0; i < conjuntoVertice.length; i++) {
            List<String> conjuntos = new ArrayList<>();
            for (int j = 0; j < conjuntoArestas.length; j++) {
                if (conjuntoVertice[i].equals(conjuntoArestas[j][0])) {
                    conjuntos.add(conjuntoArestas[j][1]);
                    lista.put(conjuntoVertice[i], conjuntos);
                }
            }
        }
        return lista;
    }

    public static boolean isRegular(Map<String, List<String>> matrizAdjacencia) {

        boolean isRegular = matrizAdjacencia.values()
                .stream()
                .map(List::size)
                .allMatch(grau -> grau.equals(matrizAdjacencia.entrySet().iterator().next().getValue().size()));

        if (isRegular) {
            System.out.println("O grafo é regular");
        } else {
            System.out.println("O grafo não é regular");
        }
        return isRegular;
    }

    public static void isCompleto(Map<String, List<String>> matrizAdjacencia) {

        if (isRegular(matrizAdjacencia)) {
            System.out.println("O grafo é completo");
        } else {
            System.out.println("O grafo não é completo");
        }
    }


    public static boolean isNulo(Map<String, List<String>> matrizAdjacencia) {
        boolean isNulo = matrizAdjacencia.values()
                .stream()
                .map(List::size)
                .allMatch(grau -> grau.equals(0));

        if (isNulo) {
            System.out.println("O grafo é nulo");
        } else {
            System.out.println("O grafo não é nulo");
        }

        return isNulo;
    }

    public static void isMultigrafo(Map<String, List<String>> matrizAdjacencia) {
        boolean isMultigrafo = false;

        // É MULTIGRAFO SE TIVER ARESTA PARALELA OU LAÇOS
        for (Map.Entry<String, List<String>> vertices : matrizAdjacencia.entrySet()) {
            if (!isMultigrafo) {
                String vertice = vertices.getKey();
                List<String> arestas = vertices.getValue();
                List<String> arestasPercorridas = new ArrayList<>();

                //verificando se tem laço
                if (arestas.contains(vertice)) {
                    isMultigrafo = true;
                    break;
                }

                //verificando se tem aresta paralela
                for (String aresta : arestas) {
                    if (arestasPercorridas.contains(aresta)) {
                        isMultigrafo = true;
                        break;
                    } else {
                        arestasPercorridas.add(aresta);
                    }
                }

            } else {
                break;
            }
        }
        if (isMultigrafo) {
            System.out.println("O grafo é multigrafo");
        } else {
            System.out.println("O grafo não é multigrafo");
        }
    }

    public static void isDirigido(String[][] conjuntoArestas) {
        boolean isDirigido = false;
        Set<String> arestasVistas = new HashSet<>();
        for (String[] aresta : conjuntoArestas) {
            String arestaInvertida = String.format("[%s, %s]", aresta[1], aresta[0]);
            String arestaString = Arrays.toString(aresta);
            if (!arestasVistas.isEmpty() && arestasVistas.contains(arestaInvertida)) {
                isDirigido = true;
                break;
            } else {
                arestasVistas.add(arestaString);
            }
        }
        if (isDirigido) {
            System.out.println("O grafo é dirigido");
        } else {
            System.out.println("O grafo não é dirigido");
        }
    }

    //dirigido ou não, simples ou multigrafo, regular, completo ou
    //nulo
    public static void tipoDoGrafo(Map<String, List<String>> matrizAdj) {
        isRegular(matrizAdj);
        isMultigrafo(matrizAdj);
        isNulo(matrizAdj);
        isCompleto(matrizAdj);
        //isDirigido(conjuntoArestas);
    }

    public static void dfs(Map<String, List<String>> matrizAdjacencia, String origem) {
        boolean possuiTodosVisitados = matrizAdjacencia.get(origem)
                .stream()
                .allMatch(verticesVisitados::contains);

        if (possuiTodosVisitados) {
            tempoFinal = tempoInicial + 1;
            verticesFinalizados.add(origem);
            verticesTempos.put(origem, Arrays.asList(String.valueOf(tempoInicial), String.valueOf(tempoFinal)));
            return;
        }

        for (String verticeDestino : matrizAdjacencia.get(origem)) {
            List<String> tempoInicialFinal = new ArrayList<>();
            tempoInicialFinal.add(String.valueOf(tempoInicial));

            if (origem.equals(verticeDestino)) {
                verticesTempos.put(verticeDestino, tempoInicialFinal);
                verticesVisitados.add(verticeDestino);
            }

            if (!verticesVisitados.contains(verticeDestino)) {
                tempoInicial++;
                List<String> list = new ArrayList<>();
                list.add(String.valueOf(tempoInicial));
                verticesVisitados.add(verticeDestino);
                verticesTempos.put(verticeDestino, list);
                dfs(matrizAdjacencia, verticeDestino);
            }
        }

        tempoFinal = tempoFinal + 1;
        verticesFinalizados.add(origem);
        verticesTempos.get(origem).add(String.valueOf(tempoFinal));
    }

    public static void buscaEmProfundidade(Map<String, List<String>> matrizAdjacencia) {
        for (String origem : matrizAdjacencia.keySet()) {
            if (!verticesFinalizados.contains(origem)) {
                dfs(matrizAdjacencia, origem);
            }
        }

        for (Map.Entry<String, List<String>> tempos : verticesTempos.entrySet()) {
            System.out.println(String.format("Vértice %s: Tempo Inicial %s / Tempo Final %s", tempos.getKey(), tempos.getValue().get(0), tempos.getValue().get(1)));
        }
    }

    public static void arestasDoGrafo(Map<String, List<String>> matrizAdjacencia, String[][] conjuntoArestas) {
        int somaArestas = 0;
        for (Map.Entry<String, List<String>> vertices : matrizAdjacencia.entrySet()) {
            somaArestas += vertices.getValue().size();
        }

        System.out.println("Quantidade de arestas do grafo: " + somaArestas);
        System.out.println("Conjunto de arestas: " + Arrays.deepToString(conjuntoArestas));
    }

    public static void grausDoVertice(Map<String, List<String>> matrizAdjacencia) {
        Map<String, String> grauVertices = new HashMap<>();
        List<Integer> listaGraus = new ArrayList<>();
        int somaArestas = 0;

        for (Map.Entry<String, List<String>> vertice : matrizAdjacencia.entrySet()) {
            int qtdeArestas = vertice.getValue().size();
            somaArestas += qtdeArestas;

            //se a chave que seria o vertice estar nos pares dos valores significa que é um laço e conta mais 1
            if (vertice.getValue().contains(vertice.getKey())) {
                qtdeArestas += 1;
            }
            grauVertices.put(vertice.getKey(), String.valueOf(qtdeArestas));
            listaGraus.add(qtdeArestas);
        }

        String sequenciaGraus = listaGraus.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        //printa qtde de vertices, qtde total e sequencia
        for (Map.Entry<String, String> verticeGrauAtual : grauVertices.entrySet()) {
            System.out.println(String.format("O vértice %s contém grau %s", verticeGrauAtual.getKey(), verticeGrauAtual.getValue()));
        }

        int grauTotalGrafo = 2 * somaArestas;
        System.out.println("O grau total do grafo é : " + grauTotalGrafo);
        System.out.println("Sequencia de graus do grafo :" + sequenciaGraus);
    }

    public static void main(String[] args) {
        String[] conjuntoVertice = {"a", "b", "c", "d"};
        String[][] conjuntoArestas = {
                {"a", "a"},
                {"a", "b"},
                {"a", "c"},
                {"b", "a"},
                {"b", "c"},
                {"c", "a"},
                {"c", "b"},
                {"c", "d"},
                {"c", "d"}, {"d", "c"}, {"d", "c"}};


//        String[] conjuntoVertice = {"v1", "v2", "v3", "v4"};
//        String[][] conjuntoArestas = {
//                {"v1", "v2"},
//                {"v2", "v4"},
//                {"v3", "v1"},
//                {"v3", "v4"},
//                {"v4", "v3"}};

        Map<String, List<String>> matrizAdj = matrizAdjacencia(conjuntoVertice, conjuntoArestas);
        grausDoVertice(matrizAdj);
        arestasDoGrafo(matrizAdj, conjuntoArestas);
        tipoDoGrafo(matrizAdj);
        buscaEmProfundidade(matrizAdj);
        isDirigido(conjuntoArestas);
    }
}