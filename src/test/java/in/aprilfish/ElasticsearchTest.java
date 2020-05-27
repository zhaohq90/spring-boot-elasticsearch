package in.aprilfish;

import in.aprilfish.entity.Book;
import in.aprilfish.repository.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class ElasticsearchTest extends AbstractTest{

    @Autowired
    private BookRepository repository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void queryWithTemplate() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .must(existsQuery("bookName"))
                        .must(existsQuery("author"))
                        .must(termQuery("author", "andy"))
                        .must(rangeQuery("time")
                                .from(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
                                .to(System.currentTimeMillis())))
                .build();

        List<Book> bookList = elasticsearchRestTemplate.queryForList(searchQuery, Book.class);

        bookList.forEach(System.out::println);
    }

    @Test
    public void saveAndQuery(){
        Book book=new Book();
        book.setId(1);
        book.setBookName("三体");
        book.setAuthor("andy");
        book.setTime(System.currentTimeMillis()-60*60*1000);

        repository.save(book);

        Optional<Book> optionalBook=repository.findById(1);

        Assert.assertTrue(optionalBook.isPresent());
        Assert.assertEquals(book.getAuthor(),optionalBook.get().getAuthor());
    }

    @Test
    public void delete(){
        repository.deleteById(1);
    }

}
