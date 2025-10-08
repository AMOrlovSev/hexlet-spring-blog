package io.hexlet.spring.controller;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.instancio.Instancio;
import org.instancio.Model;
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

import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.dto.post.PostCreateDTO;
import io.hexlet.spring.dto.post.PostUpdateDTO;
import io.hexlet.spring.dto.post.PostPatchDTO;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.repository.TagRepository;
import net.datafaker.Faker;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private Faker faker;

    private User testUser;
    private Post testPost;
    private Tag testTag1;
    private Tag testTag2;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .create();
        userRepository.save(testUser);

        testTag1 = Instancio.of(Tag.class)
                .ignore(Select.field(Tag::getId))
                .supply(Select.field(Tag::getName), () -> faker.programmingLanguage().name())
                .create();

        testTag2 = Instancio.of(Tag.class)
                .ignore(Select.field(Tag::getId))
                .supply(Select.field(Tag::getName), () -> faker.programmingLanguage().name())
                .create();

        tagRepository.saveAll(Set.of(testTag1, testTag2));

        testPost = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::getTitle), () -> faker.lorem().sentence())
                .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                .supply(Select.field(Post::getPublished), () -> true)
                .supply(Select.field(Post::getUser), () -> testUser)
                .supply(Select.field(Post::getTags), () -> Set.of(testTag1))
                .create();
    }

    @Test
    public void testIndex() throws Exception {
        postRepository.save(testPost);

        var result = mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        postRepository.save(testPost);

        var result = mockMvc.perform(get("/api/posts/" + testPost.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testPost.getTitle()),
                v -> v.node("content").isEqualTo(testPost.getContent()),
                v -> v.node("published").isEqualTo(testPost.getPublished()),
                v -> v.node("userId").isEqualTo(testUser.getId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new PostCreateDTO();
        dto.setTitle("Test Post Title");
        dto.setContent("Test Post Content");
        dto.setUserId(testUser.getId());
        dto.setTagIds(Set.of(testTag1.getId(), testTag2.getId()));

        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(dto.getTitle()),
                v -> v.node("content").isEqualTo(dto.getContent())
        );

    }

    @Test
    public void testUpdate() throws Exception {
        postRepository.save(testPost);

        var dto = new PostUpdateDTO();
        dto.setTitle(JsonNullable.of("Updated Title"));
        dto.setContent(JsonNullable.of("Updated Content"));
        dto.setTagIds(JsonNullable.of(Set.of(testTag2.getId())));

        var request = put("/api/posts/" + testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo("Updated Title"),
                v -> v.node("content").isEqualTo("Updated Content")
        );

        var updatedPost = postRepository.findById(testPost.getId()).get();
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getTags()).hasSize(1);
    }

    @Test
    public void testPatch() throws Exception {
        postRepository.save(testPost);

        var dto = new PostPatchDTO();
        dto.setTitle(JsonNullable.of("Patched Title"));
        dto.setPublished(JsonNullable.of(false));

        var request = patch("/api/posts/" + testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo("Patched Title"),
                v -> v.node("published").isEqualTo(false)
        );

        var patchedPost = postRepository.findById(testPost.getId()).get();
        assertThat(patchedPost.getTitle()).isEqualTo("Patched Title");
        assertThat(patchedPost.getPublished()).isFalse();
        assertThat(patchedPost.getContent()).isEqualTo(testPost.getContent());
    }

    @Test
    public void testDestroy() throws Exception {
        postRepository.save(testPost);

        var request = delete("/api/posts/" + testPost.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(postRepository.existsById(testPost.getId())).isFalse();
    }

    @Test
    public void testShowNotFound() throws Exception {
        mockMvc.perform(get("/api/posts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        var dto = new PostUpdateDTO();
        dto.setTitle(JsonNullable.of("Updated Title"));

        var request = put("/api/posts/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var dto = new PostCreateDTO();
        dto.setTitle(""); // Invalid: empty title
        dto.setContent("Short"); // Invalid: too short content
        dto.setUserId(testUser.getId());

        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}