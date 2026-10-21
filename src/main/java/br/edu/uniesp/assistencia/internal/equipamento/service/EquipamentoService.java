package br.edu.uniesp.assistencia.internal.equipamento.service;

import br.edu.uniesp.assistencia.internal.cliente.entity.ClienteEntity;
import br.edu.uniesp.assistencia.internal.cliente.service.ClienteService;
import br.edu.uniesp.assistencia.internal.equipamento.dto.CriarEquipamentoRequest;
import br.edu.uniesp.assistencia.internal.equipamento.dto.CriarEquipamentoResponse;
import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import br.edu.uniesp.assistencia.internal.equipamento.mapper.EquipamentoMapper;
import br.edu.uniesp.assistencia.internal.equipamento.repository.EquipamentoRepository;
import br.edu.uniesp.assistencia.shared.exception.RecursoNaoEncontradoException;
import br.edu.uniesp.assistencia.shared.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final ClienteService clienteService;

    @Transactional
    public CriarEquipamentoResponse criarEquipamento(CriarEquipamentoRequest request) {
        ClienteEntity cliente = clienteService
                .buscarEntidadePorId(request.clienteId());
        if (!cliente.isAtivo()) {
            throw new RegraNegocioException
                    ("Não é permitido abrir Ordem de Serviço para um cliente inativo.");
        }
        EquipamentoEntity equipamento = EquipamentoMapper.paraEntidade(request);
        cliente.adicionarEquipamento(equipamento);
        equipamentoRepository.save(equipamento);
        return EquipamentoMapper.paraResposta(equipamento);
    }


    @Transactional(readOnly = true)
    public EquipamentoEntity buscarEntidadePorId(Long id) {

        return equipamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Equipamento", id)
                );
    }


}
