package whoscared.library.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//readOnly for all methods without Annotation @Transaction
//Annotation for a particular method has higher precedence
@Transactional(readOnly = true)
public class PeopleService {
}
