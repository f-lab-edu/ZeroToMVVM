# ZeroToMvvm 
### 노베이스 아키텍처부터 MVVM까지의 진화과정을 공부해보자! 
## *****Tech Stack***** 
| 구분 | Tech |
|:---|:---------------------------------------------------------------------------|
| Language | Kotlin |
| Network | Retrofit, Okhttp |
| DataBase | Room |
| Asynchronous | Coroutine, Flow |
| DI | Dagger-Hilt |
| Serialization | Kotlinx-Serialization |
| ETC |Jetpack-Navigation, ViewBinding, Glide, ListAdapter |
</br>

## *****MVP / MVVM*****
![image](https://github.com/f-lab-edu/ZeroToMVVM/assets/81726145/5fc34ee7-7fd2-4432-9368-86016f0025fa)

</br>

## *****Architecture*****
### Google-Recommended-Architecture
![image](https://github.com/f-lab-edu/ZeroToMVVM/assets/81726145/2255dfd5-0d51-485c-8837-aeffd6177962)

</br>

## *****Data Flow***** 
![image](https://github.com/f-lab-edu/ZeroToMVVM/assets/81726145/89dd69ad-2070-4499-8c06-9acb80410453)  
</br>

## *****Video***** 
|Paging|Paging Fail Retry|Wish|Detail Page|
|:-----:|:-----:|:-----:|:-----:|
|<img width="240" src="https://github.com/f-lab-edu/ZeroToMVVM/assets/81726145/7b197faa-b44d-4e1f-a7be-556477ac0f94">|<img width="240" src="https://github.com/f-lab-edu/ZeroToMVVM/assets/81726145/f5fd02ae-4dab-4696-8c99-06011c8887fb">|<img width="240" src="https://github.com/f-lab-edu/ZeroToMVVM/assets/81726145/25eaa239-e329-476c-9aea-11896467445b">|<img width="240" src="https://github.com/f-lab-edu/ZeroToMVVM/assets/81726145/73b232e8-562a-47b6-ba2a-a9ce8abe6c4b">|
</br>


## *****Package Structure***** 
```
├── data
│   ├── local
│   │   └── model
│   ├── mapper
│   ├── remote
│   │   └── model
│   └── repository
├── di
├── domain
│   ├── model
│   └── repository
└── ui
    ├── detail
    └── main
        ├── adapter
        ├── home
        ├── mapper
        ├── model
        └── wish
```
