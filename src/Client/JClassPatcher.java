/**
 * rsctimes
 *
 * <p>This file is part of rsctimes.
 *
 * <p>rsctimes is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>rsctimes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with rsctimes. If not,
 * see <http://www.gnu.org/licenses/>.
 *
 * <p>Authors: see <https://github.com/RSCPlus/rsctimes>
 */
package Client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class JClassPatcher {

  // Singleton
  private static JClassPatcher instance = null;

  public static List<String> ExceptionSignatures = new ArrayList<String>();
  public static List<String> InstructionBytecode = new ArrayList<String>();

  private Printer printer = new Textifier();
  private TraceMethodVisitor mp = new TraceMethodVisitor(printer);

  private JClassPatcher() {
    // Empty private constructor to prevent extra instances from being created.
  }

  public byte[] patch(byte data[]) {
    ClassReader reader = new ClassReader(data);
    ClassNode node = new ClassNode();
    reader.accept(node, ClassReader.SKIP_DEBUG);

    /*if(node.name.equals("ua"))
    {
    	patchRenderer(node);
    }
    else if(node.name.equals("lb"))
    {
    	patchCamera(node);
    }
    else */ if (node.name.equals("jagex/client/k")) {
      patchApplet(node);
    } else if (node.name.equals("mudclient")) {
      patchClient(node);
    } else if (node.name.equals("jagex/client/d")) {
      patchFrame(node);
    } else if (node.name.equals("jagex/o")) {
      patchUtility(node);
    }

    patchGeneric(node);

    dumpClass(node);

    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    node.accept(writer);
    return writer.toByteArray();
  }

  private void patchGeneric(ClassNode node) {
    Iterator<MethodNode> methodNodeList = node.methods.iterator();

    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      // General byte patch
      Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();

      hookClassVariable(
          methodNode,
          "jagex/client/e",
          "ad",
          "I",
          "Game/Client",
          "connection_port",
          "I",
          true,
          true);
    }
  }

  private void patchApplet(ClassNode node) {
    Logger.Info("Patching applet (" + node.name + ".class)");

    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      if (methodNode.name.equals("run") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode startNode;

          if (insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL
              && ((MethodInsnNode) insnNode).name.equals("showStatus")) {
            startNode = insnNode;
            methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.POP));
            methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.POP));
            methodNode.instructions.remove(startNode);
            break;
          }
        }
      }
    }
  }

  private void patchFrame(ClassNode node) {
    Logger.Info("Patching frame (" + node.name + ".class)");

    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();
    }
  }

  private void patchClient(ClassNode node) {
    Logger.Info("Patching client (" + node.name + ".class)");

    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      if (methodNode.name.equals("yi") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Client.init patch
          AbstractInsnNode findNode = methodNode.instructions.getFirst();
          methodNode.instructions.insertBefore(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
          methodNode.instructions.insertBefore(
              findNode,
              new FieldInsnNode(
                  Opcodes.PUTSTATIC, "Game/Client", "instance", "Ljava/lang/Object;"));
          methodNode.instructions.insertBefore(
              findNode,
              new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "init", "()V", false));
        }
      }
    }
  }

  private void patchCamera(ClassNode node) {
    Logger.Info("Patching camera (" + node.name + ".class)");

    /*Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while(methodNodeList.hasNext())
    {
    	MethodNode methodNode = methodNodeList.next();

    	hookClassVariable(methodNode, "lb", "Mb", "I", "Game/Camera", "distance1", "I", false, true);
    	hookClassVariable(methodNode, "lb", "X", "I", "Game/Camera", "distance2", "I", false, true);
    	hookClassVariable(methodNode, "lb", "P", "I", "Game/Camera", "distance3", "I", false, true);
    	hookClassVariable(methodNode, "lb", "G", "I", "Game/Camera", "distance4", "I", false, true);
    }*/
  }

  private void patchRenderer(ClassNode node) {
    Logger.Info("Patching renderer (" + node.name + ".class)");
  }

  private void patchUtility(ClassNode node) {
    Logger.Info("Patching utility (" + node.name + ".class)");

    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      // Patch data read stream
      if (methodNode.desc.equals("(Ljava/lang/String;)Ljava/io/InputStream;")) {
        AbstractInsnNode first = methodNode.instructions.getFirst();

        methodNode.instructions.insertBefore(first, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            first,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "Game/Utility",
                "openStream",
                "(Ljava/lang/String;)Ljava/io/InputStream;"));
        methodNode.instructions.insertBefore(first, new VarInsnNode(Opcodes.ASTORE, 1));
        methodNode.instructions.insertBefore(first, new VarInsnNode(Opcodes.ALOAD, 1));
        methodNode.instructions.insertBefore(first, new InsnNode(Opcodes.ARETURN));
      }
    }
  }

  private void hookClassVariable(
      MethodNode methodNode,
      String owner,
      String var,
      String desc,
      String newClass,
      String newVar,
      String newDesc,
      boolean canRead,
      boolean canWrite) {
    Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
    while (insnNodeList.hasNext()) {
      AbstractInsnNode insnNode = insnNodeList.next();

      int opcode = insnNode.getOpcode();
      if (opcode == Opcodes.GETFIELD || opcode == Opcodes.PUTFIELD) {
        FieldInsnNode field = (FieldInsnNode) insnNode;
        if (field.owner.equals(owner) && field.name.equals(var) && field.desc.equals(desc)) {
          if (opcode == Opcodes.GETFIELD && canWrite) {
            FieldInsnNode newField =
                new FieldInsnNode(Opcodes.GETSTATIC, newClass, newVar, newDesc);
            InsnNode pop = new InsnNode(Opcodes.POP);
            methodNode.instructions.insert(insnNode, newField);
            methodNode.instructions.insert(insnNode, pop);
          } else if (opcode == Opcodes.PUTFIELD && canRead) {
            InsnNode dup = new InsnNode(Opcodes.DUP_X1);
            FieldInsnNode newPut = new FieldInsnNode(Opcodes.PUTSTATIC, newClass, newVar, newDesc);
            methodNode.instructions.insertBefore(insnNode, dup);
            methodNode.instructions.insert(insnNode, newPut);
          }
        }
      }
    }
  }

  private void hookStaticVariable(
      MethodNode methodNode,
      String owner,
      String var,
      String desc,
      String newClass,
      String newVar,
      String newDesc,
      boolean canRead,
      boolean canWrite) {
    Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
    while (insnNodeList.hasNext()) {
      AbstractInsnNode insnNode = insnNodeList.next();

      int opcode = insnNode.getOpcode();
      if (opcode == Opcodes.GETSTATIC || opcode == Opcodes.PUTSTATIC) {
        FieldInsnNode field = (FieldInsnNode) insnNode;
        if (field.owner.equals(owner) && field.name.equals(var) && field.desc.equals(desc)) {
          if (opcode == Opcodes.GETSTATIC && canWrite) {
            field.owner = newClass;
            field.name = newVar;
            field.desc = newDesc;
          } else if (opcode == Opcodes.PUTSTATIC && canRead) {
            field.owner = newClass;
            field.name = newVar;
            field.desc = newDesc;
          }
        }
      }
    }
  }

  private void dumpClass(ClassNode node) {
    BufferedWriter writer = null;

    try {
      File file = new File(/*Settings.Dir.DUMP +*/ "/" + node.name + ".dump");
      writer = new BufferedWriter(new FileWriter(file));

      writer.write(decodeAccess(node.access) + node.name + " extends " + node.superName + ";\n");
      writer.write("\n");

      Iterator<FieldNode> fieldNodeList = node.fields.iterator();
      while (fieldNodeList.hasNext()) {
        FieldNode fieldNode = fieldNodeList.next();
        writer.write(
            decodeAccess(fieldNode.access) + fieldNode.desc + " " + fieldNode.name + ";\n");
      }

      writer.write("\n");

      Iterator<MethodNode> methodNodeList = node.methods.iterator();
      while (methodNodeList.hasNext()) {
        MethodNode methodNode = methodNodeList.next();
        writer.write(
            decodeAccess(methodNode.access) + methodNode.name + " " + methodNode.desc + ":\n");

        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          String instruction = decodeInstruction(insnNode);
          writer.write(instruction);
        }
        writer.write("\n");
      }

      writer.close();
    } catch (Exception e) {
      try {
        writer.close();
      } catch (Exception e2) {
      }
    }
  }

  private String decodeAccess(int access) {
    String res = "";

    if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) res += "public ";
    if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) res += "private ";
    if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) res += "protected ";

    if ((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC) res += "static ";
    if ((access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL) res += "final ";
    if ((access & Opcodes.ACC_VOLATILE) == Opcodes.ACC_VOLATILE) res += "protected ";
    if ((access & Opcodes.ACC_SYNCHRONIZED) == Opcodes.ACC_SYNCHRONIZED) res += "synchronized ";
    if ((access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT) res += "abstract ";
    if ((access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE) res += "interface ";

    return res;
  }

  private String decodeInstruction(AbstractInsnNode insnNode) {
    Printer printer = new Textifier();
    TraceMethodVisitor mp = new TraceMethodVisitor(printer);

    insnNode.accept(mp);
    StringWriter sw = new StringWriter();
    printer.print(new PrintWriter(sw));
    printer.getText().clear();
    return sw.toString();
  }

  public static JClassPatcher getInstance() {
    if (instance == null) {
      synchronized (JClassPatcher.class) {
        instance = new JClassPatcher();
      }
    }
    return instance;
  }
}
