package stringSearch;


public class Consumer extends Thread {
	private Warehouse warehouse;
	private String searchedString = null;
	private boolean foundString = false;
	private int userChoice;
	public Consumer(String searchedString, Warehouse warehouse, int userChoice) {
		this.warehouse = warehouse;
		this.searchedString = searchedString;
		this.userChoice = userChoice;
	}

	private void searchString(String line) {
		if(userChoice == 2)
		{
			line = line.toLowerCase();
			this.searchedString = this.searchedString.toLowerCase();
		}
		if (line.contains(this.searchedString)) {
			this.foundString = true;
		} else
			this.foundString = false;
	}
	private synchronized void addFoundFiles(String fileName,String lineIdx){
		if(warehouse.getFoundFiles().containsKey(fileName)){
			lineIdx = warehouse.getFoundFiles().get(fileName) + "," +lineIdx;
		}
		warehouse.getFoundFiles().put(fileName, lineIdx);
	}

	@Override
	public void run() {

		try {
			while (true) {
				String line = warehouse.getLine();
				searchString(line);
				System.out.println("searching in " + line.substring(line.lastIndexOf("\n") + 1,line.lastIndexOf("\t")));
				if (foundString) {
					int startIndexForFile = line.lastIndexOf("\n");
					int startIndexForLineNumber = line.lastIndexOf("\t");
					String  fileName = line.substring(startIndexForFile + 1,startIndexForLineNumber);
					String lineIdx = line.substring(startIndexForLineNumber + 1);
					addFoundFiles(fileName, lineIdx);
				}
				sleep(200);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
	}

}
