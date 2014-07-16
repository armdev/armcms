package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.FileEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import frontend.web.utils.Util;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * @author Armen Arzumanyan
 */
@ApplicationScoped
@ManagedBean(name = "FileManagerFacade")
public class FileManagerFacade extends DbInitBean implements Serializable {

    private GridFS gfsPhoto;
    private SecureRandom random = new SecureRandom();   

    public FileManagerFacade() {
    }

    private String getFileName() {
        return new BigInteger(60, random).toString(16);
    }

    public String addFile(FileEntity file) {
        String fileName = file.getTitle() + "-" + System.currentTimeMillis();
        try {
            gfsPhoto = new GridFS(getDatabase(), "armcms_imagetable");

            InputStream in = new ByteArrayInputStream(file.getContent());
            BufferedImage originalImage = ImageIO.read(in);

            BufferedImage thumbnail = Thumbnails.of(originalImage)
                    .size(400, 400)
                    .asBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            GridFSInputFile gfsFile = gfsPhoto.createFile(imageInByte);
            Long fileId = (Util.longValue(getNextId(getDatabase(), "imagesGenSeq")));
            gfsFile.setId(fileId);
            gfsFile.setContentType(file.getMimetype());
            gfsFile.setFilename(fileName);
            gfsFile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public GridFSDBFile getFile(String fileName) {
        gfsPhoto = new GridFS(getDatabase(), "armcms_imagetable");
        GridFSDBFile imageForOutput = gfsPhoto.findOne(fileName);
        return imageForOutput;
    }

    public void removeFile(Long fileId) {
        BasicDBObject document = new BasicDBObject();
        document.put("id", fileId);
        getFileCollection().remove(document);
    }
   
    
    protected static String getNextId(DB db, String seq_name) {
        String sequence_collection = "seq"; // the name of the sequence collection
        String sequence_field = "seq"; // the name of the field which holds the sequence
        DBCollection seq = db.getCollection(sequence_collection); // get the collection (this will create it if needed)       
        if (seq == null) {
            seq = db.createCollection(sequence_collection, null);
        }
        // this object represents your "query", its analogous to a WHERE clause in SQL
        DBObject query = new BasicDBObject();
        query.put("id", seq_name); // where id = the input sequence name
        // this object represents the "update" or the SET blah=blah in SQL
        DBObject change = new BasicDBObject(sequence_field, 1);
        DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment
        // Atomically updates the sequence field and returns the value for you
        DBObject res = seq.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
        return res.get(sequence_field).toString();
    }
}
