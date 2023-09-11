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

    if (node.name.equals("jagex/client/i")) {
      patchRenderer(node);
    }
    /*else if(node.name.equals("lb"))
    {
    	patchCamera(node);
    }*/
    else if (node.name.equals("jagex/client/k")) {
      patchApplet(node);
    } else if (node.name.equals("r")) {
      patchData(node);
    } else if (node.name.equals("mudclient")) {
      patchClient(node);
    } else if (node.name.equals("jagex/client/d")) {
      patchFrame(node);
    } else if (node.name.equals("jagex/o")) {
      patchUtility(node);
    } else if (node.name.equals("jagex/client/j")) {
      patchScene(node);
    } else if (node.name.equals("jagex/client/e")) {
      patchConnection(node);
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
      while (insnNodeList.hasNext()) {
        AbstractInsnNode insnNode = insnNodeList.next();

        if (insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
          MethodInsnNode call = (MethodInsnNode) insnNode;

          // Patch calls to System.out.println and route them to Logger.Game
          if (call.owner.equals("java/io/PrintStream") && call.name.equals("println")) {
            methodNode.instructions.insertBefore(
                insnNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Client/Logger", "Game", "(Ljava/lang/String;)V"));
            methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.POP));
            methodNode.instructions.remove(insnNode);
          }
        }
      }

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

      hookConditionalClassVariable(
          methodNode,
          "jagex/client/j",
          "dm", // 4100
          "I",
          "Game/Camera",
          "distance1",
          "I",
          false,
          true,
          "VIEW_DISTANCE_BOOL");
      hookConditionalClassVariable(
          methodNode,
          "jagex/client/j",
          "em", // 4100
          "I",
          "Game/Camera",
          "distance2",
          "I",
          false,
          true,
          "VIEW_DISTANCE_BOOL");
      hookConditionalClassVariable(
          methodNode,
          "jagex/client/j",
          "fm",
          "I",
          "Game/Camera",
          "distance3",
          "I",
          false,
          true,
          "VIEW_DISTANCE_BOOL");
      hookConditionalClassVariable(
          methodNode,
          "jagex/client/j",
          "gm",
          "I",
          "Game/Camera",
          "distance4",
          "I",
          false,
          true,
          "VIEW_DISTANCE_BOOL");

      hookClassVariable(
          methodNode, "jagex/client/j", "oo", "[I", "Game/Renderer", "pixels", "[I", true, true);

      hookClassVariable(
          methodNode,
          "mudclient",
          "ty",
          "[I",
          "Game/Client",
          "current_equipment_stats",
          "[I",
          true,
          false);
      hookClassVariable(
          methodNode, "mudclient", "oy", "[I", "Game/Client", "current_level", "[I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "py", "[I", "Game/Client", "base_level", "[I", true, false);
      hookClassVariable(
          methodNode,
          "mudclient",
          "sy",
          "[Ljava/lang/String;",
          "Game/Client",
          "skill_name",
          "[Ljava/lang/String;",
          true,
          false);

      hookStaticVariable(
          methodNode, "jagex/client/e", "jd", "I", "Game/Client", "friends_count", "I");
      hookStaticVariable(
          methodNode, "jagex/client/e", "kd", "[J", "Game/Client", "friends_hash", "[J");
      hookStaticVariable(
          methodNode, "jagex/client/e", "ld", "[I", "Game/Client", "friends_online", "[I");

      hookStaticVariable(
          methodNode, "jagex/client/e", "md", "I", "Game/Client", "ignores_count", "I");
      hookStaticVariable(
          methodNode, "jagex/client/e", "nd", "[J", "Game/Client", "ignores_hash", "[J");

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
      hookClassVariable(
          methodNode, "jagex/client/i", "sj", "I", "Game/Renderer", "width", "I", false, true);
      hookClassVariable(
          methodNode, "jagex/client/i", "tj", "I", "Game/Renderer", "height", "I", false, true);
      hookClassVariable(
          methodNode, "jagex/client/i", "yj", "[I", "Game/Renderer", "pixels", "[I", true, true);

      hookClassVariable(methodNode, "m", "sj", "I", "Game/Renderer", "width", "I", false, true);
      hookClassVariable(methodNode, "m", "tj", "I", "Game/Renderer", "height", "I", false, true);
      hookClassVariable(methodNode, "m", "yj", "[I", "Game/Renderer", "pixels", "[I", true, true);

      hookClassVariable(
          methodNode,
          "jagex/client/k",
          "tq",
          "Ljava/lang/String;",
          "Game/Client",
          "modal_enteredText",
          "Ljava/lang/String;",
          true,
          true);
      hookClassVariable(
          methodNode,
          "mudclient",
          "tq",
          "Ljava/lang/String;",
          "Game/Client",
          "modal_enteredText",
          "Ljava/lang/String;",
          true,
          true);
      hookClassVariable(
          methodNode,
          "jagex/client/k",
          "uq",
          "Ljava/lang/String;",
          "Game/Client",
          "modal_text",
          "Ljava/lang/String;",
          true,
          true);
      hookClassVariable(
          methodNode,
          "mudclient",
          "uq",
          "Ljava/lang/String;",
          "Game/Client",
          "modal_text",
          "Ljava/lang/String;",
          true,
          true);

      hookClassVariable(
          methodNode, "mudclient", "bu", "I", "Game/Renderer", "width", "I", false, true);
      hookClassVariable(
          methodNode, "mudclient", "cu", "I", "Game/Renderer", "height_client", "I", false, true);

      hookClassVariable(
          methodNode, "jagex/client/k", "hp", "I", "Game/Renderer", "width", "I", false, true);
      hookClassVariable(
          methodNode, "jagex/client/k", "ip", "I", "Game/Renderer", "height", "I", false, true);

      hookClassVariable(
          methodNode, "mudclient", "tz", "I", "Game/Client", "combat_style", "I", true, true);

      hookConditionalClassVariable(
          methodNode, "mudclient", "du", "I", "Game/Camera", "fov", "I", false, true, "FOV_BOOL");

      hookClassVariable(
          methodNode, "mudclient", "lt", "I", "Game/Client", "combat_timer", "I", true, true);

      hookClassVariable(
          methodNode, "mudclient", "ht", "I", "Game/Client", "show_friends", "I", true, true);
      hookClassVariable(
          methodNode, "mudclient", "nx", "I", "Game/Client", "show_menu", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "wy", "Z", "Game/Client", "show_questionmenu", "Z", true, false);
      hookClassVariable(
          methodNode, "mudclient", "gy", "Z", "Game/Client", "show_shop", "Z", true, false);
      hookClassVariable(
          methodNode, "mudclient", "ux", "Z", "Game/Client", "show_trade", "Z", true, false);
      hookClassVariable(
          methodNode, "mudclient", "ft", "I", "Game/Client", "show_changepk", "I", true, false);

      hookClassVariable(
          methodNode, "mudclient", "mx", "I", "Game/Client", "mouse_click", "I", true, true);

      hookClassVariable(
          methodNode, "mudclient", "ox", "I", "Game/Client", "inventory_count", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "px", "[I", "Game/Client", "inventory_items", "[I", true, false);

      hookConditionalClassVariable(
          methodNode,
          "mudclient",
          "nu",
          "I",
          "Game/Camera",
          "lookat_x",
          "I",
          false,
          true,
          "CAMERA_MOVABLE_BOOL");
      hookConditionalClassVariable(
          methodNode,
          "mudclient",
          "ou",
          "I",
          "Game/Camera",
          "lookat_y",
          "I",
          false,
          true,
          "CAMERA_MOVABLE_BOOL");

      hookClassVariable(
          methodNode, "mudclient", "qu", "I", "Game/Camera", "auto_speed", "I", true, true);

      hookClassVariable(methodNode, "mudclient", "ct", "Z", "Game/Camera", "auto", "Z", true, true);
      hookClassVariable(
          methodNode, "mudclient", "pu", "I", "Game/Camera", "angle", "I", true, true);

      hookConditionalClassVariable(
          methodNode,
          "mudclient",
          "zt",
          "I",
          "Game/Camera",
          "rotation",
          "I",
          false,
          true,
          "CAMERA_ROTATABLE_BOOL");
      hookConditionalClassVariable(
          methodNode,
          "mudclient",
          "lu",
          "I",
          "Game/Camera",
          "zoom",
          "I",
          false,
          true,
          "CAMERA_ZOOMABLE_BOOL");

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
          "bib",
          "[[Ljava/lang/String;",
          "Game/JGameData",
          "objectNames",
          "[[Ljava/lang/String;");

      // Player name
      hookClassVariable(
          methodNode,
          "mudclient",
          "aw",
          "Ll;",
          "Game/Client",
          "player_object",
          "Ljava/lang/Object;",
          true,
          false);
      // coordinates related
      hookClassVariable(
          methodNode, "mudclient", "vu", "I", "Game/Client", "regionX", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "wu", "I", "Game/Client", "regionY", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "bw", "I", "Game/Client", "coordX", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "cw", "I", "Game/Client", "coordY", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "ru", "I", "Game/Client", "planeWidth", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "su", "I", "Game/Client", "planeHeight", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "xu", "I", "Game/Client", "planeIndex", "I", true, false);
      hookClassVariable(
          methodNode, "mudclient", "hu", "Z", "Game/Client", "loadingArea", "Z", true, false);

      hookClassVariable(
          methodNode, "mudclient", "tt", "I", "Game/Client", "tileSize", "I", true, false);
    }
  }

  private void patchData(ClassNode node) {
    Logger.Info("Patching data (" + node.name + ".class)");

    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      if (methodNode.name.equals("oo") && methodNode.desc.equals("([B)V")) {
        // Data hook patches
        // find node before return point
        AbstractInsnNode call = methodNode.instructions.getFirst();
        while (call.getOpcode() != Opcodes.RETURN) {
          call = call.getNext();
        }
        while (call.getOpcode() != Opcodes.INVOKESTATIC) {
          call = call.getPrevious();
        }
        methodNode.instructions.insert(
            call,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC, "Client/WorldMapWindow", "initScenery", "()V", false));
      }
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

      // draw loading screen
      if (methodNode.name.equals("hj") && methodNode.desc.equals("(ILjava/lang/String;)V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          if (insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL
              && ((MethodInsnNode) insnNode).name.equals("setColor")) {

            insnNode = insnNode.getPrevious().getPrevious().getPrevious();

            methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));

            methodNode.instructions.insertBefore(
                insnNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Client/ScaledWindow",
                    "hookLoadingGraphics",
                    "()Ljava/awt/Graphics;",
                    false));

            methodNode.instructions.insertBefore(
                insnNode,
                new FieldInsnNode(Opcodes.PUTFIELD, "jagex/client/k", "cq", "Ljava/awt/Graphics;"));

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

      // Makes the underground flickering toggleable
      if (methodNode.name.equals("cl") && methodNode.desc.equals("()V")) {
        AbstractInsnNode start = methodNode.instructions.getFirst();
        while (start != null) {
          if (start.getOpcode() == Opcodes.LDC) {
            LdcInsnNode ldcNode = (LdcInsnNode) start;
            if (ldcNode.cst instanceof Double && (double) ldcNode.cst == 3.0) {
              JumpInsnNode insnNode =
                  (JumpInsnNode) ldcNode.getPrevious().getPrevious().getPrevious();

              methodNode.instructions.insert(
                  insnNode, new JumpInsnNode(Opcodes.IFGT, insnNode.label));

              methodNode.instructions.insert(
                  insnNode,
                  new FieldInsnNode(
                      Opcodes.GETSTATIC,
                      "Client/Settings",
                      "DISABLE_UNDERGROUND_LIGHTING_BOOL",
                      "Z"));

              methodNode.instructions.insert(
                  insnNode,
                  new MethodInsnNode(
                      Opcodes.INVOKESTATIC,
                      "Client/Settings",
                      "updateInjectedVariables",
                      "()V",
                      false));

              break;
            }
          }

          start = start.getNext();
        }
      }

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
      if (methodNode.name.equals("dj") && methodNode.desc.equals("()V")) {
        // handleGameInput
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();

        AbstractInsnNode insnNode = insnNodeList.next();
        methodNode.instructions.insertBefore(
            insnNode,
            new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "update", "()V", false));
      }
      if (methodNode.name.equals("u") && methodNode.desc.equals("()V")) {
        AbstractInsnNode lastNode = methodNode.instructions.getLast().getPrevious();

        // Send combat style option
        LabelNode label = new LabelNode();

        methodNode.instructions.insert(lastNode, label);

        // Format
        methodNode.instructions.insert(
            lastNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "mudclient", "ll", "()V", false));
        methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

        // Write byte
        methodNode.instructions.insert(
            lastNode,
            new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jagex/client/a", "n", "(I)V", false));
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
            lastNode,
            new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/e", "dd", "Ljagex/client/a;"));
        methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

        // Create Packet
        methodNode.instructions.insert(
            lastNode,
            new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jagex/client/a", "i", "(I)V", false));
        methodNode.instructions.insert(lastNode, new IntInsnNode(Opcodes.SIPUSH, 231));
        methodNode.instructions.insert(
            lastNode,
            new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/e", "dd", "Ljagex/client/a;"));
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
                false)); // TODO Remove this line when COMBAT_STYLE_INT is eliminated

        // Client init_game
        methodNode.instructions.insert(
            lastNode,
            new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "init_game", "()V", false));
      }
      if (methodNode.name.equals("vl") && methodNode.desc.equals("()V")) {
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
          while (!(findNode.getOpcode() == Opcodes.LDC
              && ((LdcInsnNode) findNode).cst.equals("~439~@whi@Remove         WWWWWWWWWW"))) {
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
          AbstractInsnNode findNode, findNode2;

          // Hide Roof option
          if (insnNode.getOpcode() == Opcodes.IAND) {
            if (!roofHidePatched) {
              findNode = insnNode;
              // find aload0
              while (!(findNode.getOpcode() == Opcodes.ALOAD
                  && ((VarInsnNode) findNode).var == 0)) {
                findNode = findNode.getNext();
              }
              findNode2 = findNode;
              // find call to this.uu == 0
              while (!(findNode2.getOpcode() == Opcodes.IFNE)) {
                findNode2 = findNode2.getNext();
              }
              JumpInsnNode end = (JumpInsnNode) findNode2;
              AbstractInsnNode ifStart = findNode;
              methodNode.instructions.insertBefore(
                  ifStart,
                  new MethodInsnNode(
                      Opcodes.INVOKESTATIC,
                      "Client/Settings",
                      "updateInjectedVariables",
                      "()V",
                      false));
              methodNode.instructions.insertBefore(
                  ifStart,
                  new FieldInsnNode(Opcodes.GETSTATIC, "Client/Settings", "HIDE_ROOFS_BOOL", "Z"));
              methodNode.instructions.insertBefore(
                  ifStart, new JumpInsnNode(Opcodes.IFGT, end.label));
              roofHidePatched = true;
            }
          }

          // Move FPS display
          if (insnNode.getOpcode() == Opcodes.SIPUSH) {
            IntInsnNode call = (IntInsnNode) insnNode;
            if (call.operand == 450) {
              call.operand = 512 - call.operand;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(
                  insnNode,
                  new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Client", "is_displaying_fps", "Z"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ICONST_1));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
            }
          }
        }

        LabelNode lblNode = new LabelNode();

        AbstractInsnNode startNode, endNode;
        AbstractInsnNode findNode = methodNode.instructions.getFirst();
        // find call to fps:
        while (!(findNode.getOpcode() == Opcodes.LDC
            && ((LdcInsnNode) findNode).cst.equals("Fps: "))) {
          findNode = findNode.getNext();
        }
        // rewind to find aload0
        while (!(findNode.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) findNode).var == 0)) {
          findNode = findNode.getPrevious();
        }
        startNode = findNode;
        while (!(findNode.getOpcode() == Opcodes.INVOKEVIRTUAL
            && ((MethodInsnNode) findNode).name.equals("ef"))) {
          findNode = findNode.getNext();
        }
        endNode = findNode;
        methodNode.instructions.insert(endNode, lblNode);

        methodNode.instructions.insertBefore(
            startNode,
            new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "shouldHideFPS", "()Z"));
        methodNode.instructions.insertBefore(startNode, new JumpInsnNode(Opcodes.IFNE, lblNode));
      }
      if (methodNode.name.equals("bb") && methodNode.desc.equals("(II[B)V")) {
        Iterator<AbstractInsnNode> insnNodeList;

        // setLoadingArea
        AbstractInsnNode findNode = methodNode.instructions.getFirst();
        // find call to loadingRegion (method yk)
        while (!(findNode.getOpcode() == Opcodes.INVOKEVIRTUAL
            && ((MethodInsnNode) findNode).owner.equals("mudclient")
            && ((MethodInsnNode) findNode).name.equals("yk"))) {
          findNode = findNode.getNext();
        }
        findNode = findNode.getNext();
        // rewind back to find iload13
        methodNode.instructions.insertBefore(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            findNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "hu", "Z"));
        methodNode.instructions.insertBefore(
            findNode,
            new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "isLoadingHook", "(Z)V"));

        // hook onto received menu options
        insnNodeList = methodNode.instructions.iterator();
        LabelNode lblNode = null;
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode nextNode = insnNode.getNext();
          AbstractInsnNode twoNextNode = nextNode.getNext();

          if (nextNode == null || twoNextNode == null) break;

          if (insnNode.getOpcode() == Opcodes.ALOAD
              && ((VarInsnNode) insnNode).var == 0
              && nextNode.getOpcode() == Opcodes.GETFIELD
              && ((FieldInsnNode) nextNode).owner.equals("mudclient")
              && ((FieldInsnNode) nextNode).name.equals("yy")
              && twoNextNode.getOpcode() == Opcodes.ILOAD
              && ((VarInsnNode) twoNextNode).var == 4) {
            // in here is part where menu options are received, is a loop
            // find previous jump node
            findNode = insnNode;
            while (findNode.getOpcode() != Opcodes.GOTO) {
              findNode = findNode.getPrevious();
            }
            lblNode = ((JumpInsnNode) findNode).label;
            continue;
          }

          if (lblNode != null
              && insnNode instanceof LabelNode
              && ((LabelNode) insnNode).equals(lblNode)) {
            findNode = insnNode.getNext();
            while (findNode.getOpcode() != Opcodes.RETURN) {
              findNode = findNode.getNext();
            }
            InsnNode call = (InsnNode) findNode;
            methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                call,
                new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "yy", "[Ljava/lang/String;"));
            methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 5));
            methodNode.instructions.insertBefore(
                call,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Game/Client",
                    "receivedOptionsHook",
                    "([Ljava/lang/String;I)V"));
          }
        }

        // find last node before return
        findNode = methodNode.instructions.getLast();
        int times = 0;
        while (times < 2) {
          while (findNode.getOpcode() != Opcodes.RETURN) {
            findNode = findNode.getPrevious();
          }
          findNode = findNode.getPrevious();
          times++;
        }
        methodNode.instructions.insert(
            findNode,
            new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "check_init", "()V", false));
      }
      // hook onto selected menu option
      if (methodNode.name.equals("nl") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode nextNode = insnNode.getNext();
          AbstractInsnNode twoNextNode = nextNode.getNext();

          if (nextNode == null || twoNextNode == null) break;

          if (insnNode.getOpcode() == Opcodes.ALOAD
              && ((VarInsnNode) insnNode).var == 0
              && nextNode.getOpcode() == Opcodes.GETFIELD
              && ((FieldInsnNode) nextNode).owner.equals("jagex/client/e")
              && ((FieldInsnNode) nextNode).name.equals("dd")
              && twoNextNode.getOpcode() == Opcodes.SIPUSH
              && ((IntInsnNode) twoNextNode).operand == 237) {
            VarInsnNode call = (VarInsnNode) insnNode;
            methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                call,
                new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "yy", "[Ljava/lang/String;"));
            methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 1));
            methodNode.instructions.insertBefore(
                call,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Game/Client",
                    "selectedOptionHook",
                    "([Ljava/lang/String;I)V"));
          }
        }
      }
      if (methodNode.name.equals("gk") && methodNode.desc.equals("(Ljava/lang/String;I)V")) {
        AbstractInsnNode first = methodNode.instructions.getFirst();

        methodNode.instructions.insertBefore(first, new VarInsnNode(Opcodes.ALOAD, 1));
        methodNode.instructions.insertBefore(first, new VarInsnNode(Opcodes.ILOAD, 2));
        methodNode.instructions.insertBefore(
            first,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC, "Game/Client", "messageHook", "(Ljava/lang/String;I)V"));
      }
      if (methodNode.name.equals("jk") && methodNode.desc.equals("()V")) {
        // Show combat menu
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          if (insnNode.getOpcode() == Opcodes.GETFIELD) {
            FieldInsnNode getfield = (FieldInsnNode) insnNode;

            if (getfield.name.equals("ur")) {
              AbstractInsnNode findNode = null;

              // Hide combat menu patch
              findNode = insnNode;
              while (findNode.getOpcode() != Opcodes.ALOAD) findNode = findNode.getNext();
              LabelNode label = new LabelNode();
              LabelNode skipLabel = new LabelNode();
              methodNode.instructions.insertBefore(
                  findNode,
                  new MethodInsnNode(
                      Opcodes.INVOKESTATIC,
                      "Client/Settings",
                      "updateInjectedVariables",
                      "()V",
                      false));
              methodNode.instructions.insertBefore(
                  findNode,
                  new FieldInsnNode(
                      Opcodes.GETSTATIC, "Client/Settings", "COMBAT_MENU_HIDDEN_BOOL", "Z"));
              methodNode.instructions.insertBefore(findNode, new JumpInsnNode(Opcodes.IFGT, label));
              methodNode.instructions.insertBefore(findNode, skipLabel);
              methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ICONST_1));
              methodNode.instructions.insertBefore(
                  findNode,
                  new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Renderer", "combat_menu_shown", "Z"));
              methodNode.instructions.insert(findNode.getNext(), label);

              // Show combat menu patch
              JumpInsnNode jumpNode = (JumpInsnNode) insnNode.getNext();
              LabelNode exitLabel = jumpNode.label;
              LabelNode runLabel = new LabelNode();
              methodNode.instructions.insertBefore(jumpNode.getNext(), runLabel);
              label = new LabelNode();
              jumpNode.label = label;
              methodNode.instructions.insert(jumpNode, new JumpInsnNode(Opcodes.GOTO, exitLabel));
              methodNode.instructions.insert(jumpNode, new JumpInsnNode(Opcodes.IFGT, skipLabel));
              methodNode.instructions.insert(
                  jumpNode,
                  new FieldInsnNode(
                      Opcodes.GETSTATIC, "Client/Settings", "COMBAT_MENU_SHOWN_BOOL", "Z"));
              methodNode.instructions.insert(
                  jumpNode,
                  new MethodInsnNode(
                      Opcodes.INVOKESTATIC,
                      "Client/Settings",
                      "updateInjectedVariables",
                      "()V",
                      false));
              methodNode.instructions.insert(jumpNode, label);
              methodNode.instructions.insert(jumpNode, new JumpInsnNode(Opcodes.GOTO, runLabel));

              findNode = insnNode.getPrevious();
              while (findNode.getOpcode() != Opcodes.ALOAD) findNode = findNode.getPrevious();
              methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ICONST_0));
              methodNode.instructions.insertBefore(
                  findNode,
                  new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Renderer", "combat_menu_shown", "Z"));

              break;
            }
          }
        }
      }
      if (methodNode.name.equals("fm") && methodNode.desc.equals("()V")) {

        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();

        LabelNode lblNode = null;
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode nextNode = insnNode.getNext();

          // this part checks to see if there's a string to render for the two cases below
          if (insnNode.getOpcode() == Opcodes.ICONST_0
              && nextNode.getOpcode() == Opcodes.ISTORE
              && ((VarInsnNode) nextNode).var == 1) {
            if (lblNode == null) {
              lblNode = ((JumpInsnNode) nextNode.getNext()).label;
            }
          }

          // is_hover = 1, also getfield mudclient.qt
          AbstractInsnNode nodeIns;
          if (insnNode.getOpcode() == Opcodes.GETFIELD) {
            FieldInsnNode fieldNode = ((FieldInsnNode) insnNode);
            if (fieldNode.owner.equals("mudclient")
                && fieldNode.name.equals("qt")
                && nextNode.getOpcode() == Opcodes.ALOAD
                && ((VarInsnNode) nextNode).var == 4) {
              methodNode.instructions.insert(fieldNode, new VarInsnNode(Opcodes.ASTORE, 4));
              methodNode.instructions.insert(
                  fieldNode,
                  new MethodInsnNode(
                      Opcodes.INVOKESTATIC,
                      "Game/Client",
                      "mouse_action_hook",
                      "(Ljava/lang/String;)Ljava/lang/String;",
                      false));
              methodNode.instructions.insert(fieldNode, new VarInsnNode(Opcodes.ALOAD, 4));
              methodNode.instructions.insert(
                  fieldNode, new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Client", "is_hover", "Z"));
              methodNode.instructions.insert(fieldNode, new InsnNode(Opcodes.ICONST_1));
              continue;
            }
          }

          // is_hover = 0
          if (insnNode instanceof LabelNode
              && lblNode != null
              && ((LabelNode) insnNode).equals(lblNode)) {
            methodNode.instructions.insert(
                insnNode, new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Client", "is_hover", "Z"));
            methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ICONST_0));
          }
        }

        insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Patch max positions right click menu
          if (insnNode.getOpcode() == Opcodes.SIPUSH) {
            IntInsnNode call = (IntInsnNode) insnNode;
            if (call.operand == 510) {
              call.operand = 512 - call.operand;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
            } else if (call.operand == 315) {
              call.operand = 334 - call.operand;
              methodNode.instructions.insertBefore(
                  insnNode,
                  new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height_client", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
            }
          }
        }
      }
      if (methodNode.name.equals("hm") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Chat command patch
          if (insnNode.getOpcode() == Opcodes.LDC
              && ((LdcInsnNode) insnNode).cst.equals("simlostcon99")) {
            LdcInsnNode call = (LdcInsnNode) insnNode;
            AbstractInsnNode jmpNode = insnNode;
            while (jmpNode.getOpcode() != Opcodes.IFEQ) jmpNode = jmpNode.getNext();

            AbstractInsnNode insertNode = insnNode;
            while (insertNode.getOpcode() != Opcodes.INVOKEVIRTUAL)
              insertNode = insertNode.getPrevious();

            JumpInsnNode jmp = (JumpInsnNode) jmpNode;
            methodNode.instructions.insert(insertNode, new VarInsnNode(Opcodes.ASTORE, 6));
            methodNode.instructions.insert(
                insertNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Game/Client",
                    "processChatCommand",
                    "(Ljava/lang/String;)Ljava/lang/String;"));
            methodNode.instructions.insert(insertNode, new VarInsnNode(Opcodes.ALOAD, 6));
          }
        }
      }
      if (methodNode.name.equals("v") && methodNode.desc.equals("()V")) {
        // reset login screen vars method
        AbstractInsnNode findNode = methodNode.instructions.getLast();
        methodNode.instructions.insertBefore(
            findNode,
            new MethodInsnNode(Opcodes.INVOKESTATIC, "Game/Client", "init_login", "()V", false));
      }
      if (methodNode.name.equals("ck") && methodNode.desc.equals("()V")) {
        // Existing user button press hook, from welcome panel
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();

        insnNodeList = methodNode.instructions.iterator();

        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode nextNode = insnNode.getNext();

          if (nextNode == null) break;

          if (insnNode.getOpcode() == Opcodes.PUTFIELD
              && ((FieldInsnNode) insnNode).name.equals("bz")
              && insnNode.getPrevious().getOpcode() == Opcodes.ICONST_2
              && Settings.SAVE_LOGININFO.get(Settings.currentProfile)) {

            // methodNode.instructions.insertBefore(nextNode, new InsnNode(Opcodes.ICONST_0));
            methodNode.instructions.insertBefore(
                nextNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "keep_login_info_hook", "()V", false));
            methodNode.instructions.insertBefore(nextNode, new InsnNode(Opcodes.RETURN));
          }
        }
      }
      if (methodNode.name.equals("yk") && methodNode.desc.equals("(II)V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Move the load screen text dialogue
          if (insnNode.getOpcode() == Opcodes.SIPUSH) {
            IntInsnNode call = (IntInsnNode) insnNode;
            if (call.operand == 256) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 192) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 19));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            }
          }
        }
      }
      if (methodNode.name.equals("kk") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Center password change
          if (insnNode.getOpcode() == Opcodes.SIPUSH || insnNode.getOpcode() == Opcodes.BIPUSH) {
            IntInsnNode call = (IntInsnNode) insnNode;
            if (call.operand == 256) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 106) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 150));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 150) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 23));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 406) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 150));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 210) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 37));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            }
          }
        }
      }
      if (methodNode.name.equals("gm") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Center change pk warn
          if (insnNode.getOpcode() == Opcodes.SIPUSH || insnNode.getOpcode() == Opcodes.BIPUSH) {
            IntInsnNode call = (IntInsnNode) insnNode;
            if (call.operand == 256) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 90) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 83));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 200) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 56));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 300) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 44));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 56) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 200));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 70) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 103));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 456) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 200));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 260) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 87));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 250 && insnNode.getNext().getOpcode() == Opcodes.ISTORE) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 77));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 250) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 6));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 350) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 94));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 150) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 106));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            }
          }
        }
      }
      if (methodNode.name.equals("el") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          if (insnNode.getOpcode() == Opcodes.BIPUSH
              && ((IntInsnNode) insnNode).operand == 90
              && insnNode.getNext().getOpcode() == Opcodes.ISTORE) {
            methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.ICONST_1));
            methodNode.instructions.insertBefore(
                insnNode, new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Client", "show_visitad", "Z"));
          } else if (insnNode.getOpcode() == Opcodes.ICONST_1
              && insnNode.getNext().getOpcode() == Opcodes.PUTFIELD
              && ((FieldInsnNode) insnNode.getNext()).name.equals("kt")) {
            methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.ICONST_0));
            methodNode.instructions.insertBefore(
                insnNode, new FieldInsnNode(Opcodes.PUTSTATIC, "Game/Client", "show_visitad", "Z"));
          }
        }

        insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Center playing over 1 hour
          if (insnNode.getOpcode() == Opcodes.SIPUSH || insnNode.getOpcode() == Opcodes.BIPUSH) {
            IntInsnNode call = (IntInsnNode) insnNode;
            if (call.operand == 256) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 106) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 150));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 90) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 83));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 70) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 103));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 200) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.ISUB));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 56));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 300 && insnNode.getNext() instanceof JumpInsnNode) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "width", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 44));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 230) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 57));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            } else if (call.operand == 253) {
              call.operand = 2;
              methodNode.instructions.insertBefore(
                  insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "Game/Renderer", "height", "I"));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IADD));
              methodNode.instructions.insert(insnNode, new IntInsnNode(Opcodes.SIPUSH, 80));
              methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.IDIV));
            }
          }
        }
      }
      if (methodNode.name.equals("uk") && methodNode.desc.equals("(IIIIIII)V")) {
        // Draw NPC hook
        AbstractInsnNode insnNode = methodNode.instructions.getLast();
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 1));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 2));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 3));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 4));

        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "r", "pgb", "[[Ljava/lang/String;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "hw", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "er", "I"));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.ICONST_0));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "hw", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "sr", "I"));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "hw", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "tr", "I"));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "hw", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "er", "I"));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "hw", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "ar", "I"));
        methodNode.instructions.insertBefore(
            insnNode,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC, "Game/Client", "drawNPC", "(IIIILjava/lang/String;IIII)V"));
      }
      if (methodNode.name.equals("il") && methodNode.desc.equals("(IIIIIII)V")) {
        // Draw Player hook
        AbstractInsnNode insnNode = methodNode.instructions.getLast();
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 1));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 2));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 3));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 4));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "zv", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "zq", "Ljava/lang/String;"));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "zv", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "sr", "I"));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "zv", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "tr", "I"));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "zv", "[Ll;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETFIELD, "l", "ar", "I"));
        methodNode.instructions.insertBefore(
            insnNode,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC, "Game/Client", "drawPlayer", "(IIIILjava/lang/String;III)V"));
      }
      if (methodNode.name.equals("hk") && methodNode.desc.equals("(IIIIIII)V")) {
        // Draw Item hook
        // ILOAD 5 is item id
        AbstractInsnNode insnNode = methodNode.instructions.getLast();
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 1));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 2));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 3));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 4));

        methodNode.instructions.insertBefore(
            insnNode, new FieldInsnNode(Opcodes.GETSTATIC, "r", "vfb", "[[Ljava/lang/String;"));
        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.ICONST_0));
        methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.AALOAD));

        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 5));
        methodNode.instructions.insertBefore(
            insnNode,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC, "Game/Client", "drawItem", "(IIIILjava/lang/String;I)V"));
      }
      if (methodNode.name.equals("nl") && methodNode.desc.equals("()V")) {
        // TODO: This can be shortened, I'll fix it another time

        // NPC Dialogue keyboard
        AbstractInsnNode lastNode = methodNode.instructions.getLast().getPrevious();

        LabelNode label = new LabelNode();

        methodNode.instructions.insert(lastNode, label);

        // Hide dialogue
        methodNode.instructions.insert(
            lastNode, new FieldInsnNode(Opcodes.PUTFIELD, "mudclient", "wy", "Z"));
        methodNode.instructions.insert(lastNode, new InsnNode(Opcodes.ICONST_0));
        methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

        // Format
        methodNode.instructions.insert(
            lastNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "mudclient", "ll", "()V", false));
        methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

        // Write byte
        methodNode.instructions.insert(
            lastNode,
            new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jagex/client/a", "n", "(I)V", false));
        methodNode.instructions.insert(
            lastNode,
            new FieldInsnNode(Opcodes.GETSTATIC, "Game/KeyboardHandler", "dialogue_option", "I"));
        methodNode.instructions.insert(
            lastNode,
            new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/e", "dd", "Ljagex/client/a;"));
        methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

        // Create Packet
        methodNode.instructions.insert(
            lastNode,
            new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jagex/client/a", "i", "(I)V", false));
        methodNode.instructions.insert(lastNode, new IntInsnNode(Opcodes.SIPUSH, 237));
        methodNode.instructions.insert(
            lastNode,
            new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/e", "dd", "Ljagex/client/a;"));
        methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));

        // Check if dialogue option is pressed
        methodNode.instructions.insert(lastNode, new JumpInsnNode(Opcodes.IF_ICMPGE, label));
        // Menu option count
        methodNode.instructions.insert(
            lastNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "xy", "I"));
        methodNode.instructions.insert(lastNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insert(
            lastNode,
            new FieldInsnNode(Opcodes.GETSTATIC, "Game/KeyboardHandler", "dialogue_option", "I"));
        methodNode.instructions.insert(lastNode, new JumpInsnNode(Opcodes.IFLT, label));
        methodNode.instructions.insert(
            lastNode,
            new FieldInsnNode(Opcodes.GETSTATIC, "Game/KeyboardHandler", "dialogue_option", "I"));
      }
      if (methodNode.name.equals("ql") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();

          // Save settings from combat menu
          if (insnNode.getOpcode() == Opcodes.PUTFIELD) {
            FieldInsnNode field = (FieldInsnNode) insnNode;

            if (field.owner.equals("mudclient") && field.name.equals("tz")) {
              methodNode.instructions.insert(
                  insnNode,
                  new MethodInsnNode(
                      Opcodes.INVOKESTATIC, "Client/Settings", "save", "()V", false));
              methodNode.instructions.insert(
                  insnNode,
                  new MethodInsnNode(
                      Opcodes.INVOKESTATIC,
                      "Client/Settings",
                      "outputInjectedVariables",
                      "()V",
                      false));
              methodNode.instructions.insert(
                  insnNode,
                  new FieldInsnNode(Opcodes.PUTSTATIC, "Client/Settings", "COMBAT_STYLE_INT", "I"));
              methodNode.instructions.insert(
                  insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "tz", "I"));
              methodNode.instructions.insert(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
            }
          }
        }
      }
      // add on info for objects and wall objects
      if (methodNode.name.equals("tk") && methodNode.desc.equals("()V")) {
        int foundExamineCount = 0; // do two times, for scenery and boundary
        boolean foundObj; // whether examine found for scenery, otherwise was boundary
        boolean foundExaminePos;

        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        AbstractInsnNode insnNode = insnNodeList.next();

        while (foundExamineCount < 2) {
          foundObj = foundExaminePos = false;
          while (!foundExaminePos && insnNodeList.hasNext()) {
            insnNode = insnNodeList.next();
            if (insnNode.getOpcode() == Opcodes.SIPUSH
                && ((((IntInsnNode) insnNode).operand == 3400)
                    || (((IntInsnNode) insnNode).operand == 3300))) {
              foundExamineCount++;
              foundObj = ((IntInsnNode) insnNode).operand == 3400;
              foundExaminePos = true;
            }
          }
          Logger.Info("count " + foundExamineCount);
          if (foundExamineCount != 0) {
            // add on info for objects
            if (foundObj) {
              while (insnNode.getOpcode() != Opcodes.INVOKEVIRTUAL
                  || !((MethodInsnNode) insnNode).name.equals("append")) {
                insnNode = insnNode.getPrevious();
              }
              insnNode = insnNode.getNext();

              if (insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL
                  && ((MethodInsnNode) insnNode).name.equals("toString")) {
                // id
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 9));
                // direction
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.insertBefore(
                    insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "yw", "[I"));
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 8));
                methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.IALOAD));
                // x
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.insertBefore(
                    insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "vw", "[I"));
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 8));
                methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.IALOAD));
                // y
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.insertBefore(
                    insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "ww", "[I"));
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 8));
                methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.IALOAD));
                // mark as scenery by inserting Game/MouseText.SCENERY
                methodNode.instructions.insertBefore(
                    insnNode,
                    new FieldInsnNode(Opcodes.GETSTATIC, "Game/MouseText", "SCENERY", "I"));
                methodNode.instructions.insertBefore(
                    insnNode,
                    new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "Game/Client",
                        "appendDetailsHook",
                        "(IIIII)Ljava/lang/String;",
                        false));
                methodNode.instructions.insertBefore(
                    insnNode,
                    new MethodInsnNode(
                        Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuffer",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuffer;",
                        false));
                continue;
              }
            }
            // add on info for wall objects
            else {
              while (insnNode.getOpcode() != Opcodes.INVOKEVIRTUAL
                  || !((MethodInsnNode) insnNode).name.equals("append")) {
                insnNode = insnNode.getPrevious();
              }
              insnNode = insnNode.getNext();

              if (insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL
                  && ((MethodInsnNode) insnNode).name.equals("toString")) {
                // id
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 9));
                // direction
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.insertBefore(
                    insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "gx", "[I"));
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 8));
                methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.IALOAD));
                // x
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.insertBefore(
                    insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "ex", "[I"));
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 8));
                methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.IALOAD));
                // y
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.insertBefore(
                    insnNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "fx", "[I"));
                methodNode.instructions.insertBefore(insnNode, new VarInsnNode(Opcodes.ILOAD, 8));
                methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.IALOAD));
                // mark as boundary by inserting Game/MouseText.BOUNDARY
                methodNode.instructions.insertBefore(
                    insnNode,
                    new FieldInsnNode(Opcodes.GETSTATIC, "Game/MouseText", "BOUNDARY", "I"));
                methodNode.instructions.insertBefore(
                    insnNode,
                    new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "Game/Client",
                        "appendDetailsHook",
                        "(IIIII)Ljava/lang/String;",
                        false));
                methodNode.instructions.insertBefore(
                    insnNode,
                    new MethodInsnNode(
                        Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuffer",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuffer;",
                        false));
                continue;
              }
            }
          }
        }
      }
      if (methodNode.name.equals("jk") && methodNode.desc.equals("()V")) {
        // menu ui
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode findNode;
          LabelNode targetNode;
          LabelNode labelNode;

          if (insnNode.getOpcode() == Opcodes.INVOKEVIRTUAL
              && ((MethodInsnNode) insnNode).name.equals("rk")
              && ((MethodInsnNode) insnNode).desc.equals("()V")) {
            findNode = insnNode.getNext();
            targetNode = ((JumpInsnNode) findNode).label;

            // find last else to insert else if
            while (!(findNode.getOpcode() == Opcodes.GETFIELD
                && ((FieldInsnNode) findNode).name.equals("wy"))) {
              findNode = findNode.getNext();
            }
            findNode = findNode.getPrevious();

            labelNode = new LabelNode();
            methodNode.instructions.insertBefore(findNode, labelNode);

            methodNode.instructions.insertBefore(
                labelNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "shouldShowTextInputDialog", "()Z"));
            methodNode.instructions.insertBefore(
                labelNode, new JumpInsnNode(Opcodes.IFEQ, labelNode));
            methodNode.instructions.insertBefore(labelNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                labelNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "mq", "I"));
            methodNode.instructions.insertBefore(labelNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                labelNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "nq", "I"));
            methodNode.instructions.insertBefore(labelNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                labelNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "mx", "I"));
            methodNode.instructions.insertBefore(
                labelNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Game/Client",
                    "drawTextInputDialogMouseHook",
                    "(III)V",
                    false));
            methodNode.instructions.insertBefore(
                labelNode, new JumpInsnNode(Opcodes.GOTO, targetNode));
            break;
          }
        }

        /*Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode findNode;
          LabelNode labelNode;

          if (insnNode.getOpcode() == Opcodes.PUTFIELD
              && ((FieldInsnNode) insnNode).name.equals("mx")) {
            findNode = insnNode.getPrevious().getPrevious();

            labelNode = new LabelNode();
            methodNode.instructions.insertBefore(findNode, labelNode);

            methodNode.instructions.insertBefore(
                labelNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "shouldShowTextInputDialog", "()Z"));
            methodNode.instructions.insertBefore(
                labelNode, new JumpInsnNode(Opcodes.IFEQ, labelNode));
            methodNode.instructions.insertBefore(labelNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                labelNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "mq", "I"));
            methodNode.instructions.insertBefore(labelNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                labelNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "nq", "I"));
            methodNode.instructions.insertBefore(labelNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                labelNode, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "mx", "I"));
            methodNode.instructions.insertBefore(
                labelNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Game/Client",
                    "drawTextInputDialogMouseHook",
                    "(III)V",
                    false));
            break;
          }
        }*/
      }
      if (methodNode.name.equals("rj") && methodNode.desc.equals("(I)V")) {
        // Handle key press hook
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();

        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode nextNode = insnNode.getNext();
          AbstractInsnNode twoNextNode = nextNode.getNext();
          AbstractInsnNode findNode, nextFindNode, call;
          LabelNode exitNode;

          if (nextNode == null || twoNextNode == null) break;

          if (insnNode.getOpcode() == Opcodes.ALOAD
              && ((VarInsnNode) insnNode).var == 0
              && nextNode.getOpcode() == Opcodes.GETFIELD
              && ((FieldInsnNode) nextNode).name.equals("ut")
              && twoNextNode.getOpcode() == Opcodes.ICONST_1) {

            findNode = twoNextNode;
            while (findNode.getOpcode() != Opcodes.IF_ICMPNE) {
              // find part of checking is logged in
              findNode = findNode.getNext();
            }
            nextFindNode = findNode;

            exitNode = ((JumpInsnNode) nextFindNode).label;
            call = insnNode;

            methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                call, new FieldInsnNode(Opcodes.GETFIELD, "mudclient", "ut", "I"));
            methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 1));
            methodNode.instructions.insertBefore(
                call,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "gameKeyPressHook", "(II)I"));
            methodNode.instructions.insertBefore(
                insnNode, new JumpInsnNode(Opcodes.IFNE, exitNode));

            break;
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

        while (!(findNode.getOpcode() == Opcodes.INVOKEVIRTUAL
            && ((MethodInsnNode) findNode).name.equals("of"))) {
          if (findNode.getOpcode() == Opcodes.GETFIELD) imageNode = (FieldInsnNode) findNode;

          AbstractInsnNode prev = findNode.getPrevious();
          methodNode.instructions.remove(findNode);
          findNode = prev;
        }

        methodNode.instructions.insert(
            findNode,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC, "Game/Renderer", "present", "(Ljava/awt/Image;)V", false));
        methodNode.instructions.insert(
            findNode,
            new FieldInsnNode(Opcodes.GETFIELD, node.name, imageNode.name, imageNode.desc));
        methodNode.instructions.insert(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
      }
      // Drawstring ~1000~ fix
      if (methodNode.name.equals("ef") && methodNode.desc.equals("(Ljava/lang/String;IIII)V")) {
        AbstractInsnNode findNode = methodNode.instructions.getFirst();
        LabelNode elseCheckLabel, continueLabel;
        LabelNode nextCheckLabel = new LabelNode();
        AbstractInsnNode call;
        while (!(findNode.getOpcode() == Opcodes.BIPUSH
            && ((IntInsnNode) findNode).operand == 126)) {
          findNode = findNode.getNext();
        }
        elseCheckLabel = ((JumpInsnNode) findNode.getNext()).label;
        while (!(findNode.getOpcode() == Opcodes.IINC && ((IincInsnNode) findNode).incr == 4)) {
          findNode = findNode.getNext();
        }
        continueLabel = ((JumpInsnNode) findNode.getNext()).label;

        while (!(findNode.getOpcode() == Opcodes.GETSTATIC
            && ((FieldInsnNode) findNode).name.equals("tk"))) {
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
                Opcodes.INVOKESTATIC,
                "Game/Client",
                "check_draw_string",
                "(Ljava/lang/String;IIZ)I",
                false));

        methodNode.instructions.insertBefore(call, new InsnNode(Opcodes.ICONST_1));
        methodNode.instructions.insertBefore(
            call, new JumpInsnNode(Opcodes.IF_ICMPNE, nextCheckLabel));

        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ALOAD, 1));
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 7));
        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ILOAD, 2));
        methodNode.instructions.insertBefore(call, new InsnNode(Opcodes.ICONST_0));
        methodNode.instructions.insertBefore(
            call,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "Game/Client",
                "check_draw_string",
                "(Ljava/lang/String;IIZ)I",
                false));

        methodNode.instructions.insertBefore(call, new VarInsnNode(Opcodes.ISTORE, 2));

        methodNode.instructions.insertBefore(call, new IincInsnNode(7, 5));
        methodNode.instructions.insertBefore(call, new JumpInsnNode(Opcodes.GOTO, continueLabel));
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

  private void patchScene(ClassNode node) {
    Logger.Info("Patching scene (" + node.name + ".class)");

    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      // Patch crash due to exceeding max polygons
      if (methodNode.name.equals("qi") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
        int times = 0;
        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode nextNode = insnNode.getNext();
          AbstractInsnNode twoNextNode = nextNode.getNext();

          if (insnNode.getPrevious() == null
              || insnNode.getPrevious().getPrevious() == null
              || insnNode.getPrevious().getPrevious().getPrevious() == null) continue;

          AbstractInsnNode threePrevNode = insnNode.getPrevious().getPrevious().getPrevious();
          AbstractInsnNode findNode;
          LabelNode targetLabel;

          if (nextNode == null || twoNextNode == null) break;

          if (insnNode.getOpcode() == Opcodes.GETFIELD
              && ((FieldInsnNode) insnNode).name.equals("kn")
              && nextNode.getOpcode() == Opcodes.ICONST_1
              && twoNextNode.getOpcode() == Opcodes.IADD
              && threePrevNode.getOpcode() == Opcodes.PUTFIELD
              && ((FieldInsnNode) threePrevNode).name.equals("ofb")
              && times == 0) {
            findNode = insnNode;

            // rewind to find iload16
            while (!(findNode.getOpcode() == Opcodes.LDC
                && ((LdcInsnNode) findNode).cst.equals(12345678))) {
              findNode = findNode.getPrevious();
            }
            targetLabel = ((JumpInsnNode) findNode.getNext()).label;
            findNode = findNode.getPrevious();

            // patch add this.kn < this.ln.length - 1
            methodNode.instructions.insertBefore(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                findNode, new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/j", "kn", "I"));
            methodNode.instructions.insertBefore(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                findNode,
                new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/j", "ln", "[Ljagex/client/q;"));
            methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ARRAYLENGTH));
            methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ICONST_1));
            methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ISUB));
            methodNode.instructions.insertBefore(
                findNode, new JumpInsnNode(Opcodes.IF_ICMPGE, targetLabel));
            times++;
          }

          if (insnNode.getOpcode() == Opcodes.GETFIELD
              && ((FieldInsnNode) insnNode).name.equals("kn")
              && nextNode.getOpcode() == Opcodes.ICONST_1
              && twoNextNode.getOpcode() == Opcodes.IADD
              && threePrevNode.getOpcode() == Opcodes.PUTFIELD
              && ((FieldInsnNode) threePrevNode).name.equals("jfb")
              && times == 1) {
            findNode = insnNode;

            // rewind to find jump
            while (!(findNode.getOpcode() == Opcodes.GETFIELD
                && ((FieldInsnNode) findNode).name.equals("um")
                && findNode.getNext().getOpcode() == Opcodes.IF_ICMPGT)) {
              findNode = findNode.getPrevious();
            }
            targetLabel = ((JumpInsnNode) findNode.getNext()).label;

            // rewind to find iload12
            while (!(findNode.getOpcode() == Opcodes.ISTORE)) {
              findNode = findNode.getPrevious();
            }
            findNode = findNode.getNext();

            // patch add this.kn < this.ln.length - 1
            methodNode.instructions.insertBefore(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                findNode, new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/j", "kn", "I"));
            methodNode.instructions.insertBefore(findNode, new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.insertBefore(
                findNode,
                new FieldInsnNode(Opcodes.GETFIELD, "jagex/client/j", "ln", "[Ljagex/client/q;"));
            methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ARRAYLENGTH));
            methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ICONST_1));
            methodNode.instructions.insertBefore(findNode, new InsnNode(Opcodes.ISUB));
            methodNode.instructions.insertBefore(
                findNode, new JumpInsnNode(Opcodes.IF_ICMPGE, targetLabel));
            times++;
          }
        }
      }
    }
  }

  private void patchConnection(ClassNode node) {
    Logger.Info("Patching connection (" + node.name + ".class)");

    Iterator<MethodNode> methodNodeList = node.methods.iterator();
    while (methodNodeList.hasNext()) {
      MethodNode methodNode = methodNodeList.next();

      // Place sorting of friends list
      if (methodNode.name.equals("z") && methodNode.desc.equals("()V")) {
        Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();

        while (insnNodeList.hasNext()) {
          AbstractInsnNode insnNode = insnNodeList.next();
          AbstractInsnNode nextNode = insnNode.getNext();
          AbstractInsnNode findNode;

          if (nextNode == null) break;

          if (insnNode.getOpcode() == Opcodes.PUTFIELD
              && ((FieldInsnNode) insnNode).name.equals("ed")
              && nextNode.getOpcode() == Opcodes.RETURN) {
            methodNode.instructions.insertBefore(
                nextNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "sortFriends", "()V", false));
          }

          if (insnNode.getOpcode() == Opcodes.GETFIELD
              && ((FieldInsnNode) insnNode).name.equals("jd")
              && nextNode.getOpcode() == Opcodes.IF_ICMPLT
              && nextNode.getNext().getOpcode() == Opcodes.GOTO) {
            methodNode.instructions.insert(
                nextNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "sortFriends", "()V", false));
          }

          if (insnNode.getOpcode() == Opcodes.LDC
              && ((LdcInsnNode) insnNode).cst.equals(" has been added to your friends list")) {
            findNode = insnNode;

            // find goto
            while (!(findNode.getOpcode() == Opcodes.GOTO)) {
              findNode = findNode.getNext();
            }
            methodNode.instructions.insertBefore(
                findNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC, "Game/Client", "sortFriends", "()V", false));
          }
        }
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

  private void hookConditionalClassVariable(
      MethodNode methodNode,
      String owner,
      String var,
      String desc,
      String newClass,
      String newVar,
      String newDesc,
      boolean canRead,
      boolean canWrite,
      String boolTrigger) {
    Iterator<AbstractInsnNode> insnNodeList = methodNode.instructions.iterator();
    while (insnNodeList.hasNext()) {
      AbstractInsnNode insnNode = insnNodeList.next();

      int opcode = insnNode.getOpcode();
      if (opcode == Opcodes.GETFIELD || opcode == Opcodes.PUTFIELD) {
        FieldInsnNode field = (FieldInsnNode) insnNode;
        if (field.owner.equals(owner) && field.name.equals(var) && field.desc.equals(desc)) {
          if (opcode == Opcodes.GETFIELD && canWrite) {
            LabelNode label = new LabelNode();
            methodNode.instructions.insert(insnNode, label);
            methodNode.instructions.insert(
                insnNode, new FieldInsnNode(Opcodes.GETSTATIC, newClass, newVar, newDesc));
            methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.POP));
            methodNode.instructions.insert(insnNode, new JumpInsnNode(Opcodes.IFEQ, label));
            methodNode.instructions.insert(
                insnNode,
                new FieldInsnNode(Opcodes.GETSTATIC, "Client/Settings", boolTrigger, "Z"));
            methodNode.instructions.insert(
                insnNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Client/Settings",
                    "updateInjectedVariables",
                    "()V",
                    false));
          } else if (opcode == Opcodes.PUTFIELD && canRead) {
            LabelNode label_end = new LabelNode();
            LabelNode label = new LabelNode();
            methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.DUP_X1));
            methodNode.instructions.insert(insnNode, label_end);
            methodNode.instructions.insert(insnNode, new InsnNode(Opcodes.POP));
            methodNode.instructions.insert(insnNode, label);
            methodNode.instructions.insert(insnNode, new JumpInsnNode(Opcodes.GOTO, label_end));
            methodNode.instructions.insert(
                insnNode, new FieldInsnNode(Opcodes.PUTSTATIC, newClass, newVar, newDesc));
            methodNode.instructions.insert(insnNode, new JumpInsnNode(Opcodes.IFEQ, label));
            methodNode.instructions.insert(
                insnNode,
                new FieldInsnNode(Opcodes.GETSTATIC, "Client/Settings", boolTrigger, "Z"));
            methodNode.instructions.insert(
                insnNode,
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "Client/Settings",
                    "updateInjectedVariables",
                    "()V",
                    false));
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
  private void hookStaticVariable(
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
      if (opcode == Opcodes.GETSTATIC || opcode == Opcodes.PUTSTATIC) {
        FieldInsnNode field = (FieldInsnNode) insnNode;
        if (field.owner.equals(owner) && field.name.equals(var) && field.desc.equals(desc)) {
          field.owner = newClass;
          field.name = newVar;
          field.desc = newDesc;
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
