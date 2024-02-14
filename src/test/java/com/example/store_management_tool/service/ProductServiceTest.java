package com.example.store_management_tool.service;

import com.example.store_management_tool.exception.ProductNotAvailableException;
import com.example.store_management_tool.mapper.ProductMapper;
import com.example.store_management_tool.model.Category;
import com.example.store_management_tool.model.Product;
import com.example.store_management_tool.model.dtos.ProductDto;
import com.example.store_management_tool.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private static final UUID TEST_UUID = java.util.UUID.fromString("db2a7d76-3ff8-4605-bbbb-7ed14156334f");
    private static final UUID NEW_TEST_UUID = java.util.UUID.fromString("a01340a7-0800-43cb-8a30-cb30f7726003");
    private static final Integer NUMBER_OF_PRODUCTS = 15;

    private static final Double PRICE = 9.99;
    private static final Double NEW_PRICE = 19.99;

    @Test
    public void GIVEN_UUID_when_findProduct_then_return_ProductDto(){
        Optional<Product> optionalProduct = Optional.of(buildProduct());
        when(productRepository.findByProductCatalogNumber(TEST_UUID))
                .thenReturn(optionalProduct);
        when(productMapper.toDto(optionalProduct.get())).thenReturn(buildProductDto());

        ProductDto result = productService.findProduct(TEST_UUID);

        assertNotNull(result);
        verify(productMapper).toDto(optionalProduct.get());
        assertEquals(result.getProductCatalogNumber(), optionalProduct.get().getProductCatalogNumber());
        verify(productRepository).findByProductCatalogNumber(TEST_UUID);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void GIVEN_UUID_when_findProduct_then_return_ProductNotAvailableException(){
        Optional<Product> optionalProduct = Optional.of(buildProduct());
        when(productRepository.findByProductCatalogNumber(TEST_UUID))
                .thenThrow(new ProductNotAvailableException("Product with catalog number"));

        ProductNotAvailableException exception =assertThrows(ProductNotAvailableException.class,
                () -> productService.findProduct(TEST_UUID));

        assertEquals(exception.getMessage(), "Product with catalog number");
    }

    @Test
    public void GIVEN_ProductDto_when_addProduct_then_return_Success(){
        ProductDto productDto = buildProductDto();
        when(productMapper.fromDto(productDto, NUMBER_OF_PRODUCTS)).thenReturn(buildProduct());

        productService.addProduct(productDto, NUMBER_OF_PRODUCTS);

        verify(productMapper).fromDto(productDto, NUMBER_OF_PRODUCTS);
        verify(productRepository).save(buildProduct());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void GIVEN_ProductCatalogNumber_AND_NewPrice_when_changePrice_then_return_Success(){
        Optional<Product> optionalProduct = Optional.of(buildProduct());
        Product productAfterPriceChange = optionalProduct.get();
        productAfterPriceChange.setPrice(NEW_PRICE);
        when(productRepository.findByProductCatalogNumber(TEST_UUID))
                .thenReturn(optionalProduct);

        productService.changePrice(TEST_UUID, NEW_PRICE);

        verify(productRepository).save(productAfterPriceChange);
        verify(productRepository).findByProductCatalogNumber(TEST_UUID);
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    public void GIVEN_UUID_when_sellOneProduct_then_return_ProductDto(){
        Optional<Product> optionalProduct = Optional.of(buildProduct());
        when(productRepository.findByProductCatalogNumber(TEST_UUID))
                .thenReturn(optionalProduct);
        when(productMapper.toDto(optionalProduct.get())).thenReturn(buildProductDto());

        ProductDto result = productService.sellOneProduct(TEST_UUID);

        verify(productMapper).toDto(optionalProduct.get());
        assertNotNull(result);
        assertEquals(result.getProductCatalogNumber(), optionalProduct.get().getProductCatalogNumber());
        verify(productRepository).save(any(Product.class));
        verify(productRepository).findByProductCatalogNumber(TEST_UUID);
    }

    @Test
    public void GIVEN_UUID_when_sellOneProduct_then_return_ProductNotAvailableException(){
        Optional<Product> optionalProduct = Optional.of(buildProduct());
        when(productRepository.findByProductCatalogNumber(TEST_UUID))
                .thenThrow(new ProductNotAvailableException("Product with catalog number"));

        ProductNotAvailableException exception =assertThrows(ProductNotAvailableException.class,
                () -> productService.sellOneProduct(TEST_UUID));

        assertEquals(exception.getMessage(), "Product with catalog number");
    }

    @Test
    public void GIVEN_UUID_when_sellOneProduct_ana_product_is_zero_then_return_ProductNotAvailableException(){
        Optional<Product> optionalProduct = Optional.of(buildProduct());
        optionalProduct.get().setNumberOfProducts(0);
        when(productRepository.findByProductCatalogNumber(TEST_UUID))
                .thenReturn(optionalProduct);

        ProductNotAvailableException exception = assertThrows(ProductNotAvailableException.class,
                () -> productService.sellOneProduct(TEST_UUID));

        assertEquals(exception.getMessage(), "Product stock is not enough");
    }

    @Test
    public void GIVEN_UUID_when_sellMoreProducts_then_return_Map_of_ProductDto_and_Integer(){
        List<Product> products = buildProductsList();
        ProductDto firstProductDto = buildProductDto();
        ProductDto seccondProductDto = buildProductDto();
        seccondProductDto.setProductCatalogNumber(NEW_TEST_UUID);
        List<UUID> productCatalogNumbers = Arrays.asList(TEST_UUID, NEW_TEST_UUID);
        when(productRepository.findByProductCatalogNumberIn(productCatalogNumbers))
                .thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(firstProductDto).thenReturn(seccondProductDto);

        Map<ProductDto, Integer> result = productService.sellMoreProducts(buildShoppingListMap());

        assertNotNull(result);
        assertTrue(result.containsKey(firstProductDto));
        assertTrue(result.containsKey(seccondProductDto));
        assertEquals(result.get(firstProductDto), buildShoppingListMap().get(firstProductDto.getProductCatalogNumber()));
        assertEquals(result.get(seccondProductDto), buildShoppingListMap().get(seccondProductDto.getProductCatalogNumber()));
        verify(productRepository).findByProductCatalogNumberIn(productCatalogNumbers);
        verify(productRepository, times(2)).save(any(Product.class));
        verify(productMapper, times(1)).toDto(products.get(0));
        verify(productMapper, times(1)).toDto(products.get(1));
    }

    @Test
    public void GIVEN_ProductCatalogNumber_when_removeProductFromInventory_then_return_Success(){

        productService.removeProductFromInventory(TEST_UUID);

        verify(productRepository).deleteByProductCatalogNumber(TEST_UUID);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void GIVEN_ProductCatalogNumber_when_GetProductsInventory_then_return_Success(){
        List<Product> products = buildProductsList();
        when(productRepository.findAll()).thenReturn(products);
        Map<UUID, Integer> result = productService.getProductsInventory();

        assertNotNull(result);
        assertTrue(result.containsKey(TEST_UUID));
        assertTrue(result.containsKey(NEW_TEST_UUID));
        assertEquals(result.get(products.get(0).getProductCatalogNumber()), products.get(0).getNumberOfProducts());
        assertEquals(result.get(products.get(1).getProductCatalogNumber()), products.get(1).getNumberOfProducts());
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    private Product buildProduct(){
        return Product.builder()
                .category(Category.HOUSEHOLD)
                .numberOfProducts(NUMBER_OF_PRODUCTS)
                .price(PRICE)
                .productCatalogNumber(TEST_UUID)
                .picture("testPictureURL")
                .characteristics("Test characteristics")
                .name("TestName")
                .build();
    }

    private ProductDto buildProductDto(){
        return ProductDto.builder()
                        .category(Category.HOUSEHOLD)
                        .price(PRICE)
                        .productCatalogNumber(TEST_UUID)
                        .picture("testPictureURL")
                        .characteristics("Test characteristics")
                        .name("TestName")
                        .build();
    }

    private Map<UUID, Integer> buildShoppingListMap(){
        Map<UUID, Integer> shoppingList = new HashMap<>();
        shoppingList.put(TEST_UUID, 10);
        shoppingList.put(NEW_TEST_UUID, 12);
        return shoppingList;
    }

    private List<Product> buildProductsList(){
        List<Product>products = new ArrayList<>();
        products.add(buildProduct());
        Product product = buildProduct();
        product.setProductCatalogNumber(NEW_TEST_UUID);
        products.add(product);
        return products;
    }
}
