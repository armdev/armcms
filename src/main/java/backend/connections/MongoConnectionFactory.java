package backend.connections;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import java.io.Serializable;
import java.net.UnknownHostException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(eager = true, name = "MongoConnectionFactory")
@ApplicationScoped
public class MongoConnectionFactory implements Serializable {

    private MongoOptions mongoOptions;
    private MongoOptionsFactory factory;

    public MongoConnectionFactory() {
        mongoOptions = new MongoOptions();
        factory = new MongoOptionsFactory();
        factory.setAutoConnectRetry(true);
        factory.setConnectionsPerHost(10000);
        factory.setConnectionTimeout(11);
        //factory.setMaxWaitTime(10);
       // factory.setSocketTimeOut(23);
       // factory.setThreadsAllowedToBlockForConnectionMultiplier(31);
        mongoOptions = factory.createMongoOptions();

    }

    public Mongo createMongoInstance() {
        Mongo mongo = null;
        try {
            ServerAddress serverAddress = new ServerAddress("localhost", 27017);
            mongo = new Mongo(serverAddress, mongoOptions);
            mongo.setWriteConcern(WriteConcern.SAFE);
        } catch (UnknownHostException e) {
            throw new MongoInitializationException("Could not create the default Mongo instance", e);
        }
        return mongo;
    }
}
