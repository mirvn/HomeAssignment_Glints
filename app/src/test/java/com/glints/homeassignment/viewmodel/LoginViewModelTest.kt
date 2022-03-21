package com.glints.homeassignment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glints.homeassignment.LiveDataTestUtil
import com.glints.homeassignment.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock


class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: User

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        loginViewModel = LoginViewModel()
        user = mock(User::class.java)
    }

    @Test
    fun getUserLogin() { // testing value on liveData
        val userDummy = User("Success", "", "xxx", "test", "123")
        loginViewModel.setDataDummy(userDummy)
        val data = LiveDataTestUtil.getValue(loginViewModel.getUserLogin())
        assertNotNull(data)
        assertEquals("data null", userDummy, data)
        print("dataDummyLogin: $data")
    }
}