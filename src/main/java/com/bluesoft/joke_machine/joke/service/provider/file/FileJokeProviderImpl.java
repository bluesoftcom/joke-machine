package com.bluesoft.joke_machine.joke.service.provider.file;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.provider.randomness.RandomnessProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Service
public class FileJokeProviderImpl implements FileJokeProvider {

    private final Logger log = LoggerFactory.getLogger(FileJokeProviderImpl.class);

    private ResourceLoader resourceLoader;
    private RandomnessProvider randomnessProvider;

    @Autowired
    public FileJokeProviderImpl(final ResourceLoader resourceLoader,
                                final RandomnessProvider randomnessProvider) {
        this.resourceLoader = resourceLoader;
        this.randomnessProvider = randomnessProvider;
    }

    /**
     * JAVA 16  FEATURE - toList
     * JAVA 9 FEATURE - Stream.ofNullable()
     */
    @Override
    public Optional<Joke> getJokeFromCategory(final Category category) {

        try {
            final File categoryDir = resourceLoader
                    .getResource(String.format("classpath:/jokes/%s", category.getValue().toLowerCase()))
                    .getFile();
            if (categoryDir.isDirectory()) {
                final List<File> jokesFromCategory = Arrays
                        .stream(Optional.ofNullable(categoryDir.listFiles())
                                .orElseThrow(() -> new FileJokeProviderException("Cannot access jokes files")))
                        .toList();

                final String jokeFilePath = jokesFromCategory.get(randomnessProvider.getRandomInt(jokesFromCategory.size()))
                        .getPath();

                final String jokeContent = Files.readString(Path.of(jokeFilePath));

                return Optional.of(new Joke(jokeContent, category));
            }
        } catch (IOException | FileJokeProviderException e) {
            log.error("Cannot find or access joke file");
        }
        return Optional.empty();
    }

    /**
     * JAVA 9 FEATURE - Stream.ofNullable()
     */
    @Override
    public Optional<Set<Category>> getAvailableCategories() {
        try {
            final File jokesDir = resourceLoader.getResource("classpath:/jokes").getFile();
            if (jokesDir.isDirectory()) {
                final File[] categoriesDirs = jokesDir.listFiles();
                return Optional.of(Stream.ofNullable(categoriesDirs)
                        .flatMap(e -> Arrays.stream(e.clone()))
                        .map(File::getName)
                        .map(String::toLowerCase)
                        .map(StringUtils::capitalize)
                        .map(categoryName -> new Category(categoryName, this.getProviderName()))
                        .collect(Collectors.toSet()));
            } else {
                log.error("directory with jokes cannot be found");
            }
        } catch (IOException e) {
            log.error("{} cannot access or read joke files", this.getProviderName());
        }
        return Optional.empty();
    }
}
