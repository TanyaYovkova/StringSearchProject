package stringSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Producer extends Thread {
	private Warehouse warehouse;
	private File file = null;

	public Producer(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	private synchronized void readFile(File fileName) throws IOException,
			InterruptedException {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			int lineIdx = 1;
			String lineFilenameIdx = null;
			for (String line; (line = br.readLine()) != null;) {
				lineFilenameIdx = line + "\n" + (fileName.getName() + "\t" + lineIdx);
				this.warehouse.addLine(lineFilenameIdx);
				lineIdx++;
			}
		}
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				file = warehouse.getFile();
				readFile(file);
				 warehouse.setNumberOfFiles(warehouse.getNumberOfFiles()-1);
				// System.out.println(Thread.class.getName().toString() +
				// " reading file " + file.getName());
				sleep(500);
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
