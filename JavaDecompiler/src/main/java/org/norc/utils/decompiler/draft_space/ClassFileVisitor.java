package main.java.org.norc.utils.decompiler.draft_space;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class ClassFileVisitor extends SimpleFileVisitor<Path> {
	
		private String pattern = ".class";
		private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		 
		 
		// Compares the glob pattern against
	    // the file or directory name.
	    void find(Path file) {
	        Path name = file.getFileName();
	        if (name != null && matcher.matches(name)) {
	            System.out.println(file);
	        }
	    }
	
	    // Print information about
	    // each type of file.
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
	        System.out.println("Located: " + file.getFileName());
	        return CONTINUE;
	    }
	
	    // Print each directory visited.
	    @Override
	    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
	        System.out.format("Directory: %s%n", dir);
	        return CONTINUE;
	    }
	
	    // If there is some error accessing
	    // the file, let the user know.
	    // If you don't override this method
	    // and an error occurs, an IOException 
	    // is thrown.
	    @Override
	    public FileVisitResult visitFileFailed(Path file, IOException exc) {
	        System.err.println(exc);
	        return CONTINUE;
	    }
}
