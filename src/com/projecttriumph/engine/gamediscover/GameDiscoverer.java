package com.projecttriumph.engine.gamediscover;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameDiscoverer {
	private static Pattern jarPat = Pattern.compile("(.+).(zip|jar)$");
	private List<GameCandidate> candidates;
	
	public GameDiscoverer() {
		this.candidates = new ArrayList<GameCandidate>();
	}
	
	public void findGamesInDir(File gameDir) {
		File[] gameFiles = gameDir.listFiles();
		Arrays.sort(gameFiles, new Comparator<File>() {
			public int compare(File o1, File o2) {
	            return o1 != null && o2 != null ? o1.getName().compareToIgnoreCase(o2.getName()) : o1 == null ? -1 : 1;
	        }
		});
		
		for (File gameFile : gameFiles) {
			Matcher matcher = jarPat.matcher(gameFile.getName());
			if (matcher.matches()) {
				addCandidate(new GameCandidate(gameFile));
			}
		}
	}
	
	public List<GameCandidate> identifyValid() {
		List<GameCandidate> games = new ArrayList<GameCandidate>();
		
		for (GameCandidate candidate : candidates) {
			candidate.explore(games);
		}
		
		return games;
	}
	
	public void addCandidate(GameCandidate candidate) {
		for (GameCandidate c : candidates) {
			if (c.getLocation() == candidate.getLocation()) {
				return;
			}
		}
		candidates.add(candidate);
	}
	
	public void listCandidates() {
		for (GameCandidate c : candidates) {
			System.out.println(c.getLocation());
		}
	}
}