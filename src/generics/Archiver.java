package generics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;



public class Archiver {

	public void createJar(File jarArchive, File[] filesToBeJar) throws IOException{
		int maxBufferSize = 10240;
		byte[] buffer = new byte[maxBufferSize];

		FileOutputStream fos = new FileOutputStream(jarArchive); 
		JarOutputStream jos = new JarOutputStream(fos);

		for(int i = 0; i<filesToBeJar.length; i++ ){
			if(filesToBeJar[i] == null 
					|| !filesToBeJar[i].exists()
					|| filesToBeJar[i].isDirectory() 
				/*|| !filesToBeJar[i].getName().substring((filesToBeJar[i].getName().lastIndexOf("."))+1, (filesToBeJar[i].getName().length())).equals("class")*/){
				continue;
			}

			System.out.println("ADDING "+ filesToBeJar[i].getName());
			JarEntry jarAdd = new JarEntry(filesToBeJar[i].getName());
			jarAdd.setTime(filesToBeJar[i].lastModified());
			jos.putNextEntry(jarAdd);

			// Write file to archive
			FileInputStream in = new FileInputStream(filesToBeJar[i]);
			while (true) {
				int bytesRead = in.read(buffer, 0, buffer.length);
				if (bytesRead <= 0)
					break;
				jos.write(buffer, 0, bytesRead);
				
			}
			in.close();

		}
		jos.close();
	    fos.close();
	    System.out.println("Adding completed OK");

	}
	
}
