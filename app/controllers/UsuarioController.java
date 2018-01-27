package controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

import models.Usuario;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import validadores.ValidadorDeUsuario;
import views.html.formularioDeNovoUsuario;

public class UsuarioController extends Controller {

	@Inject
	private FormFactory formularios;

	@Inject private ValidadorDeUsuario validadorDeUsuario;

	public Result formularioDeNovoUsuario() {
		Form<Usuario> formulario = formularios.form(Usuario.class);
		return ok(formularioDeNovoUsuario.render(formulario));
	}

	public Result salvaNovoUsuario() {
		Form<Usuario> formulario = formularios.form(Usuario.class).bindFromRequest();
		if (validadorDeUsuario.hasError(formulario)) {
			flash("Atenção", "Há erros no formulário de cadastro de novo usuário");
			return badRequest(formularioDeNovoUsuario.render(formulario));
		}

		Usuario usuario = formulario.get();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(usuario.getSenha().getBytes(StandardCharsets.UTF_8));
			String hashStr = DatatypeConverter.printHexBinary(hash);
			usuario.setSenha(hashStr);
			usuario.save();
			flash("success", "Usuário cadastrado com sucesso");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			flash("", "Há erros no formulário de cadastro de novo usuário");
			return badRequest(formularioDeNovoUsuario.render(formulario));
		}
		return redirect("/login"); // TODO rota
	}
}
