package br.ufla.gac106.s2022_1.seuJogo;

/**
 * Essa é a classe principal da aplicacao "World of Zull".
 * "World of Zuul" é um jogo de aventura muito simples, baseado em texto.
 *
 * Usuários podem caminhar em um cenário. E é tudo! Ele realmente precisa ser
 * estendido para fazer algo interessante!
 *
 * Para jogar esse jogo, crie uma instancia dessa classe e chame o método "jogar".
 *
 * Essa classe principal cria e inicializa todas as outras: ela cria os ambientes,
 * cria o analisador e começa o jogo. Ela também avalia e  executa os comandos que
 * o analisador retorna.
 *
 * @author  Michael Kölling and David J. Barnes (traduzido e adaptado por Julio César Alves)
 */

public class Jogo {

    // analisador de comandos do jogo
    private Analisador analisador;
    // ambiente onde se encontra o jogador
    private Ambiente ambienteAtual;

    /**
     * Cria o jogo e incializa seu mapa interno.
     */
    public Jogo() {
        criarAmbientes();
        analisador = new Analisador();
    }

    /**
     * Cria todos os ambientes e liga as saidas deles
     */
    private void criarAmbientes() {
        Ambiente reitoria, pavilhao, cantina, departamento, laboratorio;

        // cria os ambientes
        reitoria =
            new Ambiente("em um espaço aberto, gramado, em frente à reitoria");
        pavilhao = new Ambiente("no pavilhao de aulas");
        cantina = new Ambiente("na cantina da universidade");
        departamento = new Ambiente("no departamento de computacao");
        laboratorio = new Ambiente("na laboratorio de aulas de programacao");

        // inicializa as saidas dos ambientes
        reitoria.ajustarSaidas(null, pavilhao, departamento, cantina);
        pavilhao.ajustarSaidas(null, null, null, reitoria);
        cantina.ajustarSaidas(null, reitoria, null, null);
        departamento.ajustarSaidas(reitoria, laboratorio, null, null);
        laboratorio.ajustarSaidas(null, null, null, departamento);

        ambienteAtual = reitoria; // o jogo comeca em frente à reitoria
    }

    /**
     *  Rotina principal do jogo. Fica em loop ate terminar o jogo.
     */
    public void jogar() {
        imprimirBoasVindas();

        // Entra no loop de comando principal. Aqui nós repetidamente lemos comandos e
        // os executamos até o jogo terminar.

        boolean terminado = false;
        while (!terminado) {
            Comando comando = analisador.pegarComando();
            terminado = processarComando(comando);
        }
        System.out.println("Obrigado por jogar. Até mais!");
    }

    /**
     * Imprime a mensagem de abertura para o jogador.
     */
    private void imprimirBoasVindas() {
        System.out.println("Você está " + ambienteAtual.getDescricao());

        System.out.print("Saídas: ");
        if (ambienteAtual.getSaida(Direcao.NORTE) != null) {
            System.out.print("norte ");
        }
        if (ambienteAtual.getSaida(Direcao.LESTE) != null) {
            System.out.print("leste ");
        }
        if (ambienteAtual.getSaida(Direcao.SUL) != null) {
            System.out.print("sul ");
        }
        if (ambienteAtual.getSaida(Direcao.OESTE) != null) {
            System.out.print("oeste ");
        }
        System.out.println();
    }

    /**
     * Dado um comando, processa-o (ou seja, executa-o)
     * @param comando O Comando a ser processado.
     * @return true se o comando finaliza o jogo.
     */
    private boolean processarComando(Comando comando) {
        boolean querSair = false;
        
        PalavraDeComando palavraDeComando = comando.getPalavraDeComando();
        
        if (palavraDeComando == PalavraDeComando.DESCONHECIDA) {
            System.out.println("Eu nao entendi o que voce disse...");
            return false;
        }

        if (palavraDeComando == PalavraDeComando.AJUDA) {
            imprimirAjuda();
        }
        else if (palavraDeComando == PalavraDeComando.IR) {
            irParaAmbiente(comando);
        }
        else if (palavraDeComando == PalavraDeComando.SAIR) {
            querSair = sair(comando);
        }

        return querSair;
    }

    /**
     * Exibe informações de ajuda.
     * Aqui nós imprimimos algo bobo e enigmático e a lista de  palavras de comando
     */
    private void imprimirAjuda() {
        System.out.println(
            "Voce esta perdido. Voce esta sozinho. Voce caminha"
        );
        System.out.println("pela universidade.");
        System.out.println();
        System.out.println("Suas palavras de comando sao:");
        System.out.println("   ir sair ajuda");
    }

    /**
     * Tenta ir em uma direcao. Se existe uma saída para lá entra no novo ambiente,
     * caso contrário imprime mensagem de erro.
     */
    private void irParaAmbiente(Comando comando) {
        // se não há segunda palavra, não sabemos pra onde ir..
        if (!comando.temSegundaPalavra()) {
            System.out.println("Ir para onde?");
            return;
        }

        String direcaoDigitada = comando.getSegundaPalavra();

        Direcao direcaoSelecionada;
        try {
            direcaoSelecionada = Direcao.pelaString(direcaoDigitada);
        } catch (IllegalArgumentException e) {
            System.out.println("Direção inválida! Tente novamente.");
            return;
        }

        Ambiente proximoAmbiente = ambienteAtual.getSaida(direcaoSelecionada);

        // Tenta sair do ambiente atual
        if (proximoAmbiente == null) {
            System.out.println("Não há passagem!");
        } else {
            ambienteAtual = proximoAmbiente;

            System.out.println("Você está " + ambienteAtual.getDescricao());

            System.out.print("Saídas: ");
            for (Direcao direcao : Direcao.values()) {
                if (ambienteAtual.getSaida(direcao) != null) {
                    System.out.print(direcao + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * "Sair" foi digitado. Verifica o resto do comando pra ver se nós queremos
     * realmente sair do jogo.
     * @return true, se este comando sai do jogo, false, caso contrário.
     */
    private boolean sair(Comando comando) {
        if (comando.temSegundaPalavra()) {
            System.out.println("Sair o que?");
            return false;
        } else {
            return true; // sinaliza que nós realmente queremos sair
        }
    }
}