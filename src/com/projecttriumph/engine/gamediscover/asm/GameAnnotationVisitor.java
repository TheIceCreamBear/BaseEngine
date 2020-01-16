package com.projecttriumph.engine.gamediscover.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import com.projecttriumph.engine.gamediscover.GameCandidate;

public class GameAnnotationVisitor extends AnnotationVisitor {
	private GameCandidate gc;
	private boolean isGame;
	
	public GameAnnotationVisitor(GameCandidate gc, boolean isGame) {
		super(Opcodes.ASM7);
		this.gc = gc;
		this.isGame = isGame;
	}

	@Override
	public void visit(String name, Object value) {
		if (isGame) {
			if (name.equals("gameName")) {
				gc.setGameName((String) value);
			} else if (name.equals("version")) {
				gc.setGameVersion((String) value);
			}
		}
	}
}