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

</br>

## *****Technical Experience*****

## 1) 비대해진 Activity의 책임 분할
### ■ 문제
- 아무런 아키텍처를 적용하지 않았을 때, Activity가 데이터 호출, 가공, UI 갱신 등 너무 많은 책임을 가지게
되었음. 이로 인해, 코드량이 많아 질 수록, 코드를 수정하기가 힘들어졌음
### ■ 해결
- MVP 혹은 MVVM 패턴을 적용함으로써, Data 호출/ 가공에 대한 책임을 Model, UI 갱신에 대한 책임을
View, UI 비즈니스 로직에 대한 책임을 ViewModel(Presenter)로 구분하여 코드를 구성 하고,
ViewModel(Presenter)를 통한 데이터 전달을 통해 VIew와 Model간의 의존성을 가지지 않도록 수정 함.
- 추가로, ViewModel을 AAC ViewModel로 구성함으로써, 화면 회전 등에 발생하는 Configuration Change
발생 시에도 UI state를 보존할 수 있는 이점을 얻음.

</br> 

## 2) Immutable한 구성으로 프로젝트 안정성 향상
### ■ 문제
- Model과 View 사이에 데이터를 주고 받는 과정에 있어서, mutable한 데이터 전달을 했음.
이로 인해, 데이터 전달 과정에서 데이터가 변경될 가능성이 생김으로 데이터 안정성이 떨어짐.
### ■ 해결
- Immutable 데이터 구조와 copyOf()함수를 활용함으로써 데이터 변경 가능 지점을 제한함.
- ListAdapter를 이용한 RecyclerVIew 사용을 통해, 읽기 전용 Collection 사용을 강제함.

</br> 

## 3) Jetpack-Navigation을 이용한 간결한 Fragment 관리 로직
### ■ 문제
- FragmentManager를 이용해 Fragment의 backStack을 Transaction 단위로 관리해야는 기존 방식은
작성해야하는 보일러 코드가 많았고, Configuration Change 발생 시, hide되었던 Fragment가 전부 show
처리가 되는 등의 개발자가 예상하기 힘든 실수할 수 있는 요소들이 많았음.
### ■ 해결
- Jetpack-Navigation를 이용하여 Fragment의 Transaction을 관리하던 보일러 코드를 제거하고 Fragment
관리의 안정성을 높임.

</br> 

## 4) Paging3를 사용하지 않은 ScrollListener를 이용한 페이징 처리
- Paging3 사용 경험이 있기 때문에, 라이브러리를 사용하지 않고는 어떤 방식으로 페이징이 구현되는지 궁금하여
RecyclerView의 ScrollListener를 이용하여 따로 페이징 기능을 구현해 보았음.

</br> 

##  5) RecyclerView.ViewHolder의 올바른 사용
- Kotlin in Action을 읽고, RecyclerView.Adapter 내부에 inner class로 ViewHolder를 정의하면, 모든
ViewHolder가 Adapter에게 내부적으로 참조를 가지게 되어 불필요한 메모리 참조가 발생됨을 알게됨.
- Adapter의 onBindViewHolder에서 clickListener를 연결해주는 방식은 매번 Data가 ViewHolder에 bind될
때마다 clickListener를 연결하게 됨으로, onCreateViewHolder에서 clickListener를 연결해주는 방식이 더 좋은
방식임을 알게됨
