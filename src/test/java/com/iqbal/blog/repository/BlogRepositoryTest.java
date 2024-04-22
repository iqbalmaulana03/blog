package com.iqbal.blog.repository;

import com.iqbal.blog.entity.Author;
import com.iqbal.blog.entity.Blog;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BlogRepositoryTest {

    private final BlogRepository repository;

    private final AuthorRepository authorRepository;

    @Autowired
    BlogRepositoryTest(BlogRepository repository, AuthorRepository authorRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
    }

    @Test
    void BlogRepository_SaveAll_ReturnBlog() {

        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();

        Blog blog = Blog.builder()
                .title("testing")
                .body("testing")
                .author(user)
                .build();

        Blog save = repository.save(blog);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isGreaterThan("");
    }

    @Test
    void BlogRepository_GetAll_ReturnMoreOneThanBlog() {
        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();

        authorRepository.save(user);

        Blog blog = Blog.builder()
                .title("testing")
                .body("testing")
                .author(user)
                .build();

        Blog blog2 = Blog.builder()
                .title("testing2")
                .body("testing2")
                .author(user)
                .build();

        repository.save(blog);
        repository.save(blog2);

        List<Blog> all = repository.findAll();

        Assertions.assertThat(all).hasSize(2).isNotNull();
    }

    @Test
    void blogRepository_FindById_ReturnBlog() {

        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();

        Blog blog = Blog.builder()
                .title("testing")
                .body("testing")
                .author(user)
                .build();

        repository.save(blog);

        Blog getBlog = repository.findById(blog.getId()).orElseThrow();

        Assertions.assertThat(getBlog).isNotNull();
    }

    @Test
    void BlogRepository_Update_ReturnBlog() {
        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();

        Blog blog = Blog.builder()
                .title("testing")
                .body("testing")
                .author(user)
                .build();

        repository.save(blog);

        Blog blogSaved = repository.findById(blog.getId()).orElseThrow();
        blogSaved.setTitle("TESTING");
        blogSaved.setBody("TEST");

        Blog updated = repository.save(blog);

        Assertions.assertThat(updated.getTitle()).isNotNull();
        Assertions.assertThat(updated.getBody()).isNotNull();
    }

    @Test
    void BlogRepository_Delete_ReturnBlog() {
        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();

        Blog blog = Blog.builder()
                .title("testing")
                .body("testing")
                .author(user)
                .build();

        repository.save(blog);

        repository.deleteById(blog.getId());
        Optional<Blog> byId = repository.findById(blog.getId());

        Assertions.assertThat(byId).isEmpty();
    }

    @Test
    void findByTitle() {

        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();

        authorRepository.save(user);

        Blog blog = Blog.builder()
                .title("testing")
                .body("testing")
                .author(user)
                .build();

        repository.save(blog);

        Blog getBlog = repository.findByTitle(blog.getTitle()).orElseThrow();

        Assertions.assertThat(getBlog).isNotNull();
    }
}