package br.com.heycheff.api.controller;

import java.util.List;

import br.com.heycheff.api.dto.ReceitaModal;
import br.com.heycheff.api.util.exception.ReceitaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.heycheff.api.dto.ReceitaFeed;
import br.com.heycheff.api.model.Receita;
import br.com.heycheff.api.service.ReceitaService;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
	@Autowired
	private ReceitaService service;

	@GetMapping
	public ResponseEntity<List<ReceitaFeed>> loadFeed() {
		return ResponseEntity.ok(service.loadFeed());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReceitaModal> loadModal(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.loadModal(id));
		} catch (ReceitaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<Receita> incluir(@RequestBody ReceitaModal receita) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.incluir(receita));
	}

}
