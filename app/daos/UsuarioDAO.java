package daos;

import java.util.Optional;

import models.Usuario;

import com.avaje.ebean.Finder;

public class UsuarioDAO {

	private Finder<Long, Usuario> userFinder = new Finder<>(Usuario.class);

	public Optional<Usuario> retrieveByEmail(String email) {
		Usuario user = userFinder.query().where().eq("email", email).findUnique();
		return Optional.ofNullable(user);
	}

}
