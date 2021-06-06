package com.example.cards

class Car(var name: String, var imageId: Int, var details: String, var color: String) {
    companion object {
        public val cars = arrayOf(
            Car("Red car", R.drawable.car_1, "red devil", "red"),
            Car("Black van", R.drawable.car_2, "black devil", "black"));
    }
}