package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

@Entity
public class Usuario extends Model {

	@Id
	@GeneratedValue
	private Long id;
	@Required(message = "Você precisa fornecer um nome de tratamento")
	private String nome;
	@Email @Required(message = "Você precisa fornecer seu email")
	private String email;
	@Required(message = "Você precisa fornecer uma senha")
	private String senha;
	@OneToOne(mappedBy = "usuario")
	private TokenDaApi token;
	@OneToMany(mappedBy = "usuario")
	private List<RegistroDeAcesso> acessos;
	private boolean verificado;
	private boolean admin;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		if(nome != null) {
			nome = nome.trim();
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		if(email != null) {
			email = email.trim();
		}
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		if(senha != null) {
			senha = senha.trim();
		}
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public boolean isVerificado() {
		return verificado;
	}
	public void setVerificado(boolean verificado) {
		this.verificado = verificado;
	}
	public TokenDaApi getToken() {
		return token;
	}
	public void setToken(TokenDaApi token) {
		this.token = token;
	}
	public List<RegistroDeAcesso> getAcessos() {
		return acessos;
	}
	public void setAcessos(List<RegistroDeAcesso> acessos) {
		this.acessos = acessos;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
