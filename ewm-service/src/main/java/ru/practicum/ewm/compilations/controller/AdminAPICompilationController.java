package ru.practicum.ewm.compilations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilations.dto.CompilationInputDto;
import ru.practicum.ewm.compilations.dto.CompilationOutputDto;
import ru.practicum.ewm.compilations.service.CompilationService;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminAPICompilationController {
    private final CompilationService compilationService;

    /**
     * Добавление новой подборки
     *
     * @param compilation - данные новой подборки
     */
    @PostMapping()
    public CompilationOutputDto createCompilation(@RequestBody @Valid CompilationInputDto compilation) {
        return compilationService.createCompilation(compilation);
    }

    /**
     * Удаление подборки
     *
     * @param compId - id подборки
     */
    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    /**
     * Удалить событие из подборки
     *
     * @param compId  - id подборки
     * @param eventId - id события
     */
    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventToCompilation(@PathVariable Long compId,
                                         @PathVariable Long eventId) {
        compilationService.deleteEventToCompilation(compId, eventId);
    }

    /**
     * Закрепить подборку на главной странице
     *
     * @param compId - id подборки
     */
    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        compilationService.pinCompilation(compId);
    }

    /**
     * Открепить подборку на главной странице
     *
     * @param compId - id подборки
     */
    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        compilationService.unpinCompilation(compId);
    }
}