-----------------------
This is Project for Glints Assignment
-------------------------------------
<div id="top"></div>

[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://www.ocbc.com/group/gateway">
    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/OCBC_Bank_logo.png/1200px-OCBC_Bank_logo.png" alt="Logo" width="900" height="220">
  </a>

<h3 align="center">Home Assignment - OCBC Singapore Assessment</h3>

  <p align="center">
    Application for test requirements in OCBC
    <br />
    <a href="https://github.com/mirvn/HomeAssignment_Glints"><strong>Explore the docs »</strong></a>
    <br />
  </p>
</div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#features">Features</a></li>
    <li><a href="#usage-and-impelementation">Usage and Impelementation</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://example.com)

<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

* [100% Kotlin](https://kotlinlang.org/)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running, follow these simple steps.

### Installation

Download APK File at 
[here](https://drive.google.com/file/d/1J0h_Il5z6iHi9LPz2O9j_jcYmfBqhARv/view?usp=sharing)

<p align="right">(<a href="#top">back to top</a>)</p>



## Features

1. Register
2. Login
3. Show Balance
4. Show history transaction
5. Make Transfer
<p align="right">(<a href="#top">back to top</a>)</p>


## Usage and Impelementation

Architecture : MVVM

Library used in project:
* Android Jetpack Library
* Material Component
* loopj - Android Asynchronous Http Client
* Kotlin Coroutines
* Mockito
* Junit

This application contain liveData and viewModel implementation from Jetpack Library using this dependency:
```implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.1'```

```implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'```

Using LiveData to get advantage of lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.
Here is some example:
1. initialize variable in ViewModel.class
```private val balanceLiveData = MutableLiveData<Balance>()```
2. giving value using postValue() on background thread
```balanceLiveData.postValue(balance)```
3. observing given value using observer and get the data from it
```
dashboardViewmodel.getUserBalance().observe({ lifecycle }, { balanceData -> //begin observing
            //check data
            if (balanceData.status == getString(R.string.failed) &&
                balanceData.message == getString(R.string.jwt_expired)
            ) {
                val alert = AlertDialog.Builder(this)
                alert.apply {
                    setTitle(getString(R.string.session_expired))
                    setMessage(getString(R.string.sessionExpired_message))
                    setIcon(R.drawable.ic_baseline_lock_clock_24)
                    setPositiveButton(R.string.ok) { _, _ ->
                        sessionLogin.editor.clear().apply() //clear data session
                        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                        finish()
                    }
                    setCancelable(false)
                }.create().show()
            } else {
                showProgressBar(false)
                binding.tvBalance.text = "SGD ${balanceData.balance}" // get data
                binding.tvAccNoDashboard.text = balanceData.accountNo
                binding.tvNameHolder.text =
                    sessionLogin.sharedPreferences.getString(R.string.username.toString(), "")
                        .toString()
            }
        })
        
```  

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Muhammad Irvan
[linkedin](https://linkedin.com/in/mirvn) 

Email: mirvan2599@gmail.com

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/mirvn
[product-screenshot]: https://i.ibb.co/DQ00Fvx/Untitled-design-resize.png
