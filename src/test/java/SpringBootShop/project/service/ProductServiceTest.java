package SpringBootShop.project.service;

import SpringBootShop.project.controller.ProductForm;
import SpringBootShop.project.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService service;

    @Test
    void addProduct() {
        // given
        Product product = new Product("사과", 1000L, 10L);

        // when
        service.addProduct(product);

        // then
        assertThat(service.findById(product.getId())).isEqualTo(product);
    }

    @Test
    void findAll() {
        // given
        Product product1 = service.addProduct(new Product("사과", 1000L, 10L));
        Product product2 = service.addProduct(new Product("바나나", 2000L, 20L));

        // when
        List<Product> result = service.findAll();

        // then
        assertThat(result).contains(product1, product2);
    }

    @Test
    void findById() {
        // given
        Product product = service.addProduct(new Product("사과", 1000L, 10L));

        // when
        Product result = service.findById(product.getId());

        // then
        assertThat(result).isEqualTo(product);
    }

    @Test
    void findById_Fail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.findById(999L));
        assertThat(e.getMessage()).isEqualTo("조건에 맞는 상품이 없습니다");
    }

    @Test
    void findByName() {
        // given
        Product product1 = service.addProduct(new Product("사과", 1000L, 10L));
        service.addProduct(new Product("바나나", 2000L, 20L));

        // when
        Product result = service.findByName(product1.getProductName());

        // then
        assertThat(result).isEqualTo(product1);
    }

    @Test
    void findByName_Fail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.findByName("포도"));
        assertThat(e.getMessage()).isEqualTo("조건에 맞는 상품이 없습니다");
    }

    @Test
    void deleteById() {
        // given
        Product product = service.addProduct(new Product("사과", 1000L, 10L));

        // when
        service.deleteById(product.getId());

        // then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.findById(product.getId()));
        assertThat(e.getMessage()).isEqualTo("조건에 맞는 상품이 없습니다");
    }

    @Test
    void updateName() {
        // given
        Product product = service.addProduct(new Product("사과", 1000L, 10L));
        ProductForm form = new ProductForm();
        form.setProductName("바나나");

        // when
        service.update(product.getId(), form);

        // then
        assertThat(service.findById(product.getId()).getProductName()).isEqualTo("바나나");
    }

    @Test
    void updatePrice() {
        // given
        Product product = service.addProduct(new Product("사과", 1000L, 10L));
        ProductForm form = new ProductForm();
        form.setPrice(2000L);

        // when
        service.update(product.getId(), form);

        // then
        assertThat(service.findById(product.getId()).getPrice()).isEqualTo(2000L);
    }

    @Test
    void updateStock() {
        // given
        Product product = service.addProduct(new Product("사과", 1000L, 10L));
        ProductForm form = new ProductForm();
        form.setStock(20L);

        // when
        service.update(product.getId(), form);

        // then
        assertThat(service.findById(product.getId()).getStock()).isEqualTo(20L);
    }
}