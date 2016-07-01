package asia.redact.bracket.properties.adapter;

public interface Sec {

	// obfuscation
	public void obfuscate(String key);
	public void deobfuscate(String key);
	public char[] deobfuscateToChar(String key);
	
}
