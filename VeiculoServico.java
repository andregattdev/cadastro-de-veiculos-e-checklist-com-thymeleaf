package com.br.ag.api_cadastro_veiculos.servicos;

import java.time.LocalDate; // IMPORTANTE: Faltava este import
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.ag.api_cadastro_veiculos.excecao.VeiculoNotFoundException;
import com.br.ag.api_cadastro_veiculos.modelo.ChecklistVeiculo;
import com.br.ag.api_cadastro_veiculos.modelo.Veiculo;
import com.br.ag.api_cadastro_veiculos.repositorio.VeiculoRepositorio;

@Service
public class VeiculoServico {
    
    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private ChecklistServico checklistServico;

    public Veiculo criarVeiculo(Veiculo veiculo) {
        return veiculoRepositorio.save(veiculo);
    }

    public List<Veiculo> buscarTodosVeiculos() {
        return veiculoRepositorio.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Veiculo adicionarChecklist(Long veiculoId, ChecklistVeiculo checklist) throws VeiculoNotFoundException {
        // 1. Busca o veículo atualizado
        Veiculo veiculo = veiculoRepositorio.findById(veiculoId)
                .orElseThrow(() -> new VeiculoNotFoundException("Veículo não encontrado"));

        // 2. Garante que a data não vá nula
        if (checklist.getDataChecklist() == null) {
            checklist.setDataChecklist(LocalDate.now());
        }

        // 3. Vincula as duas pontas da relação
        checklist.setVeiculo(veiculo);

        // 4. Salva o Checklist primeiro para garantir a persistência
        checklistServico.criarChecklist(checklist);

        return veiculo;
    }

    public Veiculo buscarVeiculoPorId(Long id) throws VeiculoNotFoundException {
        return veiculoRepositorio.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException("Veículo com id : " + id + " não existe"));
    }

    public void apagarVeiculo(Long id) throws VeiculoNotFoundException {
        Veiculo veiculo = buscarVeiculoPorId(id);
        veiculoRepositorio.delete(veiculo);
    }

    public Veiculo alterarVeiculo(Veiculo veiculo) {
        return veiculoRepositorio.save(veiculo);
    }

    public List<Veiculo> buscarPorFrota(String frota) {
        return veiculoRepositorio.findByFrota(frota);
    }

    public List<Veiculo> buscarPorPlaca(String placa) {
        return veiculoRepositorio.findByPlacaContainingIgnoreCase(placa);
    }

    public List<Veiculo> buscarPorModelo(String modelo) {
        return veiculoRepositorio.findByModeloContainingIgnoreCase(modelo);
    }

    public List<Veiculo> buscarPorRegiao(String regiao) {
        return veiculoRepositorio.findByRegiaoContainingIgnoreCase(regiao);
    }
}