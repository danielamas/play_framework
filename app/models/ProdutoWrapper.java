package models;

import java.util.List;

public class ProdutoWrapper {

	private List<Produto> produtoList;

	public ProdutoWrapper(List<Produto> produtoList) {
		this.produtoList = produtoList;
	}

	public List<Produto> getProdutoList() {
		return produtoList;
	}

	public void setProdutoList(List<Produto> produtoList) {
		this.produtoList = produtoList;
	}
}
