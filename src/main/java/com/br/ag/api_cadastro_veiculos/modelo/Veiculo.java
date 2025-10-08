package com.br.ag.api_cadastro_veiculos.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

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

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL)
    private List<ChecklistVeiculo> checklists = new ArrayList<>();

    public Veiculo() {
    }

    public Veiculo(Long id, String modelo, String placa, String frota, String regiao) {
        this.id = id;
        this.modelo = modelo;
        this.placa = placa;
        this.frota = frota;
        this.regiao = regiao;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return this.placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFrota() {
        return this.frota;
    }

    public void setFrota(String frota) {
        this.frota = frota;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public List<ChecklistVeiculo> getChecklists() {
        return this.checklists;
    }

    public void adicionarChecklist(ChecklistVeiculo checklist) {
        this.checklists.add(checklist);
        checklist.setVeiculo(this);
    }

    public Veiculo id(Long id) {
        setId(id);
        return this;
    }

    public Veiculo modelo(String modelo) {
        setModelo(modelo);
        return this;
    }

    public Veiculo placa(String placa) {
        setPlaca(placa);
        return this;
    }

    public Veiculo frota(String frota) {
        setFrota(frota);
        return this;
    }

    public Veiculo regiao(String regiao) {
        setRegiao(regiao);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Veiculo)) {
            return false;
        }
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id) && Objects.equals(modelo, veiculo.modelo) && Objects.equals(placa, veiculo.placa) && Objects.equals(frota, veiculo.frota) && Objects.equals(regiao, veiculo.regiao) && Objects.equals(checklists, veiculo.checklists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modelo, placa, frota, regiao, checklists);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", placa='" + getPlaca() + "'" +
            ", frota='" + getFrota() + "'" +
            ", regiao='" + getRegiao() + "'" +
         //   ", checklists='" + getChecklists() + "'" +
            "}";
    }
}
