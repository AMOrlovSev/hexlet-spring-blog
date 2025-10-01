package io.hexlet.spring.util;

import io.hexlet.spring.model.Page;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Data {
    private static final int ITEMS_COUNT = 5;

    public static List<Page> getPages() {
        Faker faker = new Faker();

        List<String> slugs = IntStream
                .range(1, ITEMS_COUNT + 1)
                .mapToObj(i -> "slug-" + i)  // Преобразуем числа в строки
                .toList();

        List<Page> pages = new ArrayList<>();

        for (int i = 0; i < ITEMS_COUNT; i++) {
            var slug = slugs.get(i);
            var name = faker.gameOfThrones().house();
            var body = faker.gameOfThrones().quote();
            var page = new Page(slug, name, body);
            pages.add(page);
        }

        return pages;
    }
}
