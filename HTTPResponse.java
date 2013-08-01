
/**
 *
 * @author devasia
 */
import java.util.Arrays;

public class HTTPResponse {

    private byte[] data;
    private String headers;
    private byte content[];

    HTTPResponse(byte[] b) {
        data = b;

        int contentStartPos = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 10 && data[i - 1] == 13 && data[i - 2] == 10 && data[i - 3] == 13) {
                contentStartPos = i + 1;
                break;
            }
        }

        /* header array is fine */
        byte[] head = new byte[contentStartPos];
        for (int i = 0; i < contentStartPos; i++) {
            head[i] = data[i];
        }
        headers = new String(head);

        content = new byte[data.length - contentStartPos];
        for (int i = contentStartPos, j = 0; i < data.length; i++, j++) {
            content[j] = data[i];
        }

        byte[] result = Arrays.copyOf(headers.getBytes(), headers.getBytes().length + content.length);
        System.arraycopy(content, 0, result, headers.getBytes().length, content.length);

        /* checking if arrays are the same */
        /*
         System.out.println(result==data);
        
         for(int i=0;i<result.length;i++){
         if(result[i]!=data[i]){
         System.out.println("false1");
         } 
         }*/
    }

    public byte[] returnTotalData() {
        return data;
    }

    public byte[] returnContent() {
        return content;
    }

    public String returnHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return new String(data);
    }

    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
