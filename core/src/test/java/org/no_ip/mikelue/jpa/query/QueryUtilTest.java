package org.no_ip.mikelue.jpa.query;

import mockit.NonStrictExpectations;
import mockit.Mocked;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class QueryUtilTest {
	public QueryUtilTest() {}

	@Mocked
	private Query mockQuery;
	@Mocked
	private TypedQuery<Integer> mockTypedQuery;

	/**
	 * Tests the normal mockQuery(without exception) for getting single result.<p>
	 */
	@Test(dataProvider="GetSingleResult")
	public void getSingleResult(
		final Object sampleResult, Object expectedResult
	) {
		new NonStrictExpectations()
		{{
			mockQuery.getSingleResult();
			result = sampleResult;
		}};

		Assert.assertEquals(QueryUtil.getSingleResult(mockQuery), expectedResult);
	}
	@DataProvider(name="GetSingleResult")
	private Object[][] getGetSingleResult()
	{
		return new Object[][] {
			{ 2456, 2456 },
			{ new NoResultException(), null }
		};
	}

	/**
	 * Tests the normal result(without exception) to get single result by typed mockQuery.<p>
	 */
	@Test(dataProvider="GetTypedSingleResult")
	public void getTypedSingleResult(
		final Object sampleResult, Integer expectedResult
	) {
		new NonStrictExpectations()
		{{
			mockTypedQuery.getSingleResult();
			result = sampleResult;
		}};

		Assert.assertEquals(QueryUtil.getSingleResult(mockTypedQuery), expectedResult);
	}
	@DataProvider(name="GetTypedSingleResult")
	private Object[][] getGetTypedSingleResult()
	{
		return new Object[][] {
			{ 1277, 1277 },
			{ new NoResultException(), null }
		};
	}

	/**
	 * Test if {@link NonUniqueResultException} thrown correctly
	 */
	@Test(expectedExceptions=NonUniqueResultException.class)
	public void getSingleResultWithException()
	{
		new NonStrictExpectations()
		{{
			mockQuery.getSingleResult();
			result = new NonUniqueResultException();
		}};

		QueryUtil.getSingleResult(mockQuery);
	}

	/**
	 * Test if {@link NonUniqueResultException} thrown correctly
	 */
	@Test(expectedExceptions=NonUniqueResultException.class)
	public void getTypedSingleResultWithException()
	{
		new NonStrictExpectations()
		{{
			mockTypedQuery.getSingleResult();
			result = new NonUniqueResultException();
		}};

		QueryUtil.getSingleResult(mockTypedQuery);
	}

    /**
     * Tests the incremental mockQuery for single result
     */
    @Test
    public void getSingleResultByIncrementalQuery()
    {
        SingleResultQueryAction nullAction = new FakeSingleQueryAction();

        /**
         * Asserts the null result
         */
        Assert.assertNull(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, nullAction
            )
        );

        Assert.assertNull(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, nullAction, nullAction
            )
        );
        // :~)

        final String testResult_1 = "test value 1",
              testResult_2 = "test value 2",
              testResult_3 = "test value 3";

        /**
         * Assert the viable data from 1st mockQuery
         */
        Assert.assertEquals(
            QueryUtil.getSingleResultByIncrementalQuery(
                new FakeSingleQueryAction(testResult_1), nullAction, nullAction
            ),
            testResult_1
        );
        // :~)

        /**
         * Assert the viable data from 2ed mockQuery
         */
        Assert.assertEquals(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, new FakeSingleQueryAction(testResult_2), nullAction
            ),
            testResult_2
        );
        // :~)

        /**
         * Assert the viable data from 4th mockQuery
         */
        Assert.assertEquals(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, nullAction, nullAction, new FakeSingleQueryAction(testResult_3)
            ),
            testResult_3
        );
        // :~)
    }

    /**
     * Tests the incremental mockQuery for list result
     */
    @Test
    public void getListResultByIncrementalQuery()
    {
        ListResultQueryAction emptyAction = new FakeListQueryAction();

        /**
         * Asserts the empty result
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, emptyAction
            ).size(),
            0
        );

        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, emptyAction
            ).size(),
            0
        );
        // :~)
        //
        final int testResult_1 = 3,
              testResult_2 = 5,
              testResult_3 = 7;

        /**
         * Assert the viable data from 1st mockQuery
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                new FakeListQueryAction(testResult_1), emptyAction, emptyAction
            ).size(),
            testResult_1
        );
        // :~)

        /**
         * Assert the viable data from 2ed mockQuery
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, new FakeListQueryAction(testResult_2), emptyAction
            ).size(),
            testResult_2
        );
        // :~)

        /**
         * Assert the viable data from 4th mockQuery
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, emptyAction, emptyAction, new FakeListQueryAction(testResult_3)
            ).size(),
            testResult_3
        );
        // :~)
    }
}

class FakeSingleQueryAction implements SingleResultQueryAction {
    private Object expectedResult;

    FakeSingleQueryAction()
    {
        this(null);
    }
    FakeSingleQueryAction(Object newExpectedResult)
    {
        expectedResult = newExpectedResult;
    }

    @Override
    public Object getSingleResult()
    {
        return expectedResult;
    }
}

class FakeListQueryAction implements ListResultQueryAction {
    private List<Object> resultList;

    FakeListQueryAction()
    {
        this(0);
    }
    FakeListQueryAction(int elements)
    {
        resultList = new ArrayList(elements);
        for (int i = 1; i <= elements; i++) {
            resultList.add(i);
        }
    }

    @Override
    public List<Object> getListResult()
    {
        return resultList;
    }
}
