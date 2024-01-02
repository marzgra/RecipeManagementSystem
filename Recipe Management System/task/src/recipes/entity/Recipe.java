package recipes.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @OneToMany(cascade = CascadeType.ALL)
    List<Ingredient> ingredients;

    @OneToMany(cascade = CascadeType.ALL)
    List<Direction> directions;

    @NotBlank
    String category;

    @NotNull
    LocalDateTime date;

    @NotNull
    String authorEmail;

    public Recipe(String name,
                  String description,
                  List<Ingredient> ingredients,
                  List<Direction> directions,
                  String category,
                  String authorEmail) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.category = category;
        this.date = LocalDateTime.now();
        this.authorEmail = authorEmail;
    }
}
