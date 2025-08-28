package br.com.fiap.cp4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.cp4.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
