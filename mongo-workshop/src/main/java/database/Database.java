package database;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import option.IOption;
import option.None;
import option.Some;

public class Database {
    
    private final MongoClient mongo;
    private final MongoDatabase db;
    private final MongoCollection<Document> gameCollection;
    private final MongoCollection<Document> userCollection;
    private static Database instance;
    
	private Database() {
        this.mongo = new MongoClient("localhost", 27017);
        this.db = mongo.getDatabase("workshop");
        this.gameCollection = db.getCollection("games");
        this.userCollection = db.getCollection("users");
    }
    
    public MongoClient getClient() {
        return mongo;
    }
    
    public MongoDatabase getDatabase() {
        return db;
    }
    
    //TODO: return collection
    public IOption<MongoCollection<Document>> getGameCollection() {
    	if (gameCollection == null)
    		 return new None<MongoCollection<Document>>();
    	return new Some<MongoCollection<Document>>(gameCollection);
    }
    
    //TODO: return collection
    public IOption<MongoCollection<Document>> getUserCollection() {
    	if (userCollection == null)
    		return new None<MongoCollection<Document>>();
    	return new Some<MongoCollection<Document>>(userCollection);
    }
    
    public MongoCollection<Document> getGameCol() {
    	return gameCollection;
    }
    
    public MongoCollection<Document> getUserCol() {
    	return userCollection;
    }
    
    public void dropCollections() {
        getGameCollection().visit(() -> "None found", x -> {
            x.drop();
            return "Collection destroyed.";
        });
        getUserCollection().visit(() -> "None found", x -> {
            x.drop();
            return "Collection destroyed.";
        });
    }
    
    public static Database getInstance() {
    	if (instance == null)
    		instance = new Database();
    	return instance;
    }
    
}
