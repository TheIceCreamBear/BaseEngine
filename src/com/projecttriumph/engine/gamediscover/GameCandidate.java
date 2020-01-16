package com.projecttriumph.engine.gamediscover;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

import com.projecttriumph.engine.gamediscover.asm.GameClassVisitor;

public class GameCandidate {
	private static Pattern classFile = Pattern.compile("[^\\s\\$]+(\\$[^\\s]+)?\\.class$");
	private File location;
	private String gameClass = null;
	private String name = null;
	private String version = null;
	private Set<String> classes = new HashSet<String>();
	private List<String> pkgs = new ArrayList<String>();
	
	public GameCandidate(File location) {
		this.location = location;
	}
	
	public File getLocation() {
		return this.location;
	}
	
	public Set<String> getClasses() {
		return this.classes;
	}
	
	public List<GameCandidate> explore(List<GameCandidate> games) {
		JarFile jar = null;
		try {
			jar = new JarFile(location);
			for (ZipEntry ze : Collections.list(jar.entries())) {
				if (ze.getName() != null && ze.getName().startsWith("__MACOSX")) {
					continue;
                }
				Matcher matcher = classFile.matcher(ze.getName());
				if (matcher.matches()) {
					InputStream is = jar.getInputStream(ze);
					
					ClassReader reader = new ClassReader(is);
					reader.accept(new GameClassVisitor(this), 0);
					
					is.close();
					this.addClassEntry(ze.getName());
				}
			}
			if (this.gameClass != null && this.name != null && this.version != null) {
				games.add(this);
			}
		} catch (IOException e) {
			// Log issue with opening 
			e.printStackTrace();
		}
		
		return games;
	}
	
	public void setGameClass(String clazz) {
		if (this.gameClass == null) {
			this.gameClass = clazz;
		} else {
			// Throw error, shouldnt have more than one
		}
	}
	
	public String getGameClassName() {
		return Type.getObjectType(gameClass).getClassName();
	}
	
	public void setGameName(String name) {
		if (this.name == null) {
			this.name = name;
		} else {
			// Throw error, shouldnt have more than one
		}
	}
	
	public void setGameVersion(String version) {
		if (this.version == null) {
			this.version = version;
		} else {
			// Throw error, shouldnt have more than one
		}
	}
	
	public void addClassEntry(String name) {
		String className = name.substring(0, name.lastIndexOf('.')); // strip the .class
		classes.add(className);
		className = className.replace('/', '.');
		int pkgIdx = className.lastIndexOf('.');
		if (pkgIdx > -1) {
			String pkg = className.substring(0, pkgIdx);
			pkgs.add(pkg);
		}
	}
	
	@Override
	public String toString() {
		return "\"" + this.name + "\"-v" + this.version + " @" + this.location.getAbsolutePath();
	}
}