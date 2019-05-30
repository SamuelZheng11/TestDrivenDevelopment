import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ReviewerPersistence {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    final static String FIRST_NAME_KEY = "FIRST_NAME";
    final static String REVIEW_COUNT_KEY = "REVIEW_COUNT";
    final static String USERTYPE_KEY = "USERTYPE";

    private List<User> reviewers = new ArrayList<User>();


    private static ReviewerPersistence instance;

    private ReviewerPersistence(){}

    public void addDatabase(MongoClient mongoClient,
                               String myCashDatabaseName, String myCashCollectionName) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = this.mongoClient.getDatabase(myCashDatabaseName);
        this.mongoCollection = this.mongoDatabase.getCollection(myCashCollectionName);
    }

    public static ReviewerPersistence getInstance(){
        if (instance == null){
            instance = new ReviewerPersistence();
        }
        return instance;
    }

    public void updateReviewCount(User codeReviewer){
        Document updatedUserCount = new Document(FIRST_NAME_KEY, codeReviewer.getName()).append(REVIEW_COUNT_KEY, codeReviewer.getReviewCount()).append(USERTYPE_KEY, UserType.NonDeveloper.ordinal());
        mongoCollection.replaceOne(eq(FIRST_NAME_KEY, codeReviewer.getName()), updatedUserCount);
    }

    public int getReviewCountForUser(User codeReviewer){
        FindIterable<Document> documents = mongoCollection.find(new Document(FIRST_NAME_KEY, codeReviewer.getName()));
        MongoCursor<Document> iterator = documents.iterator();
        int reviewCount = -1;
        while (iterator.hasNext()) {
            Document document = iterator.next();
            reviewCount = (Integer)document.get(REVIEW_COUNT_KEY);
        }
        return reviewCount;
    }

    public List<User> getAllCodeReviewers() {
        if(reviewers.size()==0) {
            FindIterable<Document> documents = mongoCollection.find();
            MongoCursor<Document> iterator = documents.iterator();
            while (iterator.hasNext()) {
                Document document = iterator.next();

                String userName = (String) document.get(FIRST_NAME_KEY);
                int reviewCount = (Integer) document.get(REVIEW_COUNT_KEY);
                UserType ut = (UserType) document.get(USERTYPE_KEY);

                if (ut == UserType.NonDeveloper) {
                    User user = new User(userName, ut, reviewCount);
                    reviewers.add(user);
                }
            }
        }
        return reviewers;
    }

}
