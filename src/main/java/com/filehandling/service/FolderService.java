package com.filehandling.service;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.filehandling.model.Folder;
import com.filehandling.repository.FolderRepository;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;
    

public String createFolder(Folder folder) {
    File folderFile = new File(folder.getPath());
    if (!folderFile.exists()) {
        boolean created = folderFile.mkdirs();
        if (created) {
            folderRepository.save(folder);
            return "Folder created successfully";
        } else {
           // throw new RuntimeException("Failed to create folder at path " + folder.getPath());
            return "Failed to create folder";
        }
    } else {
        //throw new RuntimeException("Folder already exists at path " + folder.getPath());
        return "Folder already exists";
    }
}
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found with id " + id));
    }
    public Folder updateFolder(Long id, String newFolderName, String newFolderPath) {
        // Fetch the existing folder from the database
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found with id " + id));

        // Define the old and new folder paths
        File oldFolderFile = new File(folder.getPath());
        File newFolderFile = new File(newFolderPath, newFolderName);

        // Check if the old folder exists
        if (!oldFolderFile.exists()) {
            throw new RuntimeException("The old folder does not exist: " + oldFolderFile.getPath());
        }

        // Check if the new parent directory exists and create it if necessary
        File newParentDirectory = newFolderFile.getParentFile();
        if (!newParentDirectory.exists()) {
            boolean created = newParentDirectory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + newParentDirectory.getAbsolutePath());
            }
        }

        // Log for debugging
        System.out.println("Attempting to rename folder from " + oldFolderFile.getPath() + " to " + newFolderFile.getPath());

        // Rename the folder in the filesystem
        boolean renameSuccess = oldFolderFile.renameTo(newFolderFile);
        if (!renameSuccess) {
            throw new RuntimeException("Failed to rename folder from " + oldFolderFile.getPath() + " to " + newFolderFile.getPath());
        }

        // Update the folder details in the database
        folder.setName(newFolderName);
        folder.setPath(newFolderFile.getAbsolutePath());

        try {
            Folder updatedFolder = folderRepository.save(folder);
            System.out.println("Folder updated successfully: " + updatedFolder);
            return updatedFolder;
        } catch (Exception e) {
            // Rollback the filesystem change if the database update fails
            boolean rollbackSuccess = newFolderFile.renameTo(oldFolderFile);
            if (!rollbackSuccess) {
                System.err.println("Warning: Failed to rollback filesystem changes for folder " + oldFolderFile.getPath());
            }
            throw new RuntimeException("Failed to update folder in database: " + e.getMessage(), e);
        }
    }
    public String deleteFolder(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found with id " + id));
        File folderFile = new File(folder.getPath());
        if (deleteFolder(folderFile)) {
        	folderRepository.delete(folder);
        	return "delete sucessfully";
        } else {
            throw new RuntimeException("Failed to delete folder at path " + folder.getPath());
        }
    }

    private boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!deleteFolder(file)) {
                        return false;
                    }
                }
            }
        }
        return folder.delete();
    }
}
