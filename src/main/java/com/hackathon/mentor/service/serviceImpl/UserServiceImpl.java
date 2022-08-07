package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.ERole;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Role;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RoleRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final MentorRepository mentorRepository;
    @Override
    public void initRoles() {
        if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
            Role roleAdmin = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }
        if (!roleRepository.findByName(ERole.ROLE_MENTOR).isPresent()) {
            Role roleMentor = new Role(ERole.ROLE_MENTOR);
            roleRepository.save(roleMentor);
        }
        if (!roleRepository.findByName(ERole.ROLE_MENTEE).isPresent()) {
            Role roleMentee = new Role(ERole.ROLE_MENTEE);
            roleRepository.save(roleMentee);
        }
        if (!roleRepository.findByName(ERole.ROLE_MENTEE).isPresent()) {
            Role anonymous = new Role(ERole.ANONYMOUS);
            roleRepository.save(anonymous);
        }
    }

    @Override
    public void initFakeAccounts() {
        if (userRepository.findByEmail("Alex0@mail.com").isPresent()) {
            return;
        }
        List<Role> roles = roleRepository.findAll();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            Mentor mentor = new Mentor();
            user.setFirstname("Alex" + i);
            user.setMiddlename("Alexovich" + i);
            user.setLastname("Alexov" + i);
            user.setEmail("Alex@mail.com" + i);
            user.getRoles().add(roles.get(1));
            user.setPassword("Alex@345");
            user.setTelegram("@Alex" + i);
            mentor.setUser(user);
            userRepository.save(user);
            mentorRepository.save(mentor);
        }
    }

//    public RandomNameGenerator()
//    {
//        rFirst = new Random(42);
//        rLast = new Random(rFirst.Next());
//    }
//
//    public string[] GenerateName()
//    {
//        string[] results = new string[2];
//        results[0] = _firstName[rFirst.Next(_firstName.Length)];
//        results[1] = _lastName[rLast.Next(_lastName.Length)];
//        return results;
//    }
//    Random rFirst;
//    Random rLast;
//
//    string[] _firstName =  new string[] { "Adam", "Alex", "Aaron", "Ben", "Carl", "Dan", "David", "Edward", "Fred",
//            "Frank", "George", "Hal", "Hank", "Ike", "John", "Jack", "Joe", "Larry", "Monte", "Matthew", "Mark",
//            "Nathan", "Otto", "Paul", "Peter", "Roger", "Roger", "Steve", "Thomas", "Tim", "Ty", "Victor", "Walter"};
//
//    string[] _lastName = new string[] { "Anderson", "Ashwoon", "Aikin", "Bateman", "Bongard", "Bowers", "Boyd",
//            "Cannon", "Cast", "Deitz", "Dewalt", "Ebner", "Frick", "Hancock", "Haworth", "Hesch", "Hoffman", "Kassing",
//            "Knutson", "Lawless", "Lawicki", "Mccord", "McCormack", "Miller", "Myers", "Nugent", "Ortiz", "Orwig",
//            "Ory", "Paiser", "Pak", "Pettigrew", "Quinn", "Quizoz", "Ramachandran", "Resnick", "Sagar", "Schickowski",
//            "Schiebel", "Sellon", "Severson", "Shaffer", "Solberg", "Soloman", "Sonderling", "Soukup", "Soulis",
//            "Stahl", "Sweeney", "Tandy", "Trebil", "Trusela", "Trussel", "Turco", "Uddin", "Uflan", "Ulrich", "Upson",
//            "Vader", "Vail", "Valente", "Van Zandt", "Vanderpoel", "Ventotla", "Vogal", "Wagle", "Wagner", "Wakefield",
//            "Weinstein", "Weiss", "Woo", "Yang", "Yates", "Yocum", "Zeaser", "Zeller", "Ziegler", "Bauer", "Baxster",
//            "Casal", "Cataldi", "Caswell", "Celedon", "Chambers", "Chapman", "Christensen", "Darnell", "Davidson",
//            "Davis", "DeLorenzo", "Dinkins", "Doran", "Dugelman", "Dugan", "Duffman", "Eastman", "Ferro", "Ferry",
//            "Fletcher", "Fietzer", "Hylan", "Hydinger", "Illingsworth", "Ingram", "Irwin", "Jagtap", "Jenson",
//            "Johnson", "Johnsen", "Jones", "Jurgenson", "Kalleg", "Kaskel", "Keller", "Leisinger", "LePage", "Lewis",
//            "Linde", "Lulloff", "Maki", "Martin", "McGinnis", "Mills", "Moody", "Moore", "Napier", "Nelson", "Norquist",
//            "Nuttle", "Olson", "Ostrander", "Reamer", "Reardon", "Reyes", "Rice", "Ripka", "Roberts", "Rogers", "Root",
//            "Sandstrom", "Sawyer", "Schlicht", "Schmitt", "Schwager", "Schutz", "Schuster", "Tapia", "Thompson",
//            "Tiernan", "Tisler" };
}
