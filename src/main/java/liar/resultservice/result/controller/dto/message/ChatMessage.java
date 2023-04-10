package liar.resultservice.result.controller.dto.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @NotNull
    private String userId;

    @NotNull
    private String charMessage;
}
