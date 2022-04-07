import java.security.PrivateKey;



public class IssuerData {

    private String x500name;
    private PrivateKey privateKey;

    public IssuerData() {
    }

    public IssuerData(PrivateKey privateKey, String x500name) {
        this.privateKey = privateKey;
        this.x500name = x500name;
    }

    public String getX500name() {
        return x500name;
    }

    public void setX500name(String x500name) {
        this.x500name = x500name;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

}