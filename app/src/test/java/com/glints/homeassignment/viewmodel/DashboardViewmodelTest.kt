package com.glints.homeassignment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glints.homeassignment.LiveDataTestUtil
import com.glints.homeassignment.model.Balance
import com.glints.homeassignment.model.TransactionHistory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock

class DashboardViewmodelTest {
    private val TAG = DashboardViewmodelTest::class.java.simpleName
    @Mock
    private lateinit var dashboardViewModel: DashboardViewmodel
    private lateinit var balance: Balance
    private lateinit var transactionHistory: TransactionHistory

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        dashboardViewModel = DashboardViewmodel()
        balance = mock(Balance::class.java)
        transactionHistory = mock(TransactionHistory::class.java)
    }

    @Test
    fun getUserBalance() {
        val dummyBalance = Balance("success", "", "123", "9900")
        dashboardViewModel.setDummyBalance(dummyBalance)
        val data = LiveDataTestUtil.getValue(dashboardViewModel.getUserBalance())
        assertNotNull(data)
        assertEquals("data null", dummyBalance, data)
        print("data-Balance: " + data.balance)
    }

    @Test
    fun getHistoryTransactions() {
        val dataDummy =
            TransactionHistory(
                "000", 1000, "21-March-2022", "Test Transaction",
                "Transfer", "123", "Dummy"
            )
        val dummyHistory = ArrayList<TransactionHistory>()
        dummyHistory.add(dataDummy)
        dashboardViewModel.setDummyHistoryTransaction(dummyHistory)
        val data = LiveDataTestUtil.getValue(dashboardViewModel.getHistoryTransactions())
        assertNotNull(data)
        assertEquals("data null", dummyHistory, data)
        print("dataDummyHistoryTransaction: $data")
    }
}