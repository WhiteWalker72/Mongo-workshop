package assignments;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import database.Database;

public class Assignments {

    private final Database database;
    private int userDocs = 0;
    private int gameDocs = 0;

    public Assignments() {
        this.database = new Database();
    }

    public void runAssignments() {
        runAssignment1();
        runAssignment2();
        runAssignment3();
        runAssignment4();
        runAssignment5();
        runAssignment6();
    }

    private void runAssignment1() {
        System.out.println("Assignment1: ");
        // TODO: implement the database product and user collections in the
        // Database class.

        System.out.println(database.getGameCollection().visit(() -> "Collection not found.",
                x -> "collection: " + x.getNamespace() + " found."));
        System.out.println(database.getUserCollection().visit(() -> "Collection not found.",
                x -> "collection: " + x.getNamespace() + " found."));
    }

    private void runAssignment2() {
        System.out.println("Assignment2: ");
        // TODO: Insert 3 users into the user collection with the following
        // values:
        // String name;
        // List<Integer> games;
        // Document address; with a country and postal code

        // TODO: Insert 5 games into the game collection with the following
        // values:
        // int id;
        // String name;
        // double price;
        
        //Counting user docs
        database.getUserCollection().visit(() -> {
            MongoCollection<Document> users = database.getDatabase().getCollection("users");
            return users;
        }, x -> x).find().iterator().forEachRemaining(doc -> userDocs++);
        //Counting game docs
        database.getUserCollection().visit(() -> {
            MongoCollection<Document> games = database.getDatabase().getCollection("games");
            return games;
        }, x -> x).find().iterator().forEachRemaining(doc -> gameDocs++);
        System.out.println(userDocs + " user docs found.");
        System.out.println(gameDocs + " game docs found.");
    }

    private void runAssignment3() {
        System.out.println("Assignment3: ");
        // TODO: implement the UserDocument manager class
        
        UserDocumentManager userManager = new UserDocumentManager(database.getUserCollection().visit(() -> {
            MongoCollection<Document> users = database.getDatabase().getCollection("users");
            return users;
        }, x -> x).find().first());
        System.out.println(userManager.getName());
        System.out.println(userManager.getGames());
        System.out.println(userManager.getAddress());
    }

    private void runAssignment4() {
        System.out.println("Assignment4: ");
        // TODO: implement the GameDocument manager class
        
        GameDocumentManager gameManager = new GameDocumentManager(database.getGameCollection().visit(() -> {
            MongoCollection<Document> games = database.getDatabase().getCollection("games");
            return games;
        }, x -> x).find().first());
        System.out.println(gameManager.getId());
        System.out.println(gameManager.getName());
        System.out.println(gameManager.getPrice());
    }

    private void runAssignment5() {
        System.out.println("Assignment5: ");
        // TODO: Get all game documents for a user
        
        UserDocumentManager userManager = new UserDocumentManager(database.getUserCollection().visit(() -> {
            MongoCollection<Document> users = database.getDatabase().getCollection("users");
            return users;
        }, x -> x).find().first());
//      List<Integer> games = userManager.getGames();
        
        List<Document> userGames = new ArrayList<Document>();
        System.out.println(userGames.size() + " games found for " + userManager.getName());
    }
    
    private void runAssignment6() {
        System.out.println("Assignment6: ");
        //TODO: change the name and address for a user, add these methods to the UserDocumentManager.
        
        
    }

    // TODO: implement methods
    private class GameDocumentManager {

        @SuppressWarnings("unused")
        private final Document gameDoc;

        public GameDocumentManager(Document gameDoc) {
            this.gameDoc = gameDoc;
        }

        public int getId() {

            return 0;
        }

        public String getName() {

            return "None";
        }

        public double getPrice() {

            return 0.0;
        }

    }

    // TODO: implement methods
    private class UserDocumentManager {

        @SuppressWarnings("unused")
        private final Document userDoc;

        public UserDocumentManager(Document userDoc) {
            this.userDoc = userDoc;
        }

        public String getName() {

            return "no username found";
        }

        public List<Integer> getGames() {

            return null;
        }

        public Document getAddress() {

            return null;
        }

    }

}
