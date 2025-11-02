package accounts;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @RequestMapping("/account/{customerid}")
    public Account getName(@PathVariable("customerid") String customerId) {
        return new Account("1234", "1000.00");
    }
}
