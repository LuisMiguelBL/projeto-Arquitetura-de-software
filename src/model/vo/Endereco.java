package model.vo;

/**
 * PADRÃO VO Um endereço não tem identidade própria: dois endereços com os mesmos dados são iguais.
 */
public record Endereco(String rua, String numero, String bairro, String cidade) {

    public Endereco {
        validar(rua, "rua");
        validar(numero, "número");
        validar(bairro, "bairro");
        validar(cidade, "cidade");
    }

    private static void validar(String campo, String nomeCampo) {
        if (campo == null || campo.isBlank()) {
            throw new IllegalArgumentException("O campo " + nomeCampo + " é obrigatório.");
        }
    }

    @Override
    public String toString() {
        return rua + ", " + numero + " - " + bairro + ", " + cidade;
    }
}