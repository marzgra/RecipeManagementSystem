package recipes.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecipeDto {
    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    @Size(min = 1)
    List<String> ingredients;

    @NotNull
    @Size(min = 1)
    List<String> directions;

    @NotBlank
    String category;

    LocalDateTime date;

}
