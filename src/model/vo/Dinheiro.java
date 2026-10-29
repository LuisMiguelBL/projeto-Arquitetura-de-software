package model.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public record Dinheiro(BigDecimal valor) {

    public static final Dinheiro ZERO = new Dinheiro(BigDecimal.ZERO);

    public Dinheiro {
        if (valor == null) {
            throw new IllegalArgumentException("O valor não pode ser nulo.");
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }
        valor = valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Dinheiro de(double valor) {
        return new Dinheiro(BigDecimal.valueOf(valor));
    }


    public Dinheiro somar(Dinheiro outro) {
        return new Dinheiro(this.valor.add(outro.valor));
    }

    public Dinheiro subtrair(Dinheiro outro) {
        BigDecimal resultado = this.valor.subtract(outro.valor);

        return resultado.compareTo(BigDecimal.ZERO) < 0 ? ZERO : new Dinheiro(resultado);
    }

    public Dinheiro multiplicar(int quantidade) {
        return new Dinheiro(this.valor.multiply(BigDecimal.valueOf(quantidade)));
    }

    public Dinheiro percentual(double porcentagem) {
        BigDecimal fator = BigDecimal.valueOf(porcentagem).divide(BigDecimal.valueOf(100));
        return new Dinheiro(this.valor.multiply(fator));
    }

    public boolean maiorOuIgualA(Dinheiro outro) {
        return this.valor.compareTo(outro.valor) >= 0;
    }

    @Override
    public String toString() {
        return NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(valor);
    }
}
