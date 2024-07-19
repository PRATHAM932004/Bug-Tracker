package net.bughandlers.bugtracker.repository;

import net.bughandlers.bugtracker.model.Bug;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugRepository extends MongoRepository<Bug, ObjectId> {
}
