package dev.israelld.baseBank.controller;

import java.util.List;

import dev.israelld.baseBank.model.Account;
import dev.israelld.baseBank.model.Historic;
import dev.israelld.baseBank.repository.HistoricRepository;
import dev.israelld.baseBank.service.AccountService;
import dev.israelld.baseBank.service.HistoricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historic")
@CrossOrigin("*")
public class HistoricController {

	@Autowired
    private HistoricService service;
    @Autowired
    private HistoricRepository repository;
    @Autowired
    private AccountService accountService;

	@GetMapping("/{id}")
    public ResponseEntity<List<Historic>> GetById(@PathVariable Long id) {
        Account account = accountService.findById(id);
        List<Historic> obj = this.repository.findByAccount(account);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Historic>> GetAll() {
        List<Historic> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }
}
