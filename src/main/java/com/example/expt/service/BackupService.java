package com.example.expt.service;

import org.springframework.core.io.Resource;

public interface BackupService {
    Resource createDatabaseBackup();
    String getBackupFileName();
}