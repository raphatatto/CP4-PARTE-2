package br.com.fiap.cp4.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.cp4.dto.ProdutoRequest;
import br.com.fiap.cp4.dto.ProdutoResponse;
import br.com.fiap.cp4.service.ProdutoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/mercado")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> createProduto(@Valid @RequestBody ProdutoRequest produtoRequest) {
        ProdutoResponse produto = produtoService.create(produtoRequest);
        return new ResponseEntity<>(produto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> readProdutos() {
        List<ProdutoResponse> produtos = produtoService.readAll();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> readProduto(@PathVariable Long id) {
        Optional<ProdutoResponse> produto = produtoService.readById(id);

        if (produto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(produto.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> updateProduto(@PathVariable Long id,
            @Valid @RequestBody ProdutoRequest produtoRequest) {
        Optional<ProdutoResponse> produto = produtoService.update(id, produtoRequest);

        if (produto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(produto.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoResponse> deleteProduto(@PathVariable Long id) {
        boolean wasDeleted = produtoService.delete(id);

        if (wasDeleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
