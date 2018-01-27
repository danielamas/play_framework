package daos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import models.Produto;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Finder;
import com.avaje.ebean.Query;

public class ProdutoDAO {

	private Finder<Long, Produto> produtos = new Finder<>(Produto.class);

	public Optional<Produto> comCodigo(String codigo) {
		Produto produto = produtos.query()
				.where()
				.eq("codigo", codigo)
				.findUnique();
		return Optional.ofNullable(produto);
	}

	public List<Produto> todos() {
		return produtos.all();
	}

	public List<Produto> doTipo(String tipo) {
		return produtos.query()
				.where()
				.eq("tipo", tipo)
				.findList();
	}

	public List<Produto> comFiltros(Map<String, String> parametros) {
		ExpressionList<Produto> consulta = produtos.query().where();
		parametros.forEach((k, v) -> {
			consulta.eq(k, v);
		});
		return consulta.findList();
	}
}
