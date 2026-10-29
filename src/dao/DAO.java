package dao;

import java.util.List;
import java.util.Optional;

public interface DAO <T, ID>{

    T salvar(T objeto);

    Optional<T> buscar(ID id);

    List<T> listar();

    boolean excluir(ID id);
}
