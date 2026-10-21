package br.edu.uniesp.assistencia.internal.tecnico.dto;

public record CriarTecnicoResponse(
        Long id,
        String nome,
        String email,
        String especialidade
) {
}
