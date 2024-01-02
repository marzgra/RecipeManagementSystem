package recipes.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.dto.Id;
import recipes.dto.RecipeDto;
import recipes.entity.Direction;
import recipes.entity.Ingredient;
import recipes.entity.Recipe;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository repository;

    public Id save(RecipeDto recipeDto) {
        List<Direction> dirs = recipeDto.getDirections().stream().map(Direction::new).collect(toList());

        List<Ingredient> ings = recipeDto.getIngredients().stream().map(Ingredient::new).collect(toList());

        Recipe recipe = new Recipe(recipeDto.getName(), recipeDto.getDescription(), ings, dirs,
                recipeDto.getCategory(), SecurityContextHolder.getContext().getAuthentication().getName());

        return new Id(repository.saveAndFlush(recipe).getId());
    }

    public Optional<RecipeDto> getRecipe(int id) {
        Optional<Recipe> recipeOpt = repository.findById(id);
        if (recipeOpt.isPresent()) {
            Recipe recipe = recipeOpt.get();
            return Optional.of(mapToDto(recipe));
        }
        return Optional.empty();
    }

    public boolean deleteById(int id) throws Exception {
        Optional<Recipe> opt = repository.findById(id);
        if (opt.isPresent() && canUpdateOrDeleteRecipe(opt.get())) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateRecipe(String id, RecipeDto recipeDto) throws Exception {
        Optional<Recipe> opt = repository.findById(Integer.valueOf(id));
        if (opt.isPresent() && canUpdateOrDeleteRecipe(opt.get())) {
            Recipe recipe = opt.get();
            List<Direction> dirs = recipeDto.getDirections().stream().map(Direction::new).collect(toList());

            List<Ingredient> ings = recipeDto.getIngredients().stream().map(Ingredient::new).collect(toList());

            recipe.setName(recipeDto.getName());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setDirections(dirs);
            recipe.setIngredients(ings);
            recipe.setCategory(recipeDto.getCategory());
            recipe.setDate(LocalDateTime.now());

            repository.save(recipe);
            return true;
        }

        return false;
    }

    public List<RecipeDto> search(String category, String name) {
        if (category != null) {
            return repository.findAllByCategoryIgnoreCaseOrderByDateDesc(category).stream().map(this::mapToDto).collect(toList());
        }
        if (name != null) {
            return repository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name).stream().map(this::mapToDto).collect(toList());
        }
        return emptyList();
    }

    private RecipeDto mapToDto(Recipe recipe) {
        List<String> dirs = recipe.getDirections().stream().map(Direction::getDirection).collect(toList());

        List<String> ings = recipe.getIngredients().stream().map(Ingredient::getIngredient).collect(toList());
        return new RecipeDto(recipe.getName(), recipe.getDescription(), ings, dirs, recipe.getCategory(),
                recipe.getDate());
    }

    private boolean canUpdateOrDeleteRecipe(Recipe recipe) throws Exception {
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        if (recipe.getAuthorEmail().equals(auth)) {
            return true;
        } else {
            throw new Exception("User is not the author");
        }
    }

}
