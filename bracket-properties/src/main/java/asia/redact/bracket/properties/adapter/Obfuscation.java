package asia.redact.bracket.properties.adapter;

public interface Obfuscation {

	// obfuscation
	public void obfuscate(String key);
	public void deobfuscate(String key);
	public char[] deobfuscateToChar(String key);
	
	// for additional security you can supply a password, but it must be available for deobfuscate as well
	public void obfuscate(char[]password, String key);
	public void deobfuscate(char[]password, String key);
	public char[] deobfuscateToChar(char[]password,String key);
}
