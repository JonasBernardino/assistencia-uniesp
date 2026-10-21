package br.edu.uniesp.assistencia.internal.tecnico.dto;

public record TecnicoResumoResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String especialidade,
        boolean ativo
) {
}
