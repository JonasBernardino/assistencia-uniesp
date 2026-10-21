package br.edu.uniesp.assistencia.internal.equipamento.service;

import br.edu.uniesp.assistencia.internal.cliente.entity.ClienteEntity;
import br.edu.uniesp.assistencia.internal.cliente.repository.ClienteRepository;
import br.edu.uniesp.assistencia.internal.equipamento.dto.AtualizarEquipamentoRequest;
import br.edu.uniesp.assistencia.internal.equipamento.dto.CriarEquipamentoRequest;
import br.edu.uniesp.assistencia.internal.equipamento.dto.CriarEquipamentoResponse;
import br.edu.uniesp.assistencia.internal.equipamento.dto.EquipamentoResumoResponse;
import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import br.edu.uniesp.assistencia.internal.equipamento.mapper.EquipamentoMapper;
import br.edu.uniesp.assistencia.internal.equipamento.repository.EquipamentoRepository;
import br.edu.uniesp.assistencia.shared.exception.RecursoNaoEncontradoException;
import br.edu.uniesp.assistencia.shared.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public CriarEquipamentoResponse criarEquipamento(CriarEquipamentoRequest request) {
        ClienteEntity cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", request.clienteId()));

        if (!cliente.isAtivo()) {
            throw new RegraNegocioException("Não é permitido cadastrar equipamentos para um cliente inativo.");
        }

        EquipamentoEntity equipamento = EquipamentoMapper.paraEntidade(request);
        cliente.adicionarEquipamento(equipamento);
        equipamentoRepository.save(equipamento);

        return EquipamentoMapper.paraResposta(equipamento);
    }

    @Transactional(readOnly = true)
    public EquipamentoResumoResponse buscarPorId(Long id) {
        EquipamentoEntity equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipamento", id));
        return EquipamentoMapper.paraResumoResposta(equipamento);
    }

    @Transactional(readOnly = true)
    public EquipamentoEntity buscarEntidadePorId(Long id) {

        return equipamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Equipamento", id)
                );
    }

    @Transactional(readOnly = true)
    public Page<EquipamentoResumoResponse> listarTodos(Pageable pageable) {
        return equipamentoRepository.findAll(pageable)
                .map(EquipamentoMapper::paraResumoResposta);
    }

    @Transactional(readOnly = true)
    public List<EquipamentoResumoResponse> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNaoEncontradoException("Cliente", clienteId);
        }
        return equipamentoRepository.findByClienteId(clienteId).stream()
                .map(EquipamentoMapper::paraResumoResposta)
                .toList();
    }

    @Transactional
    public EquipamentoResumoResponse atualizarEquipamento(Long id, AtualizarEquipamentoRequest request) {
        EquipamentoEntity equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipamento", id));

        equipamento.alterarDados(request.tipo(), request.marca(), request.modelo(), request.numeroSerie());
        equipamentoRepository.save(equipamento);

        return EquipamentoMapper.paraResumoResposta(equipamento);
    }
}
