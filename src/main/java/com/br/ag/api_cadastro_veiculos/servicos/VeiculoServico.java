package com.br.ag.api_cadastro_veiculos.servicos;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ag.api_cadastro_veiculos.excecao.VeiculoNotFoundException;
import com.br.ag.api_cadastro_veiculos.modelo.ChecklistVeiculo;
import com.br.ag.api_cadastro_veiculos.modelo.Veiculo;
import com.br.ag.api_cadastro_veiculos.repositorio.VeiculoRepositorio;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VeiculoServico {

    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private ChecklistServico checklistServico;

    public Veiculo criarVeiculo(Veiculo veiculo) {
        return veiculoRepositorio.save(veiculo);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Veiculo adicionarChecklist(Long veiculoId, ChecklistVeiculo checklist) throws VeiculoNotFoundException {
        Veiculo veiculo = buscarVeiculoPorId(veiculoId);

        // Recarregar o checklist se ele já existir para garantir que a versão seja inicializada
        if (checklist.getId() != null) {
            ChecklistVeiculo existingChecklistOpt = checklistServico.buscarChecklistPorId(checklist.getId());
            if (Objects.nonNull(existingChecklistOpt)) {
                checklist = existingChecklistOpt;  // Recarrega o checklist, agora com a versão inicializada
            }
        }

        checklist.setVeiculo(veiculo);  // Associa o checklist ao veículo
        veiculo.adicionarChecklist(checklist);  // Associa o checklist ao veículo
        checklistServico.criarChecklist(checklist);  // Salva o checklist

        return veiculoRepositorio.save(veiculo);  // Salva o veículo (que tem o checklist associado)
    }


    public List<Veiculo> buscarTodosVeiculos() {
        return veiculoRepositorio.findAll();
    }

    public Veiculo buscarVeiculoPorId(Long id) throws VeiculoNotFoundException {
        Optional<Veiculo> opt = veiculoRepositorio.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new VeiculoNotFoundException("Veículo com id : " + id + " não existe");
        }        
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
