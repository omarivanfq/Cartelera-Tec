package TimeUtility

import java.text.SimpleDateFormat

class TimeFormat {

    companion object {
        fun getDate(sdatetime:String):String {
            val dateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss-mm:ss")
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("dd MMM yyyy")
            return dateFormat.format(dateTime)
        }

        fun getTime(sdatetime:String):String {
            val dateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss-mm:ss")
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("HH:mm")
            return dateFormat.format(dateTime)
        }
    }

}



