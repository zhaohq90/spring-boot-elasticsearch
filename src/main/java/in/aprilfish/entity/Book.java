package in.aprilfish.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "library", type = "book")
public class Book {

    private Integer id;
    private String bookName;
    private String author;
    private Long time;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
