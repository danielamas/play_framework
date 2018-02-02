package validadores;

import java.util.Optional;

import javax.inject.Inject;

import models.Usuario;
import play.data.Form;
import play.data.validation.ValidationError;
import daos.UsuarioDAO;

public class ValidadorDeUsuario {

	@Inject 
	private UsuarioDAO usuarioDAO;

	public Usuario isUsuarioVerificado(Usuario usuario) {
		Usuario resp = null;
		if(usuario != null && usuario.getEmail() != null) {
			Optional<Usuario> u = usuarioDAO.retrieveByEmail(usuario.getEmail().trim());
			if(u.isPresent()) {
				resp = u.get();
			}
		}
		return resp;
	}

	public boolean hasError(Form<Usuario> formulario) {
		validaEmail(formulario);
		validaSenha(formulario);
		return formulario.hasErrors();
	}

	private void validaEmail(Form<Usuario> formulario) {
		Usuario user = formulario.get();
		if (usuarioDAO.retrieveByEmail(user.getEmail().trim()).isPresent()) {
			formulario.reject(new ValidationError("email", "Este email já foi cadastrado"));
		}
	}

	private void validaSenha(Form<Usuario> formulario) {
		String senha = formulario.field("senha").valueOr("");
		String confirmacao = formulario.field("confirmaSenha").valueOr("");
		if (confirmacao.isEmpty()) {
			formulario.reject(new ValidationError("confirmaSenha", "Você precisa fornecer uma confirmação de senha"));
		} else if (!senha.equals(confirmacao)) {
			formulario.reject(new ValidationError("senha", ""));
			formulario.reject(new ValidationError("confirmaSenha", "As senhas precisam ser iguais"));
		}
	}
}
