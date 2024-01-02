package recipes.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.dto.Id;
import recipes.dto.RecipeDto;
import recipes.dto.RegisterDto;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class RecipesController {

    private final RecipeService service;


    @PostMapping("/api/recipe/new")
    public ResponseEntity<Id> saveRecipe(@RequestBody @Valid RecipeDto recipe) {
        return ResponseEntity.ok(service.save(recipe));
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<RecipeDto> getSavedRecipe(@PathVariable("id") int id) {
        return ResponseEntity.of(service.getRecipe(id));
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        try {
            return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Id> updateRecipe(@RequestBody @Valid RecipeDto recipe, @PathVariable("id") String id) {
        try {
            return service.updateRecipe(id, recipe) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/api/recipe/search/")
    public ResponseEntity<?> search(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        List<String> params = Stream.of(category, name)
                .filter(it -> it != null && !it.isBlank())
                .collect(Collectors.toList());

        if(params.size() == 1) {
            return ResponseEntity.ok(service.search(category, name));
        }
        return ResponseEntity.badRequest().build();
    }
}
