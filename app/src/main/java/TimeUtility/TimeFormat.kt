package TimeUtility

import java.text.SimpleDateFormat

class TimeFormat {

    companion object {
        // function that receives a timestamp and returns a formatted date
        fun getDate(sdatetime:String):String {
            val dateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss-mm:ss")
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("dd MMM yyyy")
            return dateFormat.format(dateTime)
        }

        // function that receives a timestamp and returns a formatted time
        fun getTime(sdatetime:String):String {
            val dateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss-mm:ss")
            val dateTime = dateTimeParser.parse(sdatetime)
            val dateFormat = SimpleDateFormat("HH:mm")
            return dateFormat.format(dateTime)
        }
    }

}



