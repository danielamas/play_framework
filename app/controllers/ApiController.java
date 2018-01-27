package controllers;

import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import models.ProdutoWrapper;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import validadores.ValidationParam;
import daos.ProdutoDAO;

public class ApiController extends Controller {

	@Inject
	private ProdutoDAO produtoDAO;

	@Inject
	private FormFactory formularios;

	@Inject
	private ValidationParam validationParam;

	public Result todos() {
		ProdutoWrapper pw = new ProdutoWrapper(produtoDAO.todos());
		return ok(Json.toJson(pw));
	}

	public Result doTipo(String tipo) {
		ProdutoWrapper pw = new ProdutoWrapper(produtoDAO.doTipo(tipo));
		return ok(Json.toJson(pw));
	}

	public Result comFiltros() {
		DynamicForm form = formularios.form().bindFromRequest();
		validationParam.validate(form);
		if(form.hasErrors()) {
			JsonNode erros = Json.newObject().set("erros", form.errorsAsJson());
			return badRequest(erros);
		}
		Map<String, String> parametros = form.data();
		ProdutoWrapper pw = new ProdutoWrapper(produtoDAO.comFiltros(parametros));
		return ok(Json.toJson(pw));
	}
}
