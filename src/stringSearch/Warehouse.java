package stringSearch;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class Warehouse {

	private static final int WAREHOUSE_CAPACITY = 1000;
	private Vector<File> filesInDirectory;
	private Vector<String> lines;
	private int numberOfFiles;
	private HashMap<String, String> foundFiles;

	public Warehouse(File dir) {
		if (dir == null) {
			System.out.println("Searching in default directory C:\\");
			dir = new File("C:\\");
		}
		this.filesInDirectory = new Vector<File>();
		this.lines = new Vector<String>();
		this.foundFiles = new HashMap<String, String>();
		findFilesInDirectory(dir, 0, filesInDirectory);

	}

	public HashMap<String, String> getFoundFiles() {
		return foundFiles;
	}

	public void printFoundFiles(String searchedString) {
		if (foundFiles.isEmpty())
			System.out.println("String \"" + searchedString + "\" was not found");
		else {
			System.out.format("%-36s%-36s%s%n", "Searched string", "File name",
					"Line number");
			for (String fileName : foundFiles.keySet()) {
				System.out.format("%-36s%-36s%s%n", searchedString, fileName,
						foundFiles.get(fileName));
			}
		}
	}

	public Vector<String> getLines() {
		return lines;
	}

	private void findFilesInDirectory(File dir, int idx,
			Vector<File> vectorWithFiles) {
		File[] listOfFiles = dir.listFiles();
		if (listOfFiles != null) {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					findFilesInDirectory(listOfFiles[i], idx, vectorWithFiles);
				} else {
					if (listOfFiles[i].getName().endsWith(".txt")) {
						vectorWithFiles.add(idx, listOfFiles[i]);
						idx++;
						numberOfFiles++;
					}
				}
			}
		}
	}

	public Vector<File> getFilesInDirectory() {
		return filesInDirectory;
	}

	public synchronized void addLine(String line) throws InterruptedException {
		while (lines.size() == WAREHOUSE_CAPACITY)
			wait();
		lines.add(line);
		notify();
	}

	public synchronized String getLine() throws InterruptedException {
		while (lines.size() == 0)
			wait();
		String line = lines.firstElement();
		lines.removeElement(line);
		notify();
		return line;
	}

	public synchronized File getFile() throws InterruptedException {
		while (filesInDirectory.size() == 0)
			wait();
		System.out.println(numberOfFiles-1);
		File fileName = filesInDirectory.firstElement();
		filesInDirectory.removeElement(fileName);
		notify();
		return fileName;
	}

	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}

}
