package com.br.ag.api_cadastro_veiculos.repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ag.api_cadastro_veiculos.modelo.ChecklistVeiculo;
import com.br.ag.api_cadastro_veiculos.modelo.Veiculo;

public interface ChecklistVeiculoRepositorio extends JpaRepository<ChecklistVeiculo, Long>{
    List<ChecklistVeiculo> findByVeiculoId(Long veiculoId);
    List<ChecklistVeiculo> findByDataChecklist(LocalDate dataChecklist);
    List<ChecklistVeiculo> findByVeiculoAndDataChecklist(Veiculo veiculo, LocalDate dataChecklist);
}
