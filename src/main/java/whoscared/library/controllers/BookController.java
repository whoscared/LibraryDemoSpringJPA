package whoscared.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import whoscared.library.dao.BookDAO;
import whoscared.library.dao.PersonDAO;
import whoscared.library.models.Book;
import whoscared.library.models.Person;

import javax.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    //Страница со списком всех книг, передаем model,которая содержит этот список
    @GetMapping()
    public String allBooks(Model model) {
        model.addAttribute("books", bookDAO.allBook());
        return ("/book/list");
    }

    //Страница для каждой книги, с помощью PathVariable получаем id, отправляем model соответствующей книги
    @GetMapping("/{id}")
    public String oneBook(@PathVariable("id") int id,
                          Model model,
                          // Передаем пустой объект для выпадающего списка
                          @ModelAttribute("person") Person person) {
        // Передаем книгу, страницу которой мы открываем
        model.addAttribute("book", bookDAO.oneBook(id));
        Person bookOwner = personDAO.onePerson(bookDAO.getIdPerson(id));
        if (bookOwner != null) {
            // Передаем обладателя книги, если такой имеется
            model.addAttribute("owner", bookOwner);
        } else {
            // Передаем список всех людей, чтобы назначить владельца книги (если такового нет )
            model.addAttribute("people", personDAO.allPerson());
        }
        return "/book/id";
    }

    //Создание новой книги, передаем пустую model, которую будем заполнять с помощью формы
    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "/book/new";
    }

    //Получаем заполненную форму со страницы /new и добавляем книгу в бд
    @PostMapping()
    public String addBook(@ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        bookDAO.addBook(book);
        return "redirect:/book";
    }

    //Удаляем книгу, на странице которой находимся, получая id с помощью PathVariable
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id")int id){
        bookDAO.deleteBook(id);
        return "redirect:/book";
    }

    //Освобождаем книгу по ее id
    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id")int id){
        bookDAO.releaseBook(id);
        return "redirect:/book/" + id;
    }

    //Добавляем владельца книги, id которой получаем из страницы, а id пользователя получаем из модели,
    //которую отправили с формы (выпадающий список, который представляет список пользователей)
    //при выборе пользователя из списка в пустой объект person с помощью setId помещается id выбранного объекта
    //пустой объект person, который содержит только id, передается и из него извлекается id для выбор владельца книги
    @PatchMapping("/{id}/addowner")
    public String addOwner(@PathVariable("id") int idBook,
                           @ModelAttribute("person") Person newOwner){
        Book book = bookDAO.oneBook(idBook);
        bookDAO.userGetBook(newOwner.getId(), book);
        return "redirect:/book/" + idBook;
    }
}
