package al.oxetech.projeto_final.repository;

import al.oxetech.projeto_final.model.Relatorio;
import al.oxetech.projeto_final.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelatorioRepository extends JpaRepository<Relatorio,Long> {
    List<Relatorio> findByAutorId(Long AutorId);

    List<Relatorio> findByTituloContainingIgnoreCase(String titulo);
}
