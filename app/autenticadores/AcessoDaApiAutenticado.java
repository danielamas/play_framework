package autenticadores;

import java.util.Optional;

import javax.inject.Inject;

import daos.UsuarioDAO;
import models.Usuario;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticator;
import dto.Error;

public class AcessoDaApiAutenticado extends Authenticator {

	@Inject
	UsuarioDAO usuarioDAO;

	@Override
	public String getUsername(Context context) {
		String codigo = context.request().getHeader("API-Token");
		if(codigo != null && !codigo.isEmpty()) {
			Optional<Usuario> uDB = usuarioDAO.retrieveByTokenCodigo(codigo);
			if(uDB.isPresent()) {
				return uDB.get().getNome();
			}
		}
		return null;
	}

	@Override
	public Result onUnauthorized(Context context) {
		Error error = new Error();
		error.setMsg("Not Authorized!");
		error.setStatus(Http.Status.UNAUTHORIZED);
		return unauthorized(Json.toJson(error));
	}

	
}
