package io.spring.workshop.superheroes.villain;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/villains")
@Tag(name = "villains")
public class VillainResource {
    private final VillainService villainService;

    public VillainResource(VillainService villainService) {
        this.villainService = villainService;
    }


    @GetMapping(path = {"/hello"}, produces = {"text/plain"})
    public String hello() {
        return "Hello from villain";
    }

    @Operation(summary = "Returns a random villain")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Villain.class)))
    @GetMapping("/random")
    public ResponseEntity<Villain> getRandomVillain() {
        return ResponseEntity.ok(villainService.findRandom());
    }

    @Operation(summary = "Returns all the villains from the database")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Villain.class, type = "Array")))
    @ApiResponse(responseCode = "204", description = "No villains")
    @GetMapping
    public ResponseEntity<List<Villain>> getAllVillains() {
        List<Villain> allVillains = villainService.findAllVillains();
        if (CollectionUtils.isEmpty(allVillains)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allVillains);
    }

    @Operation(summary = "Returns a villain for a given identifier")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Villain.class)))
    @ApiResponse(responseCode = "204", description = "The villain is not found for a given identifier")
    @GetMapping("/{id}")
    public ResponseEntity<Villain> findVillainById(@PathVariable Long id) {
        Optional<Villain> villainById = villainService.findVillainById(id);
        return villainById.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.noContent().build());

    }

    @Operation(summary = "Creates a valid villain")
    @ApiResponse(responseCode = "201", description = "The URI of the created villain", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = URI.class)))
    @PostMapping
    public ResponseEntity<String> persistVillain(@RequestBody @Valid Villain entity,
                                                 HttpServletRequest httpServletRequest,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        Villain body = villainService.persistVillain(entity);
        String contextPath = httpServletRequest.getContextPath();
        URI build = uriComponentsBuilder.path(contextPath)
                                        .path(httpServletRequest.getServletPath())
                                        .path("/" + body.id.toString())
                                        .build().toUri();
        return ResponseEntity.created(build).build();
    }

    @Operation(summary = "Updates an exiting  villain")
    @ApiResponse(responseCode = "200", description = "The updated villain", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Villain.class)))
    @PutMapping
    public ResponseEntity<Villain> updateVillain(@Valid @RequestBody Villain villain) {
        return ResponseEntity.ok(villainService.updateVillain(villain));
    }

    @Operation(summary = "Deletes an exiting villain")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{id}")
    public ResponseEntity<Villain> deleteVillain(@PathVariable Long id) {
        villainService.deleteVillain(id);
        return ResponseEntity.noContent().build();
    }
}
