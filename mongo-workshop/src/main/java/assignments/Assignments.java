package assignments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import database.Database;

public class Assignments {

    private final Database database;
    private int userDocs = 0;
    private int gameDocs = 0;

    public Assignments() {
        this.database = Database.getInstance();
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
        
        //Counting user docs
        database.getUserCollection().visit(() -> {
            return database.getDatabase().getCollection("users");
        }, x -> x).find().iterator().forEachRemaining(doc -> userDocs++);
        //Counting game docs
        database.getGameCollection().visit(() -> {
            return database.getDatabase().getCollection("games");
        }, x -> x).find().iterator().forEachRemaining(doc -> gameDocs++);
        
        if (userDocs < 3) {
        	database.getUserCollection().visit(() -> {
                return database.getDatabase().getCollection("users");
            }, x -> x).insertMany(Arrays.asList(
            		createUserdoc("Kees", Arrays.asList(4, 3, 10), createAddressDoc("Nederland", "4810BG")),
            		createUserdoc("Sjaak", Arrays.asList(1, 3, 9), createAddressDoc("Nederland", "9184WD")),
    				createUserdoc("Henk", Arrays.asList(1, 5, 7), createAddressDoc("Nederland", "4910ZP"))
    			));
        }
        if (gameDocs < 5) {
        	database.getGameCollection().visit(() -> {
                return database.getDatabase().getCollection("games");
            }, x -> x).insertMany(Arrays.asList(createGamedoc(1, "Mario", 10.50),
            		createGamedoc(3, "Zelda", 12.50), createGamedoc(4, "COD", 40.00),
            		createGamedoc(9, "Battlefield", 40.00), createGamedoc(7, "CSGO", 15.00)));
        }
        
        System.out.println(userDocs + " user docs found.");
        System.out.println(gameDocs + " game docs found.");
    }
    
    private Document createGamedoc(int id, String name, double price) {
    	Document gameDoc = new Document();
    	gameDoc.put("id", id);
    	gameDoc.put("name", name);
    	gameDoc.put("price", price);
    	return gameDoc;
    }
    
    private Document createUserdoc(String name, List<Integer> games, Document address) {
    	Document userDoc = new Document();
    	userDoc.put("name", name);
    	userDoc.put("games", games);
    	userDoc.put("address", address);
    	return userDoc;
    }
    
    private Document createAddressDoc(String country, String postal) {
    	Document address = new Document();
    	address.put("country", country);
    	address.put("postal", postal);
    	return address;
    }

    private void runAssignment3() {
        System.out.println("Assignment3: ");
        // TODO: implement the UserDocument manager class
        
        UserDocumentManager userManager = new UserDocumentManager(database.getUserCollection().visit(() -> {
            return database.getDatabase().getCollection("users");
        }, x -> x).find().first());
        System.out.println(userManager.getName());
        System.out.println(userManager.getGames());
        System.out.println(userManager.getAddress());
    }

    private void runAssignment4() {
        System.out.println("Assignment4: ");
        // TODO: implement the GameDocument manager class
        
        GameDocumentManager gameManager = new GameDocumentManager(database.getGameCollection().visit(() -> {
            return database.getDatabase().getCollection("games");
        }, x -> x).find().first());
        System.out.println(gameManager.getId());
        System.out.println(gameManager.getName());
        System.out.println(gameManager.getPrice());
    }

    private void runAssignment5() {
        System.out.println("Assignment5: ");
        // TODO: Get all game documents for a user
        
        UserDocumentManager userManager = new UserDocumentManager(database.getUserCollection().visit(() -> {
            return database.getDatabase().getCollection("users");
        }, x -> x).find().first());
        List<Integer> games = userManager.getGames();
        List<Document> userGames = new ArrayList<Document>();
        for (int game : games) {
        	Document query = new Document();
        	query.put("id", game);
        	userGames.add(database.getGameCollection().visit(() -> {
        		 return database.getDatabase().getCollection("games");
        	}, x -> x).find(query).iterator().tryNext());
        }
        
        System.out.println(userGames.size() + " games found for " + userManager.getName());
    }
    
    private void runAssignment6() {
        System.out.println("Assignment6: ");
        UserDocumentManager manager = new UserDocumentManager(database.getUserCol().find().first());
        manager.setName("Bert");
        manager.setAddress("USA", "1049GB");
    }

    private class GameDocumentManager {

        private final Document gameDoc;

        public GameDocumentManager(Document gameDoc) {
            this.gameDoc = gameDoc;
        }

        public int getId() {
        	return gameDoc.getInteger("id");
        }

        public String getName() {
        	return gameDoc.getString("name");
        }

        public double getPrice() {
        	return gameDoc.getDouble("price");
        }

    }

    private class UserDocumentManager {

        private Document userDoc;

        public UserDocumentManager(Document userDoc) {
            this.userDoc = userDoc;
        }

        public String getName() {
        	return userDoc.getString("name");
        }

        @SuppressWarnings("unchecked")
		public List<Integer> getGames() {
        	return (List<Integer>) userDoc.get("games");
        }

        public Document getAddress() {
        	return (Document) userDoc.get("address");
        }
        
        public void setName(String name) {
        	String oldName = getName();
        	Document newValue = new Document();
        	newValue.put("name", name);
        	
        	update(newValue);
        	updateThisDoc(name);
        	
        	System.out.println(oldName + " changed to " + getName());
        }
        
        public void setAddress(String country, String postal) {
        	Document newAddress = new Document();
        	newAddress.put("country", country);
        	newAddress.put("postal", postal);
        	Document newValue = new Document();
        	newValue.put("address", newAddress);
        	update(newValue);
        }
        
        private void update(Document newValue) {
        	Database.getInstance().getUserCol().findOneAndUpdate(userDoc, new Document("$set", newValue));
        	updateThisDoc(getName());
        }
        
        private void updateThisDoc(String nameValue) {
        	Document newDoc = new Document();
        	newDoc.put("name", nameValue);
        	this.userDoc = Database.getInstance().getUserCol().find(newDoc).iterator().tryNext();
        }

    }

}
