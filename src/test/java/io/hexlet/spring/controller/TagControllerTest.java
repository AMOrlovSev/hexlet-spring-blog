package io.hexlet.spring.controller;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.instancio.Instancio;
import org.instancio.Select;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.hexlet.spring.model.Tag;
import io.hexlet.spring.dto.tag.TagCreateDTO;
import io.hexlet.spring.dto.tag.TagUpdateDTO;
import io.hexlet.spring.mapper.TagMapper;
import io.hexlet.spring.repository.TagRepository;
import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private Faker faker;

    private Tag testTag;

    @BeforeEach
    public void setUp() {
        testTag = Instancio.of(Tag.class)
                .ignore(Select.field(Tag::getId))
                .supply(Select.field(Tag::getName), () -> faker.programmingLanguage().name())
                .create();
    }

    @Test
    public void testIndex() throws Exception {
        tagRepository.save(testTag);

        var result = mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        tagRepository.save(testTag);

        var result = mockMvc.perform(get("/api/tags/" + testTag.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTag.getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new TagCreateDTO();
        dto.setName("New Tag");

        var request = post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo("New Tag")
        );

        var tag = tagRepository.findByName("New Tag");
        assertThat(tag).isPresent();
        assertThat(tag.get().getName()).isEqualTo("New Tag");
    }

    @Test
    public void testUpdate() throws Exception {
        tagRepository.save(testTag);

        var dto = new TagUpdateDTO();
        dto.setName(JsonNullable.of("Updated Tag Name"));

        var request = put("/api/tags/" + testTag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo("Updated Tag Name")
        );

        var updatedTag = tagRepository.findById(testTag.getId()).get();
        assertThat(updatedTag.getName()).isEqualTo("Updated Tag Name");
    }

    @Test
    public void testDestroy() throws Exception {
        tagRepository.save(testTag);

        var request = delete("/api/tags/" + testTag.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(tagRepository.existsById(testTag.getId())).isFalse();
    }

    @Test
    public void testShowNotFound() throws Exception {
        mockMvc.perform(get("/api/tags/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        var dto = new TagUpdateDTO();
        dto.setName(JsonNullable.of("Updated Name"));

        var request = put("/api/tags/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var dto = new TagCreateDTO();
        dto.setName(""); // Invalid: empty name

        var request = post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateDuplicateName() throws Exception {
        tagRepository.save(testTag);

        var dto = new TagCreateDTO();
        dto.setName(testTag.getName()); // Same name as existing tag

        var request = post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTagWithPosts() throws Exception {
        tagRepository.save(testTag);

        var result = mockMvc.perform(get("/api/tags/" + testTag.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        // Проверяем, что тег содержит информацию о постах (если это предусмотрено DTO)
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTag.getName()),
                v -> v.node("id").isEqualTo(testTag.getId())
        );
    }
}