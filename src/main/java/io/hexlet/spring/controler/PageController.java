package io.hexlet.spring.controler;

import java.util.List;

import io.hexlet.spring.util.Data;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hexlet.spring.model.Page;

@SpringBootApplication
@RestController
public class PageController {
    private List<Page> pages = Data.getPages();

    @GetMapping("/pages")
    public ResponseEntity<List<Page>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = pages.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pages.size()))
                .body(result);
    }

    @PostMapping("/pages")
    public ResponseEntity<Page> create(@RequestBody Page page) {
        pages.add(page);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(page);
    }

    @GetMapping("/pages/{id}") // Вывод страницы
    public ResponseEntity<Page> show(@PathVariable String id) {
        var page = pages.stream()
                .filter(p -> p.getSlug().equals(id))
                .findFirst();
        return ResponseEntity.of(page);
    }

    @PutMapping("/pages/{id}")
    public ResponseEntity<Page> update(@PathVariable String id, @RequestBody Page data) {
        var maybePage = pages.stream()
                .filter(p -> p.getSlug().equals(id))
                .findFirst();

        if (maybePage.isPresent()) {
            var page = maybePage.get();
            page.setSlug(data.getSlug());
            page.setName(data.getName());
            page.setBody(data.getBody());
            return ResponseEntity.ok(page); // Возвращаем обновленную страницу
        } else {
            return ResponseEntity.notFound().build(); // 404 если не найдено
        }
    }

    @DeleteMapping("/pages/{id}")
    public ResponseEntity<Void> destroy(@PathVariable String id) {
        boolean removed = pages.removeIf(p -> p.getSlug().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build(); // 204 при успешном удалении
        } else {
            return ResponseEntity.notFound().build(); // 404 если не найдено
        }
    }
}