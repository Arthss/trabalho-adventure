package br.ufla.gac106.s2022_1.seuJogo;
    
public enum Direcao {
    NORTE("norte"),
    SUL("sul"),
    LESTE("leste"),
    OESTE("oeste");

    // Atributo string com a representação do enumerador como String.
    private String direcaoString;

    // Construtor privado
    private Direcao(String direcaoString) {
        this.direcaoString = direcaoString;
    }

    // Sobrescrita do método toString retornado o atributo String
    @Override
    public String toString() {
        return this.direcaoString;
    }

    //  Método estático que, dada uma String, retorna o enumerador
    public static Direcao pelaString(String direcao) {
        for (Direcao d : Direcao.values()) {
            if (d.direcaoString.equals(direcao)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Direção inválida!");
    }
}