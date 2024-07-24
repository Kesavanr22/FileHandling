package com.filehandling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filehandling.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {

}
