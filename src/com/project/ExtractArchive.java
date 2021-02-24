/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import java.io.FileNotFoundException;

/**
 *
 * @author addie
 */
public class ExtractArchive {
//    private Log logger;

//	public void setLogger(Log logger) {
//		this.logger = logger;
//	}
	
	public boolean extractArchive(File archive, File destination) throws RarException, IOException {
		Archive arch = null;
//		try {
                    
			arch = new Archive(archive);
//		} catch (RarException e) {
//			logError(e);
//		} catch (Exception e1) {
////			logError(e1);
//		}
		if (arch != null) {
			if (arch.isEncrypted()) {
				logWarn("archive is encrypted cannot extreact");
				return false;
			}
			FileHeader fh = null;
			while (true) {
				fh = arch.nextFileHeader();
				if (fh == null) {
					break;
				}
				String fileNameString = fh.getFileNameString();
				if (fh.isEncrypted()) {
					logWarn("file is encrypted cannot extract: "+ fileNameString);
					continue;
				}
				logInfo("extracting: " + fileNameString);
				try {
					if (fh.isDirectory()) {
						createDirectory(fh, destination);
					} else {
						File f = createFile(fh, destination);
						OutputStream stream = new FileOutputStream(f);
						arch.extractFile(fh, stream);
						stream.close();
					}
				} catch (IOException e) {  
                                    return false;
//                                    throw new Exception("Error extracting the file:IOException");
//                                    new Exception("Error extracting the file:IOException");
//					logError(e, "error extracting the file");
				} catch (RarException e) {
                                    return false;                                    
//                                    new Exception("Error extracting the file:RarException");
//					logError(e,"error extraction the file");
				}
			}
		}
                return true;
	}

	private void logWarn(String warning) {
//		if(logger!=null) logger.warn(warning);
//                System.out.println("warning:"+warning);
	}

	private void logInfo(String info) {
//		if(logger!=null) logger.info(info);
//                System.out.println("info:"+info);
	}


	private File createFile(FileHeader fh, File destination) throws IOException {
		File f = null;
		String name = null;
		if (fh.isFileHeader() && fh.isUnicode()) {
			name = fh.getFileNameW();
		} else {
			name = fh.getFileNameString();
		}
		f = new File(destination, name);
		if (!f.exists()) {
			
				f = makeFile(destination, name);
			
		}
		return f;
	}

	private static File makeFile(File destination, String name)
			throws IOException {
		String[] dirs = name.split("\\\\");
		if (dirs == null) {
			return null;
		}
		String path = "";
		int size = dirs.length;
		if (size == 1) {
			return new File(destination, name);
		} else if (size > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				path = path + File.separator + dirs[i];
				new File(destination, path).mkdir();
			}
			path = path + File.separator + dirs[dirs.length - 1];
			File f = new File(destination, path);
			f.createNewFile();
			return f;
		} else {
			return null;
		}
	}

	private static void createDirectory(FileHeader fh, File destination) {
		File f = null;
		if (fh.isDirectory() && fh.isUnicode()) {
			f = new File(destination, fh.getFileNameW());
			if (!f.exists()) {
				makeDirectory(destination, fh.getFileNameW());
			}
		} else if (fh.isDirectory() && !fh.isUnicode()) {
			f = new File(destination, fh.getFileNameString());
			if (!f.exists()) {
				makeDirectory(destination, fh.getFileNameString());
			}
		}
	}

	private static void makeDirectory(File destination, String fileName) {
		String[] dirs = fileName.split("\\\\");
		if (dirs == null) {
			return;
		}
		String path = "";
		for (String dir : dirs) {
			path = path + File.separator + dir;
			new File(destination, path).mkdir();
		}

	}
}
