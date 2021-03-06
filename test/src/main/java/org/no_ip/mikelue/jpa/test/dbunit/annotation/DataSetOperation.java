package org.no_ip.mikelue.jpa.test.dbunit.annotation;

import org.dbunit.operation.DatabaseOperation;

import java.lang.annotation.Annotation;

/**
 * This type is an {@link Enum} mapping to {@link DatabaseOperation} which
 * is compatible with {@link Annotation}.<p>
 */
public enum DataSetOperation {
    /**
     * Map to {@link DatabaseOperation#INSERT}.<p>
     */
    Insert(DatabaseOperation.INSERT),
    /**
     * Map to {@link DatabaseOperation#CLEAN_INSERT}.<p>
     */
    CleanInsert(DatabaseOperation.CLEAN_INSERT),
    /**
     * Map to {@link DatabaseOperation#DELETE}.<p>
     */
    Delete(DatabaseOperation.DELETE),
    /**
     * Map to {@link DatabaseOperation#DELETE_ALL}.<p>
     */
    DeleteAll(DatabaseOperation.DELETE_ALL),
    /**
     * Map to {@link DatabaseOperation#UPDATE}.<p>
     */
    Update(DatabaseOperation.UPDATE),
    /**
     * Map to {@link DatabaseOperation#REFRESH}.<p>
     */
    Refresh(DatabaseOperation.REFRESH),
    /**
     * Map to {@link DatabaseOperation#TRUNCATE_TABLE}.<p>
     */
    TuncateTable(DatabaseOperation.TRUNCATE_TABLE),
    /**
     * Map to {@link DatabaseOperation#NONE}.<p>
     */
    None(DatabaseOperation.NONE);

    private DatabaseOperation databaseOperation;

    /**
     * Construct enum with mapped {@link DatabaseOperation}.<p>
     *
     * @param newDatabaseOperation The mapped database operation
     */
    DataSetOperation(DatabaseOperation newDatabaseOperation)
    {
        databaseOperation = newDatabaseOperation;
    }

    /**
     * Gets the database operation in <a href="http://www.dbunit.org/">DbUnit</a>.<p>
     *
     * @return one of constant from {@link DatabaseOperation}
     */
    public DatabaseOperation getDatabaseOperation()
    {
        return databaseOperation;
    }
}
