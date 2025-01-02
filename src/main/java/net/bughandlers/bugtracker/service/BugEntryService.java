package net.bughandlers.bugtracker.service;


import lombok.extern.slf4j.Slf4j;
import net.bughandlers.bugtracker.model.Bug;
import net.bughandlers.bugtracker.model.User;
import net.bughandlers.bugtracker.repository.BugRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Component
public class BugEntryService {

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private UserService userService;



    @Transactional
    public void saveEntry(Bug bug, String userName) {
        try {
            User user = userService.findByUsername(userName);
            bug.setDate(LocalDateTime.now());
            Bug saved = bugRepository.save(bug);
            user.getBugs().add(saved);
            userService.saveUser(user);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured while saving entry", e);
        }
    }

    public void saveEntry(Bug bug) {
        bugRepository.save(bug);
    }

    public List<Bug> getAll() {
        return bugRepository.findAll();
    }

    public Optional<Bug> findById(ObjectId ID) {
        return bugRepository.findById(ID);
    }

    @Transactional
    public boolean deleteById(ObjectId ID, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUsername(userName);
            removed = user.getBugs().removeIf(x -> x.getID().equals(ID));
            if(removed){
                userService.saveUser(user);
                bugRepository.deleteById(ID);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("An error occured while deleting entry", e);
        }
        return removed;
    }

    public List<Bug> findByUser(User user) {
        return user.getBugs();
    }
}
