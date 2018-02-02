package controllers;

import java.util.Optional;

import javax.inject.Inject;

import autenticadores.UsuarioAutenticado;
import daos.TokenDAO;
import daos.UsuarioDAO;
import models.EmailDeCadastro;
import models.TokenDaApi;
import models.TokenDeCadastro;
import models.Usuario;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import util.Util;
import validadores.ValidadorDeUsuario;
import views.html.formularioDeLogin;
import views.html.formularioDeNovoUsuario;
import views.html.painel;

public class UsuarioController extends Controller {

	@Inject
	private FormFactory formularios;

	@Inject
	private ValidadorDeUsuario validadorDeUsuario;

	@Inject
	private MailerClient mailerClient;

	@Inject
	private TokenDAO tokenDAO;

	@Inject
	private UsuarioDAO usuarioDAO;


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
			usuario.setSenha(Util.encodeString(usuario.getSenha().trim()));
			usuario.save();
		} catch (Exception e) {
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
		return redirect(routes.UsuarioController.formularioDeLogin());
	}

	public Result confirmaCadastro(String email, String token) {
		Usuario usuario = new Usuario();
		if(email != null && !email.isEmpty()) {
			usuario.setEmail(email.trim());

			usuario = validadorDeUsuario.isUsuarioVerificado(usuario);
			if(!usuario.isVerificado() && token != null && !token.isEmpty()) {
				Optional<TokenDeCadastro> t = tokenDAO.retrieveByEmailAndToken(email, token.trim());
				if (t.isPresent()) {
					t.get().delete();

					if(t.get().getUsuario() != null && t.get().getUsuario().getId() != null) {
						t.get().getUsuario().setVerificado(true);
						TokenDaApi tokenDaApi = new TokenDaApi(t.get().getUsuario());
						tokenDaApi.save();
						t.get().getUsuario().setToken(tokenDaApi);
						t.get().getUsuario().update();
						insereUsuarioNaSessao(t.get().getUsuario());
						flash("success", "Seu usuário foi confirmado com sucesso! Bem vindo!");
						return redirect("/usuario/painel");
					}
				}
			} else {
				flash("success", "Seu usuário já foi confirmado com sucesso!");
				insereUsuarioNaSessao(usuario);
				return redirect("/usuario/painel");
			}
		}

		flash("Error!", "Token não reconhecido");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}

	@Authenticated(UsuarioAutenticado.class)
	public Result painel() {
		String codigo = session(AUTH);
		Usuario usuario = usuarioDAO.retrieveByTokenCodigo(codigo).get();
		return ok(painel.render(usuario));
	}

	public Result formularioDeLogin() {
		return ok(formularioDeLogin.render(formularios.form()));
	}

	public Result fazLogin() {
		DynamicForm formulario = formularios.form().bindFromRequest();
		Usuario usuario = new Usuario();
		usuario.setEmail(formulario.get("email"));
		usuario.setSenha(formulario.get("senha"));
		try {
			if(usuario != null) {
				//validation
				if(usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
					formulario.reject(new ValidationError("email", "Email é obrigatório!"));
				}/* else {
					validate email structure by regex
				}*/

				String encodedPassword = null;
				if(usuario.getSenha() == null || usuario.getEmail().isEmpty()) {
					formulario.reject(new ValidationError("senha", "Senha é obrigatório!"));
				} else {
					encodedPassword = Util.encodeString(usuario.getSenha());
				}

				Optional<Usuario> uDB = usuarioDAO.retrieveByEmailAndEncodedPass(usuario.getEmail(), encodedPassword);
				if(uDB != null && uDB.isPresent()) {
					usuario = uDB.get();
					if(usuario.isVerificado()) {
						insereUsuarioNaSessao(usuario);
						flash("success", "Login efetuado com sucesso!");
						return redirect(routes.UsuarioController.painel());
					} else {
						flash("danger", "Usuário não confirmado!");
					}
				} else {
					flash("danger", "Credenciais inválidas! Usuário não encontrado!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			flash("danger", "Erro Interno!.");
		}
		return redirect(routes.UsuarioController.formularioDeLogin());
	}

	private void insereUsuarioNaSessao(Usuario usuario) {
		session(AUTH, usuario.getToken().getCodigo());
	}

	@Authenticated(UsuarioAutenticado.class)
	public Result fazLogout() {
		session().remove(AUTH);
		flash("success", "Logout efetuado com sucesso.");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}
}
