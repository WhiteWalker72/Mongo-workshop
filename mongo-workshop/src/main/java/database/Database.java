package database;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import option.IOption;
import option.None;

public class Database {
    
    private final MongoClient mongo;
    private final MongoDatabase db;
    
    public Database() {
        this.mongo = new MongoClient("localhost", 27017);
        this.db = mongo.getDatabase("workshop");
        //TODO: initialise collections
    }
    
    public MongoClient getClient() {
        return mongo;
    }
    
    public MongoDatabase getDatabase() {
        return db;
    }
    
    //TODO: return collection
    public IOption<MongoCollection<Document>> getGameCollection() {
        return new None<MongoCollection<Document>>();
    }
    
    //TODO: return collection
    public IOption<MongoCollection<Document>> getUserCollection() {
        return new None<MongoCollection<Document>>();
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
    
}
