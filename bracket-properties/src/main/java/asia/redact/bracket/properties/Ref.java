package asia.redact.bracket.properties;

public interface Ref {
	
	static final String REF_TOKEN = "_$";
	
	public String getKeyRef(String key);
	
	public void putKeyRef(String newRefKey, String existingKey);

}
