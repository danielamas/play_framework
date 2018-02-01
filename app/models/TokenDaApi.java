package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

import akka.util.Crypt;
import util.Util;

@Entity
public class TokenDaApi extends Model {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Usuario usuario;
	private String codigo;
	private Date expiracao;

	public TokenDaApi(Usuario usuario) {
		this.usuario = usuario;
		this.expiracao = new Date();
		try {
			this.codigo = Util.encodeString(UUID.randomUUID().toString() + expiracao.toString( )+ usuario.toString());
		} catch (Exception e) {
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
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Date getExpiracao() {
		return expiracao;
	}
	public void setExpiracao(Date expiracao) {
		this.expiracao = expiracao;
	}
}
