package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.ComarcaRequest;
import com.app.panama_trips.presentation.dto.ComarcaResponse;
import com.app.panama_trips.service.interfaces.IComarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comarcas")
@RequiredArgsConstructor
@Validated
@Tag(name = "Comarca", description = "Endpoints for managing comarcas")
public class ComarcaController {

  private final IComarcaService comarcaService;

  @GetMapping
  @Operation(summary = "Get all comarcas")
  public ResponseEntity<List<ComarcaResponse>> findAllComarcas() {
    return ResponseEntity.ok(this.comarcaService.getAllComarcas());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a comarca by id")
  public ResponseEntity<ComarcaResponse> findComarcaById(@PathVariable Integer id) {
    return ResponseEntity.ok(this.comarcaService.getComarcaById(id));
  }

  @GetMapping("/search")
  @Operation(summary = "Get a comarca by name")
  public ResponseEntity<ComarcaResponse> findComarcaByName(@RequestParam String name) {
    return ResponseEntity.ok(this.comarcaService.getComarcaByName(name));
  }

  @PostMapping
  @Operation(summary = "Create a new comarca")
  public ResponseEntity<ComarcaResponse> saveComarca(@RequestBody @Valid ComarcaRequest comarcaRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.comarcaService.saveComarca(comarcaRequest));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a comarca")
  public ResponseEntity<ComarcaResponse> updateComarca(@PathVariable Integer id,
      @RequestBody @Valid ComarcaRequest comarcaRequest) {
    return ResponseEntity.ok(this.comarcaService.updateComarca(id, comarcaRequest));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a comarca")
  public ResponseEntity<Void> deleteComarca(@PathVariable Integer id) {
    this.comarcaService.deleteComarca(id);
    return ResponseEntity.noContent().build();
  }
}
