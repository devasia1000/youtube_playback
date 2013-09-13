
/**
 *
 * @author devasia
 */
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class MediaStore {

    String clen;
    String type;
    File file;

    public MediaStore(File f) {
        try {

            String name = f.getName();
            String el[] = name.split("-");
            clen = el[0];
            type = el[1].replace(".mp4", "");

            if (!f.exists()) {
                System.err.println("media file does not exist");
                System.exit(-1);
            }

            file=f;

        } catch (Exception e) {
            System.err.println("could not read media file properly");
            System.exit(-1);
        }
    }

    public String getClen() {
        return clen;
    }

    public String getType() {
        return type;
    }

    public byte[] processRangeRequest(int fin, int init) {
        if (init >= fin) {
            System.err.println("incorrect range request");
            System.exit(-1);
        }

        byte[] dat=null;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            in.skip(init - 1);
            dat = new byte[fin + 1 - init];
            in.readFully(dat);
            in.close();
        } catch (Exception e) {
            System.err.println("could not process range request");
            e.printStackTrace();
        }
        
        return dat;
    }
}
