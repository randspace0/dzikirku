package id.gritlabs.dzikirku.model.legacy

import kotlinx.serialization.Serializable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonNames
import id.gritlabs.dzikirku.model.Author

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class NoteLegacy(
    var id:Int? =null,
    var title:String?="",
    var subtitle:String?="",
    @SerialName("speaker") @JsonNames("presenter")
    var speaker: String?=null,
    var location: String?=null,
    var date:String?="",
    var tags: ArrayList<String>?=null,
    var content:String?=null,
    var updatedDate:String?=null,
    var createdDate:String?=null,
    var folder:String?=null,
    var isDeleted:Boolean?=false,
    var sharedReference:String?=null,
    var author: Author?= null,
)