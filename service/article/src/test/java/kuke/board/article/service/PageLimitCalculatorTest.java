package kuke.board.article.service;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class PageLimitCalculatorTest {

    @Test
    void calculatePageLimitTest() {
        calculatePageLimit(1L, 30L, 10L, 301L);
        calculatePageLimit(7L, 30L, 10L, 301L);
        calculatePageLimit(10L, 30L, 10L, 301L);
        calculatePageLimit(11L, 30L, 10L, 601L);
        calculatePageLimit( 12L, 30L, 10L, 601L);
    }

    void calculatePageLimit(Long page, Long pageSize, Long movablePageCount, Long expected) {
        Long result = PageLimitCalculator.calculatePageLimit(page, pageSize, movablePageCount);
        assertThat(result).isEqualTo(expected);
    }
}
