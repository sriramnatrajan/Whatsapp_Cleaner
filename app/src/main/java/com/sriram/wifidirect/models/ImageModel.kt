package com.sriram.wifidirect.models

class ImageModel {


    var full_path:String
    lateinit var path:String
    var type:Int = 0
    val isSelected:Boolean = false
    constructor(full_path:String) {
        this.full_path = full_path
    }
    constructor(full_path:String, path:String) {
        this.full_path = full_path
        this.path = path
    }
    constructor(full_path:String, path:String, type:Int) {
        this.full_path = full_path
        this.path = path
        this.type = type
    }
}