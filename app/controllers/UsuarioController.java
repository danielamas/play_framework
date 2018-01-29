package controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

import models.EmailDeCadastro;
import models.TokenDeCadastro;
import models.Usuario;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import validadores.ValidadorDeUsuario;
import views.html.formularioDeNovoUsuario;
import views.html.formularioDeLogin;
import daos.TokenDAO;

public class UsuarioController extends Controller {

	@Inject
	private FormFactory formularios;

	@Inject
	private ValidadorDeUsuario validadorDeUsuario;

	@Inject
	private MailerClient mailerClient;

	@Inject
	private TokenDAO tokenDAO;


	public static final String AUTH = "auth";

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
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			flash("Atenção", "Ocorreu um erro interno");
			return badRequest(formularioDeNovoUsuario.render(formulario));
		}

		TokenDeCadastro token = null;
		try {
			token = new TokenDeCadastro(usuario);
			token.save();
			mailerClient.send(new EmailDeCadastro(token));
			flash("Success", "Um email foi enviado para que você confirme seu cadastro!");
		} catch (Exception e) {
			e.printStackTrace();
			if(token != null) {
				token.delete();
			}
			if(usuario != null) {
				usuario.delete();
			}
			flash("Atenção", "Ocorreu um erro interno");
			return badRequest(formularioDeNovoUsuario.render(formulario));
		}

		flash("success", "Usuário cadastrado com sucesso");
		return redirect("/login");
	}

	public Result confirmaCadastro(String email, String token) {
		Usuario usuario = new Usuario();
		if(email != null && !email.isEmpty()) {
			usuario.setEmail(email);

			if(!validadorDeUsuario.isUsuarioVerificado(usuario) && token != null && !token.isEmpty()) {
				Optional<TokenDeCadastro> t = tokenDAO.retrieveByEmailAndToken(email, token);
				if (t.isPresent()) {
					t.get().delete();

					if(t.get().getUsuario() != null && t.get().getUsuario().getId() != null) {
						t.get().getUsuario().setVerificado(true);
						t.get().getUsuario().update();
						flash("success", "Seu usuário foi confirmado com sucesso! Bem vindo!");
						return redirect("/usuario/painel");
					}
				}
			} else {
				flash("success", "Seu usuário já foi confirmado com sucesso!");
				return redirect("/usuario/painel");
			}
		}

		flash("Error!", "Token não reconhecido");
		return redirect("/login");
	}

	@Authenticated
	public Result painel() {
		return ok("Painel do usuário");
	}

	public Result formularioDeLogin() {
//		return ok();
		return ok(formularioDeLogin.render(formularios.form()));
	}

	public Result fazLogin() {
		DynamicForm formulario = formularios.form().bindFromRequest();

//		return redirect(routes.UsuarioController.painelDoUsuario());
		return redirect(routes.UsuarioController.formularioDeLogin());
		
	}
}
