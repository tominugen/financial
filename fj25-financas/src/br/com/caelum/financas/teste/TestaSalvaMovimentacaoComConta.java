package br.com.caelum.financas.teste;

import javax.persistence.EntityManager;

import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.util.JPAUtil;

public class TestaSalvaMovimentacaoComConta {
	public static void main(String[] args) {

		EntityManager manager = new JPAUtil().getEntityManager();
		manager.getTransaction().begin();
		Movimentacao movimentacao = manager.getReference(Movimentacao.class, 1);
		movimentacao.getConta().setTitular("Kenzo");
		manager.getTransaction().commit();
		manager.close();

	}

}
