package br.com.heycheff.api.repository;

import java.util.ArrayList;
import java.util.List;

import br.com.heycheff.api.model.Usuario;

public class UsuarioRepository {
	

	    private static List<Usuario> usuarios = new ArrayList<>();

	    public static void salvar(Usuario usuario) {
	        usuarios.add(usuario);
	    }

	    public static Usuario buscarPorId(int id) {
	        for (Usuario usuario : usuarios) {
	            if (usuario.getId() == id) {
	                return usuario;
	            }
	        }
	        return null;
	    }

	    public static List<Usuario> buscarTodos() {
	        return usuarios;
	    }

	    public static void atualizar(Usuario usuarioAtualizado) {
	        for (Usuario usuario : usuarios) {
	            if (usuario.getId() == usuarioAtualizado.getId()) {
	                usuario.setEmail(usuarioAtualizado.getEmail());
	                usuario.setSenha(usuarioAtualizado.getSenha());
	                usuario.setNickname(usuarioAtualizado.getNickname());
	                usuario.setFuncoes(usuarioAtualizado.getFuncoes());
	                break;
	            }
	        }
	    }

	    public static void deletar(int id) {
	        usuarios.removeIf(usuario -> usuario.getId() == id);
	    }
}

