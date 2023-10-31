package com.kova700.zerotomvvm

interface PokemonItemClickListener {
    fun onItemClick(itemPosition: Int)
    fun onHeartClick(itemPosition: Int)
    //CardView 누르면 디테일 Activity로 이동(pokemon 정보 가지고 가야함),
    //디테일 Activity에서 하트 풀면 나와서 하트 풀린거 반영되어야함 (다시 누르면 반영 X)

    //하트 누르면, WishFragment목록에 추가되고, 하트 눌린 모양으로 바뀌어야함 (다시 눌러서 하트 취소하면 WishFragment에 변화 전달하지 않음)
    //(데이터 객체를 전달만 하면 WishFragment목록에 추가할 수 있음)

    //Wish탭에서는
    //하트 눌러져있어야하고, 누르면 목록에서 지워져야함
    //목록에서 해당 데이터를 지워야함(데이터를 순회해서 검색하는건 비효율적이니 인덱스로 지워야할 듯함)
    //(아이템이 눌렸을 때, 눌린 아이템 인덱스로 해당 아이템 삭제)
    //다시 HomeFragment로 돌아올 때, 변경사항이 있으면 HomeFragment에도 반영을 해야함
    //변경 데이터의 객체 혹은 이름을 전달해서 그 아이템의 상태를 변경해야함 (북마크 안눌린 상태로) (어쩔 수 없이 순회)
}