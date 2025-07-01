package com.example.expt.service.impl;

import com.example.expt.service.BackupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupServiceImpl implements BackupService {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    private static final String BACKUP_DIR = "backups";

    @Override
    public Resource createDatabaseBackup() {
        try {
            String backupFileName = getBackupFileName();
            File backupDir = new File(BACKUP_DIR);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            File backupFile = new File(backupDir, backupFileName);
            String databaseName = extractDatabaseName(databaseUrl);
            String host = extractHost(databaseUrl);
            String port = extractPort(databaseUrl);

            ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_dump",
                "-h", host,
                "-p", port,
                "-U", databaseUsername,
                "-f", backupFile.getAbsolutePath(),
                "-v",
                "--no-password",
                databaseName
            );

            processBuilder.environment().put("PGPASSWORD", databasePassword);
            
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Database backup failed with exit code: " + exitCode);
            }

            return new FileSystemResource(backupFile);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to create database backup", e);
        }
    }

    @Override
    public String getBackupFileName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return "expense_tracker_backup_" + now.format(formatter) + ".sql";
    }

    private String extractDatabaseName(String url) {
        String[] parts = url.split("/");
        String dbPart = parts[parts.length - 1];
        return dbPart.split("\\?")[0];
    }

    private String extractHost(String url) {
        String cleanUrl = url.replace("jdbc:postgresql://", "");
        String[] parts = cleanUrl.split(":");
        return parts[0];
    }

    private String extractPort(String url) {
        String cleanUrl = url.replace("jdbc:postgresql://", "");
        if (cleanUrl.contains(":") && cleanUrl.split(":").length > 1) {
            String portPart = cleanUrl.split(":")[1];
            return portPart.split("/")[0];
        }
        return "5432";
    }
}