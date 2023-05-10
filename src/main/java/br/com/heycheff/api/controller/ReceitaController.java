package br.com.heycheff.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.heycheff.api.dto.ReceitaDTO;
import br.com.heycheff.api.model.Receita;
import br.com.heycheff.api.service.ReceitaService;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
	@Autowired
	private ReceitaService service;

	@GetMapping
	public ResponseEntity<List<ReceitaDTO>> listAll() {
		return service.listAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReceitaDTO> loadModal(@PathVariable Integer id) {
		return service.loadModal(id);
	}
	
	@PostMapping
	public ResponseEntity<Receita> incluir(@RequestBody Receita receita) {
		return ResponseEntity.ok(service.incluir(receita));
	}

}
