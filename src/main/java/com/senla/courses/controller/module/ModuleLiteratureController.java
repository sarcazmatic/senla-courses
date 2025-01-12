package com.senla.courses.controller.module;

import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.service.literature.LiteratureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ModuleLiteratureController {

    private final LiteratureService literatureService;

    @PostMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('teacher:write')")
    public Long addLit(@RequestBody @Valid LiteratureDTO literatureDTO, @PathVariable(name = "moduleId") Long moduleId) {
        log.info("Получен запрос на добавление литературы в модуль с id: {}", moduleId);
        Long id = literatureService.add(literatureDTO, moduleId);
        log.info("Литература успешно добавлена в модуль с id: {}, id литературы: {}", moduleId, id);
        return id;
    }

    @PutMapping("/{litId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public LiteratureDTO editLit(@RequestBody @Valid LiteratureDTO literatureDTO,
                                 @PathVariable Long litId) {
        log.info("Получен запрос на редактирование литературы с id: {}", litId);
        LiteratureDTO literatureDTOUpd = literatureService.edit(literatureDTO, litId);
        log.info("Литература с id: {} успешно отредактирована", litId);
        return literatureDTOUpd;
    }

    @GetMapping("/{litId}")
    @ResponseStatus(HttpStatus.OK)
    public LiteratureDTO findLitById(@PathVariable Long litId) {
        log.info("Получен запрос на поиск литературы по id: {}", litId);
        LiteratureDTO literatureDTO = literatureService.findById(litId);
        log.info("Литература с id: {} успешно найдена", litId);
        return literatureDTO;
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByText(@RequestParam(required = false, name = "text") String text,
                                             @RequestParam(required = false, defaultValue = "1") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на поиск литературы по названию, текст: '{}', страница: {}, размер: {}", text, from, size);
        List<LiteratureDTO> literatureDTOS = literatureService.findByText(text, from, size);
        log.info("Найдено {} записей литературы по названию", literatureDTOS.size());
        return literatureDTOS;
    }

    @GetMapping("/author")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByAuthor(@RequestParam(required = false, name = "text") String text,
                                               @RequestParam(required = false, defaultValue = "1") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на поиск литературы по автору, текст: '{}', страница: {}, размер: {}", text, from, size);
        List<LiteratureDTO> literatureList = literatureService.findByAuthor(text, from, size);
        log.info("Найдено {} записей литературы по автору", literatureList.size());
        return literatureList;
    }

    @DeleteMapping("/{litId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public void deleteLit(@PathVariable Long litId) {
        log.info("Получен запрос на удаление литературы с id: {}", litId);
        literatureService.delete(litId);
        log.info("Литература с id: {} успешно удалена", litId);
    }

}
