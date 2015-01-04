package main.java.org.norc.utils.decompiler.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import main.java.org.norc.utils.decompiler.utils.FileFinder;

public class Decompiler {
	
		private Path inputDirRoot = null;
		private Path outputDirRoot = null;
		List<Path> listClassFiles = null; 

		/**
		 * This utility will iterate through a directory and decompile every class file in the directory
		 * and its sub-directories. It will output the decompiled java files in the same package structure.
		 * 
		 * It uses the the cfr java decompiler as its engine: http://www.benf.org/other/cfr/
		 *  
		 * @param args
		 * @throws Exception 
		 * 
		 * 		Arguments:
		 *	 	args[0]:	input path
		 *		args[1]:	output path
		 *
		 *TODO: use log4j to log outputs and exceptions 
		 *TODO: handle exceptions in main method
		 *
		 *nextStep: processListOfClassFiles()
		 */
		public static void main(String[] args) {
			Decompiler decompiler = new Decompiler();		
			
			// parse input and output directories if they were supplied as parameters
			// else prompt the user to enter them
			if (args.length == 2) {
				try {
					decompiler.parseArguments(args);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// TODO: implement prompting and input taking for input/output directory
				//decompiler.setInputDir( Paths.get("src/test/resources/input/") );
				//decompiler.setOutputDir( Paths.get("src/test/resources/input/"));
			}
			
			decompiler.run();
		}
		
		
		/*
		 * This method validates the input and output directories and initializes the parameters
		 * 
		 * @param args
		 * @throws Exception, InvalidPathException
		 * 
		 * 		Arguments:
		 *	 	args[0]:	input path
		 *		args[1]:	output path
		 *
		 *TODO: add validation on arguments
		 *TODO: implement validation method/class to validate inputs
		 */
		private void parseArguments(String[] args) throws Exception{
			String inputDirString = args[0];
			System.out.println("Input directory is: " + inputDirString);
			String outputDirString = args[1];
			System.out.println("Output directory is: " + outputDirString);
			
			try {
				// turn inputDirString in to a path
				try{
					this.setInputDir( Paths.get(inputDirString) );
				} 
				catch (InvalidPathException e){
					System.err.println("This is not a valid input directory: " + inputDirRoot);
					e.printStackTrace();
				}
				
				// turn outputDirString in to a path
				try{
					this.setOutputDir( Paths.get(outputDirString) );
				} 
				catch (InvalidPathException e){
					System.err.println("This is not a valid output directory: " + outputDirString);
					e.printStackTrace();
				}
			} 
			catch (Exception e) {
				System.err.print("Unknown Error occured while parsing the input or output directories");
				e.printStackTrace();
			}
		}
		
		
		public void run(){
			
			// Recursively search the input directory to get class files
			try {
				getListofClassFiles();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// decompile the class files
			try {
				processListOfClassFiles();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		/**
		 * This method uses the FileFinder tool to search the input directory to get all class files.
		 * It stores them in the listClassFiles variable
		 * 
		 * FIXME: handle exceptions
		 */
		public void getListofClassFiles() throws Exception{
			
			String pattern = ".class";
			
			FileFinder finder = new FileFinder(pattern);
	        try {
				Files.walkFileTree(inputDirRoot, finder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			setListOfClassFiles( finder.getlistOfFilesFound() );
		}
		
		
		@Deprecated
		private List<File> getListofClassFiles(File file, List<File> tempList){
			
			// recursion: if it's a directory, enter it and search for class files
			if(file.isDirectory()){
				File[] nextFiles = file.listFiles();
				for(File nextFile: nextFiles){
					tempList = getListofClassFiles(nextFile, tempList); 
				}
			} 
			// stop condition: this is a file. If this is a class file, ad it to the list
			else {
				String name = file.getName();
				if (".class".equalsIgnoreCase(name.substring(name.indexOf("."),name.length()))){
					tempList.add(file);
				}
			}
			
			return tempList;
		}
		
		
		/*
		 *  This methods loops through listClassFiles, decompile each classFile,
		 *  creates the sub-directories to mirror the classFile package structure,
		 *  and stores them in outputDirectory
		 */
		public void processListOfClassFiles() throws Exception {
			 
			if (listClassFiles != null){
				for(Path classFile: listClassFiles){					
					copyJavaFileToOutDir( decompile(classFile), getSubdir(classFile));
				}
			} else {
				// TODO: alert user that there are no classes in the input directory
			}
		}
		
		
		/*
		 * TODO: make a call to the jar to decompile one class
		 * TODO: return a Path to the decompiled java class
		 */
		public Path decompile(Path classFile) throws Exception{
			// http://stackoverflow.com/questions/4936266/execute-jar-file-from-a-java-program
			Process proc = Runtime.getRuntime().exec("java -jar Validate.jar");
			proc.waitFor();
			// Then retrieve the process output
			InputStream in = proc.getInputStream();
			InputStream err = proc.getErrorStream();
			// std.out
			byte b[]=new byte[in.available()];
			in.read(b,0,b.length);
			System.out.println(new String(b));
			// std.err
			byte c[]=new byte[err.available()];
			err.read(c,0,c.length);
			System.out.println(new String(c));
			
			Path decompiledJavaFile = null;
			
			return decompiledJavaFile;
		}
		
		

		/*
		 * 
		 * TODO: uses Path to subtract inputdirroot and get package structure that way
		 * TODO: subtract two strings http://stackoverflow.com/questions/24107527/how-to-subtract-two-string-arrays-in-java-as-they-are-shown-in-code
		 * TODO: Sting class contains(CharSequence e) method http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#contains(java.lang.CharSequence)
		 * TODO: String class replace(CharSequence e) method http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#replace(java.lang.CharSequence,%20java.lang.CharSequence)
		 * TODO: Path class relativize(Path other) http://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html#relativize-java.nio.file.Path-
		 * TODO: Path class subpath(int beginIndex, int endIndex) http://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html#subpath-int-int-
		 */
		private Path getSubdir(Path classFile){
			Path subpath = null;
			return subpath;
		}
		
		
		/*
		 * This method writes one class file to output directory
		 * create subdirectories if they don't exist and save java file there
		 */
		public void copyJavaFileToOutDir(Path javaFile, Path subDir ) throws IOException{
			// read the package header from file
			FileReader reader = new FileReader(javaFile.toFile());
			BufferedReader in = new BufferedReader(reader);
			String packageHeader =  in.readLine();
			
			// strip "package " and everything after ";" 
			String temp = packageHeader.substring(8, packageHeader.indexOf(";"));
			// format it to a path relative to the outputDir
			String outputSubDir = temp.replace(".", "/") + "/";
			in.close();
			reader.close(); 
			
			// if subdirs do not exist, create them
			File tempFile = new File(outputDirRoot + outputSubDir);
			if(!(tempFile.exists() || tempFile.isDirectory())){
				boolean isSubDirCreated = tempFile.mkdirs();
			}
			
			// copy java file to subDirs
			Path sourcePath = javaFile.toFile().toPath();
			OutputStream outputStream = new FileOutputStream(outputDirRoot + outputSubDir +javaFile.toFile().getName());
			Files.copy(sourcePath,outputStream);
			outputStream.close();
		}
		
		
		public void setInputDir(Path inputDirRoot) 		{ this.inputDirRoot = inputDirRoot; }
		public Path getInputDir()						{ return this.inputDirRoot; }
		
		public void setOutputDir(Path outputDirRoot) 	{ this.outputDirRoot = outputDirRoot; }
		public Path getOutputDir()						{ return this.outputDirRoot; }
		
		public void setListOfClassFiles(List<Path> list){ this.listClassFiles = list; }
		public List<Path> getlistOfClassFiles()			{ return this.listClassFiles; }
}
