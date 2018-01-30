package autenticadores;

import java.util.Optional;

import javax.inject.Inject;

import models.Usuario;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticator;
import controllers.UsuarioController;
import controllers.routes;
import daos.UsuarioDAO;


public class UsuarioAutenticado extends Authenticator {

	@Inject
	private UsuarioDAO usuarioDAO;

	@Override
	public String getUsername(Context context) {
		String usuarioEmail = context.session().get(UsuarioController.AUTH);
		if(usuarioEmail != null && !usuarioEmail.isEmpty()) {
			Optional<Usuario> uDB = usuarioDAO.retrieveByEmail(usuarioEmail.trim());
			if(uDB.isPresent()) {
				return uDB.get().getNome();
			}
		}
		return null;
	}

	@Override
	public Result onUnauthorized(Context context) {
		context.flash().put("danger", "Não autorizado!");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}

	
}
