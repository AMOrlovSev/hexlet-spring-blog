package io.hexlet.spring.controler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    private List<Page> pages = new ArrayList<Page>();

    @GetMapping("/pages") // Список страниц
    public List<Page> index(@RequestParam(defaultValue = "10") Integer limit) {
        return pages.stream().limit(limit).toList();
    }

    @PostMapping("/pages") // Создание страницы
    public Page create(@RequestBody Page page) {
        pages.add(page);
        return page;
    }

    @GetMapping("/pages/{id}") // Вывод страницы
    public Optional<Page> show(@PathVariable String id) {
        var page = pages.stream()
                .filter(p -> p.getSlug().equals(id))
                .findFirst();
        return page;
    }

    @PutMapping("/pages/{id}") // Обновление страницы
    public Page update(@PathVariable String id, @RequestBody Page data) {
        var maybePage = pages.stream()
                .filter(p -> p.getSlug().equals(id))
                .findFirst();
        if (maybePage.isPresent()) {
            var page = maybePage.get();
            page.setSlug(data.getSlug());
            page.setName(data.getName());
            page.setBody(data.getBody());
        }
        return data;
    }

    @DeleteMapping("/pages/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        pages.removeIf(p -> p.getSlug().equals(id));
    }
}