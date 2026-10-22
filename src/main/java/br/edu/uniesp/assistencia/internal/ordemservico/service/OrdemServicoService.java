package br.edu.uniesp.assistencia.internal.ordemservico.service;

import br.edu.uniesp.assistencia.internal.cliente.entity.ClienteEntity;
import br.edu.uniesp.assistencia.internal.cliente.repository.ClienteRepository;
import br.edu.uniesp.assistencia.internal.cliente.service.ClienteService;
import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import br.edu.uniesp.assistencia.internal.equipamento.repository.EquipamentoRepository;
import br.edu.uniesp.assistencia.internal.equipamento.service.EquipamentoService;
import br.edu.uniesp.assistencia.internal.ordemservico.dto.CriarOrdemServicoRequest;
import br.edu.uniesp.assistencia.internal.ordemservico.dto.CriarOrdemServicoResponse;
import br.edu.uniesp.assistencia.internal.ordemservico.entity.OrdemServicoEntity;
import br.edu.uniesp.assistencia.internal.ordemservico.mapper.OrdemServicoMapper;
import br.edu.uniesp.assistencia.internal.ordemservico.repository.OrdemServicoRepository;
import br.edu.uniesp.assistencia.shared.exception.RecursoNaoEncontradoException;
import br.edu.uniesp.assistencia.shared.exception.RegraNegocioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteService clienteService;
    private final EquipamentoService equipamentoService;

    @Transactional
    public CriarOrdemServicoResponse abrirOrdemServico(
            CriarOrdemServicoRequest request) {
        ClienteEntity cliente = clienteService
                .buscarEntidadePorId(request.clienteId());
        isAtivo(cliente);

        EquipamentoEntity equipamento = equipamentoService
                .buscarEntidadePorId(request.equipamentoId());
        pertenceCliente(equipamento, cliente);

        String numeroProtocolo = gerarNumeroProtocolo();
        OrdemServicoEntity ordemServico = OrdemServicoMapper
                .paraEntidade(request,numeroProtocolo, cliente, equipamento);
        ordemServicoRepository.save(ordemServico);
        return OrdemServicoMapper.paraResposta(ordemServico);
        }

    private static void pertenceCliente(EquipamentoEntity equipamento, ClienteEntity cliente) {
        if(!equipamento.getCliente().getId().equals(cliente.getId())){
            throw new RegraNegocioException(
                    "O equipamento informado não pertence ao cliente indicado.");
        }
    }

    private static void isAtivo(ClienteEntity cliente) {
        if(!cliente.isAtivo()){
            throw new RegraNegocioException
                    ("Não é permitido abrir Ordem de Serviço para um cliente inativo.");
        }
    }

    private String gerarNumeroProtocolo() {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sufixo = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "OS-" + data + "-" + sufixo;
    }
}
