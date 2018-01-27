package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import com.avaje.ebean.Model;

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

	private boolean verificado;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
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
	
}