package al.oxetech.projeto_final.mapper;

import al.oxetech.projeto_final.dto.relatorio.RelatorioDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioPatchUpdateDTO;
import al.oxetech.projeto_final.model.Relatorio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RelatorioMapper {

    @Mapping(source = "autor.nome", target = "autorNome")
    RelatorioDTO toRelatorioDTO(Relatorio relatorio);

    Relatorio toEntity(RelatorioPatchUpdateDTO relatorioPatchUpdateDTO);

}
