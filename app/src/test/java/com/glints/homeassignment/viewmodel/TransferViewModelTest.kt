package com.glints.homeassignment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glints.homeassignment.LiveDataTestUtil
import com.glints.homeassignment.model.Payees
import com.glints.homeassignment.model.Transfer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class TransferViewModelTest {
    private lateinit var transferViewModel: TransferViewModel
    private lateinit var transfer: Transfer
    private lateinit var payees: Payees

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        transferViewModel = TransferViewModel()
        transfer = Mockito.mock(Transfer::class.java)
        payees = Mockito.mock(Payees::class.java)
    }

    @Test
    fun getDataPayees() {
        val dataDummy =
            Payees("123", "dummy", "000")
        val dummyPayee = ArrayList<Payees>()
        dummyPayee.add(dataDummy)
        transferViewModel.setDummyPayees(dummyPayee)
        val data = LiveDataTestUtil.getValue(transferViewModel.getDataPayees())
        assertNotNull(data)
        assertEquals("data null", dummyPayee, data)
        print("dataDummyPayee: $data")
    }

    @Test
    fun getTransferData() {
        val dummyTransfer = Transfer(
            "success", "",
            "123", 1000,
            "Dummy bills", "123"
        )
        transferViewModel.setDummyTransfer(dummyTransfer)
        val data = LiveDataTestUtil.getValue(transferViewModel.getTransferData())
        assertNotNull(data)
        assertEquals("data null", dummyTransfer, data)
        print("dataDummyTransfer: $data")
    }
}