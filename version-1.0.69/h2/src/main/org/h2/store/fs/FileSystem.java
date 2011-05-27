/*
 * Copyright 2004-2008 H2 Group. Licensed under the H2 License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.store.fs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * The file system is a storage abstraction.
 */
public abstract class FileSystem {

    public static final String MEMORY_PREFIX = "memFS:";
    public static final String MEMORY_PREFIX_LZF = "memLZF:";
    public static final String DB_PREFIX = "jdbc:";
    public static final String ZIP_PREFIX = "zip:";

    /**
     * Get the file system object.
     *
     * @param fileName the file name or prefix
     * @return the file system
     */
    public static FileSystem getInstance(String fileName) {
        if (isInMemory(fileName)) {
            return FileSystemMemory.getInstance();
        } else if (fileName.startsWith(DB_PREFIX)) {
            return FileSystemDatabase.getInstance(fileName);
        } else if (fileName.startsWith(ZIP_PREFIX)) {
            return FileSystemZip.getInstance();
        }
        return FileSystemDisk.getInstance();
    }

    private static boolean isInMemory(String fileName) {
        return fileName != null && (fileName.startsWith(MEMORY_PREFIX) || fileName.startsWith(MEMORY_PREFIX_LZF));
    }

    /**
     * Get the length of a file.
     *
     * @param fileName the file name
     * @return the length in bytes
     */
    public abstract long length(String fileName);

    /**
     * Rename a file if this is allowed.
     *
     * @param oldName the old fully qualified file name
     * @param newName the new fully qualified file name
     * @throws SQLException
     */
    public abstract void rename(String oldName, String newName) throws SQLException;

    /**
     * Create a new file.
     *
     * @param fileName the file name
     * @return true if creating was successful
     */
    public abstract boolean createNewFile(String fileName) throws SQLException;

    /**
     * Checks if a file exists.
     *
     * @param fileName the file name
     * @return true if it exists
     */
    public abstract boolean exists(String fileName);

    /**
     * Delete a file.
     *
     * @param fileName the file name
     */
    public abstract void delete(String fileName) throws SQLException;

    /**
     * Try to delete a file.
     *
     * @param fileName the file name
     * @return true if it could be deleted
     */
    public abstract boolean tryDelete(String fileName);

    /**
     * Create a new temporary file.
     *
     * @param prefix the file name prefix
     * @param suffix the file name suffix
     * @param deleteOnExit if the file should be deleted when the system exists
     * @param inTempDir if the file should be stored in the temp file
     * @return the file name
     */
    public abstract String createTempFile(String prefix, String suffix, boolean deleteOnExit, boolean inTempDir) throws IOException;

    /**
     * List the files in the given directory.
     *
     * @param directory the directory
     * @return the list of fully qualified file names
     */
    public abstract String[] listFiles(String directory) throws SQLException;

    /**
     * Delete a directory or file and all subdirectories and files.
     *
     * @param directory the directory
     */
    public abstract void deleteRecursive(String directory) throws SQLException;

    /**
     * Check if a file is read-only.
     *
     * @param fileName the file name
     * @return if it is read only
     */
    public abstract boolean isReadOnly(String fileName);

    /**
     * Normalize a file name.
     *
     * @param fileName the file name
     * @return the normalized file name
     */
    public abstract String normalize(String fileName) throws SQLException;

    /**
     * Get the parent directory of a file or directory.
     *
     * @param fileName the file or directory name
     * @return the parent directory name
     */
    public abstract String getParent(String fileName);

    /**
     * Check if it is a file or a directory.
     *
     * @param fileName the file or directory name
     * @return true if it is a directory
     */
    public abstract boolean isDirectory(String fileName);

    /**
     * Check if the file name includes a path.
     *
     * @param fileName the file name
     * @return if the file name is absolute
     */
    public abstract boolean isAbsolute(String fileName);

    /**
     * Get the absolute file name.
     *
     * @param fileName the file name
     * @return the absolute file name
     */
    public abstract String getAbsolutePath(String fileName);

    /**
     * Get the last modified date of a file
     *
     * @param fileName the file name
     * @return the last modified date
     */
    public abstract long getLastModified(String fileName);

    /**
     * Check if the file is writable.
     *
     * @param fileName the file name
     * @return if the file is writable
     */
    public abstract boolean canWrite(String fileName);

    /**
     * Copy a file from one directory to another, or to another file.
     *
     * @param original the original file name
     * @param copy the file name of the copy
     */
    public abstract void copy(String original, String copy) throws SQLException;

    /**
     * Create all required directories.
     *
     * @param directoryName the directory name
     */
    public void mkdirs(String directoryName) throws SQLException {
        createDirs(directoryName + "/x");
    }

    /**
     * Create all required directories that are required for this file.
     *
     * @param fileName the file name (not directory name)
     */
    public abstract void createDirs(String fileName) throws SQLException;

    /**
     * Get the file name (without directory part).
     *
     * @param name the directory and file name
     * @return just the file name
     */
    public abstract String getFileName(String name) throws SQLException;

    /**
     * Check if a file starts with a given prefix.
     *
     * @param fileName the complete file name
     * @param prefix the prefix
     * @return true if it starts with the prefix
     */
    public abstract boolean fileStartsWith(String fileName, String prefix);

    /**
     * Create an output stream to write into the file.
     * 
     * @param fileName the file name
     * @param append if true, the file will grow, if false, the file will be
     *            truncated first
     * @return the output stream
     */
    public abstract OutputStream openFileOutputStream(String fileName, boolean append) throws SQLException;

    /**
     * Open a random access file object.
     *
     * @param fileName the file name
     * @param mode the access mode. Supported are r, rw, rws, rwd
     * @return the file object
     */
    public abstract FileObject openFileObject(String fileName, String mode) throws IOException;

    /**
     * Create an input stream to read from the file.
     *
     * @param fileName the file name
     * @return the input stream
     */
    public abstract InputStream openFileInputStream(String fileName) throws IOException;
}
