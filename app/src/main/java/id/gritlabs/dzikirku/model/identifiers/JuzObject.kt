package id.gritlabs.dzikirku.model.identifiers

import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.util.LocaleProvider


/**
 * Created by Mayburger on 10/12/18.
 */
class JuzObject(val juz:Int){
    val juzString = LocaleProvider.getString(LocaleConstants.JUZ) + " " + juz
}
