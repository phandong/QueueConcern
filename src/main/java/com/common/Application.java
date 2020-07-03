package com.common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class Application {

	private volatile boolean isRunning = true;
	private static Queue queue;
	private static ServerSocket serverSocket;

	public static void main(String[] args) {
		System.out.println("[Receiver] Running...");

		while (true) {
			try {
				Socket socket = serverSocket.accept();
				
			} catch (IOException e) {
				System.out.println("[Receiver] Something get wrong with the comunication");
			}
		}
		

	}

}
