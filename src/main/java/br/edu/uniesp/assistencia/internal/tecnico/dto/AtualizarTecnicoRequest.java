package br.edu.uniesp.assistencia.internal.tecnico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarTecnicoRequest(

        @NotBlank(message = "O nome do técnico é obrigatório")
        @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres")
        String nome,

        @Size(max = 20, message = "O telefone deve possuir no máximo 20 caracteres")
        String telefone,

        @Size(max = 100, message = "A especialidade deve possuir no máximo 100 caracteres")
        String especialidade
) {
}
