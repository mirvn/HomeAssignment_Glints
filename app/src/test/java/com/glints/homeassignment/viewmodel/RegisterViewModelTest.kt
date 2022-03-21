package com.glints.homeassignment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glints.homeassignment.LiveDataTestUtil
import com.glints.homeassignment.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class RegisterViewModelTest {
    private val TAG = RegisterViewModelTest::class.java.simpleName

    @Mock
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var user: User

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        registerViewModel = RegisterViewModel()
        user = Mockito.mock(User::class.java)
    }

    @Test
    fun getUserRegistered() {
        val userDummy = User("Success", "", "xxx", "test", "123")
        registerViewModel.setDummyRegister(userDummy)
        val data = LiveDataTestUtil.getValue(registerViewModel.getUserRegistered())
        assertNotNull(data)
        assertEquals("data null", userDummy, data)
        print("data-userRegistered: $data")
    }
}