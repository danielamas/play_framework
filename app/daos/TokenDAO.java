package daos;

import java.util.Optional;

import com.avaje.ebean.Finder;

import models.TokenDeCadastro;

public class TokenDAO {

	private Finder<Long, TokenDeCadastro> tokenFinder = new Finder<>(TokenDeCadastro.class);

	public Optional<TokenDeCadastro> retrieveByEmailAndToken(String email, String token) {
		TokenDeCadastro t = tokenFinder
				.query()
				.fetch("usuario")
				.where()
				.eq("usuario.email", email)
				.eq("token", token)
				.findUnique();
		return Optional.ofNullable(t);
	}
}
