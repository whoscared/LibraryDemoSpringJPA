package whoscared.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import whoscared.library.models.Book;
import whoscared.library.models.Person;
import whoscared.library.services.BookService;
import whoscared.library.services.PersonService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }


    @GetMapping()
    public String allBooks(Model model,
                           @RequestParam(value = "page", required = false) String page,
                           @RequestParam(value = "count", required = false) String count,
                           @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        List<Book> books;
        Sort sort = Sort.by("name");
        if (sortByYear) {
            sort = Sort.by("year");
            books = new ArrayList<>(bookService.getSortList(sort));
            model.addAttribute("sort", true);
        } else {
            books = new ArrayList<>(bookService.findAll());
        }
        if (count != null) {
            int numberOfPage = Integer.parseInt(page);
            Page<Book> bookPage = bookService.getPage(numberOfPage, Integer.parseInt(count), sort);
            List<Book> bookListPage = new ArrayList<>(bookPage.getContent());

            model.addAttribute("page", numberOfPage);

            if (numberOfPage > 0) {
                model.addAttribute("previousPage", numberOfPage - 1);
            }
            if (numberOfPage < bookPage.getTotalPages() - 1) {
                model.addAttribute("nextPage", numberOfPage + 1);
            }
            model.addAttribute("books", bookListPage);
            model.addAttribute("count", count);
            return "/book/page";
        }
        model.addAttribute("books", books);
        return ("/book/list");
    }

    @GetMapping("/{id}")
    public String oneBook(@PathVariable("id") int id,
                          Model model,
                          @ModelAttribute("person") Person person) {
        Book currentBook = bookService.oneBook(id);
        model.addAttribute("book", currentBook);
        Person bookOwner = personService.getOwnerByBook(currentBook);
        if (bookOwner != null) {
            model.addAttribute("owner", bookOwner);
        } else {
            model.addAttribute("people", personService.findAll());
        }
        return "/book/id";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "/book/new";
    }

    @PostMapping()
    public String addBook(@ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        bookService.addBook(book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookService.releaseBook(id);
        return "redirect:/book/" + id;
    }

    @PatchMapping("/{id}/addowner")
    public String addOwner(@PathVariable("id") int idBook,
                           @ModelAttribute("person") Person newOwner) {
        Book book = bookService.oneBook(idBook);
        bookService.userGetBook(newOwner.getId(), book);
        return "redirect:/book/" + idBook;
    }
}
