   import java.util.*;

   public class Metodos {
      Scanner scanner = new Scanner(System.in);
      public List<String> terminais = new ArrayList<>();
      public List<String> naoterminais = new ArrayList<>();
      public List<String> derivacoes = new ArrayList<>();
      // Mapa para armazenar a gramática, mapeando cada símbolo não terminal para sua produção
      Map<String, Producao> gramatica = new HashMap<>();





      // Método para inserir não terminais na gramática
      public void InserirNaoTerminais() {
         String nterm;
         boolean key = true;
         while (key) {
            System.out.println("Insira os símbolos não terminais");
            System.out.println("Digite '0' para parar");
            nterm = scanner.nextLine();
            if (nterm .equals("0")) {
               key = false;
            } else if(naoterminais.contains(nterm)){
               System.err.println("Símbolo repetido. Insira outro. \n");
            } else naoterminais.add((nterm.toUpperCase()));
            System.out.printf(naoterminais.toString());
         }
      }
      // Método para inserir terminais na gramática
      public void InserirTerminais() {
         String term;
         boolean key = true;
         while (key) {
            System.out.println("Insira os símbolos terminais");
            System.out.println("Digite '0' para parar");
            term = scanner.nextLine();
            if (term.equals("0")) {
               key = false;
            }else if (terminais.contains(term)){
               System.err.println("Símbolo repetido. Insira outro. \n");
            } else if (term.equals("e")){
               System.out.println("'e' significa conjunto vazio, deseja continuar? \n1 - Sim \n2 - Não");
               if(scanner.nextInt() == 1){
                  terminais.add("e");
                  scanner.nextLine();
               } else System.err.println("Então insira outro símbolo ou pare.");
            }
               else {
               terminais.add((term.toLowerCase()));
            }
            System.out.printf(terminais.toString());
         }
      }
      // Método para criar a gramática, aceitando as derivações para cada não terminal e verificando se a gramática é válida
      public Map<String, Producao> criarGramatica() {
         // HashMap para armazenar as derivações para cada símbolo não terminal
         Map<String, Producao> gramatica = new HashMap<>();

         Scanner scanner = new Scanner(System.in);

         for (String naoTerminal : naoterminais) {
            System.out.println("Insira as derivações para o símbolo não terminal " + naoTerminal.toUpperCase() + " separadas por '|' (Exemplo: aB|bC|c):");
            String input = scanner.nextLine();
            String[] derivacoesInput = input.split("\\|");
            List<String> derivacoes = new ArrayList<>(Arrays.asList(derivacoesInput));

            // Verificar se os símbolos usados nas derivações existem nos ArrayLists de não terminais e terminais
            for (String derivacao : derivacoes) {
               for (int i = 0; i < derivacao.length(); i++) {
                  char simbolo = derivacao.charAt(i);
                  String simboloStr = String.valueOf(simbolo);
                  if (!naoterminais.contains(simboloStr) && !terminais.contains(simboloStr)) {
                     System.err.println("Símbolo '" + simbolo + "' não encontrado nos não terminais ou terminais. Corrija a gramática.");
                     return null;
                  }
               }
            }



            // Verificar recursão à esquerda e disjunção par a par
            boolean recursaoEsquerda = false;
            for (String derivacao : derivacoes) {
               if (derivacao.charAt(0) == naoTerminal.charAt(0)) {
                  recursaoEsquerda = true;
                  break;
               }
            }

            if (recursaoEsquerda) {
               System.err.println("Recursão à esquerda encontrada em " + naoTerminal + ". Corrija a gramática.");
            } else {
               gramatica.put(naoTerminal, new Producao(derivacoes));
            }



            boolean disjuncaoParAPar = true;

            outerLoop:
            for (int i = 0; i < derivacoes.size(); i++) {
               for (int j = i + 1; j < derivacoes.size(); j++) {
                  String firstDerivacao = derivacoes.get(i);
                  String secondDerivacao = derivacoes.get(j);

                  if (firstDerivacao.charAt(0) == secondDerivacao.charAt(0)) {
                     disjuncaoParAPar = false;
                     break outerLoop;
                  }
               }
            }

            if (!disjuncaoParAPar) {
               System.err.println("Disjunção par a par encontrada em " + naoTerminal + ". Corrija a gramática.");
            } else {
               gramatica.put(naoTerminal, new Producao(derivacoes));
            }
         }




         // Método para exibir a gramática na forma G = ({N}, {T}, P, S)
         exibirGramatica(gramatica);
         return gramatica;
      }
      private void exibirGramatica(Map<String, Producao> gramatica) {
         StringBuilder sb = new StringBuilder();
         sb.append("G = ({ ");
         for (int i = 0; i < naoterminais.size(); i++) {
            sb.append(naoterminais.get(i));
            if (i < naoterminais.size() - 1) {
               sb.append(", ");
            }
         }
         sb.append(" }, { ");
         for (int i = 0; i < terminais.size(); i++) {
            sb.append(terminais.get(i));
            if (i < terminais.size() - 1) {
               sb.append(", ");
            }
         }
         sb.append(" } P, ");
         sb.append(naoterminais.get(0));
         sb.append(")\n\nP: {\n");
         for (Map.Entry<String, Producao> entry : gramatica.entrySet()) {
            sb.append("    ");
            sb.append(entry.getKey());
            sb.append(" -> ");
            List<String> derivacoes = entry.getValue().getDerivacoes();
            for (int i = 0; i < derivacoes.size(); i++) {
               sb.append(derivacoes.get(i));
               if (i < derivacoes.size() - 1) {
                  sb.append(" | ");
               }
            }
            sb.append("\n");
         }
         sb.append("}");
         System.out.println(sb.toString());
      }
      // Método para calcular o conjunto FIRST de cada símbolo não terminal
      public Map<String, Set<String>> calcularFirst(Map<String, Producao> gramatica) {
         Map<String, Set<String>> first = new HashMap<>();

         for (String naoTerminal : naoterminais) {
            first.put(naoTerminal, calcularFirstNT(gramatica, naoTerminal));
         }

         return first;
      }
      // Método auxiliar para calcular o conjunto FIRST de um símbolo não terminal específico
         private Set<String> calcularFirstNT (Map < String, Producao > gramatica, String naoTerminal) {

               Set<String> first = new HashSet<>();

               List<String> derivacoes = gramatica.get(naoTerminal).getDerivacoes();

               for (String derivacao : derivacoes) {
                  String simbolo = String.valueOf(derivacao.charAt(0));
                  try {
                     if (terminais.contains(simbolo)) {
                        first.add(simbolo);
                     } else {
                        first.addAll(calcularFirstNT(gramatica, simbolo));
                     }
                  } catch(StackOverflowError e) {
                     System.err.println("Erro de loop. A derivação nunca acaba");
                  }
               }
            return first;
         }
      // Método para calcular o conjunto FOLLOW de cada símbolo não terminal
      public Map<String, Set<String>> calcularFollow(Map<String, Producao> gramatica, Map<String, Set<String>> first) {
         Map<String, Set<String>> follow = new HashMap<>();

         // Inicialize o conjunto FOLLOW de cada símbolo não terminal
         for (String naoTerminal : naoterminais) {
            follow.put(naoTerminal, new HashSet<>());
         }

         // Adicione $ (fim de entrada) ao conjunto FOLLOW do símbolo inicial
         follow.get(naoterminais.get(0)).add("$");

         boolean followSetChanged;

         do {
            followSetChanged = false;

            for (String naoTerminal : naoterminais) {
               List<String> derivacoes = gramatica.get(naoTerminal).getDerivacoes();

               for (String derivacao : derivacoes) {
                  for (int i = 0; i < derivacao.length(); i++) {
                     String simbolo = String.valueOf(derivacao.charAt(i));

                     if (naoterminais.contains(simbolo)) {
                        Set<String> temp = new HashSet<>();

                        if (i + 1 < derivacao.length()) {
                           String nextSimbolo = String.valueOf(derivacao.charAt(i + 1));
                           if (naoterminais.contains(nextSimbolo)) {
                              temp.addAll(first.get(nextSimbolo));
                           } else {
                              temp.add(nextSimbolo);
                           }
                           temp.remove("ε");
                        } else {
                           temp = new HashSet<>(follow.get(naoTerminal));
                        }

                        int oldSize = follow.get(simbolo).size();
                        follow.get(simbolo).addAll(temp);

                        if (follow.get(simbolo).size() > oldSize) {
                           followSetChanged = true;
                        }
                     }
                  }
               }
            }
         } while (followSetChanged);

         return follow;
      }



      // Método para construir a tabela de análise sintática
      public Map<String, Map<String, String>> construirTabela(Map<String, Producao> gramatica, Map<String, Set<String>> first, Map<String, Set<String>> follow) {
         Map<String, Map<String, String>> tabela = new HashMap<>();

         for (String naoTerminal : naoterminais) {
            tabela.put(naoTerminal, new HashMap<>());
         }

         for (Map.Entry<String, Producao> entry : gramatica.entrySet()) {
            String naoTerminal = entry.getKey();
            List<String> derivacoes = entry.getValue().getDerivacoes();

            for (String derivacao : derivacoes) {
               String simboloInicial = String.valueOf(derivacao.charAt(0));

               if (terminais.contains(simboloInicial) || simboloInicial.equals("e")) {
                  Set<String> simbolos = simboloInicial.equals("e") ? follow.get(naoTerminal) : first.get(naoTerminal);
                  for (String simbolo : simbolos) {
                     tabela.get(naoTerminal).put(simbolo, derivacao);
                  }
               } else {
                  Set<String> simbolos = first.get(simboloInicial);
                  for (String simbolo : simbolos) {
                     if (simbolo.equals("e")) {
                        for (String simboloFollow : follow.get(naoTerminal)) {
                           tabela.get(naoTerminal).put(simboloFollow, derivacao);
                        }
                     } else {
                        tabela.get(naoTerminal).put(simbolo, derivacao);
                     }
                  }
               }
            }
         }

         return tabela;
      }
      // Métodos para exibir a tabela de análise sintática e os conjuntos FIRST e FOLLOW
      public void exibirTabela(Map<String, Map<String, String>> tabela) {
         System.out.println("----- Tabela de Análise Preditiva -----");

         for (Map.Entry<String, Map<String, String>> entryNT : tabela.entrySet()) {
            String naoTerminal = entryNT.getKey();
            System.out.println("Não Terminal: " + naoTerminal);

            Map<String, String> derivacoes = entryNT.getValue();
            for (Map.Entry<String, String> entryT : derivacoes.entrySet()) {
               String terminal = entryT.getKey();
               String derivacao = entryT.getValue();

               System.out.println("  Terminal: " + terminal + " -> Derivação: " + derivacao);
            }
         }

      }
      public void exibirFirst(Map<String, Set<String>> first) {
         System.out.println("----- Conjunto FIRST -----");
         for (Map.Entry<String, Set<String>> entry : first.entrySet()) {
            System.out.println("Não Terminal: " + entry.getKey() + " -> FIRST: " + entry.getValue());
         }
      }

      public void exibirFollow(Map<String, Set<String>> follow) {
         System.out.println("----- Conjunto FOLLOW -----");
         for (Map.Entry<String, Set<String>> entry : follow.entrySet()) {
            System.out.println("Não Terminal: " + entry.getKey() + " -> FOLLOW: " + entry.getValue());
         }
      }
      // Método auxiliar para verificar se um símbolo é terminal
      private static boolean isTerminal(String simbolo) {
         return simbolo.length() == 1 && simbolo.matches("[a-z]");
      }

      // Método para analisar uma entrada de string usando a tabela de análise sintática
      public boolean analisarEntrada(String entrada, Map<String, Map<String, String>> tabela, String simboloInicial) {
         Stack<String> pilha = new Stack<>();
         pilha.push("$");
         pilha.push(simboloInicial);

         int i = 0;
         while (!pilha.empty()) {
            String topo = pilha.peek();

            System.out.println("Pilha: " + pilha);
            System.out.println("Entrada restante: " + entrada.substring(i));

            if (isTerminal(topo) || topo.equals("$")) {
               if (i < entrada.length() && topo.equals(String.valueOf(entrada.charAt(i)))) {
                  pilha.pop();
                  i++;
               } else {
                  return false;
               }
            } else if (i < entrada.length() && tabela.containsKey(topo) && tabela.get(topo).containsKey(String.valueOf(entrada.charAt(i)))) {
               pilha.pop();
               String producao = tabela.get(topo).get(String.valueOf(entrada.charAt(i)));
               for (int j = producao.length() - 1; j >= 0; j--) {
                  if (producao.charAt(j) != 'e') {
                     pilha.push(String.valueOf(producao.charAt(j)));
                  }
               }
            } else if(tabela.containsKey(topo) && tabela.get(topo).containsKey("e")) {
               pilha.pop();
               String producao = tabela.get(topo).get("e");
               for (int j = producao.length() - 1; j >= 0; j--) {
                  if (producao.charAt(j) != 'e') {
                     pilha.push(String.valueOf(producao.charAt(j)));
                  }
               }
            } else {
               return false;
            }
         }

         return i == entrada.length() && pilha.empty();
      }





   }
