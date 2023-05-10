package br.com.heycheff.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.heycheff.api.dto.ReceitaDTO;
import br.com.heycheff.api.model.Receita;
import br.com.heycheff.api.repository.ReceitaRepository;

@Service
public class ReceitaService {

	@Autowired
	private ReceitaRepository repository;

	public ResponseEntity<List<ReceitaDTO>> listAll() {
		List<Receita> receitas = repository.findAll();
		List<ReceitaDTO> receitasdto = new ArrayList<>();
		receitas.forEach(r -> {
			ReceitaDTO dto = new ReceitaDTO(r.getPathThumb(), r.getTitulo());
			receitasdto.add(dto);
		});
		return ResponseEntity.ok(receitasdto);

	}

	public ResponseEntity<ReceitaDTO> loadModal(Integer id) {
		Optional<Receita> optional = repository.findById(id);
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Receita receita = optional.get();
		ReceitaDTO dto = new ReceitaDTO(receita.getPathThumb(), receita.getTitulo());
		return ResponseEntity.ok(dto);
	}

	public Receita incluir(Receita receita) {
		return repository.save(receita);		
	}
	
}
