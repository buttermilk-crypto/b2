/*
 *  This file is part of Bracket Properties
 *  Copyright 2014-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * <p>Support for obfuscated properties. You can set a password for further security,
 * but remember you must also have that value available to deobfuscate, and there is no
 * password recovery functionality.</p>
 * 
 * @author Dave
 *
 */
public final class Obfuscater {
	
	final byte [] salt = {0x23,0x42,0x10,0x37,0x08,0x77,0x55,0x19};
	final char [] defaultPassword = {
			't','g','A','6','Q','u','/', 'g','1','8','y','G',
			't','L','G','d','z','4','l','D','v','O','s','U','J','A','j',
			'o','9','i','z','o','X','i','g','8','X','F','E','P','f','l','I'
	};
	
	// should work on most java 6 or better installs without import restrictions coming into play
	final int KEY_LENGTH = 128;
	
	final int ITERATIONS = 65536;
	
	final char[] password;
	
	public Obfuscater(char[] password) {
		super();
		this.password = password;
	}
	
	public Obfuscater(){
		password = defaultPassword;
	}

	public final String decrypt(String inBase64){
		byte [] all = javax.xml.bind.DatatypeConverter.parseBase64Binary(inBase64);
		byte [] iv = new byte[16];
		System.arraycopy(all,0,iv,0,16);
		
		byte [] ciphertext = new byte[all.length-16];
		System.arraycopy(all, 16, ciphertext, 0, all.length-16);
		
		try {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(defaultPassword, salt, ITERATIONS, KEY_LENGTH);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		return new String(cipher.doFinal(ciphertext), "UTF-8");
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	public final byte [] decryptToBytes(String inBase64){
		byte [] all = javax.xml.bind.DatatypeConverter.parseBase64Binary(inBase64);
		byte [] iv = new byte[16];
		System.arraycopy(all,0,iv,0,16);
		
		byte [] ciphertext = new byte[all.length-16];
		System.arraycopy(all, 16, ciphertext, 0, all.length-16);
		
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(defaultPassword, salt, ITERATIONS, KEY_LENGTH);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			return cipher.doFinal(ciphertext);
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	public final char [] decryptToChar(String inBase64){
		return decryptToChar(inBase64, Charset.forName("UTF-8"));
	}
	
	public final char [] decryptToChar(String inBase64, Charset charset){
		byte [] all = javax.xml.bind.DatatypeConverter.parseBase64Binary(inBase64);
		byte [] iv = new byte[16];
		System.arraycopy(all,0,iv,0,16);
		
		byte [] ciphertext = new byte[all.length-16];
		System.arraycopy(all, 16, ciphertext, 0, all.length-16);
		
		try {
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(defaultPassword, salt, ITERATIONS, KEY_LENGTH);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			byte [] result = cipher.doFinal(ciphertext);
			ByteBuffer buf = ByteBuffer.wrap(result);
			CharBuffer cBuf = charset.decode(buf);
			return cBuf.array();
			
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	public final String encrypt(char [] in, Charset charset){
		CharBuffer cBuf = CharBuffer.wrap(in);
		ByteBuffer result = charset.encode(cBuf);
		return encrypt(result.array());
	}
	
	// assume UTF-8
	public final String encrypt(char [] in){
		CharBuffer cBuf = CharBuffer.wrap(in);
		ByteBuffer result = Charset.forName("UTF-8").encode(cBuf);
		return encrypt(result.array());
	}
	
	public final String encrypt(byte [] in){
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(defaultPassword, salt, ITERATIONS, KEY_LENGTH);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			
			byte[] ciphertext = cipher.doFinal(in);
			byte[] all = new byte[iv.length+ciphertext.length];
			
			System.arraycopy(iv, 0, all, 0, iv.length);
			System.arraycopy(ciphertext, 0, all, iv.length, ciphertext.length);
			
			return javax.xml.bind.DatatypeConverter.printBase64Binary(all);
			
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	public final String encrypt(String in){
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(defaultPassword, salt, ITERATIONS, KEY_LENGTH);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			
			byte[] ciphertext = cipher.doFinal(in.getBytes("UTF-8"));
			byte[] all = new byte[iv.length+ciphertext.length];
			
			System.arraycopy(iv, 0, all, 0, iv.length);
			System.arraycopy(ciphertext, 0, all, iv.length, ciphertext.length);
			
			String body = javax.xml.bind.DatatypeConverter.printBase64Binary(all);
			
			return body;
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	/**
	 * Simple main method for utility use case. 
	 * Pass in values on the command line and they will be emitted in obfuscated form
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length == 0) {
			System.err.println("Please provide an argument, the string to be obfuscated");
			return;
		}
		
		Obfuscater o = new Obfuscater();
		for(String in: args){
			String s = o.encrypt(in);
			System.out.println(s);
		}
	}
}
