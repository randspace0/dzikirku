package id.gritlabs.dzikirku.model.request

import kotlinx.serialization.Serializable

import android.os.Parcelable
import id.gritlabs.dzikirku.model.Location
import id.gritlabs.dzikirku.model.Note
import id.gritlabs.dzikirku.model.Speaker
import kotlinx.android.parcel.Parcelize

@Parcelize
@Serializable
data class NoteRequestModel(
    var local_id:Int? =null,
    var title:String?="",
    var subtitle:String?="",
    var presenter: Speaker?=null,
    var location: Location?=null,
    var date:String?="",
    var tags: ArrayList<String>?=null,
    var content:String?=null,
    var updated_date:String?=null,
    var created_date:String?=null,
    var folder:String?=null,
    var is_deleted:Boolean?=false,
    var share_id:String?=null,
    var is_public:Boolean?=false
) : Parcelable{


}

fun Note.toRequestModel():NoteRequestModel{
    return NoteRequestModel(
        local_id = id,
        title = title,
        subtitle = subtitle,
        presenter = speaker,
        location = location,
        date = date,
        tags = tags,
        content = content,
        updated_date = updatedDate,
        created_date = createdDate,
        folder = folder,
        is_deleted = isDeleted,
        share_id = shareId,
        is_public = isPublic
    )
}