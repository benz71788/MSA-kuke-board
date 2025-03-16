package kuke.board.article.api;

import kuke.board.article.service.response.ArticlePageResponse;
import kuke.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my content", 1L, 1L
        ));
        System.out.println("create response = " + response);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(158186173866369024L);
        System.out.println("read response = " + response);
    }

    @Test
    void updateTest() {
        ArticleResponse response = update(158186173866369024L, new ArticleUpdateRequest(
                "hi3", "my content 3"
        ));
//        ArticleResponse response = read(158186173866369024L);
        System.out.println("update response = " + response);
    }

    @Test
    void deleteTest() {
        delete(158186173866369024L);
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = readAll(1L, 5000L, 30L);
        for (ArticleResponse article : response.getArticles()) {
            System.out.println("articleId = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleResponse> response = readAllInfiniteScroll(1L, 30L, null);
        for (ArticleResponse article : response) {
            System.out.println("articleId = " + article.getArticleId());
        }

        Long lastArticleId = response.getLast().getArticleId();
        System.out.println("lastArticleId = " + lastArticleId);
        List<ArticleResponse> result = readAllInfiniteScroll(1L, 30L, lastArticleId);
        for (ArticleResponse article : result) {
            System.out.println("articleId2 = " + article.getArticleId());
        }
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    void delete(Long articleId) {
        restClient.delete()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve();
    }

    ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        String uri = "/v1/articles" + "?" + "boardId=" + boardId + "&page=" + page + "&pageSize=" + pageSize;
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(ArticlePageResponse.class);
    }

    List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize, Long lastArticleId) {
        String uri = "/v1/articles/infinite-scroll?" + "boardId=" + boardId + "&pageSize=" + pageSize;
        if (lastArticleId != null) {
            uri = uri + "&lastArticleId=" + lastArticleId;
        }
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }
}
