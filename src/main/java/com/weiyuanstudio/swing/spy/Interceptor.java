package com.weiyuanstudio.swing.spy;

import net.bytebuddy.asm.Advice;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Executable;

import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;

public class Interceptor {
    private static final Object lock = new Object();
    private static MouseListener mouseListener = null;
    private static FloatingWindow floatingWindow;

    //    @RuntimeType
//    public static Object interceptor(@This Constructor instance) {
//        System.out.println("instance = " + instance);
//        return instance;
//    }
    @Advice.OnMethodExit(/*onThrowable = Throwable.class,*/ /*backupArguments = false*/)
    public static void after(
            @Advice.This(typing = DYNAMIC, optional = true) Object target,
            @Advice.Origin Executable method,
            @Advice.AllArguments(readOnly = false, typing = DYNAMIC) Object[] args,
            @Advice.Return(readOnly = false, typing = DYNAMIC) Object returnValue  //,
//    @Advice.Thrown(readOnly = false, typing = DYNAMIC) Throwable throwable
    ) {
//        System.out.println("[C] << " + method);
        try {
            injectSpy(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void injectSpy(Object target) {
        if (target instanceof Component) {
            Component c = (Component) target;
            c.addMouseListener(createMouseListener());
        }
    }

    private static MouseListener createMouseListener() {
        if (mouseListener == null) {
            mouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    int downKey = KeyEvent.CTRL_DOWN_MASK; // 可以 | 跟更多按键
                    if ((e.getModifiersEx() & downKey) != downKey) return;

                    Component component = e.getComponent();
                    String[] text = printComponentStack(component);

                    synchronized (lock) {
                        if (floatingWindow != null) {
                            floatingWindow.dispose();
                            floatingWindow = null;
                        }
                    }
                    floatingWindow = new FloatingWindow(text[0], e.getLocationOnScreen(), text[1]);
                    floatingWindow.setVisible(true);
                }
            };
        }
        return mouseListener;
    }

    private static String[] printComponentStack(Component c) {
        if (c == null) return new String[]{"", ""};
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        int count = 1;
        Component p = c;
        String clipboardText = "";
        do {
            Class<? extends Component> clazz = p.getClass();
            String clazzName = clazz.getName();
            if (count != 1) {
                sb.append("<br/>");
            } else {
                clipboardText = clazzName;
            }
            sb.append(count).append(": ").append(clazzName.replaceAll("javax.swing.", ""));
            p = p.getParent();
            ++count;
        } while (p != null);

        sb.append("</html>");
        return new String[]{sb.toString(), clipboardText};
    }
}
