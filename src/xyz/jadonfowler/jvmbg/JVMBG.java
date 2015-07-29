package xyz.jadonfowler.jvmbg;

import org.objectweb.asm.util.ASMifier;

public class JVMBG {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        ASMifier.main(new String[]{"xyz/jadonfowler/jvmbg/BasicClass"});
    }
}
