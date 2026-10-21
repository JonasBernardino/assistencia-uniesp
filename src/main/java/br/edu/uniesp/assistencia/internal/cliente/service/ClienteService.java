package br.edu.uniesp.assistencia.internal.cliente.service;

import br.edu.uniesp.assistencia.internal.cliente.dto.AtualizarClienteRequest;
import br.edu.uniesp.assistencia.internal.cliente.dto.ClienteResumoResponse;
import br.edu.uniesp.assistencia.internal.cliente.dto.CriarClienteRequest;
import br.edu.uniesp.assistencia.internal.cliente.dto.CriarClienteResponse;
import br.edu.uniesp.assistencia.internal.cliente.entity.ClienteEntity;
import br.edu.uniesp.assistencia.internal.cliente.mapper.ClienteMapper;
import br.edu.uniesp.assistencia.internal.cliente.repository.ClienteRepository;
import br.edu.uniesp.assistencia.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public CriarClienteResponse criarCliente(CriarClienteRequest request) {
        ClienteEntity cliente = ClienteMapper.converteParaEntidade(request);
        clienteRepository.save(cliente);
        return ClienteMapper.converterParaResposta(cliente);
    }

    public ClienteResumoResponse buscarClientePorId(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
        return ClienteMapper.paraResumoResposta(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteEntity buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente", id)
                );
    }

    @Transactional(readOnly = true)
    public Page<ClienteResumoResponse> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(ClienteMapper::paraResumoResposta);
    }

    @Transactional
    public ClienteResumoResponse atualizarCliente(Long id, AtualizarClienteRequest request) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
        cliente.alterarNome(request.nome());
        cliente.alterarEmail(request.email());
        clienteRepository.save(cliente);
        return ClienteMapper.paraResumoResposta(cliente);
    }

    @Transactional
    public void inativarCliente(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Cliente", id));
        cliente.inativar();
    }

    @Transactional
    public void ativarCliente(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Cliente", id));
        cliente.ativar();
    }

}
