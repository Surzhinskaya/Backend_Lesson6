import api.ProductService;
import dto.Product;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import utils.RetrofitUtils;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;

public class ProductTest {


    static ProductService productService;
    Product product = new Product();
    static long id;


    public ProductTest() throws IOException {
    }

    @BeforeAll
    static void beforeAll() throws IOException {

        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession();

    db.dao.ProductsMapper productsMapper = sqlSession.getMapper(db.dao.ProductsMapper.class);
    db.model.ProductsExample productsExample = new db.model.ProductsExample();


    @BeforeEach
    void setUp() {
   product = new Product()
                .withTitle("Apple")
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

        db.model.Products products = new  db.model.Products();
        products.setPrice((int) (Math.random() * 10000));
        products.setTitle("Apple");
        products.setCategoryTitle ("Food");
        productsMapper.insert(products);
        id=products.getId();

    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {
      Response<Product> response = productService.createProduct(product)
                .execute();


        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), Matchers.is(201));


        db.model.Products selected = productsMapper.selectByPrimaryKey(id);
     System.out.println("ID: " + selected.getId() + "\ntitle: " + selected.getTitle());

    }

  @Test
       void getProductsTest() throws IOException {

       Response<ResponseBody> response = productService.getProducts()
               .execute();
       System.out.println("AllProducts: " + response.body().string());


       assertThat(response.code(), Matchers.is(200));
       assertThat(response.isSuccessful(), CoreMatchers.is(true));
       db.model.Products selected = productsMapper.selectByPrimaryKey(id);
       System.out.println("ID: " + selected.getId() + "\ntitle: " + selected.getTitle());

   }

 @Test
        void modifyProductTest() throws IOException {

            Response<Product> response1 = productService.createProduct(product)
                    .execute();

            assertThat(response1.isSuccessful(), CoreMatchers.is(true));
            assertThat(response1.code(), Matchers.is(201));

        db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);
        System.out.println("ID: " + selected.getId() + "\ntitle: " + selected.getTitle() + "\nprice: " + selected.getPrice());

        productsMapper.updateByPrimaryKey(new Product(selected.getId(), "Lemon", 540, "Food"));


        db.model.Products selected2 = productsMapper.selectByPrimaryKey(id);
        System.out.println("ID: " + selected2.getId() + "\ntitle: " + selected2.getTitle()+ "\nprice: " + selected2.getPrice());
        productsMapper.updateByPrimaryKey(new Product(selected.getId(), "Lemon", 540, "Food"));

        }

        @Test
        void getProductByIdTest() throws IOException {

            db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);
            System.out.println("ID: " + selected.getId() + "\ntitle: " + selected.getTitle());

        }




    @SneakyThrows
        @AfterEach
        void tearDown() {

           Response<ResponseBody> response = productService.deleteProduct((int) id).execute();
            db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);
            productsMapper.deleteByPrimaryKey(selected.getId());

        }


    }

