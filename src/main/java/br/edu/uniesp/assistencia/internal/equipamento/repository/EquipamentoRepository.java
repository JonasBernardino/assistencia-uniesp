package br.edu.uniesp.assistencia.internal.equipamento.repository;

import br.edu.uniesp.assistencia.internal.equipamento.entity.EquipamentoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<EquipamentoEntity, Long> {

    List<EquipamentoEntity> findByClienteId(Long clienteId);

    Page<EquipamentoEntity> findByClienteId(Long clienteId, Pageable pageable);
}
