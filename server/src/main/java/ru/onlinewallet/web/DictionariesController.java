package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.onlinewallet.dto.account.CurrencyDto;
import ru.onlinewallet.dto.account.TypeDto;
import ru.onlinewallet.entity.account.Type;
import ru.onlinewallet.service.DictionaryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/dictionary")
@RequiredArgsConstructor
public class DictionariesController {

    private final DictionaryService dictionaryService;

    @GetMapping("/currencies")
    private ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        return ResponseEntity.ok(
                this.dictionaryService
                        .getAllCurrencies()
                        .stream()
                        .map(CurrencyDto::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/account-types")
    private ResponseEntity<List<TypeDto>> getAllTypes() {
        List<Type> types = dictionaryService.getAllTypes();
        return ResponseEntity.ok(types.stream().map(TypeDto::toDto).collect(Collectors.toList()));
    }
}
