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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
//readOnly for all methods without Annotation @Transaction
//Annotation for a particular method has higher precedence
@Transactional(readOnly = true)
//transactions start and end in service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
    public void userGetBook(Person owner, int idBook) {
        //this book in persistence context so no need .save()
        bookRepository.findById(idBook).ifPresent(
                book -> {
                    book.setOwner(owner);
                    book.setTime(new Date());
                }
        );
    }

    @Transactional
    public void releaseBook(int idBook) {
        //this book in persistence context so no need .save()
        bookRepository.findById(idBook).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTime(null);
                    book.setArrears(false);
                });
    }

    public Page<Book> getPage(int numberOfPage, int count, Sort sort) {
        PageRequest pageRequest = PageRequest.of(numberOfPage, count, sort);
        return bookRepository.findAll(pageRequest);
    }

    public List<Book> findAll(Sort sort) {
        return bookRepository.findAll(sort);
    }

    public List<Book> getByQuerySearch( String querySearch) {
        return bookRepository.findByNameContains(querySearch);
    }
}
