package net.bughandlers.bugtracker.controller;

import net.bughandlers.bugtracker.model.Bug;
import net.bughandlers.bugtracker.model.User;
import net.bughandlers.bugtracker.service.BugEntryService;
import net.bughandlers.bugtracker.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bug")
public class BugEntryController {

    @Autowired
    private BugEntryService bugEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllBugEntryController(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUsername(userName);
        List<Bug> bugList = user.getBugs();
        if(!bugList.isEmpty() && bugList != null){
            return new ResponseEntity<>(bugList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Bug> createEntry(@RequestBody Bug bug){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            bugEntryService.saveEntry(bug, userName);
            return new ResponseEntity<>(bug, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUsername(userName);
        List<Bug> bugList = user.getBugs().stream().filter(x -> x.getID().equals(myId)).collect(Collectors.toList());
        if (!bugList.isEmpty()){
            Optional<Bug> bug = bugEntryService.findById(myId);
            if (bug.isPresent()){
                return new ResponseEntity<>(bug.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{ID}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId ID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = bugEntryService.deleteById(ID, userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId myId, @RequestBody Bug newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUsername(userName);
        List<Bug> bugList = user.getBugs().stream().filter(x -> x.getID().equals(myId)).collect(Collectors.toList());
        if (!bugList.isEmpty()){
            Optional<Bug> bug = bugEntryService.findById(myId);
            if(bug != null){
                Bug old = bug.get();
                old.setAssignee(newEntry.getAssignee() != null && !newEntry.getAssignee().equals("") ?
                        newEntry.getAssignee() : old.getAssignee());
                old.setBug_Hours(newEntry.getBug_Hours() != 0 ?
                        newEntry.getBug_Hours() : old.getBug_Hours());
                old.setDescription(newEntry.getDescription() != null && !newEntry.getDescription().equals("") ?
                        newEntry.getDescription() : old.getDescription());
                old.setPriority(newEntry.getPriority() != null && !newEntry.getPriority().equals("") ?
                        newEntry.getPriority() : old.getPriority());
                old.setStatus(newEntry.getStatus() != null && !newEntry.getStatus().equals("") ?
                        newEntry.getStatus() : old.getStatus());
                old.setStart_Date(newEntry.getStart_Date() != null && !newEntry.getStart_Date().equals("") ?
                        newEntry.getStart_Date() : old.getStart_Date());
                old.setEnd_Date(newEntry.getEnd_Date() != null && !newEntry.getEnd_Date().equals("") ?
                        newEntry.getEnd_Date() : old.getEnd_Date());
                old.setOperating_System(newEntry.getOperating_System() != null && !newEntry.getOperating_System().equals("") ?
                        newEntry.getOperating_System() : old.getOperating_System());
                old.setBug_Title(newEntry.getBug_Title() != null && !newEntry.getBug_Title().equals("") ?
                        newEntry.getBug_Title() : old.getBug_Title());

                bugEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
