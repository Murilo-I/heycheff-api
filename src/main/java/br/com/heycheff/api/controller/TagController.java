package br.com.heycheff.api.controller;

import br.com.heycheff.api.model.Tag;
import br.com.heycheff.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService service;

    @GetMapping
    public ResponseEntity<List<Tag>> list(@RequestParam(required = false) Integer receitaId) {
        if (receitaId == null) return ResponseEntity.ok(service.listAll());
        else return ResponseEntity.ok(service.findByReceitaId(receitaId));
    }
}
