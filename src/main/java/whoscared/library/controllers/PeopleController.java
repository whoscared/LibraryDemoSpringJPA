package whoscared.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import whoscared.library.models.Person;

import whoscared.library.services.BookService;
import whoscared.library.services.PersonService;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public PeopleController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String allPerson(Model model) {
        model.addAttribute("people", personService.findAll());
        return ("person/list");
    }


    @GetMapping("/{id}")
    public String onePerson(@PathVariable("id") int id, Model model) {
        Person person = personService.findOne(id);
        model.addAttribute("person", person);
        if (person.getBooks() != null){
            System.out.println(bookService.getBooksByOwner(person));
            model.addAttribute("books", bookService.getBooksByOwner(person));
        }
        return "person/id";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("person", new Person());
        return "/person/new";
    }

    @PostMapping()
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "person/new";
        }
        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.findOne(id));
        return ("person/edit");
    }

    @PostMapping("/{id}")
    public String change(@PathVariable("id") int id,
                         @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ("/person/edit");
        }
        personService.update(id, person);
        return ("person/id");
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return ("redirect:/people");
    }

}
