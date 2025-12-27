package com.br.ag.api_cadastro_veiculos.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Veiculo {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O modelo do veículo deve ser informado")
    private String modelo;

    @NotBlank(message = "A placa deve ser informada")
    private String placa;

    @NotBlank(message = "A frota deve ser informada")
    private String frota;

    @NotBlank(message = "A região deve ser informada")
    private String regiao;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistVeiculo> checklists = new ArrayList<>();

    public Veiculo() {}

    public void adicionarChecklist(ChecklistVeiculo checklist) {
        this.checklists.add(checklist);
        checklist.setVeiculo(this);
    }

    // Getters e Setters padrão...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getFrota() { return frota; }
    public void setFrota(String frota) { this.frota = frota; }
    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }
    public List<ChecklistVeiculo> getChecklists() { return checklists; }
    public void setChecklists(List<ChecklistVeiculo> checklists) { this.checklists = checklists; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id); // Comparar APENAS pelo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}