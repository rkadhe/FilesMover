import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
//Author:  Rohit Kadhe
/*
INSTRUCTIONS
------------------------------------------------------------------------------
CHANGE SOURCE FOLDER PATH VARIABLE TO PATH OF WHERE THE FILES DESIRED TO MOVE LOCATED
CHANGE DEST FOLDER PATH VARIABLE TO PATH WHERE THE FILES ARE DESIRED TO MOVE TO.
CHANGE THE FOLDER NAME VARIABLE TO ANY NAME DESIRED TO RENAME THE AUTOMATICALLY CREATED FOLDER WHERE THE FILES ARE COPIED
CHANGE THE FILETYPE VARIABLE TO AN EMPTY STRING TO GET ALL FILES IN THE SOURCE PATH
------------------------------------------------------------------------------
*/
public class Files {
    public static void main(String[] args) {
        //Path of where files to move are located
        String sourceFolderpath = "C:\\Users\\rkadh\\Desktop\\Source";
        //Path where files get moved
        String destFolderpath = "C:\\Users\\rkadh\\Desktop";
        String folderName = "Dest";
        String fileType = ".pdf";
        //This methods gets files from sourceFolder as well as subfolders.
        //Also calls moveFiles function
        Files.getAndMoveFiles(sourceFolderpath, destFolderpath, folderName, fileType);
    }
    static void getAndMoveFiles(String sourceFolderPath, String destFolderpath, String folderName, String fileType) {
        FilenameFilter filterByExtension = new FilenameFilter()
        {
            @Override public boolean accept(File dir, String name)
            {
                return name.endsWith(fileType);
            }
        };
        //Create a File object to be able to listFiles in that directory
        File sourceDirectory = new File(sourceFolderPath);
        List < File > files = new ArrayList < > ();
        //ListFiles returns a array of file objects in the source path filtered by extension
        File[] sourceFiles = sourceDirectory.listFiles(filterByExtension);
        //Adds all files in the source directory to files List.
        try {
            for (int i = 0; i < sourceFiles.length; i++) {
                if (sourceFiles[i].isFile() ) {
                    files.add(sourceFiles[i]);
                }
            }
        }catch(NullPointerException e){
            System.out.println("An error has occurred! Please check sourceFolderPath and destFolderPath");
        }
        //Moves all files not in a sub directory to dest path
        moveFiles(files, destFolderpath, folderName);
        //Removes moved files from list.
        files.clear();
        Stack < File > directories = new Stack < > ();
        //Pushes sourceDirectory so that stack contains the source directories contents
        directories.push(sourceDirectory);
        while (!directories.isEmpty()) {
            //Gets first directory or file
            File content = directories.pop();
            if (content.isDirectory()) {
                //Gets all files from subdirectories
                File[] listFiles = content.listFiles(filterByExtension);
                for (int i = 0; i < listFiles.length; i++) {
                    //FOR DEBUGGING
                    System.out.println(listFiles[i].getPath());
                    directories.push( listFiles[i]);
                    //Adds to List if ListFiles[i] is a file
                    if (directories.peek().isFile()) {
                        files.add(directories.peek());
                    }
                }
            }
            //  Moves files and clears array list each iteration
            moveFiles(files,destFolderpath, folderName);
            files.clear();
        }
        if(files.size()==0){
            System.out.println("Done moving all Files!");
        }
        else{
            System.out.println("Something went wrong when trying to move the Files!");
        }
    }
    static void moveFiles(List <File> files, String dest, String folderName) {
        //FOR DEBUGGING
        boolean createdFile;
        //Creates a folder if it doesn't exist
        if (!new File(dest + "\\" + folderName).exists()) {
            createdFile = new File(dest + "\\" + folderName).mkdir();
            //FOR DEBUGGING
            //System.out.println(createdFile);
        }
        //Note destination to move files to cannot be in the source directory
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            try {
                //Create a new file and move it to the folder with given folderName
                //Name the file whatever the original file was called with files.get(i).getName()
                if (file.renameTo(new File(dest + "\\" + folderName + "\\" + file.getName()))) {
                    System.out.println("File moved successfully!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
