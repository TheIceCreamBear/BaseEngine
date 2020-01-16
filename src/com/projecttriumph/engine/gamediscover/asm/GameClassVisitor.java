package com.projecttriumph.engine.gamediscover.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.projecttriumph.engine.gamediscover.GameCandidate;

public class GameClassVisitor extends ClassVisitor {
	private GameCandidate gc;
	private String clazz;
	
	public GameClassVisitor(GameCandidate gc) {
		super(Opcodes.ASM7);
		this.gc = gc;
	}
	

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.clazz = name;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		return new GameFieldVisitor();
	}


	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		return new GameMethodVisitor();
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		if (descriptor.equals("Lcom/projecttriumph/engine/api/game/Game;")) {
			gc.setGameClass(this.clazz);
			return new GameAnnotationVisitor(gc, true);
		} else {
			return new GameAnnotationVisitor(gc, false);
		}
	}
	
	public class GameMethodVisitor extends MethodVisitor {
		
		public GameMethodVisitor() {
			super(Opcodes.ASM7);
		}
	}
	
	public class GameFieldVisitor extends FieldVisitor {

		public GameFieldVisitor() {
			super(Opcodes.ASM7);
		}
	}
}