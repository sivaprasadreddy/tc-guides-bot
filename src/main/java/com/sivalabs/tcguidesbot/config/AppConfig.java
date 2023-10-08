package com.sivalabs.tcguidesbot.config;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.UrlDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class AppConfig {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    private static final List<String> guideUrls = List.of(
        "https://testcontainers.com/guides/replace-h2-with-real-database-for-testing/",
        "https://testcontainers.com/guides/configuration-of-services-running-in-container/",
        "https://testcontainers.com/guides/testing-spring-boot-kafka-listener-using-testcontainers/",
        "https://testcontainers.com/guides/getting-started-with-testcontainers-for-java/",
        "https://testcontainers.com/guides/testing-rest-api-integrations-in-micronaut-apps-using-wiremock/",
        "https://testcontainers.com/guides/working-with-jooq-flyway-using-testcontainers/"
    );

    private final ApplicationProperties properties;

    public AppConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ConversationalRetrievalChain chain() {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        log.info("Ingesting documents...");
        List<Document> documents = guideUrls.stream().map(UrlDocumentLoader::load).toList();
        ingestor.ingest(documents);
        log.info("Ingested {} documents", documents.size());

        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(OpenAiChatModel.builder()
                        .apiKey(properties.apiKey())
                        .timeout(Duration.ofSeconds(30))
                        .build()
                )
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                // .chatMemory() // you can override default chat memory
                // .promptTemplate() // you can override default prompt template
                .build();
    }
}
