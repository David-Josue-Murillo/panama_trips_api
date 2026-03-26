package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Comarca;
import com.app.panama_trips.persistence.repository.ComarcaRepository;
import com.app.panama_trips.presentation.dto.ComarcaResponse;
import com.app.panama_trips.service.implementation.ComarcaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComarcaServiceTest {

  @Mock
  private ComarcaRepository comarcaRepository;

  @InjectMocks
  private ComarcaService comarcaService;

  @Test
  @DisplayName("Should return all comarcas")
  void getAllComarcas_Success() {
    when(comarcaRepository.findAll()).thenReturn(List.of(DataProvider.comarcaGunaYalaMock));

    List<ComarcaResponse> result = comarcaService.getAllComarcas();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).name()).isEqualTo("Guna Yala");
    verify(comarcaRepository).findAll();
  }

  @Test
  @DisplayName("Should return comarca by ID")
  void getComarcaById_Success() {
    when(comarcaRepository.findById(1)).thenReturn(Optional.of(DataProvider.comarcaGunaYalaMock));

    ComarcaResponse result = comarcaService.getComarcaById(1);

    assertThat(result.name()).isEqualTo("Guna Yala");
    verify(comarcaRepository).findById(1);
  }

  @Test
  @DisplayName("Should throw exception when comarca ID not found")
  void getComarcaById_NotFound() {
    when(comarcaRepository.findById(1)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> comarcaService.getComarcaById(1))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Comarca not found with id 1");
  }

  @Test
  @DisplayName("Should save new comarca")
  void saveComarca_Success() {
    when(comarcaRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(comarcaRepository.save(any(Comarca.class))).thenReturn(DataProvider.comarcaGunaYalaMock);

    ComarcaResponse result = comarcaService.saveComarca(DataProvider.comarcaRequestMock);

    assertThat(result.name()).isEqualTo("Guna Yala");
    verify(comarcaRepository).save(any(Comarca.class));
  }

  @Test
  @DisplayName("Should throw exception when saving comarca with existing name")
  void saveComarca_AlreadyExists() {
    when(comarcaRepository.findByName(anyString())).thenReturn(Optional.of(DataProvider.comarcaGunaYalaMock));

    assertThatThrownBy(() -> comarcaService.saveComarca(DataProvider.comarcaRequestMock))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("already exists");
  }

  @Test
  @DisplayName("Should update existing comarca")
  void updateComarca_Success() {
    Comarca existing = DataProvider.comarcaNgabeBugleMock;
    when(comarcaRepository.findById(2)).thenReturn(Optional.of(existing));
    when(comarcaRepository.save(any(Comarca.class))).thenReturn(existing);

    ComarcaResponse result = comarcaService.updateComarca(2, DataProvider.comarcaRequestMock);

    verify(comarcaRepository).save(existing);
    assertThat(existing.getName()).isEqualTo("Guna Yala");
  }

  @Test
  @DisplayName("Should delete comarca")
  void deleteComarca_Success() {
    when(comarcaRepository.existsById(1)).thenReturn(true);

    comarcaService.deleteComarca(1);

    verify(comarcaRepository).deleteById(1);
  }

  @Test
  @DisplayName("Should throw exception when deleting non-existent comarca")
  void deleteComarca_NotFound() {
    when(comarcaRepository.existsById(1)).thenReturn(false);

    assertThatThrownBy(() -> comarcaService.deleteComarca(1))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
