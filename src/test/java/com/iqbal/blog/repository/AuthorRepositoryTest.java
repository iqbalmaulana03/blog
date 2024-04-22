package com.iqbal.blog.repository;

import com.iqbal.blog.entity.Author;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthorRepositoryTest {

    private final AuthorRepository repository;

    @Autowired
    AuthorRepositoryTest(AuthorRepository repository) {
        this.repository = repository;
    }

    @Test
    void AuthorRepository_SaveAll_ReturnAuthor() {

        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();

        Author save = repository.save(user);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isGreaterThan("");
    }

    @Test
    void AuthorRepository_FindById_ReturnAuthor() {
        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        repository.save(user);

        Author getUser = repository.findById(user.getId()).orElseThrow();

        Assertions.assertThat(getUser).isNotNull();
    }


    @Test
    void findByEmail() {
        Author user = Author.builder()
                .email("test@gmail.com")
                .password("test")
                .name("testing")
                .build();
        repository.save(user);

        Author getUser = repository.findByEmail(user.getEmail()).orElseThrow();

        Assertions.assertThat(getUser).isNotNull();
    }
}