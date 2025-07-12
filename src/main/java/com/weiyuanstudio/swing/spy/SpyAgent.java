package com.weiyuanstudio.swing.spy;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.awt.*;
import java.lang.instrument.Instrumentation;

public class SpyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("com.weiyuanstudio.swing.spy: LOADED");
        System.out.println("   _______          _______ _   _  _____        _____ _______     __\n" +
                "  / ____\\ \\        / /_   _| \\ | |/ ____|      / ____|  __ \\ \\   / /\n" +
                " | (___  \\ \\  /\\  / /  | | |  \\| | |  __ _____| (___ | |__) \\ \\_/ / \n" +
                "  \\___ \\  \\ \\/  \\/ /   | | | . ` | | |_ |______\\___ \\|  ___/ \\   /  \n" +
                "  ____) |  \\  /\\  /   _| |_| |\\  | |__| |      ____) | |      | |   \n" +
                " |_____/    \\/  \\/   |_____|_| \\_|\\_____|     |_____/|_|      |_|   \n" +
                "                                                                    \n");

        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.visit(Advice.to(Interceptor.class).on(ElementMatchers.isConstructor()));

        new AgentBuilder
                .Default()
                .disableClassFormatChanges()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.RedefinitionStrategy.Listener.StreamWriting.toSystemError())
                .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
                .with(AgentBuilder.InstallationListener.StreamWriting.toSystemError())
                .type(ElementMatchers.isSubTypeOf(Component.class))
                .transform(transformer)
                .installOn(inst);
    }
}
