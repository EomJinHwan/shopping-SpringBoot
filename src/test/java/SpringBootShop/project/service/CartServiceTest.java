package SpringBootShop.project.service;

import SpringBootShop.project.domain.Cart;
import SpringBootShop.project.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Test
    void addCart() {
        // given
        Product product = productService.addProduct(new Product("사과", 1000L, 10L));

        // when
        Cart cart = cartService.addCart("user1", product.getId(), 2L);

        // then
        assertThat(cartService.findAllCart("user1")).contains(cart);
    }

    @Test
    void 같은상품_수량합산() {
        // given
        Product product = productService.addProduct(new Product("사과", 1000L, 10L));

        // when
        cartService.addCart("user1", product.getId(), 2L);
        cartService.addCart("user1", product.getId(), 4L);

        // then
        List<Cart> result = cartService.findAllCart("user1");
        assertThat(result).hasSize(1);
    }

    @Test
    void deleteCart() {
        // given
        Product product = productService.addProduct(new Product("사과", 1000L, 10L));
        cartService.addCart("user1", product.getId(), 2L);

        // when
        cartService.deleteCart("user1", product.getId());

        // then
        assertThat(cartService.findAllCart("user1")).isEmpty();
    }

    @Test
    void deleteAllCart() {
        // given
        Product p1 = productService.addProduct(new Product("사과", 1000L, 10L));
        Product p2 = productService.addProduct(new Product("바나나", 2000L, 20L));
        cartService.addCart("user1", p1.getId(), 1L);
        cartService.addCart("user1", p2.getId(), 2L);

        // when
        cartService.deleteAllCart("user1");

        // then
        assertThat(cartService.findAllCart("user1")).isEmpty();
    }
}