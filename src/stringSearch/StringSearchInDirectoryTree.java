package stringSearch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringSearchInDirectoryTree {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Scanner input = new Scanner(System.in);
		String path = "D:\\SU_FMI\\2013_2014\\winter_semester_2013\\";
		String path2 = "D:\\SU_FMI\\2014_2015\\winter_semester\\JAVA_SAP\\Assignments\\Assignments\\test";

		System.out.println("Please enter string for searching");
		String word = input.nextLine();
		Warehouse warehouse = new Warehouse(new File(path));
		System.out
				.println("Please choose:\n(1) case sensitive search\n(2) case insensitive search");
		int userChoice = input.nextInt();
		while (userChoice != 1 && userChoice != 2) {
			System.out.println("Please choose a number 1 or 2");
			userChoice = input.nextInt();
		}

		System.out.println("Number of .txt files "
				+ warehouse.getNumberOfFiles());
		List<Producer> producers = new ArrayList<Producer>();
		Producer producer = null;
		for (int i = 0; i < 1000; i++) {
			producer = new Producer(warehouse);
			producers.add(producer);
		}

		for (Producer x : producers) {
			x.start();
		}

		List<Consumer> consumers = new ArrayList<Consumer>();
		Consumer consumer = null;
		for (int i = 0; i < 1000; i++) {
			consumer = new Consumer(word, warehouse, userChoice);
			consumers.add(consumer);
		}

		for (Consumer x : consumers) {
			x.start();
		}

		if (warehouse.getNumberOfFiles() == 0
				&& warehouse.getFilesInDirectory().isEmpty()) {
			for (Consumer x : consumers) {
				x.interrupt();
			}
			for (Producer x : producers) {
				x.interrupt();
			}
			warehouse.printFoundFiles(word);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = (stopTime - startTime) / 1000 % 60;
		System.out.println(elapsedTime + " seconds");
		System.out.println("left files " + warehouse.getNumberOfFiles());
		input.close();
	}
}
