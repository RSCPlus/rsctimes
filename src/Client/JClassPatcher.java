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
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
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

    if(node.name.equals("jagex/client/i"))
    {
    	patchRenderer(node);
    }
    /*else if(node.name.equals("lb"))
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
      
      hookClassVariable(
              methodNode, "mudclient", "bz", "I", "Game/Client", "login_screen", "I", true, true);
      
      hookClassVariable(methodNode, "jagex/client/j", "oo", "[I", "Game/Renderer", "pixels", "[I", true, true);
      
      hookClassVariable(
              methodNode,
              "mudclient",
              "pt",
              "Ljagex/client/j;",
              "Game/Camera",
              "instance",
              "Ljava/lang/Object;",
              true,
              false);
      
      hookClassVariable(
              methodNode,
              "mudclient",
              "qt",
              "Lm;",
              "Game/Renderer",
              "instance",
              "Ljava/lang/Object;",
              true,
              false);
      
      hookClassVariable(
              methodNode,
              "mudclient",
              "ot",
              "Ljava/awt/Graphics;",
              "Game/Renderer",
              "graphicsInstance",
              "Ljava/awt/Graphics;",
              true,
              false);
      
      hookClassVariable(
              methodNode,
              "jagex/client/i",
              "zj",
              "Ljava/awt/image/ImageConsumer;",
              "Game/Renderer",
              "image_consumer",
              "Ljava/awt/image/ImageConsumer;",
              true,
              true);
      hookClassVariable(methodNode, "jagex/client/i", "sj", "I", "Game/Renderer", "width", "I", false, true);
      hookClassVariable(methodNode, "jagex/client/i", "tj", "I", "Game/Renderer", "height", "I", false, true);
      hookClassVariable(methodNode, "jagex/client/i", "yj", "[I", "Game/Renderer", "pixels", "[I", true, true);
      
      hookClassVariable(methodNode, "m", "sj", "I", "Game/Renderer", "width", "I", false, true);
      hookClassVariable(methodNode, "m", "tj", "I", "Game/Renderer", "height", "I", false, true);
      hookClassVariable(methodNode, "m", "yj", "[I", "Game/Renderer", "pixels", "[I", true, true);
      
      hookClassVariable(
              methodNode, "mudclient", "bu", "I", "Game/Renderer", "width", "I", false, true);
          hookClassVariable(
              methodNode, "mudclient", "cu", "I", "Game/Renderer", "height_client", "I", false, true);
      
      hookClassVariable(methodNode, "jagex/client/k", "hp", "I", "Game/Renderer", "width", "I", false, true);
      hookClassVariable(methodNode, "jagex/client/k", "ip", "I", "Game/Renderer", "height", "I", false, true);
      
      // Chat menu
      hookClassVariable(
          methodNode,
          "mudclient",
          "kz",
          "Ljagex/client/g;",
          "Game/Menu",
          "chat_menu",
          "Ljava/lang/Object;",
          true,
          false);
      hookClassVariable(
          methodNode, "mudclient", "lz", "I", "Game/Menu", "chat_type1", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "mz", "I", "Game/Menu", "chat_input", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "nz", "I", "Game/Menu", "chat_type2", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "oz", "I", "Game/Menu", "chat_type3", "I", true, false);

      // Friends menu
      hookClassVariable(
          methodNode,
          "mudclient",
          "hcb",
          "Ljagex/client/g;",
          "Game/Menu",
          "friend_menu",
          "Ljava/lang/Object;",
          true,
          false);
      hookClassVariable(
          methodNode, "mudclient", "icb", "I", "Game/Menu", "friend_handle", "I", true, false);

      // Spell menu
      hookClassVariable(
          methodNode,
          "mudclient",
          "ecb",
          "Ljagex/client/g;",
          "Game/Menu",
          "spell_menu",
          "Ljava/lang/Object;",
          true,
          false);
      hookClassVariable(
          methodNode, "mudclient", "fcb", "I", "Game/Menu", "spell_handle", "I", true, false);
      
      // Game data hooks
      hookStaticVariableClone(
          methodNode,
          "r",
          "vfb",
          "[[Ljava/lang/String;",
          "Game/JGameData",
          "objectNames",
          "[[Ljava/lang/String;");
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
      if (methodNode.name.equals("u") && methodNode.desc.equals("()V")) {
          AbstractInsnNode lastNode = methodNode.instructions.getLast().getPrevious();

          // Send combat style option
          /*LabelNode label = new LabelNode();

          methodNode.instructions.insert(lastNode, label);

          // Format
          methodNode.instructions.insert(
              lastNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "da", "b", "(I)V", false));
          methodNode.instructions.insert(lastNode, new IntInsnNode(Opcodes.SIPUSH, 21294));
          methodNode.instructions.insert(
              lastNode, new FieldInsnNode(Opcodes.GETFIELD, "client", "Jh", "Lda;"));
          methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

          // Write byte
          methodNode.instructions.insert(
              lastNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "ja", "c", "(II)V", false));
          methodNode.instructions.insert(lastNode, new IntInsnNode(Opcodes.BIPUSH, -80));
          methodNode.instructions.insert(
              lastNode,
              new FieldInsnNode(Opcodes.GETSTATIC, "Client/Settings", "COMBAT_STYLE_INT", "I"));
          methodNode.instructions.insert(
              lastNode,
              new MethodInsnNode(
                  Opcodes.INVOKESTATIC,
                  "Client/Settings",
                  "updateInjectedVariables",
                  "()V",
                  false)); // TODO Remove this line when COMBAT_STYLE_INT is eliminated
          methodNode.instructions.insert(
              lastNode, new FieldInsnNode(Opcodes.GETFIELD, "da", "f", "Lja;"));
          methodNode.instructions.insert(
              lastNode, new FieldInsnNode(Opcodes.GETFIELD, "client", "Jh", "Lda;"));
          methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

          // Create Packet
          methodNode.instructions.insert(
              lastNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "da", "b", "(II)V", false));
          methodNode.instructions.insert(lastNode, new InsnNode(Opcodes.ICONST_0));
          methodNode.instructions.insert(lastNode, new IntInsnNode(Opcodes.BIPUSH, 29));
          methodNode.instructions.insert(
              lastNode, new FieldInsnNode(Opcodes.GETFIELD, "client", "Jh", "Lda;"));
          methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

          // Skip combat packet if style is already controlled
          methodNode.instructions.insert(lastNode, new JumpInsnNode(Opcodes.IF_ICMPLE, label));
          methodNode.instructions.insert(lastNode, new InsnNode(Opcodes.ICONST_0));
          methodNode.instructions.insert(
              lastNode,
              new FieldInsnNode(Opcodes.GETSTATIC, "Client/Settings", "COMBAT_STYLE_INT", "I"));
          methodNode.instructions.insert(
              lastNode,
              new MethodInsnNode(
                  Opcodes.INVOKESTATIC,
                  "Client/Settings",
                  "updateInjectedVariables",
                  "()V",
                  false)); // TODO Remove this line when COMBAT_STYLE_INT is eliminated*/

          // Client init_game
          methodNode.instructions.insert(
              lastNode,
              new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "init_game", "()V", false));
        }
      if (methodNode.name.equals("v") && methodNode.desc.equals("()V")) {
          // Client.init_login patch
          AbstractInsnNode findNode = methodNode.instructions.getLast();
          methodNode.instructions.insertBefore(
              findNode,
              new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "init_login", "()V", false));
        }
      if (methodNode.name.equals("tl") && methodNode.desc.equals("(Z)V")) {
    	  // Patch Remove social
    	  int timesFound = 0;
    	  AbstractInsnNode findNode = methodNode.instructions.getFirst();
    	  while (timesFound < 2) {
    		  while (!(findNode.getOpcode() == Opcodes.LDC && ((LdcInsnNode) findNode).cst.equals("~439~@whi@Remove         WWWWWWWWWW"))) {
    	          findNode = findNode.getNext();
    	        }
    		  methodNode.instructions.insert(
    		            findNode,
    		            new MethodInsnNode(
    		                Opcodes.INVOKESTATIC,
    		                "Game/Renderer",
    		                "getFixedRemoveString",
    		                "(Ljava/lang/String;)Ljava/lang/String;",
    		                false));
    		  findNode = findNode.getNext();
    		  timesFound++;
    	  }
    	  
    	  Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
          while (insnNodeList.hasNext()) {
            AbstractInsnNode insnNode = insnNodeList.next();
            
            // Patch positions
            if (insnNode.getOpcode() == Opcodes.SIPUSH) {
                IntInsnNode call = (IntInsnNode) insnNode;
                if (call.operand == 489 || call.operand == 429) {
                  call.operand = 512 - call.operand;
                  methodNode.instructions.insertBefore(
                      insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
                  methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
                }
              }
          }
    	  
      }
      if (methodNode.name.equals("cl") && methodNode.desc.equals("()V")) {
          Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
          boolean roofHidePatched = false;
          while (insnNodeList.hasNext()) {
            AbstractInsnNode insnNode = insnNodeList.next();
            
            // Move FPS display
            if (insnNode.getOpcode() == Opcodes.SIPUSH) {
                IntInsnNode call = (IntInsnNode) insnNode;
                if (call.operand == 450) {
                  call.operand = 512 - call.operand;
                  methodNode.instructions.insertBefore(
                      insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
                  methodNode.instructions.insert(
                      insnNode, new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Client", "is_displaying_fps", "Z"));
                  methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ICONST_1));
                  methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
                }
              }
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
    
    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      // Renderer present hook
      if (methodNode.desc.equals("(Ljava/awt/Graphics;II)V")) {
        AbstractInsnNode findNode = methodNode.instructions.getFirst();
        FieldInsnNode imageNode = null;

        LabelNode label = new LabelNode();
        methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ICONST_0));
        methodNode.instructions.insertBefore(
            findNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Replay", "isSeeking", "Z"));
        methodNode.instructions.insertBefore(findNode, new JumpInsnNode(Opcodes.IFEQ, label));
        methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.RETURN));
        methodNode.instructions.insertBefore(findNode, label);

        while (findNode.getOpcode() != Opcodes.POP) {
          findNode = findNode.getNext();
          if (findNode == null) {
            Logger.Error("Unable to find present hook");
            break;
          }
        }

        while (!(findNode.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) findNode).name.equals("of"))) {
          if (findNode.getOpcode() == Opcodes.GETFIELD) imageNode = (FieldInsnNode) findNode;

          AbstractInsnNode prev = findNode.getPrevious();
          methodNode.instructions.remove(findNode);
          findNode = prev;
        }

        methodNode.instructions.insert(
            findNode,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "Game/Renderer",
                "present",
                "(Ljava/awt/Graphics;Ljava/awt/Image;)V",
                false));
        methodNode.instructions.insert(
            findNode,
            new FieldInsnNode(Opcodes.GETFIELD, node.name, imageNode.name, imageNode.desc));
        methodNode.instructions.insert(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insert(findNode, new VarInsnNode(Opcodes.ALOAD, 1));
      }
      // Drawstring ~1000~ fix
      if (methodNode.name.equals("ef") && methodNode.desc.equals("(Ljava/lang/String;IIII)V")) {
        AbstractInsnNode findNode = methodNode.instructions.getFirst();
        LabelNode elseCheckLabel, continueLabel;
        LabelNode nextCheckLabel = new LabelNode();
        AbstractInsnNode call;
        while (!(findNode.getOpcode() == Opcodes.BIPUSH && ((IntInsnNode) findNode).operand == 126)) {
	          findNode = findNode.getNext();
	    }
        elseCheckLabel = ((JumpInsnNode)findNode.getNext()).label;
        while (!(findNode.getOpcode() == Opcodes.IINC && ((IincInsnNode) findNode).incr == 4)) {
	          findNode = findNode.getNext();
	    }
        continueLabel = ((JumpInsnNode)findNode.getNext()).label;
        
        while (!(findNode.getOpcode() == Opcodes.GETSTATIC && ((FieldInsnNode) findNode).name.equals("tk"))) {
	          findNode = findNode.getNext();
	    }
        call = findNode;
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ALOAD, 1));
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 7));
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 2));
        methodNode.instructions.insertBefore(call, new InsnNode(Opcodes.ICONST_1));
        methodNode.instructions.insertBefore(
                call,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "check_draw_string", "(Ljava/lang/String;IIZ)I", false));
        
        methodNode.instructions.insertBefore(call, new InsnNode(Opcodes.ICONST_1));
        methodNode.instructions.insertBefore(call, new JumpInsnNode(Opcodes.IF_ICMPNE, nextCheckLabel));
        
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ALOAD, 1));
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 7));
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 2));
        methodNode.instructions.insertBefore(call, new InsnNode(Opcodes.ICONST_0));
        methodNode.instructions.insertBefore(
                call,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "check_draw_string", "(Ljava/lang/String;IIZ)I", false));
        
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ISTORE, 2));
        
        methodNode.instructions.insertBefore(call, new IincInsnNode(7, 5));
        methodNode.instructions.insertBefore(
                call, new JumpInsnNode(Opcodes.GOTO, continueLabel));
        methodNode.instructions.insertBefore(call, nextCheckLabel);
      }
    }
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
  
  /**
   * TODO: Complete JavaDoc
   *
   * @param methodNode
   * @param owner The class of the variable to be hooked
   * @param var The variable to be hooked
   * @param desc
   * @param newClass The class the hooked variable will be stored in
   * @param newVar The variable name the hooked variable will be stored in
   * @param newDesc
   */
  private void hookStaticVariableClone(
      MethodNode methodNode,
      String owner,
      String var,
      String desc,
      String newClass,
      String newVar,
      String newDesc) {
    Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
    while (insnNodeList.hasNext()) {
      AbstractInsnNode insnNode = insnNodeList.next();

      int opcode = insnNode.getOpcode();
      if (opcode == Opcodes.PUTSTATIC) {
        FieldInsnNode field = (FieldInsnNode) insnNode;
        if (field.owner.equals(owner) && field.name.equals(var) && field.desc.equals(desc)) {
          methodNode.instructions.insertBefore(field, new InsnNode(Opcodes.DUP));
          methodNode.instructions.insert(
              field, new FieldInsnNode(Opcodes.PUTSTATIC, newClass, newVar, newDesc));
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
