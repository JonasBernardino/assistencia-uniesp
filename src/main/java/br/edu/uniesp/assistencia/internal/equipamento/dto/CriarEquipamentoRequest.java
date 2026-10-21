package br.edu.uniesp.assistencia.internal.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarEquipamentoRequest(

        @NotBlank(message = "O tipo do equipamento é obrigatório")
        @Size(max = 50, message = "O tipo deve possuir no máximo 50 caracteres")
        String tipo,

        @NotBlank(message = "A marca do equipamento é obrigatória")
        @Size(max = 100, message = "A marca deve possuir no máximo 100 caracteres")
        String marca,

        @NotBlank(message = "O modelo do equipamento é obrigatório")
        @Size(max = 100, message = "O modelo deve possuir no máximo 100 caracteres")
        String modelo,

        @Size(max = 100, message = "O número de série deve possuir no máximo 100 caracteres")
        String numeroSerie,

        @NotNull(message = "O ID do cliente proprietário é obrigatório")
        Long clienteId
) {
}
