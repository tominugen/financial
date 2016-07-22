package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@Stateless
public class MovimentacaoDao {

	@Inject
	private EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		manager.joinTransaction();
		this.manager.persist(movimentacao);

	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m",
				Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		manager.joinTransaction();
		Movimentacao movimentacaoParaRemover = this.manager.find(
				Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}

	public List<Movimentacao> listaTodasMovimentacoes(Conta conta) {
		String jpql = "select m from Movimentacao m"
				+ "where m.conta = :conta order by m.valor desc";

		Query query = this.manager.createQuery(jpql);
		query.setParameter("conta", conta);
		return query.getResultList();
	}

	public List<Movimentacao> listaPorValorETipo(BigDecimal valor,
			TipoMovimentacao tipo) {
		String jpql = "select m from Movimentacao m"
				+ "where m.valor <= :valor and m.tipoMovimentacao = :tipo";

		Query query = this.manager.createQuery(jpql);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipo);
		return query.getResultList();

	}

	public List<Movimentacao> listaComCategorias() {
		return manager.createQuery(
				"select distinct m from Movimentacao m "
						+ " join fetch m.categorias", Movimentacao.class)
				.getResultList();
	}

	public List<Movimentacao> listaTodasComCriteria() {

		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteria = builder
				.createQuery(Movimentacao.class);
		criteria.from(Movimentacao.class);

		return this.manager.createQuery(criteria).getResultList();
	}

	public List<Movimentacao> pesquisa(Conta conta,
			TipoMovimentacao tipoMovimentacao, Integer mes) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteria = builder
				.createQuery(Movimentacao.class);

		Root<Movimentacao> root = criteria.from(Movimentacao.class);

		Predicate conjunction = builder.conjunction();
		if (conta.getId() != null) {
			conjunction = builder.and(conjunction,
					builder.equal(root.<Conta> get("conta"), conta));
		}

		if (mes != null && mes != 0) {
			Expression<Integer> expression = builder.function("month",
					Integer.class, root.<Calendar> get("data"));
			conjunction = builder.and(conjunction,
					builder.equal(expression, mes));

		}

		if (tipoMovimentacao != null) {
			conjunction = builder.and(conjunction, builder.equal(conjunction,
					builder.equal(
							root.<TipoMovimentacao> get("tipoMovimentacao"),
							tipoMovimentacao)));
		}

		criteria.where(conjunction);
		return manager.createQuery(criteria).getResultList();

	}
}