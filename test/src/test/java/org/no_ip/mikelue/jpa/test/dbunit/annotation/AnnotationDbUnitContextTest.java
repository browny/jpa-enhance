package org.no_ip.mikelue.jpa.test.dbunit.annotation;

import org.no_ip.mikelue.jpa.test.dbunit.AbstractDbUnitEnvTestBase;
import org.no_ip.mikelue.jpa.test.dbunit.DbUnitBuilder;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * Test the before/after action for annotation context.<p>
 */
public class AnnotationDbUnitContextTest extends AbstractDbUnitEnvTestBase {
    private AnnotationDbUnitContext dbUnitContext;

    public AnnotationDbUnitContextTest() {}

    /**
     * Test if {@link DatabaseOperation#Insert} is executed before this method.<p>
     */
    @Test
    @OpDataSet(dataSetClazz={ForeignKeyYamlDataSet1.class, ForeignKeyYamlDataSet2.class}, beforeOperation=DataSetOperation.Insert)
    public void beforeOperation()
    {
        Assert.assertEquals(
            getJdbcTmpl().queryForInt(
                "SELECT COUNT(*) FROM tt_key"
            ),
            2
        );
        Assert.assertEquals(
            getJdbcTmpl().queryForInt(
                "SELECT COUNT(*) FROM tt_box"
            ),
            4
        );
    }

    /**
     * Test if {@link DatabaseOperation#Insert} is executed by {@link #beforeOperation}.<p>
     *
     * This method is used to execute {@link DataSetOperation#Delete}.<p>
     */
    @Test(dependsOnMethods="beforeOperation")
    @OpDataSet(dataSetClazz={ForeignKeyYamlDataSet1.class, ForeignKeyYamlDataSet2.class}, afterOperation=DataSetOperation.Delete)
    public void performOperation()
    {
        Assert.assertEquals(
            getJdbcTmpl().queryForInt(
                "SELECT COUNT(*) FROM tt_key"
            ),
            2
        );
        Assert.assertEquals(
            getJdbcTmpl().queryForInt(
                "SELECT COUNT(*) FROM tt_box"
            ),
            4
        );
    }

    /**
     * Test if {@link DatabaseOperation#Delete} is executed by {@link #afterOperation}.<p>
     */
    @Test(dependsOnMethods="performOperation")
    public void checkAfterOperation()
    {
        Assert.assertEquals(
            getJdbcTmpl().queryForInt(
                "SELECT COUNT(*) FROM tt_key"
            ),
            0
        );
        Assert.assertEquals(
            getJdbcTmpl().queryForInt(
                "SELECT COUNT(*) FROM tt_box"
            ),
            0
        );
    }

    @BeforeClass
    private void prepareEnv() throws SQLException
    {
        dbUnitContext = new AnnotationDbUnitContext(getDbUnitBuilder());

        prepareForeignKeySchema();
    }

    @BeforeMethod
    private void performBeforeOperation(Method method)
    {
        OpDataSet opDataSet = method.getAnnotation(OpDataSet.class);
        if (opDataSet == null) {
            return;
        }

        dbUnitContext.beforeOperation(opDataSet);
    }
    @AfterMethod
    private void performAfterOperation(Method method)
    {
        OpDataSet opDataSet = method.getAnnotation(OpDataSet.class);
        if (opDataSet == null) {
            return;
        }

        dbUnitContext.afterOperation(opDataSet);
    }
}
