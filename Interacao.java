import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Interacao {
    Scanner scanner = new Scanner(System.in); // cria um objeto Scanner para receber entradas do usuário

    Metodos metodos = new Metodos(); // cria um objeto da classe Metodos para realizar operações na gramática

    public void menu() // define o método menu para exibir o menu e aguardar as escolhas do usuário
    {
        int choice; // define a variável "choice" para armazenar a escolha do usuário

        do { // inicia o loop para exibir o menu e aguardar as escolhas do usuário
            System.out.println("----- MENU -----");
            System.out.println("1. Inserir símbolos não terminais");
            System.out.println("2. Inserir símbolos terminais");
            System.out.println("3. Calcular first e follow, inserir derivações, mostrar tabela e analisar sentença.");
            System.out.println("0. SAIR");
            System.out.print("Escolha: ");
            choice = scanner.nextInt(); // recebe a escolha do usuário

            switch (choice) { // utiliza um switch para determinar qual método deve ser chamado de acordo com a escolha do usuário
                case 1:
                    metodos.InserirNaoTerminais(); // chama o método InserirNaoTerminais da classe Metodos
                    break;
                case 2:
                    metodos.InserirTerminais(); // chama o método InserirTerminais da classe Metodos
                    break;
                case 3:
                    Map<String, Producao> gramatica = metodos.criarGramatica(); // cria a gramática a partir dos símbolos inseridos anteriormente
                    Map<String, Set<String>> first = metodos.calcularFirst(gramatica); // calcula o conjunto first da gramática
                    Map<String, Set<String>> follow = metodos.calcularFollow(gramatica, first); // calcula o conjunto follow da gramática
                    Map<String, Map<String, String>> tabela = metodos.construirTabela(gramatica, first, follow); // constrói a tabela de análise a partir do conjunto first e follow
                    metodos.exibirFirst(first); // exibe o conjunto first na tela
                    metodos.exibirFollow(follow); // exibe o conjunto follow na tela
                    metodos.exibirTabela(tabela); // exibe a tabela de análise na tela

                    System.out.println("Digite a sentença a ser analisada:");
                    scanner.nextLine(); // consome o caractere de quebra de linha deixado pelo método scanner.nextInt()
                    String entrada = scanner.nextLine(); // recebe a sentença a ser analisada

                    if (metodos.analisarEntrada(entrada, tabela, "S")) { // analisa a sentença a partir da tabela de análise
                        System.out.println("A sentença é aceita pela gramática.");
                    } else {
                        System.out.println("A sentença não é aceita pela gramática.");
                    }
                    break;
                case 0:
                    System.out.println("Saindo..."); // exibe mensagem de saída do programa
                    break;
                default:
                    System.out.println("OPÇÃO INVÁLIDA"); // exibe mensagem de opção inválida
            }
            System.out.println();
        } while (choice != 0); // continua o loop enquanto o usuário não escolher a opção "0"
    }
}

