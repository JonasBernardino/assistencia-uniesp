package br.edu.uniesp.assistencia.internal.ordemservico.entity;

import br.edu.uniesp.assistencia.internal.cliente.entity.ClienteEntity;
import br.edu.uniesp.assistencia.internal.entity.AnexoEntity;
import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import br.edu.uniesp.assistencia.internal.ocamento.entity.OrcamentoEntity;
import br.edu.uniesp.assistencia.internal.tecnico.entity.TecnicoEntity;
import br.edu.uniesp.assistencia.shared.exception.RegraNegocioException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "ordens_servico")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_protocolo", nullable = false, unique = true, length = 30)
    private String numeroProtocolo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private EquipamentoEntity equipamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private TecnicoEntity tecnico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status = StatusOrdemServico.RECEBIDA;

    @Column(name = "descricao_problema", nullable = false, columnDefinition = "TEXT")
    private String descricaoProblema;

    @Column(name = "data_abertura", nullable = false, updatable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_finalizacao")
    private LocalDateTime dataFinalizacao;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @OneToOne(mappedBy = "ordemServico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DiagnosticoEntity diagnostico;

    @OneToOne(mappedBy = "ordemServico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrcamentoEntity orcamento;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoStatusEntity> historico = new ArrayList<>();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoPecaEntity> pecasUtilizadas = new ArrayList<>();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnexoEntity> anexos = new ArrayList<>();

    public OrdemServicoEntity(String numeroProtocolo, ClienteEntity cliente, EquipamentoEntity equipamento, String descricaoProblema) {
        if (!cliente.isAtivo()) {
            throw new RegraNegocioException("Não é permitido abrir Ordem de Serviço para um cliente inativo.");
        }
        this.numeroProtocolo = numeroProtocolo;
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.descricaoProblema = descricaoProblema;
        this.status = StatusOrdemServico.RECEBIDA;
        this.dataAbertura = LocalDateTime.now();
        adicionarHistorico(null, StatusOrdemServico.RECEBIDA, "SISTEMA", "Abertura da ordem de serviço.");
    }


    public void atribuirTecnico(TecnicoEntity tecnico, String responsavel) {
        if (tecnico == null || !tecnico.isAtivo()) {
            throw new RegraNegocioException("Somente um técnico ativo pode ser atribuído à Ordem de Serviço.");
        }
        this.tecnico = tecnico;
        if (this.status == StatusOrdemServico.RECEBIDA) {
            alterarStatus(StatusOrdemServico.EM_DIAGNOSTICO, responsavel, "Técnico " + tecnico.getNome() + " atribuído à OS.");
        }
    }

    public void registrarDiagnostico(DiagnosticoEntity diagnostico, String responsavel) {
        if (this.status != StatusOrdemServico.EM_DIAGNOSTICO) {
            throw new RegraNegocioException("Diagnóstico só pode ser registrado quando a OS estiver em diagnóstico.");
        }
        this.diagnostico = diagnostico;
        adicionarHistorico(this.status, this.status, responsavel, "Diagnóstico técnico registrado.");
    }

    public void submeterOrcamento(OrcamentoEntity orcamento, String responsavel) {
        if (this.diagnostico == null) {
            throw new RegraNegocioException("A OS não pode avançar para AGUARDANDO_APROVACAO sem diagnóstico registrado.");
        }
        this.orcamento = orcamento;
        alterarStatus(StatusOrdemServico.AGUARDANDO_APROVACAO, responsavel, "Orçamento gerado e aguardando aprovação do cliente.");
    }

    public void aprovarOrcamento(String responsavel) {
        if (this.status != StatusOrdemServico.AGUARDANDO_APROVACAO) {
            throw new RegraNegocioException("Somente ordens aguardando aprovação podem ter o orçamento aprovado.");
        }
        alterarStatus(StatusOrdemServico.APROVADA, responsavel, "Orçamento aprovado pelo cliente.");
    }

    public void rejeitarOrcamento(String responsavel, String motivo) {
        if (this.status != StatusOrdemServico.AGUARDANDO_APROVACAO) {
            throw new RegraNegocioException("Somente ordens aguardando aprovação podem ter o orçamento rejeitado.");
        }
        alterarStatus(StatusOrdemServico.REJEITADA, responsavel, "Orçamento rejeitado: " + motivo);
    }

    public void iniciarManutencao(String responsavel) {
        if (this.status != StatusOrdemServico.APROVADA && this.status != StatusOrdemServico.AGUARDANDO_PECA) {
            throw new RegraNegocioException("A manutenção só pode ser iniciada se o orçamento estiver aprovado ou após espera de peças.");
        }
        alterarStatus(StatusOrdemServico.EM_MANUTENCAO, responsavel, "Início dos serviços de manutenção.");
    }

    public void aguardarPeca(String responsavel, String detalhe) {
        if (this.status != StatusOrdemServico.EM_MANUTENCAO) {
            throw new RegraNegocioException("Apenas ordens em manutenção podem transitar para aguardo de peças.");
        }
        alterarStatus(StatusOrdemServico.AGUARDANDO_PECA, responsavel, "Aguardando reposição de peças: " + detalhe);
    }

    public void concluirManutencao(String responsavel) {
        if (this.status != StatusOrdemServico.EM_MANUTENCAO) {
            throw new RegraNegocioException("Uma OS só pode ser concluída quando estiver em manutenção.");
        }
        this.dataFinalizacao = LocalDateTime.now();
        alterarStatus(StatusOrdemServico.CONCLUIDA, responsavel, "Manutenção técnica concluída com sucesso.");
    }

    public void entregarEquipamento(String responsavel) {
        if (this.status != StatusOrdemServico.CONCLUIDA) {
            throw new RegraNegocioException("Uma OS só pode ser marcada como ENTREGUE se estiver concluída.");
        }
        this.dataEntrega = LocalDateTime.now();
        alterarStatus(StatusOrdemServico.ENTREGUE, responsavel, "Equipamento entregue ao cliente.");
    }

    public void cancelar(String responsavel, String motivo) {
        if (this.status == StatusOrdemServico.CONCLUIDA || this.status == StatusOrdemServico.ENTREGUE) {
            throw new RegraNegocioException("Não é permitido cancelar uma OS já concluída ou entregue.");
        }
        alterarStatus(StatusOrdemServico.CANCELADA, responsavel, "OS cancelada: " + motivo);
    }

    public void adicionarPecaUtilizada(OrdemServicoPecaEntity pecaUtilizada) {
        if (this.status != StatusOrdemServico.EM_MANUTENCAO) {
            throw new RegraNegocioException("Peças só podem ser registradas com a OS em manutenção.");
        }
        this.pecasUtilizadas.add(pecaUtilizada);
    }

    public void adicionarAnexo(AnexoEntity anexo) {
        this.anexos.add(anexo);
    }

    private void alterarStatus(StatusOrdemServico novoStatus, String responsavel, String observacao) {
        StatusOrdemServico anterior = this.status;
        this.status = novoStatus;
        adicionarHistorico(anterior, novoStatus, responsavel, observacao);
    }

    private void adicionarHistorico(StatusOrdemServico anterior, StatusOrdemServico novo, String responsavel, String observacao) {
        HistoricoStatusEntity entrada = new HistoricoStatusEntity(this, anterior, novo, responsavel, observacao);
        this.historico.add(entrada);
    }
}
