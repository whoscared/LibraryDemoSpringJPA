package whoscared.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import whoscared.library.dao.BookDAO;
import whoscared.library.dao.PersonDAO;
import whoscared.library.models.Person;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    //Ничего не указываем, тк метод вызывается на странице /people
    @GetMapping()
    public String allPerson(Model model) {
        model.addAttribute("people", personDAO.allPerson());
        return ("person/list");
    }

    //Получаем id из url
    @GetMapping("/{id}")
    //C помощью аннотации PathVariable извлекаем id из адреса url и помещаем его в пременную, следующую за ней
    public String onePerson(@PathVariable("id") int id, Model model) {
        // Метод addAttribute передает в модель пару key - value
        // В представлении, используя эти данные можем вывести информацию о полученном из DAO Person
        model.addAttribute("person", personDAO.onePerson(id));
        model.addAttribute("books", bookDAO.personBook(id));
        // Возвращаем страницу view (html-файл)
        return "person/id";
    }

    @GetMapping("/new")
    //C помощью Model передаем на страницу пустой объект Person, который заполним с помощью формы
    public String newUser(Model model) {
        model.addAttribute("person", new Person());
        return "/person/new";
    }

    @PostMapping()
    //Благодаря ModelAttribute на вход принимаем из представления view готовый объект Person
    //Для того, чтобы значения с формы валидировались, добавляем аннотацию Valid
    //Теперь на этапе внедрения значений из формы в объект, поля будут проверяться согласно условиям, которые мы
    //прописали для каждого из них. Если не все будут выполнены, вызывается ошибка
    //Ошибки помещаются в отдельный объект BindingResult (он всегда должен идти после той модели, которая валидируется)
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult) {
        //Если имеются ошибки
        if (bindingResult.hasErrors()) {
            //Сразу же возвращаем форму создания человека, если были допущены ошибки
            //Возвращаем с текущей моделью, в которой допущены ошибки
            return "person/new";
        }
        //model.addAttribute()
        //model.addAttribute("books", bookDAO.freeBook());
        personDAO.addPerson(person);
        //bookDAO.userGetBooks(person.getId(), chooseBooks);
        return "redirect:/people";
    }

    //id получаем из url-запроса, новый объект Person из формы с помощью ModelAttribute
    //Страница с редактированием человека, то есть при GET-запросе мы будем обращаться к данному методу
    //Данный метод возвращает html-страницу для редактирования человека
    @GetMapping("/{id}/edit")
    //Передаем Model и извлекаем id, который передается в запросе
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.onePerson(id));
        //В представлении view будем иметь доступ к модели, а именно к объекту, который хотим изменить
        //Чтобы в самой форме поля были не пустые, а имели значения текущего человека
        return ("person/edit");
    }

    @PostMapping("/{id}")
    public String change(@PathVariable("id") int id,
                         @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ("/person/edit");
        }
        personDAO.edit(person, id);
        return ("person/id");
    }

    // На странице конкретного человека будет кнопка удалить, из url получаем id и удаляем
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.deletePerson(id);
        return ("redirect:/people");
    }

}
