package com.br.ag.api_cadastro_veiculos.servicos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ag.api_cadastro_veiculos.modelo.ChecklistVeiculo;
import com.br.ag.api_cadastro_veiculos.modelo.Veiculo;
import com.br.ag.api_cadastro_veiculos.repositorio.ChecklistVeiculoRepositorio;

@Service
public class ChecklistServico {

    @Autowired
    private ChecklistVeiculoRepositorio checklistRepositorio;

    public ChecklistVeiculo criarChecklist(ChecklistVeiculo checklist) {
        System.out.println("criando checklist de veiculo");
        System.out.println(checklist);
        return checklistRepositorio.save(checklist);
    }

    public List<ChecklistVeiculo> buscarTodosChecklists() {
        return checklistRepositorio.findAll();
    }

    public ChecklistVeiculo buscarChecklistPorId(Long id) {
        Optional<ChecklistVeiculo> opt = checklistRepositorio.findById(id);
        return opt.orElse(null);
    }

    public ChecklistVeiculo alterarChecklist(ChecklistVeiculo checklistVeiculo) {
        return checklistRepositorio.save(checklistVeiculo);
    }

    // Novo método para buscar checklists por data
    public List<ChecklistVeiculo> buscarChecklistsPorData(LocalDate dataChecklist) {
        return checklistRepositorio.findByDataChecklist(dataChecklist);
    }

    // Novo método para buscar checklists por veículo e data
    public List<ChecklistVeiculo> buscarChecklistsPorVeiculoEData(Veiculo veiculo, LocalDate dataChecklist) {
        return checklistRepositorio.findByVeiculoAndDataChecklist(veiculo, dataChecklist);
    }

     // Método para deletar checklist
     public void deletarChecklist(Long id) {
        checklistRepositorio.deleteById(id);
    }
}
