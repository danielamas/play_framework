package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

public class TokenDeCadastro extends Model {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Usuario user;
	private String codigo;

	public TokenDeCadastro(Usuario user) {
		this.user = user;
		try {
			String source = user.getNome() + user.getEmail();
			byte[] bytes = source.getBytes("UTF-8");
			this.codigo = UUID.nameUUIDFromBytes(bytes);

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
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
