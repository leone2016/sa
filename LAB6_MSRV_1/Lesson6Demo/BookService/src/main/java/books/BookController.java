package books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    @Autowired
    AuthorFeignClient authorClient;
    @RequestMapping("/books/{isbn}")
    public Book getName(@PathVariable("isbn") String isbn) {
        Author author = authorClient.getAuthor(isbn);
        return new Book("isbn", "1000.00", author.firstname()+" "+author.lastname());
    }

    @FeignClient(name = "author-service", url = "http://localhost:8093")
    interface AuthorFeignClient {
        @RequestMapping("/authors/{isbn}")
        public Author getAuthor(@PathVariable("isbn") String isbn);
    }
}

