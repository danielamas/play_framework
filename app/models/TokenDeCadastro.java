package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

@Entity(name="token_de_cadastro")
public class TokenDeCadastro extends Model {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Usuario usuario;

	private String token;

	public TokenDeCadastro() {}

	public TokenDeCadastro(Usuario user) {
		this.usuario = user;
		try {
			String source = user.getNome() + user.getEmail();
			byte[] bytes = source.getBytes("UTF-8");
			this.token = UUID.nameUUIDFromBytes(bytes).toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
