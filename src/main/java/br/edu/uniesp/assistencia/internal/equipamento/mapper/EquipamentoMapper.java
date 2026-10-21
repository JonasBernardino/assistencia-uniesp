package br.edu.uniesp.assistencia.internal.equipamento.mapper;

import br.edu.uniesp.assistencia.internal.equipamento.dto.CriarEquipamentoRequest;
import br.edu.uniesp.assistencia.internal.equipamento.dto.CriarEquipamentoResponse;
import br.edu.uniesp.assistencia.internal.equipamento.dto.EquipamentoResumoResponse;
import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;

public final class EquipamentoMapper {

    private EquipamentoMapper() {
    }

    public static EquipamentoEntity paraEntidade(CriarEquipamentoRequest request) {
        return new EquipamentoEntity(
                request.tipo(),
                request.marca(),
                request.modelo(),
                request.numeroSerie()
        );
    }

    public static CriarEquipamentoResponse paraResposta(EquipamentoEntity equipamento) {
        return new CriarEquipamentoResponse(
                equipamento.getId(),
                equipamento.getTipo(),
                equipamento.getMarca(),
                equipamento.getModelo(),
                equipamento.getNumeroSerie(),
                equipamento.getCliente() != null ? equipamento.getCliente().getId() : null
        );
    }

    public static EquipamentoResumoResponse paraResumoResposta(EquipamentoEntity equipamento) {
        return new EquipamentoResumoResponse(
                equipamento.getId(),
                equipamento.getTipo(),
                equipamento.getMarca(),
                equipamento.getModelo(),
                equipamento.getNumeroSerie(),
                equipamento.getCliente() != null ? equipamento.getCliente().getId() : null,
                equipamento.getCliente() != null ? equipamento.getCliente().getNome() : null
        );
    }
}
