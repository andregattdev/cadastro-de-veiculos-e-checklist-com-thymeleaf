package com.br.ag.api_cadastro_veiculos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ag.api_cadastro_veiculos.modelo.Veiculo;

public interface VeiculoRepositorio extends JpaRepository<Veiculo, Long>{
    List<Veiculo> findByFrota(String frota);
    List<Veiculo> findByPlacaContainingIgnoreCase(String placa);
    List<Veiculo> findByModeloContainingIgnoreCase(String modelo);
    List<Veiculo> findByRegiaoContainingIgnoreCase(String regiao);
}
