package authors;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController {
    @RequestMapping("/authors/{isbn}")
    public Author getAuthor(@PathVariable("isbn") String isbn) {
        return new Author("Joanne", "Rowling");
    }
}
