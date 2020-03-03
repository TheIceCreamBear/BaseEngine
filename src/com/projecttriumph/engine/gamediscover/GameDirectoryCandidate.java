package com.projecttriumph.engine.gamediscover;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import org.objectweb.asm.ClassReader;

import com.projecttriumph.engine.gamediscover.asm.GameClassVisitor;

public class GameDirectoryCandidate extends GameCandidate {
	private HashMap<File, String> files = new HashMap<File, String>();
	
	public GameDirectoryCandidate(File location) {
		super(location);
		this.discoverFiles(location, null);
	}
	
	private void discoverFiles(File dir, String packag) {
		File[] contents = dir.listFiles();
		
		if (contents == null) {
			return;
		}
		
		if (packag == null) {
			packag = "";
		}
		
		for (File file : contents) {
			if (file.isDirectory()) {
				discoverFiles(file, packag + file.getAbsolutePath().substring(dir.getAbsolutePath().length() + 1) + "/");
			} else {
				files.put(file, packag);
			}
		}
	}
	
	public List<GameCandidate> explore(List<GameCandidate> games) {
		try {
			for (File f : files.keySet()) {
				if (f.getName() != null && f.getName().startsWith("__MACOSX")) {
					continue;
                }
				Matcher matcher = classFile.matcher(f.getName());
				if (matcher.matches()) {
					InputStream is = new FileInputStream(f);
					
					ClassReader reader = new ClassReader(is);
					reader.accept(new GameClassVisitor(this), 0);
					
					is.close();
					this.addClassEntry(files.get(f) + f.getName());
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
}