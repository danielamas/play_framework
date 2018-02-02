package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import autenticadores.AdminAutenticado;
import daos.ProdutoDAO;
import daos.UsuarioDAO;
import models.Produto;
import models.Usuario;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import views.html.produtos;
import views.html.usuarios;

@Authenticated(AdminAutenticado.class)
public class AdminController extends Controller {

	@Inject
	private ProdutoDAO produtoDAO;

	@Inject
	private UsuarioDAO usuarioDAO;

	public Result usuarios() {
		Optional<List<Usuario>> usuariosDB = usuarioDAO.todos();
		List<Usuario> usuarioList = new ArrayList<>();
		if(usuariosDB.isPresent()) {
			usuarioList = usuariosDB.get();
		}
		return ok(usuarios.render(usuarioList));
	}

	//segundo modo para nao quebrar o todos do produtoDAO
	public Result produtos() {
		List<Produto> produtoList = produtoDAO.todos();
		if(produtoList == null) {
			produtoList = new ArrayList<>();
		}
		return ok(produtos.render(produtoList));
	}
}
