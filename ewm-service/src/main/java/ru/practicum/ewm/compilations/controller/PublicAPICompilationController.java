package ru.practicum.ewm.compilations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilations.dto.CompilationOutputDto;
import ru.practicum.ewm.compilations.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicAPICompilationController {
    private final CompilationService compilationService;

    /**
     * Получение подборок событий
     *
     * @param pinned - искать только закрепленные/не закрепленные подборки
     * @param from   - количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   - количество элементов в наборе
     */
    @GetMapping()
    public List<CompilationOutputDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     * Получение подборки событий по его id
     *
     * @param compId - id подборки
     */
    @GetMapping("/{compId}")
    public CompilationOutputDto getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}