

/**
 *
 * @author devasia
 */

public class HTTPResponse {
    
    private byte[] data;
    
    HTTPResponse(byte[] b){
        data=b;
    }
    
    public byte[] returnData(){
        return data;
    }
    
    @Override
    public String toString(){
        return new String(data);
    }
}
