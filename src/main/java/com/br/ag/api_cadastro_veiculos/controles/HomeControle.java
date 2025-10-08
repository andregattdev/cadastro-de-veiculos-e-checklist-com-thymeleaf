package com.br.ag.api_cadastro_veiculos.controles;

import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.br.ag.api_cadastro_veiculos.excecao.VeiculoNotFoundException;
import com.br.ag.api_cadastro_veiculos.modelo.ChecklistVeiculo;
import com.br.ag.api_cadastro_veiculos.modelo.Veiculo;
import com.br.ag.api_cadastro_veiculos.servicos.ChecklistServico;
import com.br.ag.api_cadastro_veiculos.servicos.VeiculoServico;

import jakarta.validation.Valid;

@Controller
public class HomeControle {

    @Autowired
    private VeiculoServico veiculoServico;

    @Autowired
    private ChecklistServico checklistServico;

    @GetMapping("/")
    public String home() {
        return "/home";
    }

    // Mostrar a lista de veculos cadastrados
    @GetMapping("/lista-veiculos")
    public String listarVeiculos(Model model) {
        List<Veiculo> veiculos = veiculoServico.buscarTodosVeiculos();
        model.addAttribute("listaVeiculos", veiculos);
        return "/lista-veiculos";
    }

    // Mostrar a lista de checklists feitos
    @GetMapping("/lista-checklists")
    public String buscarTodosChecklists(Model model) {
        List<ChecklistVeiculo> checklists = checklistServico.buscarTodosChecklists();
        model.addAttribute("listaChecklists", checklists);
        return "/lista-checklists";
    }

    @GetMapping("/novo")
    public String novoVeiculo(Model model) {
        Veiculo veiculo = new Veiculo();
        model.addAttribute("novoVeiculo", veiculo);
        model.addAttribute("checklist", new ChecklistVeiculo());
        return "/novo-veiculo";
    }

    @PostMapping("/gravar")
    public String gravarVeiculo(@ModelAttribute("novoVeiculo") @Valid Veiculo veiculo,
            BindingResult erros,
            RedirectAttributes attributes) throws VeiculoNotFoundException {
        if (erros.hasErrors()) {
            return "/novo-veiculo";
        }
        veiculoServico.criarVeiculo(veiculo);
        attributes.addFlashAttribute("mensagem", "Veículo salvos com sucesso!");
        return "redirect:/novo";
    }

    // Método apagar veículo do BD.
    @GetMapping("/apagar/{id}")
    public String apagarVeiculo(@PathVariable long id, RedirectAttributes attributes) {
        try {
            veiculoServico.apagarVeiculo(id);
        } catch (VeiculoNotFoundException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/lista-veiculos";
    }

    // Método editar veículo cadastrado
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable long id, RedirectAttributes attributes, Model model) {
        try {
            Veiculo veiculo = veiculoServico.buscarVeiculoPorId(id);
            model.addAttribute("objetoVeiculo", veiculo);
            return "/alterar-veiculo";
        } catch (VeiculoNotFoundException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/lista-veiculos";
    }

    @PostMapping("/editar/{id}")
    public String editarVeiculo(@PathVariable long id,
            @ModelAttribute("objetoVeiculo") @Valid Veiculo veiculo,
            BindingResult erros) {
        if (erros.hasErrors()) {
            veiculo.setId(id);
            return "/alterar-veiculo";
        }
        veiculoServico.alterarVeiculo(veiculo);
        return "redirect:/lista-veiculos";
    }

    // Motor de busca do site
    @PostMapping("/buscar")
    public String buscarVeiculos(Model model, @Param("termoBusca") String termoBusca) {
        List<Veiculo> veiculos = null;

        if (termoBusca != null && !termoBusca.isEmpty()) {
            veiculos = veiculoServico.buscarPorFrota(termoBusca);
            if (veiculos.isEmpty()) {
                veiculos = veiculoServico.buscarPorPlaca(termoBusca);
            }
            if (veiculos.isEmpty()) {
                veiculos = veiculoServico.buscarPorModelo(termoBusca);
            }
            if (veiculos.isEmpty()) {
                veiculos = veiculoServico.buscarPorRegiao(termoBusca);
            }
        } else {
            return "redirect:/lista-veiculos";
        }

        model.addAttribute("listaVeiculos", veiculos);
        return "/lista-veiculos";
    }

    @GetMapping("novo-checklist/{id}")
    public String novoChecklist(@PathVariable("id") Long idVeiculo, Model model) throws VeiculoNotFoundException {
        try {
            System.out.println("enviando inf");
            Veiculo veiculo = veiculoServico.buscarVeiculoPorId(idVeiculo);
            ChecklistVeiculo checklist = new ChecklistVeiculo();
            checklist.setVeiculo(veiculo);
            model.addAttribute("item", checklist);
            model.addAttribute("veiculo", veiculo);
            return "novo-checklist";
        } catch (VeiculoNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Veículo não encontrado!");
            return "erro"; 
        }
    }
    
   @PostMapping("/gravar-checklist/{id}")
public String gravarChecklist(@PathVariable("id") long idVeiculo,
        @ModelAttribute("item") @Valid ChecklistVeiculo checklist,
        BindingResult result, RedirectAttributes attributes) {

    if (result.hasErrors()) {
        return "novo-checklist";
    }

    try {
        veiculoServico.adicionarChecklist(idVeiculo, checklist);
        attributes.addFlashAttribute("mensagem", "Checklist salvo com sucesso!");
        return "redirect:/lista-checklists";
    } catch (ObjectOptimisticLockingFailureException e) {
        attributes.addFlashAttribute("erro", "O checklist foi modificado por outro usuário. Tente novamente.");
        return "redirect:/erro";
    } catch (Exception e) {
        attributes.addFlashAttribute("erro", "Erro ao salvar checklist.");
        return "redirect:/erro";
    }
}
    

    @PostMapping("/buscar-checklists")
    public String buscarChecklistsPorData(Model model, @RequestParam String data) {
        LocalDate dataChecklist = LocalDate.parse(data);
        List<ChecklistVeiculo> checklists = checklistServico.buscarChecklistsPorData(dataChecklist);
        model.addAttribute("listaChecklists", checklists);
        return "/lista-checklists";
    }

    @GetMapping("/deletar-checklist/{id}")
    public String deletarChecklist(@PathVariable Long id, RedirectAttributes attributes) {
        checklistServico.deletarChecklist(id);
        attributes.addFlashAttribute("mensagem", "Checklist deletado com sucesso!");
        return "redirect:/checklist/lista-checklists";
    }
}
