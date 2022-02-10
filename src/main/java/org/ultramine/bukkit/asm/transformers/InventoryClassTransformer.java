package org.ultramine.bukkit.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class InventoryClassTransformer {
	public static byte[] transformClass(byte[] basicClass) {
		ClassNode cNode = new ClassNode();
		new ClassReader(basicClass).accept(cNode, 0);
		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		if (!cNode.interfaces.contains("org/ultramine/bukkit/api/IInventoryTransactionProvider")) {
			// implements IInventoryTransactionProvider
			cNode.interfaces.add("org/ultramine/bukkit/api/IInventoryTransactionProvider");

			// private List<HumanEntity> transaction;
			cWriter.visitField(ACC_PUBLIC, "transaction", "Ljava/util/List;",
					"Ljava/util/List<Lorg/bukkit/entity/HumanEntity;>;", null).visitEnd();

			// public void onOpen(CraftHumanEntity craftHumanEntity)
			// {
			// if (this.transaction == null)
			// {
			// this.transaction = new ArrayList<HumanEntity>(1);
			// }
			// this.transaction.add((HumanEntity)craftHumanEntity);
			// }
			MethodVisitor mv = cWriter.visitMethod(ACC_PUBLIC, "onOpen",
					"(Lorg/bukkit/craftbukkit/v1_7_R4/entity/CraftHumanEntity;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cNode.name, "transaction", "Ljava/util/List;");
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "(I)V", false);
			mv.visitFieldInsn(PUTFIELD, cNode.name, "transaction", "Ljava/util/List;");
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cNode.name, "transaction", "Ljava/util/List;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			mv.visitInsn(RETURN);

			// public void onClose(CraftHumanEntity craftHumanEntity)
			// {
			// if (this.transaction != null)
			// {
			// this.transaction.remove(craftHumanEntity);
			// if (this.transaction.isEmpty())
			// {
			// this.transaction = null;
			// }
			// }
			// }
			mv = cWriter.visitMethod(ACC_PUBLIC, "onClose",
					"(Lorg/bukkit/craftbukkit/v1_7_R4/entity/CraftHumanEntity;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cNode.name, "transaction", "Ljava/util/List;");
			Label t1 = new Label();
			mv.visitJumpInsn(IFNULL, t1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cNode.name, "transaction", "Ljava/util/List;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "remove", "(Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cNode.name, "transaction", "Ljava/util/List;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "isEmpty", "()Z", true);
			mv.visitJumpInsn(IFEQ, t1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, cNode.name, "transaction", "Ljava/util/List;");
			mv.visitLabel(t1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitEnd();

			// public List<HumanEntity> getViewers()
			// {
			// if (this.transaction == null)
			// {
			// return Collections.emptyList();
			// }
			// return this.transaction;
			// }
			mv = cWriter.visitMethod(ACC_PUBLIC, "getViewers", "()Ljava/util/List;",
					"()Ljava/util/List<Lorg/bukkit/entity/HumanEntity;>;", null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cNode.name, "transaction", "Ljava/util/List;");
			Label k1 = new Label();
			mv.visitJumpInsn(IFNONNULL, k1);
			mv.visitMethodInsn(INVOKESTATIC, "java/util/Collections", "emptyList", "()Ljava/util/List;", false);
			mv.visitInsn(ARETURN);
			mv.visitLabel(k1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cNode.name, "transaction", "Ljava/util/List;");
			mv.visitInsn(ARETURN);

			mv.visitEnd();
		}
		cNode.accept(cWriter);
		return cWriter.toByteArray();
	}
}
