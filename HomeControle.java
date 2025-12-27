package com.br.ag.api_cadastro_veiculos.controles;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "home";
    }

    @GetMapping("/lista-veiculos")
    public String listarVeiculos(Model model) {
        model.addAttribute("listaVeiculos", veiculoServico.buscarTodosVeiculos());
        return "lista-veiculos";
    }

    @GetMapping("/lista-checklists")
    public String buscarTodosChecklists(Model model) {
        model.addAttribute("listaChecklists", checklistServico.buscarTodosChecklists());
        return "lista-checklists";
    }

    @GetMapping("/novo")
    public String novoVeiculo(Model model) {
        model.addAttribute("novoVeiculo", new Veiculo());
        return "novo-veiculo";
    }

    @PostMapping("/gravar")
    public String gravarVeiculo(@ModelAttribute("novoVeiculo") @Valid Veiculo veiculo,
                                BindingResult erros, RedirectAttributes attributes) {
        if (erros.hasErrors()) {
            return "novo-veiculo";
        }
        veiculoServico.criarVeiculo(veiculo);
        attributes.addFlashAttribute("mensagem", "Veículo salvo com sucesso!");
        return "redirect:/lista-veiculos";
    }

    @GetMapping("/apagar/{id}")
    public String apagarVeiculo(@PathVariable long id, RedirectAttributes attributes) {
        try {
            veiculoServico.apagarVeiculo(id);
            attributes.addFlashAttribute("mensagem", "Veículo removido com sucesso!");
        } catch (VeiculoNotFoundException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/lista-veiculos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable long id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("objetoVeiculo", veiculoServico.buscarVeiculoPorId(id));
            return "alterar-veiculo";
        } catch (VeiculoNotFoundException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/lista-veiculos";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarVeiculo(@PathVariable long id,
                                @ModelAttribute("objetoVeiculo") @Valid Veiculo veiculo,
                                BindingResult erros) {
        if (erros.hasErrors()) {
            veiculo.setId(id);
            return "alterar-veiculo";
        }
        veiculoServico.alterarVeiculo(veiculo);
        return "redirect:/lista-veiculos";
    }

    @PostMapping("/buscar")
    public String buscarVeiculos(Model model, @Param("termoBusca") String termoBusca) {
        if (termoBusca == null || termoBusca.isEmpty()) return "redirect:/lista-veiculos";

        List<Veiculo> veiculos = veiculoServico.buscarPorFrota(termoBusca);
        if (veiculos.isEmpty()) veiculos = veiculoServico.buscarPorPlaca(termoBusca);
        if (veiculos.isEmpty()) veiculos = veiculoServico.buscarPorModelo(termoBusca);
        if (veiculos.isEmpty()) veiculos = veiculoServico.buscarPorFrota(termoBusca);
        if (veiculos.isEmpty()) veiculos = veiculoServico.buscarPorRegiao(termoBusca);

        model.addAttribute("listaVeiculos", veiculos);
        return "lista-veiculos";
    }

    @GetMapping("/novo-checklist/{id}")
    public String novoChecklist(@PathVariable("id") Long idVeiculo, Model model) {
        try {
            Veiculo veiculo = veiculoServico.buscarVeiculoPorId(idVeiculo);
            ChecklistVeiculo item = new ChecklistVeiculo();
            item.setVeiculo(veiculo);
            item.setDataChecklist(LocalDate.now()); 

            model.addAttribute("item", item);
            model.addAttribute("veiculo", veiculo);
            return "novo-checklist";
        } catch (VeiculoNotFoundException e) {
            return "redirect:/lista-veiculos";
        }
    }

    @PostMapping("/gravar-checklist/{id}")
    public String gravarChecklist(@PathVariable("id") long idVeiculo,
                                  @ModelAttribute("item") @Valid ChecklistVeiculo checklist,
                                  BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            try {
                model.addAttribute("veiculo", veiculoServico.buscarVeiculoPorId(idVeiculo));
            } catch (Exception e) {}
            return "novo-checklist";
        }
 
        try {
            
            veiculoServico.adicionarChecklist(idVeiculo, checklist);
            attributes.addFlashAttribute("mensagem", "Checklist gravado com sucesso!");
            return "redirect:/lista-checklists";
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", "Erro ao gravar: " + e.getMessage());
            return "redirect:/lista-veiculos";
        }
    }
}