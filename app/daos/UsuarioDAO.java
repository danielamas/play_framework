package daos;

import java.util.List;
import java.util.Optional;

import com.avaje.ebean.Finder;

import models.Usuario;

public class UsuarioDAO {

	private Finder<Long, Usuario> userFinder = new Finder<>(Usuario.class);

	public Optional<Usuario> retrieveByEmail(String email) {
		Usuario user = userFinder.query().where().eq("email", email).findUnique();
		return Optional.ofNullable(user);
	}

	public Optional<Usuario> retrieveByTokenCodigo(String codigo) {
		Usuario user = userFinder.
				query().
				where().
				eq("token.codigo", codigo).
				findUnique();
		return Optional.ofNullable(user);
	}

	public Optional<Usuario> retrieveByEmailAndEncodedPass(String email, String encodedPass) {
		Usuario user = userFinder
				.query()
				.where()
				.eq("email", email != null ? email : "")
				.eq("senha", encodedPass != null ? encodedPass : "") 
				.findUnique();
		return Optional.ofNullable(user);
	}

	public Optional<List<Usuario>> todos() {
		List<Usuario> usuarioList = userFinder.all();
		return Optional.ofNullable(usuarioList);
	}

}
