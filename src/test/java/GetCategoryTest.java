
import api.CategoryService;
import dto.GetCategoryResponse;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetCategoryTest {

    static CategoryService categoryService;
   String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession();

   db.dao.CategoriesMapper categoriesMapper = sqlSession.getMapper(db.dao.CategoriesMapper.class);
   db.model.CategoriesExample categoriesExample = new db.model.CategoriesExample();

    public GetCategoryTest() throws IOException {
    }

    // public GetCategoryTest() throws IOException {
  //  }

    @BeforeAll
    static void beforeAll() throws IOException {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
       String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

       db.dao.CategoriesMapper categoriesMapper = sqlSession.getMapper(db.dao.CategoriesMapper.class);
       db.model.CategoriesExample categoriesExample = new db.model.CategoriesExample();
    }

    @SneakyThrows
    @Test
    void getCategoryByIdPositiveTest() {
        Response<GetCategoryResponse> response = categoryService.getCategory(1).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo("Food"));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo("Food")));

        db.model.Categories selected = categoriesMapper.selectByPrimaryKey(1L);
        System.out.println("ID: " + selected.getId() + "\ntitle: " + selected.getTitle());


    }

}