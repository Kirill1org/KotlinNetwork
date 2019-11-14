package bonch.dev.kotlinnetwork.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class PhotoRealm : RealmObject() {

    var id:Int = 0
    var title : String? = null
    var url : String? = null
}