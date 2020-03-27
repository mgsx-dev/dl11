package net.mgsx.dl11;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenPaths {
	
	public static void main(String[] args) throws IOException {
		String data = "";
		for(String s : new File("maps").list()){
			if(s.endsWith(".tmx")){
				System.out.println(s);
				if(!data.isEmpty()) data += ",";
				data += s;
			}
		}
		FileWriter fw = new FileWriter(new File("maps.txt"));
		fw.write(data);
		fw.close();
	}
}
