package com.senla.courses.controller.module;

import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.service.literature.LiteratureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/module/lit")
@AllArgsConstructor
public class ModuleLiteratureController {

    private final LiteratureService literatureService;


    @PostMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('teacher:write')")
    public Long addLit(@RequestBody @Valid LiteratureDTO literatureDTO, @PathVariable(name = "moduleId") Long moduleId) {
        return literatureService.add(literatureDTO, moduleId);
    }

    @PutMapping("/{litId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public LiteratureDTO editLit(@RequestBody @Valid LiteratureDTO literatureDTO,
                                 @PathVariable Long litId) {
        return literatureService.edit(literatureDTO, litId);
    }

    @GetMapping("/{litId}")
    @ResponseStatus(HttpStatus.OK)
    public LiteratureDTO findLitById(@PathVariable Long litId) {
        return literatureService.findById(litId);
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByText(@RequestParam(required = false, name = "text") String text,
                                             @RequestParam(required = false, defaultValue = "1") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return literatureService.findByText(text, from, size);
    }

    @GetMapping("/author")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByAuthor(@RequestParam(required = false, name = "text") String text,
                                               @RequestParam(required = false, defaultValue = "1") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return literatureService.findByAuthor(text, from, size);
    }

    @DeleteMapping("/{litId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public void deleteLit(@PathVariable Long litId) {
        literatureService.delete(litId);
    }

}
