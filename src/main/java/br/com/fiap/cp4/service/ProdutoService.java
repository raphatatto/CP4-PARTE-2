package br.com.fiap.cp4.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.fiap.cp4.dto.ProdutoPatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.cp4.controller.ProdutoController;
import br.com.fiap.cp4.dto.ProdutoRequest;
import br.com.fiap.cp4.dto.ProdutoResponse;
import br.com.fiap.cp4.entity.Produto;
import br.com.fiap.cp4.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoResponse create(ProdutoRequest produtoRequest) {
        Produto produto = produtoRepository.save(ProdutoService.toProduto(produtoRequest));
        return ProdutoService.toResponse(produto, false);
    }

    public List<ProdutoResponse> readAll() {
        List<Produto> produtos = produtoRepository.findAll();
        return ProdutoService.toResponse(produtos);
    }

    public Optional<ProdutoResponse> readById(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty())
            return Optional.empty();

        return Optional.of(ProdutoService.toResponse(produto.get(), false));
    }

    public Optional<ProdutoResponse> updatePatch(Long id, ProdutoPatchRequest produtoPatchRequest){
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty())
            return Optional.empty();
        Produto newProduto = toProduto(produtoPatchRequest);
        newProduto.setId(id);
        if (!produtoPatchRequest.getNome().isEmpty())
            newProduto.setNome(produtoPatchRequest.getNome());
        produtoRepository.save(newProduto);
        return Optional.of(toResponse(newProduto, false));
    }

    public Optional<ProdutoResponse> update(Long id, ProdutoRequest produtoRequest) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty())
            return Optional.empty();

        Produto newProduto = toProduto(produtoRequest);
        newProduto.setId(id);

        produtoRepository.save(newProduto);
        return Optional.of(ProdutoService.toResponse(newProduto, false));
    }

    public boolean delete(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty())
            return false;

        produtoRepository.deleteById(id);
        return true;
    }

    private static ProdutoResponse toResponse(Produto produto, boolean isSelfRel) {
        ProdutoResponse produtoResponse = new ProdutoResponse();
        produtoResponse.setId(produto.getId());
        produtoResponse.setNome(produto.getNome());
        produtoResponse.setTipo(produto.getTipo());
        produtoResponse.setSetor(produto.getSetor());
        produtoResponse.setTamanho(produto.getTamanho());
        produtoResponse.setPreco(produto.getPreco());

        if (isSelfRel)
            produtoResponse
                    .add(linkTo(methodOn(ProdutoController.class).readProduto(produtoResponse.getId())).withSelfRel());
        else
            produtoResponse.add(linkTo(methodOn(ProdutoController.class).readProdutos()).withRel("Lista de produtos"));

        return produtoResponse;
    }

    private static List<ProdutoResponse> toResponse(List<Produto> produtos) {
        return produtos.stream().map(produto -> ProdutoService.toResponse(produto, true)).collect(Collectors.toList());
    }

    private static Produto toProduto(ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setTipo(produtoRequest.getTipo());
        produto.setSetor(produtoRequest.getSetor());
        produto.setTamanho(produtoRequest.getTamanho());
        produto.setPreco(produtoRequest.getPreco());

        return produto;
    }

    private static Produto toProduto(ProdutoPatchRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setTipo(produtoRequest.getTipo());
        produto.setSetor(produtoRequest.getSetor());
        produto.setTamanho(produtoRequest.getTamanho());
        produto.setPreco(produtoRequest.getPreco());

        return produto;
    }

    public ProdutoRepository getProdutoRepository() {
        return produtoRepository;
    }

    public void setProdutoRepository(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

}
