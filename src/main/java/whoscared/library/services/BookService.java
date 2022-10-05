package whoscared.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whoscared.library.models.Book;
import whoscared.library.models.Person;
import whoscared.library.repositories.BookRepository;
import whoscared.library.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService( BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book oneBook(int id) {
        Optional<Book> findBook = bookRepository.findById(id);
        return findBook.orElse(null);
    }

    @Transactional
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void userGetBook(int idPerson, Book book) {
        Person owner = personRepository.findById(idPerson).orElse(null);
        owner.setBooks(book);
        personRepository.save(owner);
        bookRepository.save(book);
    }

    @Transactional
    public void releaseBook(int idBook) {
        Book book = bookRepository.findById(idBook).orElse(null);
        Person notOwner = book.getOwner();
        book.release();
        bookRepository.save(book);
        personRepository.save(notOwner);
    }

    public List<Book> getBooksByOwner(Person owner) {
        return bookRepository.findByOwner(owner);
    }


    public Page<Book> getPage(int numberOfPage, int count) {
        PageRequest pageRequest = PageRequest.of(numberOfPage, count, Sort.by("name"));
        return bookRepository.findAll(pageRequest);
    }
    public Page<Book> getPage(int numberOfPage, int count, Sort sort){
        PageRequest pageRequest = PageRequest.of(numberOfPage, count, sort);
        return bookRepository.findAll(pageRequest);
    }

    public List<Book> getSortList(Sort sort){
        return bookRepository.findAll(sort);
    }
}
