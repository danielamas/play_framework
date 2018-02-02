package autenticadores;

import play.mvc.Http.Context;

import java.util.Optional;

import javax.inject.Inject;

import controllers.UsuarioController;
import controllers.routes;
import daos.UsuarioDAO;
import models.Usuario;
import play.mvc.Result;
import play.mvc.Security.Authenticator;

public class AdminAutenticado extends Authenticator {

	@Inject
	UsuarioDAO usuarioDAO;

	@Override
	public String getUsername(Context context) {
		String usuarioCodigo = context.session().get(UsuarioController.AUTH);
		if(usuarioCodigo != null && !usuarioCodigo.isEmpty()) {
			Optional<Usuario> uDB = usuarioDAO.retrieveByTokenCodigo(usuarioCodigo.trim());
			if(uDB.isPresent() && uDB.get().isAdmin()) {
				context.args.put("usuario", uDB.get());
				return uDB.get().getNome();
			}
		}
		return null;
	}

	@Override
	public Result onUnauthorized(Context context) {
		context.flash().put("danger", "Não autorizado! Usuário não é um Administrador!");
		return redirect(routes.UsuarioController.painel());
	}

}
