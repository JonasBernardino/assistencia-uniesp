package br.edu.uniesp.assistencia.internal.equipamento.service;

import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import br.edu.uniesp.assistencia.internal.equipamento.repository.EquipamentoRepository;
import br.edu.uniesp.assistencia.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    @Transactional(readOnly = true)
    public EquipamentoEntity buscarEntidadePorId(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Equipamento", id)
                );
    }


}
