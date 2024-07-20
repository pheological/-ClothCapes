package com.grappenmaker.cloth

import net.weavemc.loader.api.Hook
import net.weavemc.loader.api.util.asm
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

@Suppress("unused")
class LunarClothCapes : Hook() {
    override fun transform(node: ClassNode, config: AssemblerConfig) {
        for (m in node.methods) for (i in m.instructions) {
            if (i !is LdcInsnNode) continue
            if (i.cst == "Refreshed render target textures.") {
                node.update()
                return
            }
        }
    }

    private fun MethodNode.hasTarget(): Boolean {
        for (i in instructions) {
            if (i !is MethodInsnNode) continue
            if (i.name == "bridge\$getUniqueID") return true
        }

        return false
    }

    private fun ClassNode.update() {
        val target = methods.find { it.desc.endsWith(")Z") && it.hasTarget() }
        if (target == null) {
            println("[LunarClothCapes] Weird, class $name got matched but didn't have the target method")
            return
        }

        with (target) {
            instructions = asm {
                iconst_1
                ireturn
            }

            localVariables.clear()
            tryCatchBlocks.clear()
        }
    }
}