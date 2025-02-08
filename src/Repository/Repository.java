package Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Repository {
	
	private File repository;
	
	public Repository(String path) {
		repository = new File(path);
		if(!repository.exists()) repository.mkdirs();
	}
	
	public void createFile(String path) {
		File file = new File(repository.getAbsolutePath() + path);
		File parentDir = file.getParentFile();
		
		if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
      
		 try {
			if(file.exists()) file.delete();
			file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error on file creation: " + e.getMessage());
        }
	}
	
	private List<String> listFiles(File directory) {
		List<String> fileList = new ArrayList<String>();
		
		if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                	if(!file.isDirectory()) fileList.add(file.getAbsolutePath());
                	
                    if (file.isDirectory()) fileList.addAll(this.listFiles(file));
                }
            }
        }
		
		return fileList;
	}

}
