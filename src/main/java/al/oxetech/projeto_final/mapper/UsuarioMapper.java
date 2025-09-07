package al.oxetech.projeto_final.mapper;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioPatchDTO;
import al.oxetech.projeto_final.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toUsuarioDTO(Usuario usuario);

    UsuarioDTO toUsuarioDTO(UsuarioInputDTO usuarioInputDTO);

    UsuarioInputDTO toUsuarioInputDTO(UsuarioDTO usuarioDTO);

    UsuarioInputDTO toUsuarioInputDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);

    Usuario toEntity(UsuarioInputDTO usuarioInputDTO);

    Usuario toEntity(UsuarioPatchDTO usuarioPatchDTO);

}
