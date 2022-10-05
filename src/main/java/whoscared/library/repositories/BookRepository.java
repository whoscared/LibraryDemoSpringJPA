package whoscared.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whoscared.library.models.Book;
import whoscared.library.models.Person;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner (Person owner);
}
