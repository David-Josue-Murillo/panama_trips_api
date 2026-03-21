package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Comarca;
import com.app.panama_trips.persistence.repository.ComarcaRepository;
import com.app.panama_trips.presentation.dto.ComarcaRequest;
import com.app.panama_trips.presentation.dto.ComarcaResponse;
import com.app.panama_trips.service.interfaces.IComarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComarcaService implements IComarcaService {

  private final ComarcaRepository comarcaRepository;

  @Override
  @Transactional(readOnly = true)
  public List<ComarcaResponse> getAllComarcas() {
    return this.comarcaRepository.findAll().stream()
        .map(ComarcaResponse::new)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ComarcaResponse getComarcaById(Integer id) {
    return this.comarcaRepository.findById(id)
        .map(ComarcaResponse::new)
        .orElseThrow(() -> new ResourceNotFoundException("Comarca not found with id " + id));
  }

  @Override
  @Transactional(readOnly = true)
  public ComarcaResponse getComarcaByName(String name) {
    return this.comarcaRepository.findByName(name)
        .map(ComarcaResponse::new)
        .orElseThrow(() -> new ResourceNotFoundException("Comarca not found with name " + name));
  }

  @Override
  @Transactional
  public ComarcaResponse saveComarca(ComarcaRequest comarcaRequest) {
    validateComarcaName(comarcaRequest.name());
    Comarca comarca = Comarca.builder()
        .name(comarcaRequest.name())
        .build();
    return new ComarcaResponse(this.comarcaRepository.save(comarca));
  }

  @Override
  @Transactional
  public ComarcaResponse updateComarca(Integer id, ComarcaRequest comarcaRequest) {
    Comarca comarcaExisting = this.comarcaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Comarca not found with id " + id));

    if (!comarcaExisting.getName().equals(comarcaRequest.name())) {
      validateComarcaName(comarcaRequest.name());
    }

    comarcaExisting.setName(comarcaRequest.name());
    return new ComarcaResponse(this.comarcaRepository.save(comarcaExisting));
  }

  @Override
  @Transactional
  public void deleteComarca(Integer id) {
    if (!this.comarcaRepository.existsById(id)) {
      throw new ResourceNotFoundException("Comarca not found with id " + id);
    }
    this.comarcaRepository.deleteById(id);
  }

  private void validateComarcaName(String name) {
    if (comarcaRepository.findByName(name).isPresent()) {
      throw new IllegalArgumentException("Comarca with name " + name + " already exists");
    }
  }
}
