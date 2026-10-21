package br.edu.uniesp.assistencia.internal.equipamento.dto;

public record EquipamentoResumoResponse(
        Long id,
        String tipo,
        String marca,
        String modelo,
        String numeroSerie,
        Long clienteId,
        String clienteNome
) {
}
